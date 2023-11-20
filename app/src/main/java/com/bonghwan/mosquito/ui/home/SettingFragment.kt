package com.bonghwan.mosquito.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bonghwan.mosquito.App
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseFragment
import com.bonghwan.mosquito.data.api.dto.ReqFcmToken
import com.bonghwan.mosquito.data.models.LoginManager
import com.bonghwan.mosquito.data.models.SecurePreferencesHelper
import com.bonghwan.mosquito.databinding.FragmentSettingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private lateinit var viewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun init() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.apply {
            account = LoginManager.getCurrentUser()?.account
            viewModel = this@SettingFragment.viewModel
        }
        initView()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    val dialog = PermissionDialog.newInstance()
                    dialog.setDismissCallback {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "알림권한이 거부되었습니다. 확인을 누르면 설정 화면으로 이동합니다.",
                            Snackbar.LENGTH_SHORT
                        ).setAction("확인") {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${requireContext().packageName}"))
                            startActivity(intent)
                        }.show()
                    }
                    dialog.show(childFragmentManager, "permission")
                } else {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "알림권한이 거부되었습니다. 확인을 누르면 설정 화면으로 이동합니다.",
                        Snackbar.LENGTH_SHORT
                    ).setAction("확인") {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${requireContext().packageName}"))
                        startActivity(intent)
                    }.show()
                }
            }
        }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initView() = with(binding) {
        layoutNotificationTest.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel?.testNotification()
            }
        }
        switchNotification.apply {
            isChecked =
                (SecurePreferencesHelper.getNotification(requireContext())!! and (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED))
            setOnCheckedChangeListener { _, isChecked ->
                Log.d("isChecked", isChecked.toString())
                if (isChecked) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        // device_token 추가
                        CoroutineScope(Dispatchers.IO).launch {
                            val token: String? = requestDeviceToken()
                            if (token != null) {
                                SecurePreferencesHelper.setNotification(requireContext(), true)
                                viewModel?.uploadToken(ReqFcmToken(deviceToken = token))
                            } else {
                                this@apply.isChecked = false
                                App.getInstanceApp().makeText("기능 활성화에 실패하였습니다.")
                            }
                        }
                    } else {
                        this.isChecked = false
                        askNotificationPermission()
                    }
                } else {
                    // device_token 삭제
                    CoroutineScope(Dispatchers.IO).launch {
                        val token: String? = requestDeviceToken()
                        if (token != null) {
                            SecurePreferencesHelper.setNotification(requireContext(), false)
                            viewModel?.deleteToken(token)
                        } else {
                            this@apply.isChecked = true
                            App.getInstanceApp().makeText("기능 비활성화에 실패하였습니다.")
                        }
                    }
                }
            }
        }
    }

    private suspend fun requestDeviceToken(): String? {
        return withContext(Dispatchers.IO) {
            try {
                // FCM 토큰을 비동기적으로 얻기
                return@withContext FirebaseMessaging.getInstance().token.await<String?>()
            } catch (e: Exception) {
                // 실패한 경우 또는 예외가 발생한 경우
                return@withContext null
            }
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}