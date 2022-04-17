package ru.netology.nmedia.ui.mapper

import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.data.model.PostInfoUi

object UiMapper {

    fun mapPostInfoToPostInfoUi(postInfo: PostInfo): PostInfoUi {
        return PostInfoUi(
            likesCount = mapCountToTitle(postInfo.likesCount),
            sharedCount = mapCountToTitle(postInfo.sharedCount),
            viewsCount = mapCountToTitle(postInfo.viewsCount),
            isLiked = postInfo.isLiked,
            authorName = postInfo.authorName,
            date = postInfo.date,
            content = postInfo.content,
            linkPart = postInfo.linkPart
        )
    }

    private fun mapCountToTitle(count: Int): String {
        return when {
            count >= 1_000_000 && (count.toDouble() % 1_000_000) > 99_999 -> {
                ((count - count.toDouble() % 100_000) / 1_000_000).toString() + "M"
            }
            count >= 1_000_000 -> {
                (count / 1_000_000).toString() + "M"
            }
            count >= 1_000 && (count.toDouble() % 1_000) > 99 -> {
                ((count - count.toDouble() % 100) / 1_000).toString() + "K"
            }
            count >= 1_000 -> {
                (count / 1_000).toString() + "K"
            }
            else -> count.toString()
        }
    }

}