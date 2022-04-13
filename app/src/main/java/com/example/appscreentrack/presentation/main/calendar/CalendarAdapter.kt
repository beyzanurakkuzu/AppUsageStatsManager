package com.example.appscreentrack.presentation.main.calendar

import android.content.Context
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

class CalendarAdapter(val context: Context) :
    RecyclerView.Adapter<CalendarItemViewHolder>() {
    private val data: ArrayList<CalendarDateModel> = ArrayList();
    lateinit var constraintLayoutHorizontal: ConstraintLayout
    var callback: Callback? = null
    private lateinit var whiteOutLineDrawable: Drawable
    private lateinit var fillOutLineDrawable: Drawable

    companion object {
        var focusedItem: Int? = 9
    }

    fun setFillOutLine(position: Int) {
        constraintLayoutHorizontal.background = fillOutLineDrawable
        focusedItem = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemViewHolder {
        val binding = CalendarItemViewHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

        constraintLayoutHorizontal = binding.constraintLayoutHorizontal
        whiteOutLineDrawable =
            ContextCompat.getDrawable(context, R.drawable.horizontalpicker_outline_white)!!
        fillOutLineDrawable =
            ContextCompat.getDrawable(context, R.drawable.horizontalpicker_outline_empty)!!
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

            constraintLayoutHorizontal.setOnClickListener {
                focusedItem = position
                callback?.onItemClicked(position)
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

    interface Callback {
        fun onItemClicked(position: Int)
    }

    private fun premiumVisibilityControl(position: Int, icPremium: ImageView) {
        when (position) {
            8, 9 -> icPremium.visibility = View.INVISIBLE
            else -> icPremium.visibility = View.VISIBLE
        }
    }
}