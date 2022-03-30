package com.example.appscreentrack.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.appscreentrack.R
import com.google.android.material.appbar.AppBarLayout

class CustomToolbar @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {
    private lateinit var imgBack: ImageView
    private lateinit var title: TextView
    var appBarText: String = "Usage Stats"
        set(value) {
            title.text = value
            field = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_toolbar, this, true)
        if (attributeSet != null) {
            val obtainStyledAttributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.CustomToolbar)
            val string =
                obtainStyledAttributes.getString(R.styleable.CustomToolbar_CustomToolbar_title)

            imgBack = findViewById(R.id.imageViewBack)
            title = findViewById(R.id.textViewToolbarTitle)

            if (string != null) {
                title.text = string
                title.textSize = 20f
            }
            val background = obtainStyledAttributes.getColor(
                R.styleable.CustomToolbar_CustomToolbar_background,
                ContextCompat.getColor(context, R.color.white)
            )
            (findViewById<AppBarLayout>(R.id.appBarLayout)).setBackgroundColor(background)

            val titleColor = obtainStyledAttributes.getColor(
                R.styleable.CustomToolbar_CustomToolbar_title_color,
                ContextCompat.getColor(context, R.color.white)
            )
            (findViewById<TextView>(R.id.textViewToolbarTitle)).setTextColor(titleColor)
            (findViewById<ImageView>(R.id.imageViewBack)).setColorFilter(titleColor)

            imgBack = findViewById(R.id.imageViewBack)
            obtainStyledAttributes.recycle()
        }
        isClickable = true
        isFocusable = true
    }

    fun imgBackButtonClick(listener: OnClickListener) {
        imgBack.setOnClickListener(listener)
    }
}