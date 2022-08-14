package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostsBinding
import ru.netology.nmedia.ui.posts.PostViewModel
import ru.netology.nmedia.ui.posts.adapter.OnInteractionListener
import ru.netology.nmedia.ui.posts.adapter.PostAdapter
import ru.netology.nmedia.ui.posts.model.PostInfoUi


class PostsFragment : Fragment() {
    private lateinit var binding: FragmentPostsBinding
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val adapter by lazy {
        PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: PostInfoUi) {
                val action =
                    PostsFragmentDirections.actionPostsFragmentToPostAddEditFragment(postInfo = post)
                findNavController().navigate(action)
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

            override fun onItemClicked(post: PostInfoUi) {
                val action =
                    PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(postInfo = post)
                findNavController().navigate(action)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postListRecyclerview.adapter = adapter

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.uiData.observe(this.viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
            binding.postListRecyclerview.post {
                binding.postListRecyclerview.smoothScrollToPosition(
                    0
                )
            }
        }
        viewModel.error.observe(this.viewLifecycleOwner) {
            val text = it ?: return@observe
            val snackbar = Snackbar.make(
                binding.root, text,
                Snackbar.LENGTH_LONG
            ).setAction("Retry", { viewModel.onRetryLastRequestClicked() })

            snackbar.show()
        }
        viewModel.newerCount.observe(this.viewLifecycleOwner) {
            val newerCount = viewModel.newerCount.value ?: return@observe
            if (newerCount > 0) {
                binding.showNewPostsTextview.visibility = View.VISIBLE
            }
        }
    }

    private fun initListeners() {
        binding.addPostFab.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_postAddEditFragment)
        }
        binding.showNewPostsTextview.setOnClickListener {
            binding.showNewPostsTextview.visibility = View.GONE
            viewModel.onShowNewPostsClicked()
        }
    }

}
