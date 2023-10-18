package com.bonghwan.mosquito.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
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
            viewModel.setReady()
        }

        initView()

        viewModel.toast.observe(this) {
            App.getInstanceApp().makeText(it)
        }
    }

    private fun initView() = with(binding) {
        // 뷰 이벤트 리스너 할당
    }
}