package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityPostsBinding
import ru.netology.nmedia.ui.posts.Constants.EDIT_POST
import ru.netology.nmedia.ui.posts.Constants.POST_TEXT
import ru.netology.nmedia.ui.posts.PostViewModel
import ru.netology.nmedia.ui.posts.adapter.OnInteractionListener
import ru.netology.nmedia.ui.posts.adapter.PostAdapter
import ru.netology.nmedia.ui.posts.model.PostInfoUi


class PostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostsBinding
    private val viewModel: PostViewModel by viewModels()
    private val adapter by lazy {
        PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: PostInfoUi) {
                val intent = Intent(this@PostsActivity, PostAddEditActivity::class.java)
                intent.putExtra(EDIT_POST, post)
                registerListenerForResult.launch(intent)
            }

            override fun onLike(post: PostInfoUi) {
                viewModel.onLikeButtonClicked(post)
            }

            override fun onRemove(post: PostInfoUi) {
                viewModel.onRemoveMenuItemClicked(post)
            }

            override fun onShare(post: PostInfoUi) {
                viewModel.onShareButtonClicked(post)
            }

            override fun onPlayVideo(videoUrl: String) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(videoUrl)
                startActivity(intent)
            }
        })
    }

    private val registerListenerForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val postText: String = result.data?.getStringExtra(POST_TEXT) ?: ""
                val editPost: PostInfoUi? =
                    result.data?.getSerializableExtra(EDIT_POST) as? PostInfoUi
                viewModel.onSaveButtonClicked(postText, editPost)
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.postListRecyclerview.adapter = adapter

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.uiData.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }

    private fun initListeners() {
        binding.addPostFab.setOnClickListener {
            registerListenerForResult.launch(
                Intent(
                    this@PostsActivity,
                    PostAddEditActivity::class.java
                )
            )
        }
    }

}
