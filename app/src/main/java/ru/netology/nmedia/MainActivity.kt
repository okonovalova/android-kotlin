package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.extensions.setStyledSpan
import ru.netology.nmedia.model.PostInfo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isLiked: Boolean = false
    private var postInfo: PostInfo = PostInfo(999, 1599999, 1499)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListeners()
    }

    fun initViews() {
        binding.postTextTextview.setStyledSpan(
            getString(R.string.post_text_link_part),
            getColor(R.color.teal_200)
        )
        binding.countLikesTextview.text = CountMapper.mapCountToTitle(postInfo.likesCount)
        binding.countSharesTextview.text = CountMapper.mapCountToTitle(postInfo.sharedCount)
        binding.countViewsTextview.text = CountMapper.mapCountToTitle(postInfo.viewsCount)
    }

    fun initListeners() {
        binding.likesImageview.setOnClickListener {
            onLikeButtonClicked(it)
        }
        binding.postShareImageview.setOnClickListener {
            onShareButtonClicked()
        }
    }

    private fun onLikeButtonClicked(view: View) {
        if (isLiked) {
            (view as ImageView).setImageResource(R.drawable.ic_favorite_border_24)
            view.setColorFilter(
                ContextCompat.getColor(this, R.color.gray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            isLiked = false
            postInfo = postInfo.copy(likesCount = postInfo.likesCount - 1)
        } else {
            (view as ImageView).setImageResource(R.drawable.ic_liked_24)
            view.setColorFilter(
                ContextCompat.getColor(this, R.color.red),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            isLiked = true
            postInfo = postInfo.copy(likesCount = postInfo.likesCount + 1)
        }
        binding.countLikesTextview.text = CountMapper.mapCountToTitle(postInfo.likesCount)
    }

    private fun onShareButtonClicked() {
        postInfo = postInfo.copy(sharedCount = postInfo.sharedCount + 1)
        binding.countSharesTextview.text = CountMapper.mapCountToTitle(postInfo.sharedCount)
    }
}
