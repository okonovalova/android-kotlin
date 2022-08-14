package ru.netology.nmedia.ui.posts

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.netology.nmedia.data.api.DataResult
import ru.netology.nmedia.data.repository.PostInfoRepository
import ru.netology.nmedia.data.repository.PostInfoRepositoryImpl
import ru.netology.nmedia.domain.model.PostInfo
import ru.netology.nmedia.data.room.RoomDb
import ru.netology.nmedia.ui.posts.mapper.UiMapper
import ru.netology.nmedia.ui.posts.model.*
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val postInfoRepository: PostInfoRepository =
        PostInfoRepositoryImpl(RoomDb.getInstance(context = application).postDao())

    private val data: LiveData<List<PostInfo>> =
        postInfoRepository.data.asLiveData(Dispatchers.Main)

    val error: SingleLiveEvent<String> = SingleLiveEvent()
    private var idPostUiForDetail: Long = -1

    private var requestType: RequestType = GetAllType

    val uiData: LiveData<List<PostInfoUi>> = data
        .map {
            it.map {
                UiMapper.mapPostInfoToPostInfoUi(it)
            }
        }
        .distinctUntilChanged()

    val newerCount: LiveData<Int> = data.switchMap {
        postInfoRepository.getNewerCount(it.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Main)
    }

    init {
        loadPosts()

    }

    fun loadPosts() {
        requestType = GetAllType
        viewModelScope.launch {
            postInfoRepository.getAll()
                .collect { result ->
                    if (result.status == DataResult.Status.SUCCESS) {
                        Log.d("getAll", result.toString())
                    } else {
                        Log.d("getAll", result.toString())
                        error.postValue(result.error?.code.orEmpty())
                    }

                }
        }
    }

    val postUiForDetail: MutableLiveData<PostInfoUi?> = uiData
        .map {
            it.firstOrNull { it.id == idPostUiForDetail }
        } as MutableLiveData<PostInfoUi?>

    fun initPostUiForDetail(id: Long) {
        idPostUiForDetail = id
        val post = uiData.value?.firstOrNull { it.id == idPostUiForDetail }
        postUiForDetail.value = post
    }

    fun onLikeButtonClicked(postInfoUi: PostInfoUi) {
        requestType = LikeByIdType(postInfoUi)

        if (postInfoUi.isLiked) {
            viewModelScope.launch {
                postInfoRepository.dislikeById(postInfoUi.id)
                    .collect { result ->
                        if (result.status == DataResult.Status.SUCCESS) {
                            Log.d("disLikeById", result.toString())
                        } else {
                            Log.d("disLikeById", result.toString())
                            error.postValue(result.error?.code.orEmpty())
                        }
                    }
            }

        } else {
            viewModelScope.launch {
                postInfoRepository.likeById(postInfoUi.id)
                    .collect { result ->
                        if (result.status == DataResult.Status.SUCCESS) {
                            Log.d("likeById", result.toString())
                        } else {
                            Log.d("likeById", result.toString())
                            error.postValue(result.error?.code.orEmpty())
                        }
                    }
            }
        }
    }

    fun onShareButtonClicked(postInfoUi: PostInfoUi) {
        /* val posts = data.value?.toMutableList() ?: return
         val index = posts.indexOfFirst { it.id == postInfoUi.id }
         if (index == -1) return
         posts[index] = posts[index].copy(sharedCount = posts[index].sharedCount + 1)
         postInfoRepository.updatePostsData(
             posts[index],
             object : PostRepository.Callback<Unit> {
                 override fun onSuccess(result: Unit) {
                     Log.d("Share post", "SUCCESS")
                     loadPosts()
                 }

                 override fun onError(e: Throwable) {
                     error.postValue(e.message)
                     Log.d("Share post", "ERROR")
                 }
             }
         )*/
    }

    fun onRemoveMenuItemClicked(postInfoUi: PostInfoUi) {
        requestType = RemoveByIdType(postInfoUi)
        viewModelScope.launch {
            postInfoRepository.removeById(postInfoUi.id)
                .collect { result ->
                    if (result.status == DataResult.Status.SUCCESS) {
                        Log.d("removeById", result.toString())
                    } else {
                        Log.d("removeById", result.toString())
                        error.postValue(result.error?.code.orEmpty())
                    }
                }
        }
    }

    fun onSaveButtonClicked(postText: String, postInfoUi: PostInfoUi?) {
        requestType = SaveType(postText, postInfoUi)
        if (postInfoUi == null) addPost(postText) else editPost(postText, postInfoUi)
    }

    private fun addPost(postText: String) {
        val post = PostInfo(
            id = 0,
            likesCount = 0,
            sharedCount = 0,
            viewsCount = 0,
            isLiked = false,
            authorName = "Нетология. Университет интернет-профессий",
            date = "21 мая 18:36",
            content = postText,
            linkPart = "",
            authorAvatar = null,
            attachment = null
        )
        viewModelScope.launch {
            postInfoRepository.save(post)
                .collect { result ->
                    if (result.status == DataResult.Status.SUCCESS) {
                        Log.d("add", result.toString())
                    } else {
                        Log.d("add", result.toString())
                        error.postValue(result.error?.code.orEmpty())
                    }
                }
        }

    }

    private fun editPost(postText: String, postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val postId = postInfoUi.id
        val index = posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            posts[index] = posts[index].copy(content = postText)
            viewModelScope.launch {
                postInfoRepository.save(posts[index])
                    .collect { result ->
                        if (result.status == DataResult.Status.SUCCESS) {
                            Log.d("edit", result.toString())
                        } else {
                            Log.d("edit", result.toString())
                            error.postValue(result.error?.code.orEmpty())
                        }
                    }
            }
        }
    }

    fun onRetryLastRequestClicked() {
        val type = requestType
        when (type) {
            is LikeByIdType -> onLikeButtonClicked(type.postInfoUi)
            GetAllType -> loadPosts()
            is SaveType -> onSaveButtonClicked(type.postText, type.postInfoUi)
            is RemoveByIdType -> onRemoveMenuItemClicked(type.postInfoUi)
        }
    }

    fun onShowNewPostsClicked() {
        viewModelScope.launch {
            postInfoRepository.updateHiddenPosts()
                .collect { result ->
                    if (result.status == DataResult.Status.SUCCESS) {
                        Log.d("updateHiddenPosts", result.toString())
                    } else {
                        Log.d("updateHiddenPosts", result.toString())
                    }
                }
        }
    }
}