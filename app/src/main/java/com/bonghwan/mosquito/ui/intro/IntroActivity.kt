package com.bonghwan.mosquito.ui.intro

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import com.bonghwan.mosquito.App
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseActivity
import com.bonghwan.mosquito.data.api.dto.KakaoProfileResponseDto
import com.bonghwan.mosquito.data.models.LoginManager
import com.bonghwan.mosquito.data.models.SecurePreferencesHelper
import com.bonghwan.mosquito.databinding.ActivityIntroBinding
import com.bonghwan.mosquito.rx.AutoClearedDisposable
import com.bonghwan.mosquito.ui.home.BottomActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    private lateinit var viewModel: IntroViewModel
    private lateinit var kakaoProfileResponseDto: KakaoProfileResponseDto
    private var isLoggedIn: Boolean? = null
    private var isAutoLogin: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isAutoLogin = intent.getBooleanExtra("auto-login", true)
        autoLogin()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (isLoggedIn != null) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        if (isLoggedIn as Boolean) {
                            startMain()
                        }
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )
    }

    override fun init() {
        viewModel = ViewModelProvider(this)[IntroViewModel::class.java]
        binding.apply {
            viewModel = this@IntroActivity.viewModel
        }

        initView()

        viewModel.error.observe(this) {
            if (it != null) Log.d(TAG, it.toString())
            isLoggedIn = false
        }

        viewModel.kakaoProfile.observe(this) {
            if (it != null) {
                // 카카오 회원정보를 가져오면 해당 정보로 가입된 계정이 있는지 확인
                Log.i("KakaoLogin", "모기경보 가입 정보를 불러옵니다")
                kakaoProfileResponseDto = it
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getAccount(kakaoProfileResponseDto.id.toString())
                }
            }
        }

        viewModel.accountResponse.observe(this) {
            // 이미 가입된 계정이면 로그인 처리, else인 경우 회원가입 처리
            if (it) {
                Log.i("Login", "이미 가입된 계정입니다.")
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.login(
                        kakaoProfileResponseDto.id.toString(),
                        kakaoProfileResponseDto.id.toString()
                    )
                }
            } else {
                Log.i("Login", "회원가입을 시작합니다.")
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.createAccount(
                        kakaoProfileResponseDto.id.toString(),
                        kakaoProfileResponseDto.id.toString(),
                        kakaoProfileResponseDto.properties?.nickname ?: "사용자"
                    )
                }
            }
        }

        viewModel.createResponse.observe(this) {
            CoroutineScope(Dispatchers.Main).launch {
                Log.i("Login", "회원가입 성공")
                viewModel.login(
                    kakaoProfileResponseDto.id.toString(),
                    kakaoProfileResponseDto.id.toString()
                )
            }
        }

        viewModel.loginResponse.observe(this) {
            if (it != null) {
                Log.i("Login", "로그인 성공")
                App.getInstanceApp().makeText("로그인 성공")
                CoroutineScope(Dispatchers.Main).launch {
                    LoginManager.login(it)
                    SecurePreferencesHelper.saveRefreshToken(
                        this@IntroActivity,
                        it.token.refreshToken
                    )
                    Log.d("refreshToken", it.token.refreshToken)
                    startActivity(Intent(this@IntroActivity, BottomActivity::class.java))
                }
            }
        }

        viewModel.autoLoginResponse.observe(this) {
            if (it != null) {
                Log.i("Login", "로그인 성공")
                App.getInstanceApp().makeText("로그인 성공")
                CoroutineScope(Dispatchers.Main).launch {
                    LoginManager.login(it)
                    SecurePreferencesHelper.saveRefreshToken(
                        this@IntroActivity,
                        it.token.refreshToken
                    )
                    Log.d("refreshToken", it.token.refreshToken)
                    isLoggedIn = true
                }
            } else {
                isLoggedIn = false
            }
        }
    }

    private fun initView() = with(binding) {
        btLoginKakao.setOnClickListener {
            loginWithKakao()
        }
    }

    private fun autoLogin() {
        val refreshToken = SecurePreferencesHelper.getRefreshToken(this)
        if (refreshToken != null && isAutoLogin) {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.autoLogin(refreshToken)
            }
        } else {
            isLoggedIn = false
        }
    }

    private fun startMain() {
        startActivity(Intent(this, BottomActivity::class.java))
        finish()
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getKakaoProfile(accessToken = token.accessToken)
            }
        }
    }

    private fun loginWithKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.getKakaoProfile(accessToken = token.accessToken)
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}