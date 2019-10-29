package by.overpass.gather.commons.android

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun View.toast(@StringRes stringRes: Int, length: Int = Toast.LENGTH_SHORT) {
    context.toast(stringRes, length)
}

fun Context.toast(@StringRes stringRes: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, length).show()
}

fun Fragment.toast(@StringRes stringRes: Int, length: Int = Toast.LENGTH_SHORT) {
    requireContext().toast(stringRes, length)
}

fun View.toast(string: String, length: Int = Toast.LENGTH_SHORT) {
    context.toast(string, length)
}

fun Context.toast(string: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, length).show()
}

fun Fragment.toast(string: String, length: Int = Toast.LENGTH_SHORT) {
    requireContext().toast(string, length)
}