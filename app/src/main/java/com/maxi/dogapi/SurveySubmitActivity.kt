package com.maxi.dogapi

//import sun.security.krb5.Confounder.bytes

import android.Manifest
import android.Manifest.permission.*
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.example.smarttag.test.Contact
import com.example.smarttag.test.ContactsAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.maxi.dogapi.activities.ManagePermissions
import com.maxi.dogapi.databinding.ActivitySubmitSurveyBinding
import com.maxi.dogapi.locationtrack.GeoLocationManager
import com.maxi.dogapi.utils.Utils
import com.maxi.dogapi.viewmodel.SubmitSurveyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_submit_survey.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint

class SurveySubmitActivity : AppCompatActivity(), ContactsAdapter.IContactsAdapter ,
    LocationListener {
    private val LOCATION_PERMISSION_CODE = 1000
    private val CAMERA_PERMISSION_CODE = 1001
    private lateinit var locationManager: GeoLocationManager
    private lateinit var locationManagers: LocationManager

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var lat: String? = null
    private var long: String? = null
    private val contactList = ArrayList<Contact>()
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
    var stateId:String=""
    var schoolId:String=""
    private val viewModel by viewModels<SubmitSurveyViewModel>()
    lateinit var mImageUri:Uri
    var photoFile: File? = null
    private lateinit var managePermissions: ManagePermissions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_survey)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        fetchLocation()
        getLocation()
        tpqaId = intent.getStringExtra("tpqaId").toString()
        userId = intent.getStringExtra("userId").toString()
        levelId = intent.getStringExtra("levelId").toString()
        fullName = intent.getStringExtra("fullName").toString()
        Log.e("TPQ_ID",tpqaId+"set")
        initToolBar()
        initActivityData()
        initDateAndTime()
        intiAddMore()
//        binding.etLat.setText("28.89")
//        binding.etLong.setText("28.89")

        binding.btSubmit.setOnClickListener(View.OnClickListener {


            submitForm()
        })
        if (!levelId.equals("1")) {
            binding.etSchoolName.visibility=View.VISIBLE
            binding.etSchoolName.setText(fullName)
            initStateData()
            initDateAndTime()
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
                if (position>0) {
                    val clickedItem: String = parent.getItemAtPosition(position) as String
                    val name: String = clickedItem
                    tpqaId = adapterTpList.dataSource[position].tpqaId.toString()
                    Log.e("TPID", tpqaId + "test")
                    initStateData()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun intiAddMore() {
        binding.addMore.setOnClickListener {
           addMoreData()
        }


    }


    private fun addMoreData(){

        val contact = Contact(null, null, null,contactList.get(0).activityList,-1)
        contactList.add(contact)
        binding.rvRecycler.adapter?.notifyItemChanged(contactList.size-1,contact)
    }
    private fun initDateAndTime() {
         binding.etDate.setText(Utils.checkCurrentDate())
//         binding.etDate.isEnabled=false
        binding.etVisitTime.setText(Utils.checkCurrentTime())
    }

    private fun getLocation() {
        locationManagers = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            val permission = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            // Display permission dialog
            requestPermissions(permission, LOCATION_PERMISSION_CODE)
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
        }
//        if (isLocationEnabled(applicationContext) == true) {
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { tasks ->
                val location: Location? = tasks.result
                if (location != null) {
                    binding.etLat.setText(location.latitude.toString())
                    binding.etLong.setText(location.longitude.toString())
                }
            }

//            locationManagers.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
//        }
//        locationManagers.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

//    override fun onLocationChanged(location: Location) {
//        tvGpsLocation = findViewById(R.id.textView)
//        tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
//    }
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == locationPermissionCode) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}

    private fun fetchLocation() {
//        locationManagers = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val permissionGranted = requestLocationPermission();
        if (permissionGranted) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
            }
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { tasks ->
                val location: Location? = tasks.result
                if (location != null) {
                    binding.etLat.setText(location.latitude.toString())
                    binding.etLong.setText(location.longitude.toString())
                }
            }
//            if (isLocationEnabled()) {

