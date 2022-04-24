package ru.netology.nmedia.ui.posts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.databinding.ItemPostBinding

class PostAdapter(
    private val onLikelistener: (postInfo: PostInfoUi) -> Unit,
    private val onSharelistener: (postInfo: PostInfoUi) -> Unit
) : ListAdapter<PostInfoUi, PostViewHolder> (PostDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PostViewHolder(binding, parent.context, onLikelistener, onSharelistener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}