package com.maxi.dogapi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.maxi.dogapi.activities.WelcomeActivity
import com.maxi.dogapi.activities.WelcomeCalaActivity
import com.maxi.dogapi.databinding.ActivityLoginBinding
import com.maxi.dogapi.roomdb.ListDBActivity
import com.maxi.dogapi.utils.NetworkResult
import com.maxi.dogapi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var _binding: ActivityLoginBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_CODE = 1000

    private var lat: String? = null
    private var long: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        val isLogin = sharedPreference.getBoolean("isLogin",false)

        if (isLogin){
            val levelId = sharedPreference.getString("levelId","")
            val fullName = sharedPreference.getString("fullName","")
            val userId = sharedPreference.getString("userId","")
            val tpqaId = sharedPreference.getString("tpqaId","")

            val intent = Intent(this, ListDBActivity::class.java)
            intent.putExtra("levelId", levelId.toString())
            intent.putExtra("fullName", fullName.toString())
            intent.putExtra("userId", userId.toString())
            intent.putExtra("tpqaId", tpqaId.toString())
            startActivity(intent)
        }

//        fetchData()
        _binding.button.setOnClickListener {
            if (_binding.tvUName.text.toString().isNotEmpty() && _binding.tvUPswd.text.toString()
                    .isNotEmpty()
            ) {

                var uName = _binding.tvUName.text.toString()
                var uPswd = _binding.tvUPswd.text.toString()

                val auth_seed = "45685956"
//                uPswd += auth_seed
                Log.e("valuJSONuPswd",uPswd.toString())
//            val auth_seed = RandomStringUtil

//            val randomString=givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect()
//                var uPswd1 = encyptiontech(auth_seed + uPswd, "md5")
                uName = encyptiontech(uName, "SHA-256")
                uPswd = encyptiontech(uPswd, "SHA-256")
//            uName="27aa6643a3725d65cd2653c9c2bc8ffaecfb4ff0ed14724911889f2a8b6dace0df765d007275b0f0e8247b369e165cb398a0e917f254fc9a77751753cee15ec9"
//            uPswd1="f98b805003148f938f0a7c53d86f814e06c2cded0a019823ce2122589351069f1a199f9eae5d3894f0eb4c19172372f9b9bceab0e44667741a9fb20905f6debb"
                fetchData(uName, uPswd+auth_seed, auth_seed)
                //            fetchResponse(uName.toString(),uPswd.toString())
            }
        }
    }

    private fun fetchData(uName: String, uPswd: String, auth_seed: String) {
        fetchResponse(uName, uPswd,auth_seed)
        mainViewModel.response.observe(this) { response ->
            Log.e("RESPONSE","response"+response.toString())

            when (response) {
                is NetworkResult.Success -> {

                    response.data?.let {
                        _binding.pbDog.visibility = View.GONE

                        Log.e("RESPONSE1","response"+response.data.status)

                        if (response.data.status.equals("true")) {
                            val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                            var editor = sharedPreference.edit()
                            editor.putString("fullName",response.data.full_name)
                            editor.putString("levelId",response.data.level_id)
                            editor.putString("userId",response.data.id)
                            editor.putString("tpqaId",response.data.tpqa_id?.toString())
                            editor.putBoolean("isLogin",true)
                            editor.commit()
                            val intent = Intent(this, SurveySubmitActivity::class.java)
                            intent.putExtra("levelId", response.data.level_id.toString())
                            intent.putExtra("fullName", response.data.full_name.toString())
                            intent.putExtra("userId", response.data.id.toString())
                            intent.putExtra("tpqaId", response.data.tpqa_id?.toString())
                            startActivity(intent)
                            finish()
//
//                                intent.putExtra("loginId", response.data.message[0].loginId)
////                                intent.putExtra("loginId", "95524.73741")
//                                intent.putExtra("user_id", response.data.message[0].user_id)
////                                startActivity(intent)

//                            if (response.data.message[0].utid.equals("5")) {
//                                val intent = Intent(this, WelcomeActivity::class.java)
//                                intent.putExtra("utypeId", response.data.message[0].utid)
//                                intent.putExtra("loginId", response.data.message[0].loginId)
////                                intent.putExtra("loginId", "95524.73741")
//                                intent.putExtra("user_id", response.data.message[0].user_id)
////                                startActivity(intent)
//                            }
//                            else{
//                                val intent = Intent(this, WelcomeCalaActivity::class.java)
//                                intent.putExtra("utypeId", response.data.message[0].utid)
//                                intent.putExtra("loginId", response.data.message[0].loginId)
////                                intent.putExtra("loginId", "49906.71536")
//                                intent.putExtra("user_id", response.data.message[0].user_id)
//                                intent.putExtra("org_office_id", response.data.message[0].org_office_id)
////                                startActivity(intent)
//                            }
                        }
                        else{
                            Toast.makeText(
                                this,
                                "Server can not find username",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

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
                    Log.e("RESPONSE1","response"+response.message.toString())

//                    val intent = Intent(this, CalaTrackingActivity::class.java)
//                    intent.putExtra("survey_id", "")
//                    startActivity(intent)
                }

                is NetworkResult.Loading -> {
                    _binding.pbDog.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun fetchResponse(uName: String, uPswd: String, uSeed: String) {

        mainViewModel.fetchLoginResponse(uName,uPswd,"45685956")
        _binding.pbDog.visibility = View.VISIBLE
    }

    private fun encyptiontech(input:String,type:String):String {

        try {

        // Static getInstance method is called with hashing MD5
            var  md = MessageDigest.getInstance(type);

        // digest() method is called to calculate message digest
        // of an input digest() return array of byte
            var messageDigest  = md.digest(input.toByteArray());

        // Convert byte array into signum representation
        val no =  BigInteger(1, messageDigest);

        // Convert message digest into hex value
        var hashtext = no.toString(16);
        while (hashtext.length < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    // For specifying wrong message digest algorithms
    catch (e: NoSuchAlgorithmException) {
        throw  RuntimeException(e);
    }
    }


private fun fetchLocation() {
    val permissionGranted = requestLocationPermission();
    if (permissionGranted) {
//            if (isLocationEnabled()) {
        mFusedLocationClient.lastLocation.addOnCompleteListener(this) { tasks ->
            val location: Location? = tasks.result
            if (location != null) {
               lat= location.latitude.toString()
                long=location.longitude.toString()

//                        val geocoder = Geocoder(this, Locale.getDefault())
//                        binding.etLat.setText(location.latitude.toString())
//                        binding.etLong.setText(location.longitude.toString())
//                        val list: List<Address> =
//                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                        mainBinding.apply {
//                            tvLatitude.text = "Latitude\n${list[0].latitude}"
//                            tvLongitude.text = "Longitude\n${list[0].longitude}"
//                            tvCountryName.text = "Country Name\n${list[0].countryName}"
//                            tvLocality.text = "Locality\n${list[0].locality}"
//                            tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
//                        }
            }
        }
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




 fun givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect():String {
    val array = ByteArray(4) // length is bounded by 4
    Random().nextBytes(array)
    val generatedString = String(array, Charset.forName("UTF-8"))
    println(generatedString)
    return generatedString
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

