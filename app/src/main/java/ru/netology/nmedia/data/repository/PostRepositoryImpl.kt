package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.model.PostInfo

class PostRepositoryImpl : PostRepository {
    private val postInfo: PostInfo = PostInfo(
        id = 1,
        likesCount = 999,
        sharedCount = 1599999,
        viewsCount = 1499,
        isLiked = false,
        authorName = "Нетология. Университет интернет-профессий",
        date = "21 мая 18:36",
        content = "Таким образом начало повседневной работы по формированию позиции способствует подготовки и реализации новых предложений. Идейные соображения высшего порядка, а также начало повседневной работы по формированию позиции играет важную роль в формировании форм развития. Равным образом рамки и место обучения кадров влечет за собой процесс внедрения и модернизации систем массового участия. Идейные соображения высшего порядка, а также дальнейшее развитие различных форм деятельности способствует подготовки и реализации существенных финансовых и административных условий. https://google.com",
        linkPart = "https://google.com"
    )

    private val posts: MutableList<PostInfo> =
        mutableListOf(postInfo, postInfo.copy(id = 2, isLiked = true, sharedCount = 0), postInfo.copy(id = 3), postInfo.copy(id = 4), postInfo.copy(id = 5))

    private val postsLiveData: MutableLiveData<List<PostInfo>> = MutableLiveData(posts)

    override fun getPostsData(): LiveData<List<PostInfo>> {
        return postsLiveData
    }

    override fun updatePostsData(postsInfo: List<PostInfo>) {
        postsLiveData.value = postsInfo
    }
}