package com.inspiredcoda.whatsappstatusgrabber.ui.status

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.inspiredcoda.whatsappstatusgrabber.BaseFragment
import com.inspiredcoda.whatsappstatusgrabber.R
import com.inspiredcoda.whatsappstatusgrabber.adapter.ViewedStatusAdapter
import com.inspiredcoda.whatsappstatusgrabber.data.entity.Status
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoConstant.VIDEO_FILE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoConstant.VIDEO_SOURCE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.VideoSource
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.OnStoragePermissionCallback
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.StatusMediaInterface
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import com.inspiredcoda.whatsappstatusgrabber.viewmodel.MainViewModel
import hendrawd.storageutil.library.StorageUtil
import kotlinx.android.synthetic.main.fragment_saved_status.*
import kotlinx.android.synthetic.main.fragment_viewed_status.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [ViewedStatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewedStatusFragment : BaseFragment(), OnStoragePermissionCallback, StatusMediaInterface {

    lateinit var mainViewModel: MainViewModel

    lateinit var mAdapter: ViewedStatusAdapter

    private var file: File? = null

    private var referenceList = mutableListOf<Status>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_viewed_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewed_status_swipe_refresh.let {
            it.setOnRefreshListener {
                refreshStatusDirectories()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStatusDirectories()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requestStoragePermissions(this)

    }

    private fun proceedToBusiness() {

        mAdapter = ViewedStatusAdapter(this)

        initRecyclerView(mAdapter)

        observers()

    }

    private fun observers() {
        mainViewModel.loadingState.observe(viewLifecycleOwner) { state ->
            when (state.result) {
                Constants.ResultState.LOADING.name -> {
//                    requireContext().toast("LOADING...")
                    Log.d(
                        "ViewedStatusFragment", "File Path: ${file?.absolutePath}\nTotal files found: " +
                                "${state.message}"
                    )
                    viewed_status_swipe_refresh.isRefreshing = true
                    viewed_status_progress_bar.visibility = View.VISIBLE
                }

                Constants.ResultState.SUCCESS.name -> {
//                    requireContext().toast("SUCCESS...\n${state.message}")
                    Log.d(
                        "ViewedStatusFragment", "File Path: ${file?.absolutePath}\nTotal files found: " +
                                "${state.message}"
                    )

                    Handler().postDelayed({
                        if (viewed_status_swipe_refresh != null) {
                            if (viewed_status_swipe_refresh.isRefreshing) {
                                viewed_status_swipe_refresh.isRefreshing = false
                            }
                        }
                    }, 2000)

                    viewed_status_progress_bar.visibility = View.GONE
                }

                Constants.ResultState.ERROR.name -> {
                    requireContext().toast("ERROR...")

                    Handler().postDelayed({
                        if (viewed_status_swipe_refresh.isRefreshing) {
                            viewed_status_swipe_refresh.isRefreshing = false
                        }
                    }, 2000)

                    viewed_status_progress_bar.visibility = View.GONE
                }

            }
        }

        mainViewModel.directoryViewedStatusFiles.observe(this) {
            Log.d("ViewedStatusFragment", "MutableList<File> size: ${it.size}")

            if (!it.isNullOrEmpty()) {
                status_available.visibility = View.GONE

                mAdapter.addToList(it, mainViewModel.oldViewedStatusList?.toList())

            } else {
                status_available.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView(filesAdapter: ViewedStatusAdapter) {
        viewed_status_recycler_view?.apply {
            adapter = filesAdapter
            layoutManager = if(resources.configuration.orientation == LinearLayoutManager.VERTICAL) {
                GridLayoutManager(this.context, 3)
            }else{
                GridLayoutManager(this.context, 5)
            }
        }
    }

    override fun onPermissionSuccess() {
        proceedToBusiness()
    }

    override fun onPermissionFailure() {
        requireContext().toast("An error occurred!!!")
    }

    override fun onVideoFileSelected(file: File) {
        mAdapter.notifyDataSetChanged()
        mainViewModel.saveStatusDetailToDb(Status(file.name, file.name, false))
        val bundle = bundleOf(
            Pair(VIDEO_FILE, file),
            Pair<String, String>(VIDEO_SOURCE, VideoSource.VIEWED_STATUS)
        )

        findNavController().navigate(R.id.action_statusFragment_to_videoPlayerFragment, bundle)


    }

    override fun onImageFileSelected(file: File) {

    }


}