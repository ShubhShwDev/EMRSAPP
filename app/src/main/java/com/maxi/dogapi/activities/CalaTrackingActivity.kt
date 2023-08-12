package com.maxi.dogapi.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.maxi.dogapi.R
import com.maxi.dogapi.databinding.ActivityCalasurveytrackingBinding
import com.maxi.dogapi.locationtrack.GeoLocationManager
import com.maxi.dogapi.viewmodel.WelcomeLPViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CalaTrackingActivity : AppCompatActivity(), OnMapReadyCallback {
    private val welcomeLPViewModel by viewModels<WelcomeLPViewModel>()
    private lateinit var _binding: ActivityCalasurveytrackingBinding
    private lateinit var locationManager: GeoLocationManager
    private var locationTrackingRequested = true
    private val LOCATION_PERMISSION_CODE = 1000
    private var mMap: GoogleMap? = null
    var TamWorth = LatLng(-31.083332, 150.916672)
    var NewCastle = LatLng(-32.916668, 151.750000)
    var Brisbane = LatLng(-27.470125, 153.021072)
    private var route: Polyline? = null
    private var routeOpts: PolylineOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= DataBindingUtil.setContentView(this,R.layout.activity_calasurveytracking);

//        val binding =
//            DataBindingUtil.setContentView<>(this,R.layout.activity_calasurveytracking)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.maos) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        locationManager = GeoLocationManager(applicationContext as Context)

        _binding.buttonStartLocationScan.setOnClickListener(View.OnClickListener {
            val permissionGranted = requestLocationPermission();
            if (permissionGranted) {
                locationManager.startLocationTracking(locationCallback)
                _binding.textviewStatus.text = "Started"
            } else {
                requestLocationPermission()
            }

        })

        _binding.buttonStopLocationScan.setOnClickListener(View.OnClickListener {
            locationManager.stopLocationTracking()
            _binding.textviewStatus.text = "Stopped"

        })


//        _binding.tvUName.text="Hello,\n" + intent.getStringExtra("uName")+" !"


//        fetchData("","")


//        welcomeLPViewModel.clickLocationTrackingEvent.observe(this){event->
//            if (event){
//                val intent = Intent(this, CalaTrackingActivity::class.java)
//                intent.putExtra("survey_id", "")
//                startActivity(intent)
//            }
//
//        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // inside on map ready method
        // we will be displaying polygon on Google Maps.
        // on below line we will be adding polyline on Google Maps.

        if (mMap != null) {
            routeOpts = PolylineOptions()
                .color(Color.GREEN)
                .width(50F /* TODO: respect density! */)
                .geodesic(true);
            route = mMap?.addPolyline(routeOpts!!);
        }
//            route?.setVisible(drawTrack);
//        mMap?.addPolyline(
//            PolylineOptions().add(Brisbane, NewCastle, TamWorth, Brisbane)
//                .width // below line is use to specify the width of poly line.
//                    (5f) // below line is use to add color to our poly line.
//                .color(Color.RED) // below line is to make our poly line geodesic.
//                .geodesic(true)
//        )
        // on below line we will be starting the drawing of polyline.
    }

    private fun requestLocationPermission(): Boolean {
        var permissionGranted = false

        // If system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val cameraPermissionNotGranted = ContextCompat.checkSelfPermission(
//                applicationContext as Context,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_DENIED
//            if (cameraPermissionNotGranted){
            val permission = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            // Display permission dialog
            requestPermissions(permission, LOCATION_PERMISSION_CODE)
        } else {
            // Permission already granted
            permissionGranted = true
//            }
        }
//        else{
        // Android version earlier than M -> no need to request permission
        permissionGranted = true
//        }

        return permissionGranted
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            locationResult ?: return
            for (location in locationResult.locations) {
                // Update UI
                _binding.textviewLatitudeLabel.text = location.latitude.toString()
                _binding.textviewLongitudeLabel.text = location.longitude.toString()

                var des = LatLng(location.latitude, location.longitude)
                if (locationTrackingRequested) {
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(des, 13f))
                    locationTrackingRequested = false
                }

                if (routeOpts != null) {
                    val myLatLng = LatLng(location.latitude, location.longitude)
                    val points = route!!.points
                    points.add(myLatLng)
                    route?.points = points
                    routeOpts?.add(myLatLng)
                    mMap?.addPolyline(routeOpts!!)
                }
//                mMap?.addPolyline(
//                    PolylineOptions().add(Brisbane, NewCastle, TamWorth, des,des,des)
//                        .width // below line is use to specify the width of poly line.
//                            (5f) // below line is use to add color to our poly line.
//                        .color(Color.RED) // below line is to make our poly line geodesic.
//                        .geodesic(true)
//                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.stopLocationTracking()

    }

/*
    private fun fetchData(uName: String, uPswd: String) {
        fetchResponse(uName, uPswd)
        welcomeLPViewModel.response.observe(this) { response ->
            Log.e("RESPONSE","response"+response.toString())

            when (response) {
                is NetworkResult.Success -> {

                    response.data?.let {
                        _binding.pbDog.visibility = View.GONE
                        _binding.rvMain.adapter= LpRecyclerAdapter(welcomeLPViewModel, response.data.message, this)

                        Log.e("RESPONSE1","response"+response.data.message.toString())

//                        val intent = Intent(this, WelcomeActivity::class.java)
//                        intent.putExtra("uName", response.data.message[0].toString())
//                        startActivity(intent)

//                        _binding.imgDog.load(
//                            response.data.message
//                        ) {
////                            transformations(RoundedCornersTransformation(16f))
//                        }
                    }
//                    _binding.pbDog.visibility = View.GONE
                }

                is NetworkResult.Error -> {
//                    _binding.pbDog.visibility = View.GONE
                    Toast.makeText(
                        this,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    _binding.pbDog.visibility = View.VISIBLE
                }
            }
        }
    }
*/

    // Handle Allow or Deny response from the permission dialog
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        if (requestCode === LOCATION_PERMISSION_CODE) {
//            if (grantResults.size === 2 &&
//                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                // Permission was granted
//                locationManager.startLocationTracking(locationCallback)
//                _binding.textviewStatus.text = "Started"
//            }
//            else{
//                // Permission was denied
////                showAlert("Location permission was denied. Unable to track location.")
//            }
//        }
    }


    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(applicationContext as Context)
        builder.setMessage(message)
        val dialog = builder.create()
        dialog.show()
    }


}
