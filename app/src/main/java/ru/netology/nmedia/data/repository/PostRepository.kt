package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.model.PostInfo

interface PostRepository {
    fun getPostsData(): List<PostInfo>

    //fun updatePostsData(postsInfo: List<PostInfo>)
    fun updatePostsData(postsInfo: PostInfo) {}
    fun likeByID(id: Long, isLiked: Boolean) {}
    fun removeById(id: Long) {}
    fun shareById(id: Long) {}
}