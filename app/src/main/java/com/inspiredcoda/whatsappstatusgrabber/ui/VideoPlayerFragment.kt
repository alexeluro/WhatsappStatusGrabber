package com.inspiredcoda.whatsappstatusgrabber.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telecom.InCallService
import android.util.Log
import android.view.*
import androidx.core.net.toFile
import androidx.core.util.TimeUtils
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.inspiredcoda.whatsappstatusgrabber.BaseFragment
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoConstant.VIDEO_FILE_NAME
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoConstant.VIDEO_URI
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import hendrawd.storageutil.library.StorageUtil
import kotlinx.android.synthetic.main.fragment_video_player.*
import java.io.*
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerFragment : BaseFragment() {

    private var videoUri: Uri? = null
    private var videoName: String? = null
    private var player: SimpleExoPlayer? = null

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
            videoName = it.getString(VIDEO_FILE_NAME)
        }

//        val controller = MediaController(requireActivity())
//        controller.setAnchorView(video_view)

        setUpExoPlayer()
//        player?.play()

        createWsgFolder()

//        video_view.setVideoURI(videoUri!!)
//        video_view.setMediaController(controller)
//        video_view.requestFocus()
//        video_view.start()
//        video_view.keepScreenOn = true

        share_btn.setOnClickListener {
            player?.pause()
            val intent = Intent(ACTION_SEND)
            intent.data = videoUri
            intent.type = "video/*"
            startActivity(createChooser(intent, "Send to"))
//            it.context.toast("this will share this file")
        }

        save_btn.setOnClickListener {
            saveFile(videoName!!)
//            it.context.toast("Save file")
        }

    }

    private fun setUpExoPlayer(){
        player = SimpleExoPlayer.Builder(requireActivity()).build()
        video_view.player = player
        player?.setMediaItem(MediaItem.fromUri(videoUri!!))
        player?.prepare()
        player?.repeatMode = Player.REPEAT_MODE_ONE
        player?.playWhenReady = true
    }

    private fun createWsgFolder(){
        val rootPath = StorageUtil.getStorageDirectories(requireContext())
        var defaultFolder: File? = null

        for(x in rootPath){
            Log.d("VideoPlayerFragment", "createWsgFolder: $x")
            defaultFolder = File(x, "WhatsApp Status Grabber")
        }

        if (defaultFolder!!.exists()){
            requireContext().toast("Folder already exist")
        }else{
            defaultFolder.mkdirs()
            if (defaultFolder.isDirectory){
                requireContext().toast("Folder created successfully")
            }else{
                requireContext().toast("Failed to create Folder")
            }
        }

    }

    private fun saveFile(fileName: String){
        val rootPath = StorageUtil.getStorageDirectories(requireContext())
        var wsgFolder: File? = null
        var fos: FileOutputStream? = null

        for (x in rootPath) {
            wsgFolder = File("$x/WhatsApp Status Grabber")
        }


        try {
            val file = File(wsgFolder, "WSG_${fileName}")
            file.writeBytes(videoUri?.toFile()?.readBytes()!!)
            fos = FileOutputStream(file)
            fos.write(file.readBytes())
//            fos = requireActivity().openFileOutput()
//            fos.write(wsgFolder?.readBytes())

        }catch (e: IOException){
            this.context?.toast("Exception: \n${e.message!!}")
        }finally {
            val file = File(wsgFolder, "WSG_${fileName}")
            if (file.exists()){
                requireContext().toast("saved successfully")
            }else{
                requireContext().toast("failed to save!")
            }
            fos?.close()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showActionBar(false)

    }

    override fun onPause() {
//        player?.release()
//        player = null
        video_view.onPause()
        super.onPause()
    }

    override fun onResume() {
        video_view.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        player?.release()
        player = null
        super.onDestroyView()
    }

}