//            locationManagers.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
//        }
//        override fun onLocationChanged(location: Location) {
//            tvGpsLocation = findViewById(R.id.textView)
//            tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
//        }
//                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { tasks ->
//                    val location: Location? = tasks.result
//                    if (location != null) {
////                        val geocoder = Geocoder(this, Locale.getDefault())
////                        binding.etLat.setText(location.latitude.toString())
////                        binding.etLong.setText(location.longitude.toString())
////                        val list: List<Address> =
////                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
////                        mainBinding.apply {
////                            tvLatitude.text = "Latitude\n${list[0].latitude}"
////                            tvLongitude.text = "Longitude\n${list[0].longitude}"
////                            tvCountryName.text = "Country Name\n${list[0].countryName}"
////                            tvLocality.text = "Locality\n${list[0].locality}"
////                            tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
////                        }
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }

//            SingleShotLocationProvider.requestSingleUpdate(applicationContext,
//                object : LocationCallback(), SingleShotLocationProvider.LocationCallback {
//                    override fun onNewLocationAvailable(location: SingleShotLocationProvider.GPSCoordinates) {
//                        if (binding.etLat.text.toString().length<1) {
//                            binding.etLat.setText("Latitude: $lat")
//                            binding.etLong.setText("Longitude: $long")
//                        }
//                        Log.e("RESPONSE", "lat" + lat + "" + long + "" + long)
//                        Log.d("Location", "my location is " + location.toString())
//                    }
//                })
//            locationManager = GeoLocationManager(applicationContext as Context)
//            locationManager.startLocationTracking(locationCallback)
//            locationManager.startLocationTracking(locationCallback)

        } else {
            requestLocationPermission()
        }
    }

    private fun setActivityData() {

        viewModel._response_activity.observe(this){
            it?.let {
                val contact = Contact(null, null, null,null,-1)
                contactList.add(contact)
                adapter = ContactsAdapter(contactList, this,this)
                binding.rvRecycler.adapter = adapter
                contactList.get(0).activityList = it
                Log.e("ACZTi", contactList.get(0).activityList.toString())
            }
        }

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
                schoolId = adapterSchool.dataSource[position].id.toString()


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
                    stateId = adapterState.dataSource[position].id.toString()


                    initSchoolData(userId, levelId, tpqaId, positionStates ?: "")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
//        binding.spinnerState.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View, position: Int, id: Long
//            ) {
//
//                    val clickedItem: String = parent.getItemAtPosition(position) as String
//                    val name: String = clickedItem
//                    val positionStates = viewModel._response.value?.get(position)?.id
//                if (position>0)
//                stateId = adapterState.dataSource[position].id.toString()
//
//
//                initSchoolData(userId, levelId, tpqaId, positionStates ?: "")
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
    }

    private fun initActivityData() {
        viewModel.fetchActivityResponse(userId)
        setActivityData()


//        viewModel._response_activity.observe(this) {
//            it?.let {
//                Log.e("TESTDATA", "dadata" + it.toString())
//                addMoreData()
//
////                adapterActivityList = ActivitySpinnerAdapter(this, it)
////                binding.spinnerState.adapter = adapterActivityList
//            }
////            binding.spinnerState.setSelection(0)
//        }
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
                locationManager.stopLocationTracking()

//                var des = LatLng(location.latitude, location.longitude)

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
//                fetchLocation()
                getLocation()
//                 Permission was granted
//                locationManager.startLocationTracking(locationCallback)
            }
            else{
                requestLocationPermission()
//                // Permission was denied
////                showAlert("Location permission was denied. Unable to track location.")
            }
//        }
        }
        else if(requestCode== CAMERA_PERMISSION_CODE) {

            if (grantResults.size === 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
//                    val cameraPermission = grantResults[0] === PackageManager.PERMISSION_GRANTED
//                    if (cameraPermission) {
                        addImage(imageClickPos)

//                    }
                }

        }

    }

    override fun addImage(position: Int) {
        imageClickPos = position
//        val list = listOf<String>(
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )

        // Initialize a new instance of ManagePermissions class
//        managePermissions = ManagePermissions(this,list,200)
        val permissionGranted = checkPermission()
        Log.e("TESTDATAS",permissionGranted.toString())
