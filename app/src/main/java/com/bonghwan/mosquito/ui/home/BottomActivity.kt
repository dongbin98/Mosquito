package com.bonghwan.mosquito.ui.home

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bonghwan.mosquito.App
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseActivity
import com.bonghwan.mosquito.data.api.dto.ReqFcmToken
import com.bonghwan.mosquito.data.models.LoginManager
import com.bonghwan.mosquito.databinding.ActivityBottomBinding
import com.bonghwan.mosquito.ui.intro.IntroActivity
import com.bonghwan.mosquito.ui.intro.IntroViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomActivity : BaseActivity<ActivityBottomBinding>(R.layout.activity_bottom) {

    private var valueFragment: ValueFragment? = null
    private var editFragment: EditFragment? = null
    private var settingFragment: SettingFragment? = null

    private var time: Long = 0

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - time >= 2000) {
                time = System.currentTimeMillis()
                App.getInstanceApp().makeText("한번 더 클릭 시 처음 화면으로 이동합니다.")
            } else if (System.currentTimeMillis() - time < 2000) {
                LoginManager.logout()
                val intent = Intent(this@BottomActivity, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("auto-login", false)
                startActivity(intent)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(),) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                val dialog = PermissionDialog.newInstance()
                dialog.setDismissCallback {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "알림권한이 거부되었습니다. 확인을 누르면 설정 화면으로 이동합니다.",
                        Snackbar.LENGTH_SHORT
                    ).setAction("확인") {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${packageName}"))
                        startActivity(intent)
                    }.show()
                }
                dialog.show(supportFragmentManager, "permission")
            } else {
//                Snackbar.make(
//                    findViewById(android.R.id.content),
//                    "알림권한이 거부되었습니다. 확인을 누르면 설정 화면으로 이동합니다.",
//                    Snackbar.LENGTH_SHORT
//                ).setAction("확인") {
//                    val intent =
//                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${packageName}"))
//                    startActivity(intent)
//                }.show()
            }
        }
    }

    override fun init() {
        initBottomNavigation()
        makeStatusBarTransparent()
        askNotificationPermission()
        binding.bottomNavigationView.setPadding(0, 0, 0, this.navigationHeight())
        binding.layoutParent.setPadding(0, this.statusBarHeight(), 0, 0)
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initBottomNavigation() = with(binding) {
        valueFragment = ValueFragment()
        supportFragmentManager.beginTransaction().replace(fragmentContainer.id, valueFragment!!).commit()

        bottomNavigationView.apply {
            selectedItemId = R.id.valueFragment
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.valueFragment -> {
                        if(valueFragment == null) {
                            valueFragment = ValueFragment()
                            supportFragmentManager.beginTransaction().add(fragmentContainer.id, valueFragment!!).commit()
                        } else {
                            supportFragmentManager.beginTransaction().show(valueFragment!!).commit()
                        }
                        if(editFragment != null) supportFragmentManager.beginTransaction().hide(editFragment!!).commit()
                        if(settingFragment != null) supportFragmentManager.beginTransaction().hide(settingFragment!!).commit()

                        return@setOnItemSelectedListener true
                    }
                    R.id.editFragment -> {
                        if(editFragment == null) {
                            editFragment = EditFragment()
                            supportFragmentManager.beginTransaction().add(fragmentContainer.id, editFragment!!).commit()
                        } else {
                            supportFragmentManager.beginTransaction().show(editFragment!!).commit()
                        }
                        if(valueFragment != null) supportFragmentManager.beginTransaction().hide(valueFragment!!).commit()
                        if(settingFragment != null) supportFragmentManager.beginTransaction().hide(settingFragment!!).commit()

                        return@setOnItemSelectedListener true
                    }
                    R.id.settingFragment -> {
                        if(settingFragment == null) {
                            settingFragment = SettingFragment()
                            supportFragmentManager.beginTransaction().add(fragmentContainer.id, settingFragment!!).commit()
                        } else {
                            supportFragmentManager.beginTransaction().show(settingFragment!!).commit()
                        }
                        if(editFragment != null) supportFragmentManager.beginTransaction().hide(editFragment!!).commit()
                        if(valueFragment != null) supportFragmentManager.beginTransaction().hide(valueFragment!!).commit()

                        return@setOnItemSelectedListener true
                    }
                    else -> {
                        return@setOnItemSelectedListener true
                    }
                }
            }
        }
    }
}