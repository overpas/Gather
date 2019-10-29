package by.overpass.gather.ui.map.detail

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.model.repo.icon.IconRepo
import by.overpass.gather.ui.base.BackPressFragment
import by.overpass.gather.ui.map.Meeting
import by.overpass.gather.ui.meeting.MeetingActivity
import by.overpass.gather.ui.profile.ProfileActivity
import by.overpass.gather.ui.search.SearchBottomFragment
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
