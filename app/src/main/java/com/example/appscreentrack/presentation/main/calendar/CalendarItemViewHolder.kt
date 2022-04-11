package com.example.appscreentrack.presentation.main.calendar

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appscreentrack.databinding.DateItemBinding

class CalendarItemViewHolder(binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val calendarDay = binding.textViewDay
    val calendarDate = binding.textViewDayNum
    val calendarMonth = binding.textViewMonth
    val sliderImageViewPremium: ImageView = binding.imageViewPremium
    val constraintLayoutHorizontal: ConstraintLayout = binding.constraintLayoutHorizontal
}