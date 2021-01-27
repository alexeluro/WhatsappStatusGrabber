package com.inspiredcoda.whatsappstatusgrabber.ui

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.inspiredcoda.whatsappstatusgrabber.BaseFragment
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoConstant.VIDEO_FILE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoSource
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
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

        when(VIDEO_SOURCE){
            VideoSource.VIEWED_STATUS -> save_btn.visibility = View.VISIBLE

            VideoSource.SAVED_STATUS -> save_btn.visibility = View.INVISIBLE
        }

        setUpExoPlayer()
//        player?.play()

        createWsgFolder()


        share_btn.setOnClickListener {
            player?.pause()
            val intent = Intent(ACTION_SEND)
            intent.data = Uri.fromFile(videoSourceFile)
            intent.type = "video/*"
            intent.`package` = "com.whatsapp"
            startActivity(createChooser(intent, "Send to"))
        }

        save_btn.setOnClickListener {
            saveFile(videoSourceFile?.name!!)
        }

    }

    private fun setUpExoPlayer(){
        player = SimpleExoPlayer.Builder(requireActivity()).build()
        video_view.player = player
        player?.setMediaItem(MediaItem.fromUri(Uri.fromFile(videoSourceFile)))
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

        if (!defaultFolder!!.exists()){
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
        var destinationFile: File? = null

        var destinationChannel: FileChannel? = null
        var sourceChannel: FileChannel? = null

        for (x in rootPath) {
            destinationFile = File("$x/WhatsApp Status Grabber")
        }

        try {
            val file = File(destinationFile, fileName)

            sourceChannel = FileInputStream(videoSourceFile).channel
            destinationChannel = FileOutputStream(file).channel

            destinationChannel?.transferFrom(sourceChannel, 0, sourceChannel.size())

        }catch (e: IOException){
            requireContext().toast("Exception: \n${e.message!!}")
        }finally {
            val file = File(destinationFile, fileName)
            if (file.exists()){
                requireContext().toast("saved successfully")
            }else{
                requireContext().toast("failed to save!")
            }
            sourceChannel?.close()
            destinationChannel?.close()
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