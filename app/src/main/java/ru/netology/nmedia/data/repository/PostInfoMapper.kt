package ru.netology.nmedia.data.repository

import ru.netology.nmedia.data.model.*

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

}