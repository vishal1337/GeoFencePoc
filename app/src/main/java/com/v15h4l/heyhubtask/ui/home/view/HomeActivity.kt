package com.v15h4l.heyhubtask.ui.home.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.PolyUtil
import com.v15h4l.heyhubtask.R
import com.v15h4l.heyhubtask.data.HubDataProvider
import com.v15h4l.heyhubtask.ui.home.model.HubData
import com.v15h4l.heyhubtask.ui.home.viewmodel.HomeViewModel
import com.v15h4l.heyhubtask.utils.LookUpUtils
import kotlinx.android.synthetic.main.activity_maps.*

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private var viewModel: HomeViewModel? = null

    private var hubData: HubData? = null

    private var mMap: GoogleMap? = null

    //To Store Polygon Points to be used for LookUp Later
    val geometryPoints: MutableList<LatLng> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Observe File selection Event and update hubData and plot map again.
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java).apply {
            fileName.observe(this@HomeActivity, Observer {
                it?.let {
                    hubData = HubDataProvider.getHubData(this@HomeActivity, it)
                    mapGeoFence()
                }
            })
        }

        initMap()
        initClickListener()

    }

    /***
     * Load Map Fragment when ready
     */
    private fun initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initClickListener() {
        btnSearch.setOnClickListener {
            lookUpCoordinates()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_toggle_input -> toggleInput()

            R.id.action_file_1 -> viewModel?.selectFile(HomeViewModel.DataFiles.F1)
            R.id.action_file_2 -> viewModel?.selectFile(HomeViewModel.DataFiles.F2)
            R.id.action_file_3 -> viewModel?.selectFile(HomeViewModel.DataFiles.F3)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toggleInput() {
        inputs.visibility =
            if (inputs.isVisible)
                View.GONE
            else
                View.VISIBLE
    }

    /**
     * Obtain Google Map Instance once the Map is ready to use it for further Manipulation.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Load Default File when the Map is ready
        viewModel?.selectFile(HomeViewModel.DataFiles.F1)
    }

    private fun mapGeoFence() {

        mMap?.let {
            //Reset Polygon points
            geometryPoints.clear()
        } ?: return

        hubData?.let { hubData ->

            //Extract areaList for later LookUp Use
            hubData.hub.geometry
                .takeIf { it.isNotEmpty() }
                ?.first()
                ?.let {
                    geometryPoints.addAll(it.map { geo -> LatLng(geo.lat, geo.lon) })
                }

            //Clear any Previous Polygons added to add a new one. [For when Changing Geo-Fence Location]
            mMap?.clear()

            //Add Polygon onto the Map
            val polyPoints = PolyUtil.simplify(geometryPoints, 5.0 /* Meters */)
            mMap?.addPolygon(
                PolygonOptions()
                    .addAll(polyPoints)
                    .fillColor(ContextCompat.getColor(this, R.color.polyFillColor) - 0x77000000)
                    .strokeColor(ContextCompat.getColor(this, R.color.polyStroke))
                    .strokeWidth(5f)
            )

            val builder = LatLngBounds.Builder()
                .include(LatLng(hubData.hub.bounds.maxLat, hubData.hub.bounds.maxLon))
                .include(LatLng(hubData.hub.bounds.minLat, hubData.hub.bounds.minLon))

            //Zoom into the Area
            mMap?.setOnMapLoadedCallback {
                mMap?.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        builder.build(),
                        50 /*padding for map boundary*/
                    )
                )
            }

        }

    }

    private fun lookUpCoordinates() {
        val lat = etLat.text.toString()
        val long = etLong.text.toString()
        val accuracy = etAccuracy.text.toString()

        if (lat.isNotBlank() && long.isNotBlank() && accuracy.isNotBlank()) {

            if (LookUpUtils.isLocationWithInArea(
                    LatLng(lat.toDouble(), long.toDouble()),
                    geometryPoints,
                    accuracy.toInt()
                )
            ) {
                showToast("Within Area")
            } else {
                showToast("Outside Area")
            }

        } else {
            showToast("Try with right Information")
        }
    }


    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)

        toast.view.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.holo_orange_dark
            )
        )

        toast.show()


    }
}
