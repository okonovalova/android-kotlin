package ru.netology.nmedia.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.data.model.PostInfoUi
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.data.repository.PostRepositoryImpl
import ru.netology.nmedia.ui.mapper.UiMapper

class PostViewModel : ViewModel() {
    private val postRepository: PostRepository = PostRepositoryImpl()
    private val data: LiveData<PostInfo> = postRepository.getPostData()

    val uiData: LiveData<PostInfoUi> = postRepository.getPostData()
        .map { UiMapper.mapPostInfoToPostInfoUi(it) }

    fun onLikeButtonClicked() {
        val postInfo = data.value ?: return
        val newLikesCount =
            if (postInfo.isLiked) postInfo.likesCount - 1 else postInfo.likesCount + 1
        postRepository.updatePostData(
            postInfo.copy(
                isLiked = !postInfo.isLiked,
                likesCount = newLikesCount
            )
        )
    }

    fun onShareButtonClicked() {
        val postInfo = data.value ?: return
        postRepository.updatePostData(postInfo.copy(sharedCount = postInfo.sharedCount + 1))
    }
}