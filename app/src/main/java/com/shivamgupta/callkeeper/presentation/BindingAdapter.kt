package com.shivamgupta.callkeeper.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.domain.models.ContactItemUiState
import com.shivamgupta.callkeeper.util.Constants
import com.shivamgupta.callkeeper.util.ResourceProvider
import com.shivamgupta.callkeeper.util.afterTextChanged
import com.shivamgupta.callkeeper.util.truncateStringWithDots
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("handleTextLength")
fun bindHandleTextLength(textView: TextView, maxLengthToShow: Int) {
    val text = textView.text.toString()
    textView.text = text.truncateStringWithDots(maxLengthToShow)
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

@BindingAdapter("formatAsDate")
fun bindFormatAsDate(textView: TextView, timeStamp: Long?){
    timeStamp?.let {
        val currentDate = Date(Timestamp(it).time)
        val desiredDateFormat = SimpleDateFormat(Constants.CALL_LOG_DATE_FORMAT, Locale.getDefault())
        textView.text = desiredDateFormat.format(currentDate)
    }
}

@BindingAdapter("setError")
fun bindSetError(textInputLayout: TextInputLayout, errorMessage: String?){
    if(!errorMessage.isNullOrEmpty()) {
        textInputLayout.apply {
            error = errorMessage
            isErrorEnabled = true
            editText?.requestFocus()

            afterTextChanged {
                error = null
                isErrorEnabled = false
            }
        }
    }
}

@BindingAdapter("enableOrDisableSubmitButton")
fun bindEnableOrDisableSubmitButton(button: MaterialButton, contactItem: ContactItemUiState){
    button.isEnabled = contactItem.name.isNotEmpty() && contactItem.phoneNumber.isNotEmpty()
}
