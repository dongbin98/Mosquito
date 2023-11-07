package com.bonghwan.mosquito.ui.home

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseFragment
import com.bonghwan.mosquito.data.api.dto.ReqUserData
import com.bonghwan.mosquito.data.models.LoginManager
import com.bonghwan.mosquito.databinding.FragmentEditBinding
import com.bonghwan.mosquito.util.SaturdayDecorator
import com.bonghwan.mosquito.util.SundayDecorator
import com.bonghwan.mosquito.util.TodayDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter


class EditFragment : BaseFragment<FragmentEditBinding>(R.layout.fragment_edit) {
    private lateinit var viewModel: HomeViewModel
    private var selectedDate: String? = null

    override fun init() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.apply {
            viewModel = this@EditFragment.viewModel
        }

        initView()

        viewModel.userDataError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            if (!binding.btSave.isEnabled) {
                binding.btSave.isEnabled = true
            } else if (!binding.btDelete.isEnabled) {
                binding.btDelete.isEnabled = true
            }
        }

        viewModel.userDataCreateLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
            binding.btSave.text = "수정"
            binding.btSave.isEnabled = true
            binding.btDelete.visibility = View.VISIBLE
        }

        viewModel.userDataGetLiveData.observe(viewLifecycleOwner) {
            binding.etCount.setText(it.count.toString())
            binding.btSave.text = "수정"
            binding.btDelete.visibility = View.VISIBLE
        }

        viewModel.userDataUpdateLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show()
            binding.btSave.isEnabled = true
        }

        viewModel.userDataDeleteLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show()
            binding.etCount.setText("")
            binding.btSave.text = "저장"
            binding.btDelete.visibility = View.GONE
            binding.btDelete.isEnabled = true
        }
    }

    private fun initView() = with(binding) {
        calendarView.apply {
            state().edit()
                .setMaximumDate(CalendarDay.from(CalendarDay.today().date)).commit()
            addDecorator(TodayDecorator(requireContext()))
            addDecorator(SaturdayDecorator())
            addDecorator(SundayDecorator())
            setTitleFormatter {
                String.format("%4d년 %2d월", it.year, it.month)
            }

            setOnDateChangedListener { _, date, _ ->
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                this@EditFragment.selectedDate = formatter.format(date.date)
                etCount.setText("")
                btDelete.visibility = View.GONE
                btSave.text = "저장"
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel?.getUserData(LoginManager.getCurrentUser()!!.account.id, this@EditFragment.selectedDate!!)
                }
            }
        }

        btSave.setOnClickListener {
            if (!etCount.text.isNullOrBlank()) {
                btSave.isEnabled = false
                if (btSave.text == "저장") {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel?.createUserData(
                            ReqUserData(
                                LoginManager.getCurrentUser()!!.account.id,
                                this@EditFragment.selectedDate!!,
                                etCount.text.toString()
                            )
                        )
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel?.updateUserData(
                            LoginManager.getCurrentUser()!!.account.id,
                            this@EditFragment.selectedDate!!,
                            etCount.text.toString()
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "몇방 물렸는지 적어주세요", Toast.LENGTH_SHORT).show()
            }
        }

        btDelete.setOnClickListener {
            btDelete.isEnabled = false
            CoroutineScope(Dispatchers.IO).launch {
                viewModel?.deleteUserData(LoginManager.getCurrentUser()!!.account.id, this@EditFragment.selectedDate!!)
            }
        }
    }
}