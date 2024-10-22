package com.example.datafestapp.data

import com.example.datafestapp.Model.Post
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PostApi {

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    object RetrofitServiceFactory {
        fun makePostService(): PostApi {
            val client = OkHttpClient.Builder().build()

            return Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PostApi::class.java)
        }
    }
}



class PostRepository(private val postService: PostApi) {

    suspend fun fetchPosts(): List<Post> {
        return postService.getPosts()
    }

    suspend fun addPost(post: Post): Response<Post> {
        return postService.createPost(post)
    }
}
