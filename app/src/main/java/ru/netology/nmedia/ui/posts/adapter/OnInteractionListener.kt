package ru.netology.nmedia.ui.posts.adapter

import ru.netology.nmedia.ui.posts.model.PostInfoUi

interface OnInteractionListener {
    fun onLike(post: PostInfoUi)
    fun onEdit(post: PostInfoUi)
    fun onRemove(post: PostInfoUi)
    fun onShare(post: PostInfoUi)
    fun onPlayVideo(videoUrl: String)
}