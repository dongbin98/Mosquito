package com.bonghwan.mosquito.util

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.bonghwan.mosquito.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.DayOfWeek

/* 일요일 날짜의 색상을 설정하는 클래스 */
class SundayDecorator : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        val sunday = day.date.with(DayOfWeek.SUNDAY).dayOfMonth
        return sunday == day.day
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(object:ForegroundColorSpan(Color.RED){})
    }
}

/* 토요일 날짜의 색상을 설정하는 클래스 */
class SaturdayDecorator : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        val saturday = day.date.with(DayOfWeek.SATURDAY).dayOfMonth
        return saturday == day.day
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(object:ForegroundColorSpan(Color.BLUE){})
    }
}

class TodayDecorator(context: Context): DayViewDecorator {
    private val drawable = ContextCompat.getDrawable(context, R.drawable.calendar_circle_blue)
    private var date = CalendarDay.today()
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day.equals(date)
    }
    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable!!)
    }
}