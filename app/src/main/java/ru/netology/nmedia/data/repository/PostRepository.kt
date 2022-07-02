package ru.netology.nmedia.data.repository

import okhttp3.Callback
import ru.netology.nmedia.data.model.PostInfo

interface PostRepository {
    fun getPostsData(): List<PostInfo>

    //fun updatePostsData(postsInfo: List<PostInfo>)
    fun updatePostsData(postsInfo: PostInfo) {}
    fun likeByID(id: Long, isLiked: Boolean) {}
    fun removeById(id: Long) {}
    fun shareById(id: Long) {}
    fun getPostsDataAsync(callback: Callback<List<PostInfo>>) {}
    fun likeByIDAsync(id: Long, isLiked: Boolean, callback: Callback<Unit>){}
    fun removeByIdAsync(id: Long, callback: Callback<Unit>){}
    interface Callback <T>{
        fun onSuccess(result: T) {}
        fun onError(e: Exception) {}
    }
}