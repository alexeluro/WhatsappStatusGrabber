package com.inspiredcoda.whatsappstatusgrabber.ui

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.inspiredcoda.whatsappstatusgrabber.BaseFragment
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoConstant.VIDEO_FILE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoSource
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import com.inspiredcoda.whatsappstatusgrabber.viewmodel.MainViewModel
import hendrawd.storageutil.library.StorageUtil
import kotlinx.android.synthetic.main.fragment_video_player.*
import java.io.*
import java.nio.channels.FileChannel

/**
 * A simple [Fragment] subclass.
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerFragment : BaseFragment() {

    //    private var videoUri: Uri? = null
    private var videoSourceFile: File? = null
    private var VIDEO_SOURCE: String? = null
    private var player: SimpleExoPlayer? = null

    private lateinit var mainViewModel: MainViewModel

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
            videoSourceFile = it.get(VIDEO_FILE) as File
            VIDEO_SOURCE = it.getString(Constants.VideoConstant.VIDEO_SOURCE)
        }

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        when (VIDEO_SOURCE) {
            VideoSource.VIEWED_STATUS -> save_btn.visibility = View.VISIBLE

            VideoSource.SAVED_STATUS -> save_btn.visibility = View.INVISIBLE
        }

        setUpExoPlayer()

        createWsgFolder()


        share_btn.setOnClickListener { it ->
            player?.pause()
            val intent = Intent(ACTION_SEND).also {
//                it.data = FileProvider.getUriForFile(requireContext(), "//com.whatsapp//.fileProvider",videoSourceFile!!)
                it.data = Uri.fromFile(videoSourceFile!!)
                it.type = "video/*"
//                it.`package` = "com.whatsapp"
                it.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
//                it.putExtra(EXTRA_TEXT, "This file is shared from WhatsApp Status Grabber")
            }

            startActivity(createChooser(intent, "Send to"))
        }

        save_btn.setOnClickListener {
            saveFile(videoSourceFile?.name!!)
        }

    }

    private fun setUpExoPlayer() {
        player = SimpleExoPlayer.Builder(requireActivity()).build()
        video_view.player = player
        player?.setMediaItem(MediaItem.fromUri(Uri.fromFile(videoSourceFile)))
        player?.prepare()
        player?.repeatMode = Player.REPEAT_MODE_ONE
        player?.playWhenReady = true
    }

    private fun createWsgFolder() {
        val rootPath = StorageUtil.getStorageDirectories(requireContext())
        var defaultFolder: File? = null

        for (x in rootPath) {
            Log.d("VideoPlayerFragment", "createWsgFolder: $x")
            defaultFolder = File(x, "WhatsApp Status Grabber")
        }

        if (!defaultFolder!!.exists()) {
            defaultFolder.mkdirs()
            if (defaultFolder.isDirectory) {
                requireContext().toast("Folder created successfully")
            } else {
                requireContext().toast("Failed to create Folder")
            }
        }

    }

    private fun saveFile(fileName: String) {
        requireContext().toast("Saved Successfully")
        val rootPath = StorageUtil.getStorageDirectories(requireContext())
        mainViewModel.saveStatus(fileName, rootPath, videoSourceFile!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showActionBar(false)

    }

    private fun pausePlayer(){
        player?.let{
            it.playWhenReady = false
            it.playbackState
        }
    }

    private fun resumePlayer(){
        player?.let{
            it.playWhenReady = true
            it.playbackState
        }
    }

    override fun onPause() {
        pausePlayer()
        video_view.onPause()
        super.onPause()
    }

    override fun onResume() {
        video_view.onResume()
        resumePlayer()
        super.onResume()
    }

    override fun onDestroyView() {
        player?.release()
        player = null
        super.onDestroyView()
    }

}