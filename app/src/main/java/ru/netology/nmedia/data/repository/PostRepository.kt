package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.model.PostInfo

interface PostRepository {
    fun getPostsData(): LiveData<List<PostInfo>>
    fun updatePostsData(postsInfo: List<PostInfo>)
}