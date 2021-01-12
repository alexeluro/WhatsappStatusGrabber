package com.inspiredcoda.whatsappstatusgrabber.ui.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inspiredcoda.whatsappstatusgrabber.BaseFragment
import com.inspiredcoda.whatsappstatusgrabber.R

/**
 * A simple [Fragment] subclass.
 * Use the [SavedStatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedStatusFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_status, container, false)
    }


}