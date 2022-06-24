package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostAddEditBinding
import ru.netology.nmedia.ui.posts.PostViewModel


class PostAddEditFragment : Fragment() {
    private lateinit var binding: FragmentPostAddEditBinding
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    val args: PostAddEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.postInfo == null) {
            binding.editLayout.root.visibility = View.GONE
        } else {
            binding.editLayout.root.visibility = View.VISIBLE
            binding.editLayout.postTextTextview.text = args.postInfo?.content.orEmpty()
            binding.addPostEdittext.setText(args.postInfo?.content.orEmpty())
        }
        initListeners()
    }


    private fun initListeners() {
        binding.saveImageview.setOnClickListener {
            viewModel.onSaveButtonClicked(
                binding.addPostEdittext.text.toString(),
                postInfoUi = args.postInfo
            )
            findNavController().navigateUp()
        }

        binding.editLayout.cancelImageview.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}