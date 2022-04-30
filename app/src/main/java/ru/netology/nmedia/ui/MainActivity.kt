package ru.netology.nmedia.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.ui.posts.PostViewModel
import ru.netology.nmedia.ui.posts.adapter.PostAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()
    private val adapter by lazy {
        PostAdapter(
            onLikelistener = viewModel::onLikeButtonClicked,
            onSharelistener = viewModel::onShareButtonClicked
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.postListRecyclerview.adapter = adapter

        initObservers()
    }

    private fun initObservers() {
        viewModel.uiData.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }

}