//        val permissionGranted = requestCameraPermission()
        if (permissionGranted) {
//            var photo: File?=null
            dispatchTakePictureIntent()
//            try {
//                // place where to store camera taken picture
//                photo = createTemporaryFile("picture", ".jpg")
//                photo?.delete()
//            } catch (e: Exception) {
////                Log.v(TAG, "Can't create file to take picture!")
//                Toast.makeText(applicationContext, "Please check SD card! Image shot is impossible!", 10000)
////                return false
//            }
////            mImageUri = Uri.fromFile(photo)
//            mImageUri = photo?.let {
//                FileProvider.getUriForFile(applicationContext,
//                    "com.maxi.dogapi.fileprovider",
//                    it
//                )
//            }!!;
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
//
//            startActivityForResult(cameraIntent, 101)
        }
        else{
            requestNecessaryPermissions()
//            var perms = arrayOf("android.permission.CAMERA")

//            requestPermissions(perms, CAMERA_PERMISSION_CODE);

//            requestCameraPermission()
        }
    }


    private fun hasPermissions(): Boolean {
        var res = 0
        // list all permissions which you want to check are granted or not.
        val permissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
        for (perms in permissions) {
            res = checkCallingOrSelfPermission(perms)
            if (res != PackageManager.PERMISSION_GRANTED) {
                // it return false because your app dosen't have permissions.
                return false
            }
        }
        // it return true, your app has permissions.
        return true
    }
    private fun dispatchTakePictureIntent() {

        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            Log.d("mylog", "Exception while creating file: $ex")
        }
        // Continue only if the File was successfully created
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Log.d("mylog", "Photofile not null")
            val photoURI = FileProvider.getUriForFile(
                applicationContext,
                "com.maxi.dogapi.fileprovider",
                photoFile!!
            )
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, 101)
        }

//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        this,
//                        "com.maxi.dogapi.android.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, 101)
//                }
//            }
//        }
    }


    private fun requestNecessaryPermissions() {
        // make array of permissions which you want to ask from user.
        val permissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // have arry for permissions to requestPermissions method.
            // and also send unique Request code.
            requestPermissions(permissions, CAMERA_PERMISSION_CODE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        var mCurrentPhotoPath="storage/emulated/0/Pictures"
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        Log.d("mylog", "Path: $mCurrentPhotoPath")
        return image
    }
//    lateinit var currentPhotoPath: String
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//        }
//    }
    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, CAMERA)
//        val result1 = isGrantedPermissionWRITE_EXTERNAL_STORAGE(this)
//        val result1 = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
//        return result == PackageManager.PERMISSION_GRANTED && result1
//        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    fun isGrantedPermissionWRITE_EXTERNAL_STORAGE(activity: Activity?): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version <= 32) {
            val isAllowPermissionApi28 = ActivityCompat.checkSelfPermission(
                activity!!,
                WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            Log.i(
                "general_em",
                "isGrantedPermissionWRITE_EXTERNAL_STORAGE() - isAllowPermissionApi28: $isAllowPermissionApi28"
            )
            isAllowPermissionApi28
        } else {
            val isAllowPermissionApi33 = Environment.isExternalStorageManager()
            Log.i(
                "general_em",
                "isGrantedPermissionWRITE_EXTERNAL_STORAGE() - isAllowPermissionApi33: $isAllowPermissionApi33"
            )
            isAllowPermissionApi33
        }
    }

    private fun createTemporaryFile(part: String, ext: String): File? {
        var tempDir = Environment.getExternalStorageDirectory()
        tempDir = File(tempDir.absolutePath + "/.temp/")
        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }
        return File.createTempFile(part, ext, tempDir)
    }

    fun grabImage() : Bitmap? {
        contentResolver.notifyChange(mImageUri, null)
        val cr = contentResolver
        var bitmap:Bitmap?=null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, mImageUri)
