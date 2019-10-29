package by.overpass.gather.commons.android

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(@StringRes stringRes: Int, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, stringRes, length).show()
}

fun View.snackbar(string: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, string, length).show()
}

fun Fragment.snackbar(@StringRes stringRes: Int, length: Int = Snackbar.LENGTH_SHORT) {
    requireView().snackbar(stringRes, length)
}

fun Fragment.snackbar(string: String, length: Int = Snackbar.LENGTH_SHORT) {
    requireView().snackbar(string, length)
}