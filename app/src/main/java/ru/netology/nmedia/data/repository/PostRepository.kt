package ru.netology.nmedia.data.repository

import ru.netology.nmedia.domain.model.PostInfo

interface PostRepository {
    fun getPostsData(): List<PostInfo> {
        return emptyList()
    }

    //fun updatePostsData(postsInfo: List<PostInfo>)
    fun updatePostsData(postsInfo: PostInfo, callback: Callback<Unit>) {}
    fun likeByID(id: Long, isLiked: Boolean) {}
    fun removeById(id: Long) {}
    fun shareById(id: Long) {}
    fun getPostsDataAsync(callback: Callback<List<PostInfo>>) {}
    fun likeByIDAsync(id: Long, isLiked: Boolean, callback: Callback<Unit>){}
    fun removeByIdAsync(id: Long, callback: Callback<Unit>){}
    interface Callback <T>{
        fun onSuccess(result: T) {}
        fun onError(e: Throwable) {}
    }
}