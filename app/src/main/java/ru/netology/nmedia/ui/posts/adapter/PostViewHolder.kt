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
            countLikesTextview.text = postInfoUi.likesCount
            countSharesTextview.text = postInfoUi.sharedCount
            countViewsTextview.text = postInfoUi.viewsCount
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
            binding.likesImageview.setImageResource(R.drawable.ic_liked_24)
            binding.likesImageview.setColorFilter(
                ContextCompat.getColor(context, R.color.red),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.likesImageview.setImageResource(R.drawable.ic_favorite_border_24)
            binding.likesImageview.setColorFilter(
                ContextCompat.getColor(context, R.color.gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun initListeners(postInfoUi: PostInfoUi) {
        binding.likesImageview.setOnClickListener {
            onLikelistener(postInfoUi)
        }
        binding.postShareImageview.setOnClickListener {
            onSharelistener(postInfoUi)
        }
    }
}