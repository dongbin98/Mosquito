package com.bonghwan.mosquito.ui.intro

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseActivity
import com.bonghwan.mosquito.databinding.ActivityIntroBinding
import com.bonghwan.mosquito.ui.main.MainActivity
import com.bonghwan.mosquito.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    private lateinit var viewModel: IntroViewModel

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
        viewModel = ViewModelProvider(this)[IntroViewModel::class.java]

        binding.apply {
            viewModel = this@IntroActivity.viewModel
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.isUser()
        }

        initView()
    }

    private fun initView() = with(binding) {
        btLoginKakao.setOnClickListener {

        }

        btNoLogin.setOnClickListener {
            Intent(this@IntroActivity, MainActivity::class.java).run {
                startActivity(this)
            }
        }
    }
}