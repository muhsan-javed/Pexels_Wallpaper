package com.mjtech.pexelswallpaper.kotlin

class WallpaperAdapterKotlin {
}
/*

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WallpaperAdapter(
    private val context: Context,
    private val wallpaperModelList: List<WallpaperModel>
) :
    RecyclerView.Adapter<WallpaperViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
        return WallpaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {

        Glide.with(context).load(wallpaperModelList[position].mediumUrl).into(holder.imageView)
//        Glide.with(context).load(wallpaperModelList[position]).into(holder.imageView)

        holder.imageView.setOnClickListener {
            context.startActivity(
                Intent(context, FullScreenWallpaper::class.java)
                    .putExtra("originalUrl", wallpaperModelList[position].originalUrl)
//                    .putExtra("originalUrl",wallpaperModelList[position])
            )
        }
    }

    override fun getItemCount(): Int {
        return wallpaperModelList.size
    }
}

class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView

    init {
        imageView = itemView.findViewById(R.id.imageViewItem)
    }
}*/
