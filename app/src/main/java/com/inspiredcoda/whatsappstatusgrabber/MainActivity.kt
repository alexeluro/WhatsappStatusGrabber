package com.inspiredcoda.whatsappstatusgrabber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayout
import com.inspiredcoda.whatsappstatusgrabber.adapter.ViewPagerAdapter
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.OnStoragePermissionCallback
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.StatusGrabberInterface
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions.READ_EXTERNAL_STORAGE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions.STORAGE_REQUEST_CODE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions.WRITE_EXTERNAL_STORAGE
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.UserInterfaceListener
import com.inspiredcoda.whatsappstatusgrabber.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), StatusGrabberInterface,
    EasyPermissions.PermissionCallbacks, UserInterfaceListener {

    lateinit var mainViewModel: MainViewModel
    private var permissions = mutableListOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    lateinit var onStorageCallback: OnStoragePermissionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        toolbar.setupWithNavController(navController, AppBarConfiguration(
            navController.graph,
            null
        ))

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

//        requestStoragePermission()


    }

    override fun showActionBar(status: Boolean) {
        if (status){
            supportActionBar?.show()
            main_app_bar.setExpanded(status)
            main_app_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
//            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }else{
            supportActionBar?.hide()
            main_app_bar.setExpanded(status)
            main_app_bar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
//            this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun requestStoragePermission() {
//        if (!areStoragePermissionsGranted()) {
            EasyPermissions.requestPermissions(
                this,
                Permissions.PERMISSION_RATIONALE,
                STORAGE_REQUEST_CODE,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
//        }

//        Dexter.withContext(this)
//            .withPermissions(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
//            .withListener(object : MultiplePermissionsListener {
//
//                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
//                    if (p0?.areAllPermissionsGranted()!!){
//                        toast("ALL PERMISSIONS GRANTED")
//                    }
//
//                    for (x in p0.deniedPermissionResponses){
//                        if (x.isPermanentlyDenied){
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                shouldShowRequestPermissionRationale(x.permissionName)
//                            }else{
//                                // show permission rationale for other android versions
//                            }
//                        }else{
//
//                        }
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    p0: MutableList<PermissionRequest>?,
//                    p1: PermissionToken?
//                ) {
//                    p1?.continuePermissionRequest()
//                }
//
//            })
//            .check()

    }

    private fun areStoragePermissionsGranted(): Boolean{
        return (!EasyPermissions.hasPermissions(
                this,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
            )
        )
    }

    private fun displayClosingDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("This app requires these permissions to access your WhatsApp status")
            .setPositiveButton("Okay") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when(requestCode){
            STORAGE_REQUEST_CODE -> {
                onStorageCallback.onPermissionSuccess()
            }
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when(requestCode){
            STORAGE_REQUEST_CODE -> {
//                onStoragePermissionCallback.onPermissionFailure()
                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
                    AppSettingsDialog.Builder(this).build().show()
                }
            }
        }
    }

    override fun requestStoragePermissions(onStoragePermissionCallback: OnStoragePermissionCallback) {
        onStorageCallback = onStoragePermissionCallback
        requestStoragePermission()
    }

    override fun hasStoragePermission(): Boolean {
//        return areStoragePermissionsGranted()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            STORAGE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK){
                    onStorageCallback.onPermissionSuccess()
                }else{
                    onStorageCallback.onPermissionFailure()
                }
            }
        }

    }

}