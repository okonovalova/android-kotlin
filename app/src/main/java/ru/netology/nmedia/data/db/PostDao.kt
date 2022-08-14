package ru.netology.nmedia.data.db

import ru.netology.nmedia.domain.model.PostInfo

interface PostDao {
    fun getAll(): List<PostInfo>
    fun save(postInfo: PostInfo): PostInfo
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun shareById(id: Long)
}