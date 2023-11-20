package com.bonghwan.mosquito.ui.home

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.bonghwan.mosquito.databinding.DialogPermissionBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PermissionDialog : DialogFragment() {
    private var _binding: DialogPermissionBinding? = null
    private val binding get() = _binding!!

    private var dismissCallback: (() -> Unit)? = null

    companion object {
        fun newInstance(): PermissionDialog {
            return PermissionDialog()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            dismiss()
        } else {
            dismiss()
            dismissCallback?.invoke()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogPermissionBinding.inflate(inflater, container, false)

        // 레이아웃 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvConfirm.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                delay(200)
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        return binding.root
    }

    fun setDismissCallback(callback: () -> Unit) {
        dismissCallback = callback
    }

    private fun dialogFragmentResize() {
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = windowManager.currentWindowMetrics
        val deviceWidth = size.bounds.width()
        params?.width = (deviceWidth * 0.85).toInt()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onResume() {
        super.onResume()
        dialogFragmentResize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}