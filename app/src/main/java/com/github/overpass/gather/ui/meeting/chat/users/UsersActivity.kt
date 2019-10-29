package com.github.overpass.gather.ui.meeting.chat.users

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.commons.android.lifecycle.on
import com.github.overpass.gather.commons.android.snackbar
import com.github.overpass.gather.commons.android.toast
import com.github.overpass.gather.ui.base.BaseActivityKt
import com.github.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
import com.github.overpass.gather.ui.map.AuthUser
import com.github.overpass.gather.ui.meeting.chat.users.list.PendingUsersAdapter
import com.github.overpass.gather.ui.meeting.chat.users.list.UsersAdapter
import com.github.overpass.gather.ui.meeting.chat.users.model.Acceptance
import com.github.overpass.gather.ui.meeting.chat.users.model.LoadUsersStatus
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : BaseActivityKt<UsersViewModel>() {

    private lateinit var membersAdapter: UsersAdapter
    private lateinit var pendingUsersAdapter: PendingUsersAdapter

    override fun getLayoutRes(): Int {
        return R.layout.activity_users
    }

    override fun inject() {
        componentManager.getUsersComponent()
                .inject(this)
    }

    override fun createViewModel(): UsersViewModel {
        return viewModelProvider.get(UsersViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupList()
    }

    override fun onBind() {
        super.onBind()
        on(viewModel.members) {
            handleUsers(it)
        }
        on(viewModel.pendingUsers) {
            handleUsers(it)
        }
        viewModel.checkUserRole().observe(this, Observer<AuthUser.Role> { this.handleRole(it) })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActionBar(actionBar: ActionBar) {
        super.onActionBar(actionBar)
        actionBar.setTitle(R.string.members)
        val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
        actionBar.setBackgroundDrawable(ColorDrawable(primaryColor))
    }

    private fun handleRole(role: AuthUser.Role) {
        if (role == AuthUser.Role.USER) {
            pendingUsersAdapter.setSwipeLocked(true)
        } else if (role == AuthUser.Role.NOBODY) {
            toast(getString(R.string.not_allowed))
            finish()
        }
    }

    private fun handleUsers(status: LoadUsersStatus) {
        when (status.tag()) {
            LoadUsersStatus.ERROR -> handleError(status.`as`(LoadUsersStatus.Error::class.java))
            LoadUsersStatus.PROGRESS -> handleProgress(status.`as`(LoadUsersStatus.Progress::class.java))
            LoadUsersStatus.MEMBERS_SUCCESS -> handleMembersSuccess(status.`as`(LoadUsersStatus.MembersSuccess::class.java))
            LoadUsersStatus.PENDING_SUCCESS -> handlePendingUsersSuccess(status.`as`(LoadUsersStatus.PendingSuccess::class.java))
        }
    }

    private fun handleError(error: LoadUsersStatus.Error) {
        rvMembers.snackbar(error.throwable.localizedMessage)
    }

    private fun handleProgress(progress: LoadUsersStatus.Progress) {
        membersAdapter.setProgress()
        pendingUsersAdapter.setProgress()
    }

    private fun handleMembersSuccess(success: LoadUsersStatus.MembersSuccess) {
        membersAdapter.setUsers(success.users)
    }

    private fun handlePendingUsersSuccess(success: LoadUsersStatus.PendingSuccess) {
        pendingUsersAdapter.setUsers(success.users)
    }

    private fun setupList() {
        membersAdapter = UsersAdapter { id -> }
        rvMembers.layoutManager = LinearLayoutManager(this)
        rvMembers.adapter = membersAdapter
        pendingUsersAdapter = PendingUsersAdapter(UsersAdapter.OnItemClickListener { this.handleAcceptPendingUser(it) })
        rvPendingUsers.layoutManager = LinearLayoutManager(this)
        rvPendingUsers.adapter = pendingUsersAdapter
    }

    private fun handleAcceptPendingUser(id: String) {
        viewModel.acceptUser(id)
                .observe(this, Observer { this.handleAcceptance(it) })
    }

    private fun handleAcceptance(acceptance: Acceptance) {
        when (acceptance) {
            Acceptance.ERROR -> handleAcceptanceError()
            Acceptance.SUCCESS -> handleAcceptanceSuccess()
            Acceptance.PROGRESS -> handleAcceptanceProgress()
        }
    }

    private fun handleAcceptanceProgress() {
        ProgressDialogFragment.show(supportFragmentManager)
    }

    private fun handleAcceptanceSuccess() {
        ProgressDialogFragment.hide(supportFragmentManager)
        recreate()
    }

    private fun handleAcceptanceError() {
        ProgressDialogFragment.hide(supportFragmentManager)
        rvMembers.snackbar(R.string.couldnt_accept)
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, UsersActivity::class.java)
            context.startActivity(intent)
        }
    }
}
