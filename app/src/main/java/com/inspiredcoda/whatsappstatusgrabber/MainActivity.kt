package com.inspiredcoda.whatsappstatusgrabber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.inspiredcoda.whatsappstatusgrabber.data.MainRepository
import com.inspiredcoda.whatsappstatusgrabber.data.database.AppDatabase
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.OnStoragePermissionCallback
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.StatusGrabberInterface
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions.READ_EXTERNAL_STORAGE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions.STORAGE_REQUEST_CODE
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.Permissions.WRITE_EXTERNAL_STORAGE
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.RefreshStatusDirectory
import com.inspiredcoda.whatsappstatusgrabber.utils.callbacks.UserInterfaceListener
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import com.inspiredcoda.whatsappstatusgrabber.viewmodel.MainViewModel
import com.inspiredcoda.whatsappstatusgrabber.viewmodel.MainViewModelFactory
import hendrawd.storageutil.library.StorageUtil
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class MainActivity : AppCompatActivity(), StatusGrabberInterface,
    EasyPermissions.PermissionCallbacks, UserInterfaceListener, RefreshStatusDirectory {

    lateinit var mainViewModel: MainViewModel
    private var permissions = mutableListOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var aboutDialog: AlertDialog

    lateinit var onStorageCallback: OnStoragePermissionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
//        toolbar.setupWithNavController(navController, AppBarConfiguration(
//            navController.graph,
//            null
//        ))

        createAboutDialog()

        val mainViewModelFactory = MainViewModelFactory(MainRepository(AppDatabase.getInstance(this).getStatusDao()))

        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        initiateStatusSearch()
//        requestStoragePermission()


    }

    private fun initiateStatusSearch(){
        val path = StorageUtil.getStorageDirectories(this)
        initiateViewedStatusSearch(path)
        initiateSavedStatusSearch(path)

    }

    private fun initiateViewedStatusSearch(path: Array<String>){
        var file: File? = null
        for (x in path){
            file = File("$x/WhatsApp/Media/.Statuses")
        }
        mainViewModel.loadViewedStatuses(file!!)
    }

    private fun initiateSavedStatusSearch(path: Array<String>){
        var file: File? = null
        for (x in path){
            file = File("$x/WhatsApp Status Grabber")
        }
        mainViewModel.loadSavedStatuses(file!!)
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

    private fun createAboutDialog(){
        aboutDialog = AlertDialog.Builder(this)
            .setView(R.layout.custom_about_layout)
            .setCancelable(true)
            .create()
    }

    private fun displayAboutDialog(){
        aboutDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
//            R.id.refresh -> {
//                initiateStatusSearch()
//            }

            R.id.rate_us -> {
                toast("still in progress...")
            }

            R.id.about_us -> {
                displayAboutDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun refreshStatusDirectories() {
        initiateStatusSearch()
    }


}