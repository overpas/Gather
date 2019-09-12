package com.github.overpass.gather.screen.auth.register.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.base.personal.DataFragment
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment
import com.github.overpass.gather.screen.map.MapActivity
import kotlinx.android.synthetic.main.fragment_add_personal_data.*

class AddPersonalDataFragment : DataFragment<AddPersonalDataViewModel>() {

    override fun createViewModel(): AddPersonalDataViewModel {
        return viewModelProvider.get(AddPersonalDataViewModel::class.java)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_add_personal_data
    }

    override fun inject() {
        componentManager.getAddPersonalDataComponent()
                .inject(this);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSubmit.setOnClickListener {
            super.onSubmitClick()
        }
        ivPhotoPreview.setOnClickListener {
            super.onChooseImageClick()
        }
    }

    override fun handleSuccess(success: AddDataStatus.Success) {
        super.handleSuccess(success)
        viewModel.setSignUpComplete()
        ProgressDialogFragment.hide(fragmentManager)
        val intent = Intent(context, MapActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    companion object {

        fun newInstance(): AddPersonalDataFragment {
            return AddPersonalDataFragment()
        }
    }
}
