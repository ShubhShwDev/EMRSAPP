package com.maxi.dogapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.maxi.dogapi.activities.WelcomeActivity
import com.maxi.dogapi.activities.WelcomeCalaActivity
import com.maxi.dogapi.databinding.ActivityLoginBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= DataBindingUtil.setContentView(this,R.layout.activity_login);


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

                        if (response.data.status.equals("200")) {
//                            Toast.makeText(applicationContext,response.data.status+"  "+response.data.level_id,Toast.LENGTH_LONG).show()
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
                            finish()
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
}

 fun givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect():String {
    val array = ByteArray(4) // length is bounded by 4
    Random().nextBytes(array)
    val generatedString = String(array, Charset.forName("UTF-8"))
    println(generatedString)
    return generatedString
}

