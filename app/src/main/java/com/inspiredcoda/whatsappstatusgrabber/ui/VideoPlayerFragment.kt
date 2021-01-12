package com.inspiredcoda.whatsappstatusgrabber.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.inspiredcoda.whatsappstatusgrabber.BaseFragment
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoConstant.VIDEO_URI
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import kotlinx.android.synthetic.main.fragment_video_player.*
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * A simple [Fragment] subclass.
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerFragment : BaseFragment() {

    private var videoUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            videoUri = it.get(VIDEO_URI) as Uri
        }

//        val controller = MediaController(requireActivity())
//        controller.setAnchorView(video_view)

        val player = SimpleExoPlayer.Builder(requireActivity()).build()

        player.setMediaItem(MediaItem.fromUri(videoUri!!))
        player.prepare()
        player.playWhenReady = true


//        video_view.setVideoURI(videoUri!!)
//        video_view.setMediaController(controller)
//        video_view.requestFocus()
//        video_view.start()
//        video_view.keepScreenOn = true

        requireContext().toast("Video Duration: ${videoUri?.userInfo}")

        share_btn.setOnClickListener {
            it.context.toast("this will share this file")
        }

        save_btn.setOnClickListener {
            it.context.toast("Save file")
        }

    }

    private fun saveFile(){

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showActionBar(false)

    }

}