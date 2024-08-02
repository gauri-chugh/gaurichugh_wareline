package com.example.gaurichugh

data class PhotoResponse(
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)
