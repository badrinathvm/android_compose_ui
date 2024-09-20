package com.example.composeuiapp.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.composeuiapp.data.Post
import com.example.composeuiapp.viewModel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(viewModel: PostViewModel = viewModel()) {
    val posts by viewModel.posts

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Posts List") }
            )
        }
    ) { padding ->
        if (posts.isNotEmpty()) {
            LazyColumn(contentPadding = padding) {
                items(posts) { post ->
                    PostItem(post)
                }
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleLarge)
            Text(text = post.body, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
