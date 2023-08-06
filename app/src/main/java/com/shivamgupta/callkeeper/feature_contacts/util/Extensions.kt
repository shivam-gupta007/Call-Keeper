package com.shivamgupta.callkeeper.feature_contacts.util

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Fragment.showToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Any?.printToLog(tag: String = "DEBUG_LOG") {
    Log.d(tag, toString())
}

//View Extensions
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Activity.showSnackBar(text: String, rootLayout: View) {
    Snackbar.make(
        rootLayout,
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Fragment.showSnackBar(text: String, rootLayout: View) {
    Snackbar.make(
        rootLayout,
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}


//TextInputLayout Extensions
var TextInputLayout.textValue: String
    get() = editText?.text.toString()
    set(value) {
        editText?.setText(value)
    }

fun TextInputLayout.setErrorMessage(message: String) {
    error = message
    isErrorEnabled = true
    editText?.requestFocus()

    afterTextChanged {
        error = null
        isErrorEnabled = false
    }

}

fun TextInputLayout.afterTextChanged(afterTextChanged: (String) -> Unit) {
    editText?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s.toString())
        }
    })
}

fun ViewModel.launchIO(
    block: suspend CoroutineScope.() -> Unit
) {
    viewModelScope.launch(Dispatchers.IO) {
        block
    }
}

fun LifecycleOwner.launchMain(
    block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(Dispatchers.Main) {
        block
    }
}