//            imageView.setImageBitmap(bitmap)
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show()
//            Log.d(TAG, "Failed to load", e)
        }
        return bitmap
    }
    override fun addObservation(position: Int) {
//        observationList.add()
    }

    override fun addActivity(position: Int) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode== RESULT_OK) {
//            var bitmap:Bitmap? =grabImage();
            var bitmap:Bitmap? =BitmapFactory.decodeFile(photoFile?.absolutePath)

//            val photo1: Uri? = data?.data

//            var base64 = "" //Your encoded string

//            base64 =
//                "data:image/" + getMimeType(context, profileUri).toString() + ";base64," + base64
//            val imageStream = photo1?.let { contentResolver.openInputStream(it) }
//            val selectedImage = BitmapFactory.decodeStream(imageStream)
//            val encodedImage = encodeImage(selectedImage)

//            val bitmap: Bitmap? = data?.extras?.getParcelable("data") as Bitmap?
//            var width:Int =photo.width/2
//            var height:Int =photo.height/2
//            var compresedBitmap:Bitmap =Bitmap.createScaledBitmap(photo,width,height,true)
            val baos = ByteArrayOutputStream()
//            val byteBuffer = ByteBuffer.allocate(photo.getByteCount())
//            Log.e("PHOTO","Di"+byteBuffer.toString())
//            photo.copyPixelsToBuffer(byteBuffer)
//            byteBuffer.rewind()
//            val byteArray= byteBuffer.array()
//            val lnth: Int = photo.getByteCount()
//            val dst: ByteBuffer = ByteBuffer.allocate(lnth)
//            photo.copyPixelsToBuffer(dst)
//            val barray: ByteArray = dst.array()

//            val size: Int = photo.getRowBytes() * photo.getHeight()
//
//            val b = ByteBuffer.allocate(size)
//
//            photo.copyPixelsToBuffer(b)
//
//            val bytes = ByteArray(size)
//
//            try {
//                b[bytes, 0, bytes.size]
//            } catch (e: BufferUnderflowException) {
//
//                // always happens
//            }
            bitmap?.compress(Bitmap.CompressFormat.JPEG,100, baos) // photo is the bitmap object
            val byteArray = baos.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)


            val encodedImage: String? = encoded
//            val encoded: String? = getBase64Image(data?.extras?.get("data") as String)
//            Log.e("BitmapPHOTO",encodedImage.toString())
//            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            contactList[imageClickPos].image = encodedImage
            contactList[imageClickPos].selectactivity = contactList[imageClickPos].selectactivity
            contactList[imageClickPos].spinnerActivityPosition = contactList[imageClickPos].spinnerActivityPosition
            contactList[imageClickPos].observation = contactList[imageClickPos].observation
            binding.rvRecycler.adapter?.notifyItemChanged(imageClickPos,contactList[imageClickPos])

        }
    }

