package ru.netology.nmedia.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.data.model.PostInfo

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, PostInfo::class.java).type
    private val key = "posts"
    private var posts = emptyList<PostInfo>()
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it, type)
            data.value = posts
        }
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(data.value))
            apply()
        }
    }

    override fun getPostsData(): LiveData<List<PostInfo>> {
        return data
    }


//    override fun updatePostsData(postsInfo: List<PostInfo>) {
//        data.value = postsInfo
//        sync()
//    }

}