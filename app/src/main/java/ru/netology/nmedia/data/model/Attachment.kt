package ru.netology.nmedia.data.model

class Attachment(
    val url: String,
    val description: String,
    val type: AttachmentType
)

enum class AttachmentType{
    IMAGE, VIDEO, OTHER
}