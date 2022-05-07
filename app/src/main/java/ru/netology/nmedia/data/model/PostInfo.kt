package ru.netology.nmedia.data.model

data class PostInfo(
    val id: Int,
    val likesCount: Int,
    val sharedCount: Int,
    val viewsCount: Int,
    val isLiked: Boolean = false,
    val authorName: String,
    val date: String,
    val content: String,
    val linkPart: String?,
    val videoPreviewUrl: String? = null,
    val videoUrl: String? = null
)
