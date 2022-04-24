package ru.netology.nmedia.ui.posts.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.ui.posts.model.PostInfoUi

class PostDiffCallBack: DiffUtil.ItemCallback<PostInfoUi>() {
    override fun areItemsTheSame(oldItem: PostInfoUi, newItem: PostInfoUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostInfoUi, newItem: PostInfoUi): Boolean {
        return oldItem == newItem
    }
}