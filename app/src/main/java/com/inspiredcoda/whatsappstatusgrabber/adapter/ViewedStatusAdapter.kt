package com.inspiredcoda.whatsappstatusgrabber.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.data.entity.Status
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.StatusMediaInterface
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import java.io.File

class ViewedStatusAdapter(
    private var mediaInterface: StatusMediaInterface
) : RecyclerView.Adapter<ViewedStatusAdapter.StatusViewHolder>() {

    private var files: MutableList<File> = mutableListOf()

    private var referenceList: MutableList<Status?>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewed_status_layout, parent, false)
        return StatusViewHolder(root)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {

        Glide.with(holder.fileImg.context)
            .load(Uri.fromFile(files[position]))
            .thumbnail(0.1f)
            .placeholder(R.drawable.ic_play)
            .into(holder.fileImg)

        if (!referenceList.isNullOrEmpty()) {
            if (referenceList?.any { it?.fileName == files[position].name }!!){
                holder.fileStatus.visibility = View.GONE
            } else {
                holder.fileStatus.visibility = View.VISIBLE
            }
        }
//        holder.fileImg.setOnClickListener {
//            holder.fileImg.context.toast("You clicked ${files[position].name}")
//        }

        holder.fileContainer.setOnClickListener {
            holder.fileImg.context.toast("You clicked ${files[position].name}")
//            referenceList?.get(position)?.isNewStatus = false
            mediaInterface.onVideoFileSelected(files[position])
        }

    }

    override fun getItemCount(): Int {
        return files.size
    }

    fun addToList(files: MutableList<File>, referenceList: List<Status?>?) {
        this.files.clear()
        this.files.addAll(files)
        this.referenceList = referenceList?.toMutableList()
        notifyDataSetChanged()
    }

    class StatusViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val fileContainer = itemView.findViewById<FrameLayout>(R.id.viewed_status_container)
        val filePlayIcon = itemView.findViewById<ImageView>(R.id.viewed_status_file_play_btn)
        val fileImg = itemView.findViewById<ImageView>(R.id.viewed_status_img)
        val fileStatus = itemView.findViewById<LinearLayoutCompat>(R.id.viewed_new_status_indicator)


    }


}