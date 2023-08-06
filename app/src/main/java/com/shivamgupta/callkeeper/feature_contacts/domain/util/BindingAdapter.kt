package com.shivamgupta.callkeeper.feature_contacts.domain.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider

@BindingAdapter("setProfileBackgroundColor")
fun bindProfileBackgroundColor(imageView: ShapeableImageView, colorRes: Int) {
    imageView.setBackgroundColor(ResourceProvider.getColor(colorRes))
}

@BindingAdapter("changeIconOnClick")
fun bindChangeIconOnClick(imageView: ImageView, isChecked: Boolean) {

    imageView.setImageResource(
        if (isChecked) {
            imageView.tag = "checked"
            R.drawable.ic_circle_check
        } else {
            imageView.tag = "unchecked"
            R.drawable.ic_circle
        }
    )

    imageView.setOnClickListener {
        imageView.tag = if (imageView.tag == "unchecked") "checked" else "unchecked"
        imageView.setImageResource(if (imageView.tag == "unchecked") R.drawable.ic_circle else R.drawable.ic_circle_check)
    }
}

@BindingAdapter("setProfileIconText")
fun bindSetProfileIconText(textView: MaterialTextView, fullName: String) {
    val dataList = fullName.split(" ")
    if (dataList.size <= 2) {
        textView.text = "${dataList[0].toCharArray()[0]} ${dataList[1].toCharArray()[0]}"
    }
}