package com.example.jsonconsig

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val REQUEST_READ_EXTERNAL = 1
    var media : Media = Media("aaa", -1)
    lateinit var config : Config

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.i("onRequestPermissionsResult")


        if ( requestCode == REQUEST_READ_EXTERNAL) {
            config = Config("/storage/emulated/0/JMGames/configJMGames.json")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        setContentView(R.layout.activity_main)


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Timber.i("--------shouldShowRequestPermissionRationale")
        }


        Timber.i("----------------------Filename: [%s]  Volume:%d", media.filename, media.volume)

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED ) {
            Timber.i("Sem permissao")

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL)
        } else {
            config = Config("/storage/emulated/0/JMGames/configJMGames.json")
        }


    }





}
