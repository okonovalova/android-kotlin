package ru.netology.nmedia.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
)