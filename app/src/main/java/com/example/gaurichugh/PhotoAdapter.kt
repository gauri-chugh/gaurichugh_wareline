package com.example.gaurichugh

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gaurichugh.databinding.ItemImageBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.Executors

class PhotoAdapter(private val photos: List<Photo>, private val context: Context) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val executor = Executors.newFixedThreadPool(4)
    private val memoryCache = androidx.collection.LruCache<String, Bitmap>(50)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int = photos.size

    inner class PhotoViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            // Load image asynchronously with caching and error handling
            val url = photo.source.large2x
            binding.imageView.setImageResource(R.drawable.placeholder) // Placeholder image

            val cachedBitmap = memoryCache.get(url) ?: CacheUtils.getBitmapFromCache(context, url)
            if (cachedBitmap != null) {
                binding.imageView.setImageBitmap(cachedBitmap)
            } else {
                executor.submit {
                    try {
                        val client = OkHttpClient()
                        val request = Request.Builder().url(url).build()
                        val response = client.newCall(request).execute()
                        val inputStream = response.body?.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        if (bitmap != null) {
                            memoryCache.put(url, bitmap)
                            CacheUtils.saveBitmapToCache(context, bitmap, url)

                            (binding.imageView.context as MainActivity).runOnUiThread {
                                binding.imageView.setImageBitmap(bitmap)
                            }
                        }
                        inputStream?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        (binding.imageView.context as MainActivity).runOnUiThread {
                            Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
