package com.github.overpass.gather.screen.create

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.di.newmeeting.NewMeetingComponent
import com.github.overpass.gather.di.newmeeting.NewMeetingComponentManager
import com.github.overpass.gather.model.commons.UIUtil.*
import com.github.overpass.gather.model.commons.getDoubleExtra
import com.github.overpass.gather.screen.base.BaseActivity
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment
import com.github.overpass.gather.screen.map.SaveMeetingStatus
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.android.synthetic.main.activity_new_meeting.*

class NewMeetingActivity : BaseActivity<NewMeetingViewModel, NewMeetingComponent>() {

    override val componentManager: NewMeetingComponentManager =
            appComponentManager.getNewMeetingComponentManager()

    override val layoutRes: Int = R.layout.activity_new_meeting

    override fun createComponent(): NewMeetingComponent = componentManager.getOrCreate(Unit)

    override fun onComponentCreated(component: NewMeetingComponent) {
        component.inject(this)
    }

    override fun createViewModel(): NewMeetingViewModel {
        return viewModelProvider.get(NewMeetingViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_new_meeting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create -> {
                create()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun create() {
        val meetingType: MeetingType = when (bnvMeetingType.selectedItemId) {
            R.id.type_business -> MeetingType.BUSINESS
            R.id.type_entertainment -> MeetingType.ENTERTAINMENT
            else -> MeetingType.PROTEST
        }
        val latitude = this.getDoubleExtra(KEY_LATITUDE)
        val longitude = this.getDoubleExtra(KEY_LONGITUDE)
        val title = textOf(tietName)
        val date = dateOf(datePicker)
        val maxPeople = npMaxPeople.value
        val isPrivate = switchPrivate.isChecked
        viewModel.createMeeting(latitude, longitude, title, date, meetingType, maxPeople, isPrivate)
                .observe(this, Observer<SaveMeetingStatus> { this.handleSaveMeetingStatus(it) })
    }

    private fun handleSaveMeetingStatus(saveMeetingStatus: SaveMeetingStatus) {
        when (saveMeetingStatus.tag()) {
            SaveMeetingStatus.ERROR -> {
                ProgressDialogFragment.hide(supportFragmentManager)
                val message = saveMeetingStatus.`as`(SaveMeetingStatus.Error::class.java)
                        .throwable
                        .localizedMessage
                snackbar(tietName, message)
            }
            SaveMeetingStatus.SUCCESS -> {
                ProgressDialogFragment.hide(supportFragmentManager)
                snackbar(tietName, "Success")
                finish()
            }
            SaveMeetingStatus.PROGRESS -> ProgressDialogFragment.show(supportFragmentManager)
            SaveMeetingStatus.EMPTY_NAME -> {
                ProgressDialogFragment.hide(supportFragmentManager)
                tietName.error = "Please, type a proper name"
            }
        }
    }

    override fun onActionBar(actionBar: ActionBar) {
        super.onActionBar(actionBar)
        actionBar.title = getString(R.string.new_meeting)
        actionBar.setDisplayHomeAsUpEnabled(true)
        val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
        actionBar.setBackgroundDrawable(ColorDrawable(primaryColor))
    }

    companion object {

        private const val KEY_LATITUDE = "KEY_LATITUDE"
        private const val KEY_LONGITUDE = "KEY_LONGITUDE"

        fun start(coordinates: LatLng, context: Context) {
            val latitude = coordinates.latitude
            val longitude = coordinates.longitude
            val intent = Intent(context, NewMeetingActivity::class.java)
            intent.putExtra(KEY_LATITUDE, latitude)
            intent.putExtra(KEY_LONGITUDE, longitude)
            context.startActivity(intent)
        }
    }
}