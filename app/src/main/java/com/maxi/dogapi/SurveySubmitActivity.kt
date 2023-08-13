package com.maxi.dogapi

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.smarttag.test.Contact
import com.example.smarttag.test.ContactsAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.maxi.dogapi.databinding.ActivitySubmitSurveyBinding
import com.maxi.dogapi.locationtrack.GeoLocationManager
import com.maxi.dogapi.utils.Utils
import com.maxi.dogapi.viewmodel.SubmitSurveyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint

class SurveySubmitActivity : AppCompatActivity(), ContactsAdapter.IContactsAdapter {
    private val LOCATION_PERMISSION_CODE = 1000
    private val CAMERA_PERMISSION_CODE = 1001
    private lateinit var locationManager: GeoLocationManager
    private var lat: String? = null
    private var long: String? = null
    private val contactList = ArrayList<Contact>()
    private val observationList = ArrayList<String>()
    private val activityList = ArrayList<String>()
    private val imageList = ArrayList<String>()
    private lateinit var adapter :ContactsAdapter
    private var imageClickPos: Int = 0
    private lateinit var binding: ActivitySubmitSurveyBinding
    private lateinit var adapterState: StateSpinnerAdapter
    private lateinit var adapterSchool: SchoolSpinnerAdapter
    private lateinit var adapterTpList: TpSpinnerAdapter
    var tpqaId:String=""
    var fullName:String=""
    var userId:String=""
    var levelId:String=""
    private val viewModel by viewModels<SubmitSurveyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_survey)
        fetchLocation()
        tpqaId = intent.getStringExtra("tpqaId").toString()
        userId = intent.getStringExtra("userId").toString()
        levelId = intent.getStringExtra("levelId").toString()
        fullName = intent.getStringExtra("fullName").toString()
        Log.e("TPQ_ID",tpqaId+"set")
        initToolBar()
        addMoreData()
        initDateAndTime()
        binding.btSubmit.setOnClickListener(View.OnClickListener {
            submitForm()
        })
        if (!levelId.equals("1")) {
            binding.etSchoolName.visibility=View.VISIBLE
            binding.etSchoolName.setText(fullName)
            initStateData()
            initDateAndTime()
            intiAddMore()
        }
        else{
            initTPData()
        }
    }

    private fun initToolBar() {
        binding.tvUsername.text=fullName
        binding.ivLogout.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
                        var editor = sharedPreference.edit()
                        editor.putBoolean("isLogin",false)
                        editor.commit()
                        finish()

                    })
                .setNegativeButton("No", null)
                .show()

        })

    }


    private fun initTPData() {
        binding.spinnerTpName.visibility=View.VISIBLE
        viewModel.fetchTpResponse(userId, levelId)
        viewModel._response_tp.observe(this) {
             adapterTpList = TpSpinnerAdapter(this, it)
            binding.spinnerTpName.adapter = adapterTpList
//            binding.spinnerTpName.setSelection(0)
        }
        binding.spinnerTpName.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                    val clickedItem: String = parent.getItemAtPosition(position) as String
                    val name: String = clickedItem
                    tpqaId = adapterTpList.dataSource[position].tpqaId.toString()
                    Log.e("TPID", tpqaId + "test")
                    initStateData()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun intiAddMore() {
        binding.addMore.setOnClickListener {
//            observationList.add()
           addMoreData()
        }


    }


    private fun addMoreData(){
//        val contact = Contact(null, null, null,null)
//        contactList.add(contact)
//        binding.rvRecycler.adapter?.notifyItemChanged(contactList.size-1,contact)
        setActivityData()
    }
    private fun initDateAndTime() {
         binding.etDate.setText(Utils.checkCurrentDate())
//         binding.etDate.isEnabled=false
        binding.etVisitTime.setText(Utils.checkCurrentTime())
    }

    private fun fetchLocation() {
        val permissionGranted = requestLocationPermission();
        if (permissionGranted) {
            locationManager = GeoLocationManager(applicationContext as Context)
            locationManager.startLocationTracking(locationCallback)
            locationManager.startLocationTracking(locationCallback)

        } else {
            requestLocationPermission()
        }
    }

    private fun setActivityData() {
        val contact = Contact(null, null, null,null)
        contactList.add(contact)
        adapter = ContactsAdapter(contactList, this)
        binding.rvRecycler.adapter = adapter
    }


    private fun initSchoolData(userId:String,levelId:String,tpqaId:String,stateId:String) {
        viewModel.fetchSchoolResponse(userId, levelId, tpqaId,stateId)

        viewModel._response_school.observe(this) {
            it?.let {
                adapterSchool = SchoolSpinnerAdapter(this, it)
                binding.spinnerSchool.adapter = adapterSchool
            }
//            binding.spinnerSchool.setSelection(0)

        }
        binding.spinnerSchool.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                    val clickedItem: String = parent.getItemAtPosition(position) as String
                    val name: String = clickedItem

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initStateData() {
        viewModel.fetchStateResponse(userId, levelId, tpqaId)
        viewModel._response.observe(this) {
            it?.let {
                Log.e("TESTDATA", "dadata" + it.toString())
                adapterState = StateSpinnerAdapter(this, it)
                binding.spinnerState.adapter = adapterState
            }
//            binding.spinnerState.setSelection(0)
        }
        binding.spinnerState.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                    val clickedItem: String = parent.getItemAtPosition(position) as String
                    val name: String = clickedItem
                    val positionStates = viewModel._response.value?.get(position)?.id
                    initSchoolData(userId, levelId, tpqaId, positionStates ?: "")

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
                if (binding.etLat.text.toString().length<1) {
                    binding.etLat.setText("Latitude: $lat")
                    binding.etLong.setText("Longitude: $long")
                }
                Log.e("RESPONSE", "lat" + lat + "" + long + "" + long)
//                locationManager.stopLocationTracking()

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
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA

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
//        permissionGranted = true
//        }

        return permissionGranted
    }

    private fun requestCameraPermission(): Boolean {
        var permissionGranted = false

        // If system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val cameraPermissionNotGranted = ContextCompat.checkSelfPermission(
//                applicationContext as Context,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_DENIED
//            if (cameraPermissionNotGranted){
            val permission = arrayOf(
                Manifest.permission.CAMERA
            )

            // Display permission dialog
            requestPermissions(permission, CAMERA_PERMISSION_CODE)
        } else {
            // Permission already granted
            permissionGranted = true
//            }
        }
//        else{
        // Android version earlier than M -> no need to request permission
//        permissionGranted = true
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

    override fun addImage(position: Int) {
        imageClickPos = position
        val permissionGranted = requestCameraPermission()
//        if (permissionGranted) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 101)
//        }
//        else{
//            requestCameraPermission()
//        }
    }

    override fun addObservation(position: Int) {
//        observationList.add()
    }

    override fun addActivity(position: Int) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode== RESULT_OK) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos) // photo is the bitmap object
            val byteArray = baos.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            contactList[imageClickPos].image = encoded
            binding.rvRecycler.adapter?.notifyItemChanged(imageClickPos,contactList[imageClickPos])

        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Closing Activity")
            .setMessage("Are you sure you want to close this activity?")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which -> finish() })
            .setNegativeButton("No", null)
            .show()
    }

    private fun submitForm(){
//        viewModel.fetchSchoolResponse(userId, levelId, tpqaId,stateId)


    }
}