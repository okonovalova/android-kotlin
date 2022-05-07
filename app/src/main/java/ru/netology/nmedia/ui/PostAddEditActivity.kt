package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityPostAddEditBinding
import ru.netology.nmedia.ui.posts.Constants.EDIT_POST
import ru.netology.nmedia.ui.posts.Constants.POST_TEXT
import ru.netology.nmedia.ui.posts.model.PostInfoUi


class PostAddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAddEditBinding

    val editPost: PostInfoUi? by lazy {
        intent.getSerializableExtra(EDIT_POST) as? PostInfoUi
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (editPost == null) {
            binding.editLayout.root.visibility = View.GONE
        } else {
            binding.editLayout.root.visibility = View.VISIBLE
            binding.editLayout.postTextTextview.text = editPost?.content.orEmpty()
            binding.addPostEdittext.setText(editPost?.content.orEmpty())
        }
        initListeners()
    }

    private fun initListeners() {
        binding.saveImageview.setOnClickListener {
            val data = Intent()
            data.putExtra(POST_TEXT, binding.addPostEdittext.text.toString())
            data.putExtra(EDIT_POST, editPost)
            setResult(RESULT_OK, data)
            finish()
        }

        binding.editLayout.cancelImageview.setOnClickListener {
            finish()
        }
    }

}