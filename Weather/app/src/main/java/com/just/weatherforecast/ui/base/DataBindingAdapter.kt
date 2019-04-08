package com.just.weatherforecast.ui.base

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.pwittchen.weathericonview.WeatherIconView


class DataBindingAdapter {
//    @BindingAdapter("app:weatherIconResource")
//    fun setIconResource(imageView: WeatherIconView, resource: String) {
//        imageView.setIconResource(resource)
//    }


    @BindingAdapter("android:text")
    fun setText(text : TextView, resource: String) {
        text.text = "BIND"
    }
}