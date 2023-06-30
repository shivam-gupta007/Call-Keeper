package com.shivamgupta.autocallblocker.feature_contacts.domain.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.shivamgupta.autocallblocker.R
import com.shivamgupta.autocallblocker.feature_contacts.util.ResourceProvider

@BindingAdapter("setProfileBackgroundColor")
fun bindProfileBackgroundColor(imageView: ShapeableImageView, colorRes: Int) {
    imageView.setBackgroundColor(ResourceProvider.getColor(colorRes))
}

@BindingAdapter("changeIconOnClick")
fun bindChangeIconOnClick(imageView: ImageView, isChecked: Boolean) {
    imageView.setOnClickListener {
        imageView.tag = if (imageView.tag == "unchecked") "checked" else "unchecked"
        imageView.setImageResource(if (imageView.tag == "unchecked") R.drawable.ic_circle else R.drawable.ic_circle_check)
    }
}