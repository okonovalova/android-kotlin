package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.model.PostInfo

interface PostRepository {
    fun getPostData(): LiveData<PostInfo>
    fun updatePostData(postInfo: PostInfo)
}