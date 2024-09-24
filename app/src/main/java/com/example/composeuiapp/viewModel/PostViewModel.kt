package com.example.composeuiapp.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeuiapp.service.APiClient
import com.example.composeuiapp.data.Post
import com.example.composeuiapp.service.APIService
import kotlinx.coroutines.launch

class PostViewModel(private val apiService: APIService = APiClient.apiService) : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                _posts.value = apiService.getPosts()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

/** Older code

 //PostViewModel class is responsible for managing UI-related data for the Post screen.
class PostViewModel : ViewModel() {

    // Mutable state to hold the list of posts. It starts as an empty list.
    // '_posts' is private to prevent direct modification from outside the ViewModel.
    private val _posts = mutableStateOf<List<Post>>(emptyList())

    // Public read-only access to the posts state.
    // This is exposed so that the UI can observe changes to the list of posts.
    val posts: State<List<Post>> = _posts

    init {
        fetchPosts()
    }

    // Private function to fetch posts from the network asynchronously.
    // This function uses Kotlin Coroutines and is launched within the ViewModel's coroutine scope.
    fun fetchPosts() {
        viewModelScope.launch {
            try {
                // Network call to get the posts from the API. This is a suspend function.
                val fetchedPosts = APiClient.apiService.getPosts()

                // Updates the _posts mutable state with the fetched posts.
                // This triggers a recomposition in the UI when the state changes.
                _posts.value = fetchedPosts
            } catch (e: Exception) {
                e.printStackTrace()
                // Logs the error message for easier debugging in Logcat with a "PostViewModel" tag.
                Log.e("PostViewModel", "Error fetching posts: ${e.message}")
            }
        }
    }
}
**/