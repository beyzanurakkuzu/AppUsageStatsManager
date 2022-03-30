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

class CalendarAdapter(val callback:((pos:Int)->Unit)) :
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


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        holder.calendarDay.text = data[position].dayOfMonth
        holder.calendarDate.text = data[position].dayOfWeek
        holder.calendarMonth.text = data[position].month
        holder.sliderImageViewPremium.bringToFront()
        holder.sliderImageViewPremium.invalidate()

        premiumVisibilityControl(
            position,
            holder.sliderImageViewPremium
        )
        constraintLayoutHorizontal = holder.constraintLayoutHorizontal
        holder.constraintLayoutHorizontal.setOnClickListener {
            focusedItem = position

            callback(position)
        }

         if(focusedItem != position) {
            holder.constraintLayoutHorizontal.background =
                whiteOutLineDrawable

            holder.calendarDay.setTextColor(
                ContextCompat.getColor(holder.itemView.context,R.color.text_color_gray))
            holder.calendarDate.setTextColor(
                ContextCompat.getColor(holder.itemView.context,R.color.text_color_gray))
            holder.calendarMonth.setTextColor(
                ContextCompat.getColor(holder.itemView.context,R.color.text_color_gray))

        }
        else{
            holder.constraintLayoutHorizontal.background =
                fillOutLineDrawable
            holder.calendarDay.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.date_item_color))
            holder.calendarDate.setTextColor(
                ContextCompat.getColor(holder.itemView.context,R.color.date_item_color))
            holder.calendarMonth.setTextColor(
                ContextCompat.getColor(holder.itemView.context,R.color.date_item_color))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<CalendarDateModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
    private fun premiumVisibilityControl(mPosition: Int, icPremium: ImageView) {
        when (mPosition) {
            8 -> {
                icPremium.visibility = View.INVISIBLE
            }
            9 -> {
                icPremium.visibility = View.INVISIBLE
            }
            else -> {
                icPremium.visibility = View.VISIBLE

            }
        }
    }
}