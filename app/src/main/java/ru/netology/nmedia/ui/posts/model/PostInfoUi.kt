package ru.netology.nmedia.ui.posts.model

import java.io.Serializable

data class PostInfoUi(
    val id: Long,
    val likesCount: String,
    val sharedCount: String,
    val viewsCount: String,
    val isLiked: Boolean = false,
    val authorName: String,
    val date: String,
    val content: String,
    val linkPart: String?,
    val videoPreviewUrl: String?,
    val videoUrl: String?
) : Serializable
