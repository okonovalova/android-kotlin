package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailBinding
import ru.netology.nmedia.ui.extensions.setStyledSpan
import ru.netology.nmedia.ui.posts.PostViewModel
import ru.netology.nmedia.ui.posts.model.PostInfoUi


class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val args: PostDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initPostUiForDetail(args.postInfo.id)
        initObservers()
    }


    private fun bind(postInfoUi: PostInfoUi) {
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
                Glide.with(requireContext())
                    .load(postInfoUi.videoPreviewUrl)
                    .into(postVideoPreviewImageview)
            }
        }
        initListeners(postInfoUi)
    }

    private fun initLinkView(linkPart: String) {
        binding.postTextTextview.setStyledSpan(
            linkPart,
            requireContext().getColor(R.color.teal_200)
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

    private fun initObservers() {
        viewModel.uiData.observe(viewLifecycleOwner) {}
        viewModel.postUiForDetail.observe(viewLifecycleOwner) { post ->
            post?.let { bind(it) }
        }
    }

    private fun initListeners(postInfoUi: PostInfoUi) {
        binding.likesButton.setOnClickListener {
            viewModel.onLikeButtonClicked(postInfoUi)
        }
        binding.postShareButton.setOnClickListener {
            viewModel.onShareButtonClicked(postInfoUi)
        }
        binding.menuImageview.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            viewModel.onRemoveMenuItemClicked(postInfoUi)
                            findNavController().navigateUp()
                            true
                        }
                        R.id.edit -> {
                            val action =
                                PostDetailFragmentDirections.actionPostDetailFragmentToPostAddEditFragment(
                                    postInfo = postInfoUi
                                )
                            findNavController().navigate(action)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }

        binding.postVideoPreviewImageview.setOnClickListener { view ->
            postInfoUi.videoUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
    }
}