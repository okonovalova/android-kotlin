package ru.netology.nmedia.ui.posts.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.ui.extensions.setStyledSpan

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val context: Context,
    private val onLikelistener: (postInfo: PostInfoUi) -> Unit,
    private val onSharelistener: (postInfo: PostInfoUi) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(postInfoUi: PostInfoUi) {
        with(binding) {
            likesButton.text = postInfoUi.likesCount
            postShareButton.text = postInfoUi.sharedCount
            postViewButton.text = postInfoUi.viewsCount
            logoNameTextview.text = postInfoUi.authorName
            postDateTextview.text = postInfoUi.date
            postTextTextview.text = postInfoUi.content
            postInfoUi.linkPart?.let { initLinkView(it) }
            updateLikeView(postInfoUi.isLiked)
        }
        initListeners(postInfoUi)
    }

    private fun initLinkView(linkPart: String) {
        binding.postTextTextview.setStyledSpan(
            linkPart,
            context.getColor(R.color.teal_200)
        )
    }

    private fun updateLikeView(isLiked: Boolean) {
        if (isLiked) {
            binding.likesButton.setIconResource(R.drawable.ic_liked_24)
            binding.likesButton.setIconTintResource(R.color.red)
        } else {
            binding.likesButton.setIconResource(R.drawable.ic_favorite_border_24)
            binding.likesButton.setIconTintResource(R.color.gray)
        }
    }

    private fun initListeners(postInfoUi: PostInfoUi) {
        binding.likesButton.setOnClickListener {
            onLikelistener(postInfoUi)
        }
        binding.postShareButton.setOnClickListener {
            onSharelistener(postInfoUi)
        }
    }
}