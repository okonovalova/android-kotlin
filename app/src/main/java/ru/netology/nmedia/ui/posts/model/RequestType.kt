package ru.netology.nmedia.ui.posts.model

sealed class RequestType ()
object GetAllType: RequestType()
class SaveType(val postText: String, val postInfoUi: PostInfoUi?): RequestType()
class RemoveByIdType(val postInfoUi: PostInfoUi): RequestType()
class LikeByIdType(val postInfoUi: PostInfoUi): RequestType()