package com.github.overpass.gather.screen.map.detail

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.repo.icon.IconRepo
import com.github.overpass.gather.screen.base.BackPressFragment
import com.github.overpass.gather.screen.map.Meeting
import com.github.overpass.gather.screen.meeting.MeetingActivity
import com.github.overpass.gather.screen.profile.ProfileActivity
import com.github.overpass.gather.screen.search.SearchBottomFragment
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style

class MapFragment : BaseMapFragment<MapDetailViewModel>(), BackPressFragment, OnMapReadyCallback {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_map
    }

    override fun inject() {
        componentManager.getMapDetailComponent()
                .inject(this)
    }

    override fun createViewModel(): MapDetailViewModel {
        return viewModelProvider.get(MapDetailViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bottomAppBar.setNavigationOnClickListener { v -> startActivity(Intent(context, ProfileActivity::class.java)) }
        bottomAppBar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_search) {
                SearchBottomFragment.open(requireFragmentManager())
                return@setOnMenuItemClickListener true
            }
            false
        }
    }

    override fun clearComponent() {
        super.clearComponent()
        componentManager.clearMapDetailComponent()
    }

    override fun onLocationUpdated(location: Location, forceCameraMove: Boolean) {
        super.onLocationUpdated(location, forceCameraMove)
        viewModel.scanArea(location)
                .observe(viewLifecycleOwner, Observer<Map<String, Meeting>> { this.onMeetingsLoaded(it) })
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        viewModel.getCurrent2MaxPeopleRatio(marker, viewLifecycleOwner, Observer { ratio -> onRatioReceived(ratio, marker) })
        return super.onMarkerClick(marker)
    }

    override fun onInfoWindowClick(marker: Marker): Boolean {
        viewModel.openMeeting(marker) { id -> MeetingActivity.start(requireContext(), id) }
        return super.onInfoWindowClick(marker)
    }

    override fun onStyleLoaded(style: Style, mapboxMap: MapboxMap) {
        super.onStyleLoaded(style, mapboxMap)
        viewModel.setMarkersHelper(MarkersHelper(map, IconRepo(context!!)))
    }

    private fun onRatioReceived(ratio: Current2MaxPeopleRatio, marker: Marker) {
        viewModel.updateSnippet(marker, ratio).observe(viewLifecycleOwner, Observer { updated ->
            // probably do smth
        })
    }

    private fun onMeetingsLoaded(meetings: Map<String, Meeting>) {
        Log.d(TAG, "onMeetingsLoaded: $meetings")
        viewModel.replace(meetings)
    }

    companion object {

        private const val TAG = "MainMapFragment"

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}
