package ru.netology.nmedia.data.model

class PostInfoData (
    val id: Long,
    val author: String,
    val content: String,
    val published: Int,
    val likedByMe: Boolean,
    val likes: Int,
    val authorAvatar: String? = null,
    val attachment: AttachmentData? = null
    )