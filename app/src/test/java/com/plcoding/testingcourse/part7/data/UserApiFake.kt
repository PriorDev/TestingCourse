package com.plcoding.testingcourse.part7.data

import com.plcoding.testingcourse.part7.domain.Post
import com.plcoding.testingcourse.part7.domain.User

class UserApiFake: UserApi {
    val users = (1..10).map {
        User(
            id = it.toString(),
            username = "User-$it"
        )
    }
    val post = (1..10).map {
        Post(
            id = it.toString(),
            userId = it.toString(),
            title = "Test Title $it",
            body = "Test Body $it"
        )
    }
    override suspend fun getUser(id: String): User? {
        return users.find { it.id == id }
    }

    override suspend fun getPosts(id: String): List<Post> {
         return post.filter { it.userId == id }
    }
}