package com.bonghwan.mosquito.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.bonghwan.mosquito.App
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseActivity
import com.bonghwan.mosquito.databinding.ActivityBottomBinding
import com.bonghwan.mosquito.ui.intro.IntroActivity

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
//                LoginManager.logout()
                val intent = Intent(this@BottomActivity, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("auto-login", false)
                startActivity(intent)
            }
        }
    }

    override fun init() {
        initBottomNavigation()
        makeStatusBarTransparent()
        binding.bottomNavigationView.setPadding(0, 0, 0, this.navigationHeight())
        binding.layoutParent.setPadding(0, this.statusBarHeight(), 0, 0)
        onBackPressedDispatcher.addCallback(this, callback)
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