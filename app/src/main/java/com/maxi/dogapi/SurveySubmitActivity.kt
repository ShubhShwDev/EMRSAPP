package com.maxi.dogapi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.maxi.dogapi.databinding.ActivitySubmitSurveyBinding
import com.maxi.dogapi.locationtrack.GeoLocationManager
import com.maxi.dogapi.model.school.SchoolDetails
import com.maxi.dogapi.model.state.StateDetail
import com.maxi.dogapi.viewmodel.SubmitSurveyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SurveySubmitActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_CODE = 1000
    private lateinit var locationManager: GeoLocationManager
    private var lat: String? = null
    private var long: String? = null
    private lateinit var binding: ActivitySubmitSurveyBinding
    private lateinit var adapterState: StateSpinnerAdapter
    private lateinit var adapterSchool: SchoolSpinnerAdapter
    private val viewModel by viewModels<SubmitSurveyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_survey)
        fetchLocation()
        initStateData()
//        initSchoolData()
    }

    private fun fetchLocation() {
        val permissionGranted = requestLocationPermission();
        if (permissionGranted) {
            locationManager = GeoLocationManager(applicationContext as Context)
            locationManager.startLocationTracking(locationCallback)
        } else {
            requestLocationPermission()
        }


    }

    private fun initSchoolData(userId:String,levelId:String,tpqaId:String,stateId:String) {

        viewModel.fetchSchoolResponse(userId, levelId, tpqaId,stateId)

//        viewModel.schoolList.observe(this) {
//            adapterSchool = SchoolSpinnerAdapter(this, it)
//            binding.spinnerSchool.adapter = adapterState
//            binding.spinnerSchool.setSelection(0)
//
//        }
        binding.spinnerSchool.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val clickedItem: SchoolDetails = parent.getItemAtPosition(position) as SchoolDetails
                val name: String = clickedItem.name
                Toast.makeText(this@SurveySubmitActivity, "$name selected", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initStateData() {
        val userId = intent.getStringExtra("userId").toString()
        val levelId = intent.getStringExtra("levelId").toString()
        val tpqaId = intent.getStringExtra("tpqaId").toString()
        viewModel.fetchStateResponse(userId, levelId, tpqaId)

        viewModel.stateList.observe(this) {
            Log.e("Values","data"+it.toString())
            adapterState = StateSpinnerAdapter(this, it)
            binding.spinnerState.adapter = adapterState
            binding.spinnerState.setSelection(0)

        }
        binding.spinnerState.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val clickedItem: String = parent.getItemAtPosition(position) as String
                val name: String = clickedItem
                val positionStates = viewModel.stateList.value?.get(position)?.id
                initSchoolData(userId,levelId,tpqaId,positionStates?:"")
                Toast.makeText(this@SurveySubmitActivity, "$name selected", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            locationResult ?: return
            for (location in locationResult.locations) {
                // Update UI
                lat = location.latitude.toString()
                long = location.longitude.toString()
                Log.e("RESPONSE", "lat" + lat + "" + long + "" + long)
                locationManager.stopLocationTracking()

                var des = LatLng(location.latitude, location.longitude)

            }
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode === LOCATION_PERMISSION_CODE) {
            if (grantResults.size === 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
//                 Permission was granted
//                locationManager.startLocationTracking(locationCallback)
            }
//            else{
//                // Permission was denied
////                showAlert("Location permission was denied. Unable to track location.")
//            }
//        }
        }

    }
}