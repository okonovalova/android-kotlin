package ru.netology.nmedia.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.data.repository.PostRepositoryImpl
import ru.netology.nmedia.ui.posts.mapper.UiMapper

class PostViewModel : ViewModel() {
    private val postRepository: PostRepository = PostRepositoryImpl()
    private val data: LiveData<List<PostInfo>> = postRepository.getPostsData()

    val uiData: LiveData<List<PostInfoUi>> = postRepository.getPostsData()
        .map {
            it.map {
                UiMapper.mapPostInfoToPostInfoUi(it)
            }
        }

    fun onLikeButtonClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        val newLikesCount =
            if (posts[index].isLiked) posts[index].likesCount - 1 else posts[index].likesCount + 1
        posts[index] = posts[index].copy(
            likesCount = newLikesCount,
            isLiked = !postInfoUi.isLiked,
        )
        postRepository.updatePostsData(posts)
    }


    fun onShareButtonClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        posts[index] = posts[index].copy(sharedCount = posts[index].sharedCount + 1)
        postRepository.updatePostsData(posts)
    }
}