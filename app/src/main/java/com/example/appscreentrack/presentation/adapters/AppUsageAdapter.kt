package com.example.appscreentrack.presentation.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appscreentrack.R
import com.example.appscreentrack.presentation.main.utils.TimeUtils
import com.example.appscreentrack.databinding.UsageRowItemBinding
import com.example.appscreentrack.domain.models.AppUsageStatsModel

class AppsUsageAdapter(
) : ListAdapter<AppUsageStatsModel, AppsUsageAdapter.ViewHolder>(UsageDiffUtil()) {

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
        fun bind(item: AppUsageStatsModel) {
            //app usage row items
            with(binding) {
                totalUsageTimeTV.text = TimeUtils.getUsageTimeString(item.totalTime)
                appIcon.setImageDrawable(item.app.iconDrawable)
                appNameTv.text = item.app.appName
                //one day ago -> usageToYesterdayTV= today-yesterday
                val dif =
                    if (item.differenceBetweenOneDayAgoTime < 0) (-1L) * item.differenceBetweenOneDayAgoTime
                    else item.differenceBetweenOneDayAgoTime

                val difference = TimeUtils.getUsageTimeString(dif)

                if (item.oneDayAgoTime != 0L) {
                    usageToYesterdayTV.text = difference
                    imageViewRow.isGone = false

                    if (item.oneDayAgoTime < item.totalTime) imageViewRow.setImageResource(R.drawable.increase)
                    else imageViewRow.setImageResource(R.drawable.decrease)
                }
                else {
                    usageToYesterdayTV.text = "0"
                    imageViewRow.isGone = true
                }
            }
        }
    }

    class UsageDiffUtil : DiffUtil.ItemCallback<AppUsageStatsModel>() {
        override fun areItemsTheSame(oldItem: AppUsageStatsModel, newItem: AppUsageStatsModel) =
            oldItem.app.packageName == newItem.app.packageName

        override fun areContentsTheSame(oldItem: AppUsageStatsModel, newItem: AppUsageStatsModel) =
            (oldItem.app.packageName == newItem.app.packageName
                    && oldItem.totalTime == newItem.totalTime)
    }
}