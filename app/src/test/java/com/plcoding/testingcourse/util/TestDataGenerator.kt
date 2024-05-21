package com.plcoding.testingcourse.util

import com.plcoding.testingcourse.part7.domain.Post
import com.plcoding.testingcourse.part7.domain.Profile
import com.plcoding.testingcourse.part7.domain.User
import java.util.UUID

fun user() = User(
        id = UUID.randomUUID().toString(),
        username = "test-user"
    )


fun post(userId: String) = Post(
    id = UUID.randomUUID().toString(),
    userId = userId,
    title = "Post Title",
    body = "Post Body"
)

fun profile(): Profile {
    val user = user()
    return  Profile(
        user = user,
        posts = (1..10).map {
            post(user.id)
        }
    )
}