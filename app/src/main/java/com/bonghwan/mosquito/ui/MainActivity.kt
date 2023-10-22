package com.bonghwan.mosquito.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.bonghwan.mosquito.App
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseActivity
import com.bonghwan.mosquito.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (viewModel.isReady) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 2.5f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 2.5f, 1f)

            ObjectAnimator.ofPropertyValuesHolder(splashScreenView.iconView, scaleX, scaleY).run {
                interpolator = AnticipateInterpolator()
                duration = 1500L
                doOnEnd {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        splashScreenView.remove()
                    }
                }
                start()
            }
        }

        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this

        init()
    }

    override fun init() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.apply {
            viewModel = this@MainActivity.viewModel
        }

        CoroutineScope(Dispatchers.Main).launch {
            val today = LocalDate.now(ZoneId.of("Asia/Seoul"))
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = today.format(formatter)
            viewModel.getMosquitoStatus(formattedDate)
        }

        initView()

        viewModel.error.observe(this) {
            App.getInstanceApp().makeText(it.toString())
        }

        viewModel.mosquitoLiveData.observe(this) {
            binding.data = it.mosquitoStatus?.row?.get(0)?.copy()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() = with(binding) {
        // 뷰 이벤트 리스너 할당
        ivSearch.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel?.getMosquitoStatus(etSearch.text.toString())
            }
        }

        etSearch.apply {
            isFocusable = false
            val today = GregorianCalendar()
            var tYear = today.get(Calendar.YEAR)
            var tMonth = today.get(Calendar.MONTH)
            var tDay = today.get(Calendar.DATE)

            setOnClickListener {
                DatePickerDialog(context, { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val formattedDate = dateFormat.format(calendar.time)
                    setText(formattedDate)
                    tYear = year
                    tMonth = month
                    tDay = dayOfMonth
                }, tYear, tMonth, tDay).show()
            }
        }
    }
}