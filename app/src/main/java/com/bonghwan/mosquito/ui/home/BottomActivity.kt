package com.bonghwan.mosquito.ui.home

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
            App.getInstanceApp().makeText("알림 권한을 거부하셨습니다.")
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
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