package com.bonghwan.mosquito.mpandroidchart

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.bonghwan.mosquito.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val text: TextView = findViewById<TextView>(R.id.markerText) as TextView

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        text.text = "" + e?.y
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height - 20f)
    }
}