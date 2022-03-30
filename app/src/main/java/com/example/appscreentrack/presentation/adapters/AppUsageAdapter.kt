package com.example.appscreentrack.presentation.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appscreentrack.R
import com.example.appscreentrack.presentation.main.utils.Utils
import com.example.appscreentrack.databinding.UsageRowItemBinding
import com.example.appscreentrack.domain.models.AppUsage

class AppsUsageAdapter(
) : ListAdapter<AppUsage, AppsUsageAdapter.ViewHolder>(UsageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = UsageRowItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(var binding: UsageRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: AppUsage) {
            with(binding) {
                textViewTotalUsageTime.text = Utils.getUsageTimeString(item.totalTime)
                imageViewAppIcon.setImageDrawable(item.app.iconDrawable)
                textViewAppName.text = item.app.appName
                val dif = if (item.differenceBetweenOneDayAgoTime<0) (-1L)*item.differenceBetweenOneDayAgoTime else item.differenceBetweenOneDayAgoTime
                val difference = Utils.getUsageTimeString(dif)
                if (item.oneDayAgoTime != 0L) {
                    textViewUsageToYesterday.text = difference
                    imageViewRow.isGone = false
                    if (item.oneDayAgoTime < item.totalTime) {
                        imageViewRow.setImageResource(R.drawable.increase)
                    } else {
                        imageViewRow.setImageResource(R.drawable.decrease)
                    }
                } else {
                    textViewUsageToYesterday.text = "0"
                    imageViewRow.isGone = true
                }

            }

        }
    }

    class UsageDiffUtil : DiffUtil.ItemCallback<AppUsage>() {
        override fun areItemsTheSame(oldItem: AppUsage, newItem: AppUsage) =
            oldItem.app.packageName == newItem.app.packageName

        override fun areContentsTheSame(oldItem: AppUsage, newItem: AppUsage) =
            (oldItem.app.packageName == newItem.app.packageName
                    && oldItem.totalTime == newItem.totalTime)

    }
}