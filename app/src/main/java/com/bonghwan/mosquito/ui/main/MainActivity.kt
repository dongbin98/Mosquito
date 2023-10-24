package com.bonghwan.mosquito.ui.main

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bonghwan.mosquito.App
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseActivity
import com.bonghwan.mosquito.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var viewModel: MainViewModel

    override fun init() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.apply {
            viewModel = this@MainActivity.viewModel
        }

        CoroutineScope(Dispatchers.Main).launch {
            val today = LocalDate.now(ZoneId.of("Asia/Seoul"))
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = today.format(formatter)
            viewModel.getMosquitoRecent()
        }

        initView()

        viewModel.error.observe(this) {
            App.getInstanceApp().makeText(it.toString())
        }

        viewModel.mosquitoLiveData.observe(this) {
            binding.data = it
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() = with(binding) {
        // 뷰 이벤트 리스너 할당
    }
}