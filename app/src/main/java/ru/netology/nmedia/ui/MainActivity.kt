package ru.netology.nmedia.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.ui.extensions.hideKeyboard
import ru.netology.nmedia.ui.posts.PostViewModel
import ru.netology.nmedia.ui.posts.adapter.OnInteractionListener
import ru.netology.nmedia.ui.posts.adapter.PostAdapter
import ru.netology.nmedia.ui.posts.model.PostInfoUi

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()
    private val adapter by lazy {
        PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: PostInfoUi) {
                viewModel.onEditMenuItemClicked(post)
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
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.postListRecyclerview.adapter = adapter

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.uiData.observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.editPostData.observe(this) { post ->
            if (post == null) {
                binding.editLayout.root.visibility = View.GONE
                binding.addPostEdittext.setText("")
            } else {
                binding.editLayout.root.visibility = View.VISIBLE
                binding.editLayout.postTextTextview.setText(post.content)
                binding.addPostEdittext.setText(post.content)
            }
        }
    }

    private fun initListeners() {
        binding.saveImageview.setOnClickListener {
            viewModel.onSaveButtonClicked(binding.addPostEdittext.text.toString())
            binding.addPostEdittext.setText("")
            it.hideKeyboard()
        }

        binding.editLayout.cancelImageview.setOnClickListener {
            viewModel.onCancelButtonClicked()
        }
    }

}
