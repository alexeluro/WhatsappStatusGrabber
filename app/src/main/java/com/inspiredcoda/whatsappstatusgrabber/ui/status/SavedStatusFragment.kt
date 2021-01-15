package com.inspiredcoda.whatsappstatusgrabber.ui.status

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.inspiredcoda.whatsappstatusgrabber.BaseFragment
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.adapter.SavedStatusAdapter
import com.inspiredcoda.whatsappstatusgrabber.adapter.ViewedStatusAdapter
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.FileCategory.SAVED_STATUS
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.StatusMediaInterface
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import com.inspiredcoda.whatsappstatusgrabber.viewmodel.MainViewModel
import hendrawd.storageutil.library.StorageUtil
import kotlinx.android.synthetic.main.fragment_saved_status.*
import kotlinx.android.synthetic.main.fragment_viewed_status.*
import kotlinx.android.synthetic.main.fragment_viewed_status.status_available
import kotlinx.android.synthetic.main.fragment_viewed_status.viewed_status_progress_bar
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [SavedStatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedStatusFragment : BaseFragment(), StatusMediaInterface {

    lateinit var mainViewModel: MainViewModel

    lateinit var mAdapter: SavedStatusAdapter

    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        mAdapter = SavedStatusAdapter(this)

        initRecyclerView(mAdapter)

        observers()

        val path = StorageUtil.getStorageDirectories(requireContext())

        for (x in path) {
//            file = File("/storage/emulated/0/WhatsApp/Media/.Statuses")
            file = File("$x/WhatsAppStatusGrabber/")

            mainViewModel.loadDirectoryFiles(file!!, SAVED_STATUS)

        }

    }

    private fun observers(){
        mainViewModel.loadingState.observe(viewLifecycleOwner) { state ->
            when (state.result) {
                Constants.ResultState.LOADING.name -> {
                    requireContext().toast("LOADING...")
                    Log.d(
                        "ViewedStatusFragment", "File Path: ${file?.path!!}\nTotal files found: " +
                                "${state.message}"
                    )
                    saved_status_progress_bar.visibility = View.VISIBLE
                }

                Constants.ResultState.SUCCESS.name -> {
                    requireContext().toast("SUCCESS...\n${state.message}")
                    Log.d(
                        "ViewedStatusFragment", "File Path: ${file?.path!!}\nTotal files found: " +
                                "${state.message}"
                    )
                    saved_status_progress_bar.visibility = View.GONE
                }

                Constants.ResultState.ERROR.name -> {
                    requireContext().toast("ERROR...")
                    saved_status_progress_bar.visibility = View.GONE
                }

            }
        }

        mainViewModel.directoryFiles.observe(viewLifecycleOwner) {
            Log.d("ViewedStatusFragment", "MutableList<File> size: ${it.size}")

            if (!it.isNullOrEmpty()) {
                saved_status_available.visibility = View.GONE

                mAdapter.addToList(it)

            } else {
                saved_status_available.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView(filesAdapter: SavedStatusAdapter) {
        viewed_status_recycler_view?.apply {
            adapter = filesAdapter
            layoutManager = GridLayoutManager(this.context, 3)
        }
    }

    override fun onVideoFileSelected(file: File) {
        val bundle = bundleOf(
            Pair<String, Uri>(Constants.VideoConstant.VIDEO_URI, Uri.fromFile(file)),
            Pair<String, String>(Constants.VideoConstant.VIDEO_FILE_NAME, file.name)
        )

        findNavController().navigate(R.id.action_statusFragment_to_videoPlayerFragment, bundle)

    }

    override fun onImageFileSelected(file: File) {

    }

}