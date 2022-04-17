package ru.netology.nmedia.data.model

data class PostInfoUi(
    val likesCount: String,
    val sharedCount: String,
    val viewsCount: String,
    val isLiked: Boolean = false,
    val authorName: String,
    val date: String,
    val content: String,
    val linkPart: String?
)
