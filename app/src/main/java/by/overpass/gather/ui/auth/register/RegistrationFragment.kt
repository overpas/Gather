package by.overpass.gather.ui.auth.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import by.overpass.gather.ui.base.BaseFragmentKt

abstract class RegistrationFragment<VM : ViewModel> : BaseFragmentKt<VM>() {

    protected var registrationController: RegistrationController? = null

    protected fun getInitialStep(): Int {
        return registrationController?.getInitialStep() ?: 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RegistrationController) {
            registrationController = context
        } else {
            Log.d(TAG, context.toString() + " must implement "
                    + RegistrationController::class.java.simpleName)
        }
    }

    override fun onDetach() {
        super.onDetach()
        registrationController = null
    }

    companion object {

        private const val TAG = "RegistrationFragment"
    }
}
