package com.bonghwan.mosquito.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import com.bonghwan.mosquito.App
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.core.BaseFragment
import com.bonghwan.mosquito.data.models.Status
import com.bonghwan.mosquito.databinding.FragmentValueBinding
import com.bonghwan.mosquito.mpandroidchart.MyMarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ValueFragment : BaseFragment<FragmentValueBinding>(R.layout.fragment_value) {

    private lateinit var viewModel: HomeViewModel

    override fun init() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.apply {
            viewModel = this@ValueFragment.viewModel
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getMosquitoRecent()
            viewModel.getMosquitoWeek()
        }

        initView()

        viewModel.error.observe(this) {
            App.getInstanceApp().makeText(it.toString())
        }

        viewModel.mosquitoLiveData.observe(this) {
            binding.data = it
        }

        viewModel.mosquitoListLiveData.observe(this) {
            initChart(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() = with(binding) {
        // 뷰 이벤트 리스너 할당
    }

    private fun initChart(dataList: List<Status>) = with(binding) {
        val sortedList = dataList.sortedBy { it.date }

        val entryHouseList = mutableListOf<Entry>()
        val entryParkList = mutableListOf<Entry>()
        val entryWaterList = mutableListOf<Entry>()
        val dateList = mutableListOf<String>()

        // 데이터 엔트리 생성
        for ((index, data) in sortedList.withIndex()) {
            entryHouseList.add(Entry(index.toFloat(), data.house))
            entryParkList.add(Entry(index.toFloat(), data.park))
            entryWaterList.add(Entry(index.toFloat(), data.water))
            dateList.add(data.date.substring(5, data.date.length))
        }

        // 섹션 별 라인 데이터 생성
        val chartData = LineData()
        val set1 = LineDataSet(entryHouseList, "주거")
        set1.lineWidth = 2f
        set1.color = requireContext().getColor(R.color.mainYellow)
        set1.setDrawCircles(true)
        set1.circleRadius = 4f
        set1.setCircleColor(requireContext().getColor(R.color.mainYellow))
        set1.setDrawCircleHole(true)
        set1.circleHoleRadius = 3f
        set1.circleHoleColor = requireContext().getColor(R.color.white)
        set1.setDrawValues(false)
        set1.setDrawHighlightIndicators(false)

        val set2 = LineDataSet(entryParkList, "공원")
        set2.lineWidth = 2f
        set2.color = requireContext().getColor(R.color.green)
        set2.setDrawCircles(true)
        set2.circleRadius = 4f
        set2.setCircleColor(requireContext().getColor(R.color.green))
        set2.setDrawCircleHole(true)
        set2.circleHoleRadius = 3f
        set2.circleHoleColor = requireContext().getColor(R.color.white)
        set2.setDrawValues(false)
        set2.setDrawHighlightIndicators(false)

        val set3 = LineDataSet(entryWaterList, "수변")
        set3.lineWidth = 2f
        set3.color = requireContext().getColor(R.color.mainBlue)
        set3.setDrawCircles(true)
        set3.circleRadius = 4f
        set3.setCircleColor(requireContext().getColor(R.color.mainBlue))
        set3.setDrawCircleHole(true)
        set3.circleHoleRadius = 3f
        set3.circleHoleColor = requireContext().getColor(R.color.white)
        set3.setDrawValues(false)
        set3.setDrawHighlightIndicators(false)

        chartData.addDataSet(set1)
        chartData.addDataSet(set2)
        chartData.addDataSet(set3)

        // 하단 X Label
        val xAxis: XAxis = chartMosquito.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dateList)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = requireContext().getColor(R.color.gray2)
        xAxis.gridColor = requireContext().getColor(R.color.gray2)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        // 좌측 Y Label
        val yLAxis: YAxis = chartMosquito.axisLeft
        yLAxis.textSize = 10f
        yLAxis.textColor = requireContext().getColor(R.color.gray2)
        yLAxis.mAxisMinimum = 0f
        yLAxis.mAxisMaximum = 4.5f
        yLAxis.setDrawAxisLine(false)

        // 우측 Y Label
        val yRAxis: YAxis = chartMosquito.axisRight
        yRAxis.setDrawLabels(true)
        yRAxis.textColor = requireContext().getColor(R.color.gray2)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)

        // Marker
        val marker = MyMarkerView(requireContext(), R.layout.marker_form)

        // 차트 데이터 삽입
        chartMosquito.legend.xEntrySpace = 20f
        chartMosquito.data = chartData
        chartMosquito.marker = marker
        chartMosquito.setNoDataText("데이터가 존재하지 않습니다.")
        chartMosquito.setDrawGridBackground(false)
        chartMosquito.setDrawBorders(false)
        chartMosquito.isDoubleTapToZoomEnabled = false
        chartMosquito.setScaleEnabled(false)
        chartMosquito.setPinchZoom(false)
        chartMosquito.isDragEnabled = false
        chartMosquito.description = null
        chartMosquito.setExtraOffsets(10f, 30f, 30f, 10f)
        chartMosquito.animateX(1000)
        chartMosquito.animateY(1000)

        chartMosquito.invalidate()
    }
}