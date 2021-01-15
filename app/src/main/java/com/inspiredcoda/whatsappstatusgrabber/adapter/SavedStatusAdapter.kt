package com.inspiredcoda.whatsappstatusgrabber.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.StatusMediaInterface
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import java.io.File

class SavedStatusAdapter(
    private var mediaInterface: StatusMediaInterface
): RecyclerView.Adapter<SavedStatusAdapter.StatusViewHolder>() {

    private var files: MutableList<File> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_status_layout, parent, false)
        return StatusViewHolder(root)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        Glide.with(holder.fileImg.context)
            .load(Uri.fromFile(files[position]))
            .thumbnail(0.1f)
            .placeholder(R.drawable.ic_play)
            .into(holder.fileImg)

        holder.fileContainer.setOnClickListener {
            holder.fileImg.context.toast("You clicked ${files[position].name}")
            mediaInterface.onVideoFileSelected(files[position])
        }

    }

    override fun getItemCount(): Int {
        return files.size
    }

    fun addToList(files: MutableList<File>) {
        this.files.clear()
        this.files.addAll(files)
        notifyDataSetChanged()
    }

    class StatusViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val fileContainer = itemView.findViewById<FrameLayout>(R.id.saved_status_container)
        val filePlayIcon = itemView.findViewById<ImageView>(R.id.saved_status_file_play_btn)
        val fileImg = itemView.findViewById<ImageView>(R.id.saved_status_img)


    }


}