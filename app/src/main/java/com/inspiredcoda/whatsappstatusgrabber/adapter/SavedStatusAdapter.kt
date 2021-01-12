package com.inspiredcoda.whatsappstatusgrabber.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.inspiredcoda.whatsappstatusgrabber.R
import java.io.File

class SavedStatusAdapter(
    private var files: MutableList<File>
): RecyclerView.Adapter<SavedStatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_status_layout, parent, false)
        return StatusViewHolder(root)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return files.size
    }

    class StatusViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val fileContainer = itemView.findViewById<FrameLayout>(R.id.saved_status_container)
        val filePlayIcon = itemView.findViewById<ImageView>(R.id.saved_status_file_play_btn)
        val fileImg = itemView.findViewById<ImageView>(R.id.saved_status_img)


    }


}