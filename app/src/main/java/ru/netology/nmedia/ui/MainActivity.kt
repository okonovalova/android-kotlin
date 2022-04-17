package ru.netology.nmedia.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.ui.extensions.setStyledSpan

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initObservers()
    }

    private fun initLinkView(linkPart: String) {
        binding.postTextTextview.setStyledSpan(
            linkPart,
            getColor(R.color.teal_200)
        )
    }

    private fun initListeners() {
        binding.likesImageview.setOnClickListener {
            viewModel.onLikeButtonClicked()
        }
        binding.postShareImageview.setOnClickListener {
            viewModel.onShareButtonClicked()
        }
    }

    private fun initObservers() {
        viewModel.uiData.observe(this) { postInfo ->
            with(binding) {
                countLikesTextview.text = postInfo.likesCount
                countSharesTextview.text = postInfo.sharedCount
                countViewsTextview.text = postInfo.viewsCount
                logoNameTextview.text = postInfo.authorName
                postDateTextview.text = postInfo.date
                postTextTextview.text = postInfo.content
                postInfo.linkPart?.let { initLinkView(it) }
                updateLikeView(postInfo.isLiked)
            }
        }
    }

    private fun updateLikeView(isLiked: Boolean) {
        if (isLiked) {
            binding.likesImageview.setImageResource(R.drawable.ic_liked_24)
            binding.likesImageview.setColorFilter(
                ContextCompat.getColor(this, R.color.red),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.likesImageview.setImageResource(R.drawable.ic_favorite_border_24)
            binding.likesImageview.setColorFilter(
                ContextCompat.getColor(this, R.color.gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
    }
}
