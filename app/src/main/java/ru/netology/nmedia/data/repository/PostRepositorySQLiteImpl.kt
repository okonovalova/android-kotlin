package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.db.PostDao
import ru.netology.nmedia.data.model.PostInfo

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<PostInfo>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getPostsData(): LiveData<List<PostInfo>> = data

    override fun updatePostsData(postsInfo: PostInfo) {
        val id = postsInfo.id
        val saved = dao.save(postsInfo)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun likeByID(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id == id) {
                if (it.isLiked) {
                    it.copy(
                        isLiked = !it.isLiked,
                        likesCount = it.likesCount - 1
                    )
                } else {
                    it.copy(
                        isLiked = !it.isLiked,
                        likesCount = it.likesCount + 1
                    )
                }
            } else it
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id == id) it.copy(sharedCount = it.sharedCount + 1) else it
        }
        data.value = posts
    }
}