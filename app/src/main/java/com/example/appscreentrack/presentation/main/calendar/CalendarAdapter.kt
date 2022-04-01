package com.example.appscreentrack.presentation.main.calendar

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appscreentrack.R
import com.example.appscreentrack.databinding.DateItemBinding
import kotlin.collections.ArrayList

class CalendarAdapter(val callback: ((pos: Int) -> Unit)) :
    RecyclerView.Adapter<CalendarItemViewHolder>() {
    private val data: ArrayList<CalendarDateModel> = ArrayList();
    lateinit var constraintLayoutHorizontal: ConstraintLayout
    lateinit var whiteOutLineDrawable: Drawable
    lateinit var fillOutLineDrawable: Drawable
    lateinit var blueOutLineDrawable: Drawable

    companion object {
        var focusedItem: Int? = 9
    }

    fun setFillOutLine(position: Int) {
        constraintLayoutHorizontal.background = fillOutLineDrawable
        focusedItem = position
        notifyDataSetChanged()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemViewHolder {
        val binding = CalendarItemViewHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
        constraintLayoutHorizontal = binding.constraintLayoutHorizontal
        whiteOutLineDrawable =
            binding.itemView.context.getDrawable(R.drawable.horizontalpicker_outline_white)!!
        fillOutLineDrawable =
            binding.itemView.context.getDrawable(R.drawable.horizontalpicker_outline_empty)!!
        blueOutLineDrawable =
            binding.itemView.context.getDrawable(R.drawable.horizontalpicker_outline_blue)!!
        return binding
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        with(holder) {
            calendarDay.text = data[position].dayOfMonth
            calendarDate.text = data[position].dayOfWeek
            calendarMonth.text = data[position].month
            sliderImageViewPremium.bringToFront()
            sliderImageViewPremium.invalidate()

            premiumVisibilityControl(
                position,
                sliderImageViewPremium
            )
            this@CalendarAdapter.constraintLayoutHorizontal = constraintLayoutHorizontal
            constraintLayoutHorizontal.setOnClickListener {
                focusedItem = position
                callback(position)
            }

            if (focusedItem != position) {
                constraintLayoutHorizontal.background =
                    whiteOutLineDrawable

                calendarDay.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.text_color_gray)
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.text_color_gray)
                )
                calendarMonth.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.text_color_gray)
                )

            } else {
                constraintLayoutHorizontal.background =
                    fillOutLineDrawable
                calendarDay.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.date_item_color)
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.date_item_color)
                )
                calendarMonth.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.date_item_color)
                )
            }
        }
    }

    fun setData(data: ArrayList<CalendarDateModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    private fun premiumVisibilityControl(mPosition: Int, icPremium: ImageView) {
        when (mPosition) {
            8, 9 -> icPremium.visibility = View.INVISIBLE
            else -> icPremium.visibility = View.VISIBLE
        }
    }
}