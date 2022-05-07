package ru.netology.nmedia.ui.posts.adapter

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.ui.extensions.setStyledSpan

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val context: Context,
    private val onInteractionListener: OnInteractionListener
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
            if (postInfoUi.videoPreviewUrl == null) {
                postVideoPreviewImageview.visibility = View.GONE
                playImageview.visibility = View.GONE
            } else {
                postVideoPreviewImageview.visibility = View.VISIBLE
                playImageview.visibility = View.VISIBLE
                Glide.with(context)
                    .load(postInfoUi.videoPreviewUrl)
                    .into(postVideoPreviewImageview)
            }
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
            onInteractionListener.onLike(postInfoUi)
        }
        binding.postShareButton.setOnClickListener {
            onInteractionListener.onShare(postInfoUi)
        }
        binding.menuImageview.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.onRemove(postInfoUi)
                            true
                        }
                        R.id.edit -> {
                            onInteractionListener.onEdit(postInfoUi)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }

        binding.postVideoPreviewImageview.setOnClickListener { view ->
            postInfoUi.videoUrl?.let { url -> onInteractionListener.onPlayVideo(url) }
        }
    }
}