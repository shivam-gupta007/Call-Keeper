package com.shivamgupta.callkeeper.feature_contacts.domain.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider

@BindingAdapter("handleTextLength")
fun bindHandleTextLength(textView: TextView, maxLengthToShow: Int) {
    if (textView.text.length > maxLengthToShow) {
        val text = textView.text
        textView.text = text.substring(startIndex = 0, endIndex = maxLengthToShow).plus("....")
    }
}

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
}

@BindingAdapter("setProfileIconText")
fun bindSetProfileIconText(textView: TextView, fullName: String) {
    val dataList = fullName.split(" ")
    textView.text = if (dataList.size >= 2) {
        "${dataList[0].toCharArray()[0]} ${dataList[1].toCharArray()[0]}"
    } else {
        "${dataList[0].toCharArray()[0]}"
    }
}

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, shouldShow: Boolean) {
    view.visibility = if (shouldShow) View.VISIBLE else View.GONE
}

@BindingAdapter("updateBtnTextOnMode")
fun bindBtnText(button: MaterialButton, shouldUpdateContactDetails: Boolean) {
    button.text = ResourceProvider.getString(
        if (shouldUpdateContactDetails) R.string.label_update else R.string.label_add
    )
}