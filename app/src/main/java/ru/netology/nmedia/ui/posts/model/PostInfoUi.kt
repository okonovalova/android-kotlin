package ru.netology.nmedia.ui.posts.model

data class PostInfoUi(
    val id: Int,
    val likesCount: String,
    val sharedCount: String,
    val viewsCount: String,
    val isLiked: Boolean = false,
    val authorName: String,
    val date: String,
    val content: String,
    val linkPart: String?
)
