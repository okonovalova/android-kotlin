package ru.netology.nmedia.data.repository

import ru.netology.nmedia.data.model.*
import ru.netology.nmedia.domain.model.Attachment
import ru.netology.nmedia.domain.model.AttachmentType
import ru.netology.nmedia.domain.model.PostInfo

object PostInfoMapper {
    fun mapDataToDomain(postInfoData: PostInfoData): PostInfo {
        return PostInfo(
            id = postInfoData.id,
            likesCount = postInfoData.likes,
            sharedCount = 0,
            viewsCount = 0,
            isLiked = postInfoData.likedByMe,
            authorName = postInfoData.author,
            date = "24 июня",
            content = postInfoData.content,
            linkPart = null,
            videoPreviewUrl = null,
            videoUrl = null,
            authorAvatar = postInfoData.authorAvatar,
            attachment = postInfoData.attachment?.let {
                Attachment(it.url, it.description, mapAttachmentDataToDomain(it.type))
            }
        )
    }

    private fun mapAttachmentDataToDomain(type: String) : AttachmentType {
        return when (type){
            "IMAGE" -> AttachmentType.IMAGE
            "VIDEO" -> AttachmentType.VIDEO
            else -> AttachmentType.OTHER
        }
    }

    private fun mapAttachmentDomainToData(type: AttachmentType) : String {
        return when (type){
            AttachmentType.IMAGE -> "IMAGE"
            AttachmentType.VIDEO -> "VIDEO"
            else -> "OTHER"
        }
    }

    fun mapDomainToData(postInfo: PostInfo): PostInfoData {
        return PostInfoData(
            postInfo.id,
            postInfo.authorName,
            postInfo.content,
            0,
            postInfo.isLiked,
            postInfo.likesCount,
            postInfo.authorAvatar,
            postInfo.attachment?.let {
                AttachmentData(it.url, it.description, mapAttachmentDomainToData(it.type))
            }
        )
    }

    fun mapDbToDomain(postInfoEntity: PostInfoEntity): PostInfo{
        return PostInfo(
            id = postInfoEntity.id,
            likesCount = postInfoEntity.likes,
            sharedCount = 0,
            viewsCount = 0,
            isLiked = postInfoEntity.likedByMe,
            authorName = postInfoEntity.author,
            date = postInfoEntity.published,
            content = postInfoEntity.content,
            linkPart = null,
            videoPreviewUrl = null,
            videoUrl = null,
            authorAvatar = postInfoEntity.authorAvatar,
            attachment = null
        )
    }

    fun mapDomainToDb(postInfo: PostInfo): PostInfoEntity{
        return PostInfoEntity(
            id = postInfo.id,
            author = postInfo.authorName,
            authorAvatar = postInfo.authorAvatar,
            content = postInfo.content,
            published = postInfo.date,
            likedByMe = postInfo.isLiked,
            likes = postInfo.likesCount
        )
    }

}