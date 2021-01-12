package com.inspiredcoda.whatsappstatusgrabber.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.inspiredcoda.whatsappstatusgrabber.ui.status.SavedStatusFragment
import com.inspiredcoda.whatsappstatusgrabber.ui.status.ViewedStatusFragment

class ViewPagerAdapter(
    activity: FragmentManager
) : FragmentStatePagerAdapter(activity, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> ViewedStatusFragment()
            1 -> SavedStatusFragment()
            else -> ViewedStatusFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Viewed Status"
            1 -> "Saved Status"
            else -> "Viewed Status"
        }
    }

}