//    private fun setPic() : Bitmap{
//
//        /* There isn't enough memory to open up more than a couple camera photos */
//        /* So pre-scale the target bitmap into which the file is decoded */
//
//        /* Get the size of the ImageView */
//        val targetW: Int = recipeImage.getWidth()
//        val targetH: Int = recipeImage.getHeight()
//
//        /* Get the size of the image */
//        val bmOptions = BitmapFactory.Options()
//        bmOptions.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
//        val photoW = bmOptions.outWidth
//        val photoH = bmOptions.outHeight
//
//        /* Figure out which way needs to be reduced less */
//        var scaleFactor = 2
//        if (targetW > 0 && targetH > 0) {
//            scaleFactor = Math.max(photoW / targetW, photoH / targetH)
//        }
//
//        /* Set bitmap options to scale the image decode target */bmOptions.inJustDecodeBounds =
//            false
//        bmOptions.inSampleSize = scaleFactor
//        bmOptions.inPurgeable = true
//        val matrix = Matrix()
//        matrix.postRotate(getRotation())
//
//        /* Decode the JPEG file into a Bitmap */
//        var bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
//
//        /* Associate the Bitmap to the ImageView */recipeImage.setImageBitmap(bitmap)
//    }

    fun getMimeType(context: Context, uri: Uri): String? {
        val extension: String?
        extension = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //If scheme is a content
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
        return extension
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
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
       val officer_name = binding.etNameOfVisitingOfficer.text.toString()
       val designation = binding.etDesignation.text.toString()
       val contact = binding.etContact.text.toString()
       val email = binding.etEmail.text.toString()
       val visitDate = binding.etDate.text.toString()
       val visitTime = binding.etVisitTime.text.toString()
       val lat = binding.etLat.text.toString()
       val long = binding.etLong.text.toString()
       val plumbing = binding.etInternalPlumbing.text.toString()
       val electricalWork = binding.etInternalElectricalWork.text.toString()
       val extService = binding.etService.text.toString()
       val otherDevelopmentWork = binding.etOtherDevelopmentWork.text.toString()
       val materialQuality = binding.etMaterialQuality.text.toString()
       val overallObservation = binding.etOverallObservation.text.toString()
       val remark = binding.etRemark.text.toString()
        var observationList = ArrayList<String>()
        var activityList = ArrayList<String>()
        var imageList = ArrayList<String>()
//        val array:Array<String>=["http://103.48.65.52:8080/EMRS/backend/web/images/vision-mission.png", "http://103.48.65.52:8080/EMRS/backend/web/images/vision-mission.png"]
        contactList.forEach(){
            it.let {
//                if (it.observation?.isNotEmpty() == true && it.image?.isNotEmpty()==true&& it.selectactivity?.isNotEmpty()==true){
                    observationList.add(it.observation?:"")
                    activityList.add(it.selectactivity?:"")
                    imageList.add(it.image?:"")
//                    imageList.add("http://103.48.65.52:8080/EMRS/backend/web/images/vision-mission.png")
                }
//            }
        }
        Log.e("RESPONSESUBMITMAINFORMIMAGE",imageList.toMutableList().size.toString()+imageList.toString())

        if (officer_name.isNotEmpty() && designation.isNotEmpty()  && contact.isNotEmpty()
            && email.isNotEmpty()  && visitDate.isNotEmpty()  && visitTime.isNotEmpty()
            && plumbing.isNotEmpty()
            && electricalWork.isNotEmpty()  && extService.isNotEmpty()  && otherDevelopmentWork.isNotEmpty()
            && materialQuality.isNotEmpty()  && overallObservation.isNotEmpty()  && remark.isNotEmpty()) {
            viewModel.fetchSubmitResponse(
                userId,
                levelId,
                tpqaId,
                officer_name,
                designation,
                contact,
                email,
                stateId,
                schoolId,
                visitDate,
                visitTime,
                lat,
                long,
                activityList,
                observationList,
                imageList,
                plumbing,
                electricalWork,
                extService,
                otherDevelopmentWork,
                materialQuality,
                overallObservation,
                remark
            )

//        viewModel.fetchSubmitResponse(userId, levelId, tpqaId, officer_name, designation,contact,email,stateId,schoolId,
//            visitDate,visitTime,lat,long,activityList.toString(),observationList.toString(),imageList.toString(),
//            plumbing,electricalWork,extService,otherDevelopmentWork,materialQuality,overallObservation,remark)


//        viewModel.fetchSubmitResponse("805", "8", "4", "Manish", "Web","7503227204","manish@gmail.com","49","177",
//            "2023-07-27","10:00","28.97","28.96",activityList,observationList, imageList,
//            "Testing","Testing","Testing","Testing","Testing","Testing","Testing")

            viewModel._response_main_form_submit.observe(this) {
                it?.let {
                    showDialog(this)
//                Toast.makeText(applicationContext,it,Toast.LENGTH_LONG).show()
//                onBackPressed()
                }
            }
        }
        else{
                            Toast.makeText(applicationContext,"Fill all entries first",Toast.LENGTH_LONG).show()

        }


    }

    override fun onLocationChanged(location: Location) {
        lat=location.latitude.toString()
        long=location.longitude.toString()
        etLat.setText(lat)
        etLong.setText(long)
    }

    private fun showDialog(activity: Activity) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window: Window? = dialog.window

        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val body = dialog.findViewById(R.id.btn_Ok) as TextView
//        body.text = title

        body.setOnClickListener {
            dialog.dismiss()
            activity.finish()
            activity.startActivity(intent)

        }
        dialog.show()

    }

    fun isLocationEnabled(context: Context): Boolean? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            val lm = context.getSystemService(LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            // This was deprecated in API 28
            val mode: Int = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

    fun getBase64Image(bitmap: String): String? {
        val inputStream: InputStream =
            FileInputStream(bitmap) // You can get an inputStream using any I/O API

        val bytes: ByteArray
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()

        try {
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bytes = output.toByteArray()
        val encodedString = Base64.encodeToString(bytes, Base64.DEFAULT)
        return encodedString
}
}