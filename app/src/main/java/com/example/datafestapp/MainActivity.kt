package com.example.datafestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datafestapp.Model.Post
import com.example.datafestapp.data.PostRepository
import com.example.datafestapp.data.PostApi
import com.example.datafestapp.ui.theme.DatafestAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val postRepository = PostRepository(PostApi.RetrofitServiceFactory.makePostService())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatafestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BlogApp(
                        postRepository = postRepository,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BlogApp(postRepository: PostRepository, modifier: Modifier = Modifier) {
    var posts by remember { mutableStateOf(listOf<Post>()) }
    var newPostTitle by remember { mutableStateOf("") }
    var newPostBody by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        posts = postRepository.fetchPosts()
    }

    Column(modifier = modifier.padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(posts) { post ->
                PostItem(post)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = newPostTitle,
            onValueChange = { newPostTitle = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = newPostBody,
            onValueChange = { newPostBody = it },
            label = { Text("Body") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    val newPost = Post(userId = 1, id = 0, title = newPostTitle, body = newPostBody)
                    postRepository.addPost(newPost)
                    posts = postRepository.fetchPosts()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Post")
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = post.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = post.body, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun BlogAppPreview() {
    DatafestAppTheme {
        BlogApp(postRepository = PostRepository(PostApi.RetrofitServiceFactory.makePostService()))
    }
}