package com.example.eventfuboru.view

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.eventfuboru.R
import com.example.eventfuboru.api.ApiRegister
import com.example.eventfuboru.api.RetrofitAPI
import com.example.eventfuboru.model.DataScan
import com.example.eventfuboru.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.activity_konfirm.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class ActivityKonfirm : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var dbReg: DatabaseReference
    var register: ArrayList<String> = arrayListOf()
    @SuppressLint("NewApi")
    var formatDate = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirm)

        alertDialog = AlertDialog.Builder(this)
        dbReg = FirebaseDatabase.getInstance().getReference("register")
        register = arrayListOf("", "", "", "", "", "", "", "", "", "", "", "", "", "")
        getData()

        btnRegister.setOnClickListener {
            if(validate()) {
                alertDialog.setMessage("Apakah data peserta sudah benar ?").setCancelable(false)
                    .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            saveData()
                        }
                    })
                    .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            dialog.cancel()
                        }
                    }).create().show()
            }
        }
    }

    private fun getData() {
        if(intent.getStringExtra("input").toString() == "Manual") {
            val fetchData = FetchData(ApiRegister.REGISTRASI_DETAIL +
                    "?registration_number=" + intent.getStringExtra("QRCode").toString())
            if (fetchData.startFetch()) {
                if (fetchData.onComplete()) {
                    val result: String = fetchData.result
                    try {
                        val data = JSONObject(result)
                        val dataObject = data.getJSONObject("data")
                        register[0] = dataObject.getString("registration_number")
                        register[2] = dataObject.getString("fullname")
                        register[3] = dataObject.getString("users")
                        register[4] = dataObject.getString("no_hp")
                        register[5] = dataObject.getString("jobs")
                        register[6] = dataObject.getString("vehicle_type")
                        register[7] = dataObject.getString("manufactures")
                        register[8] = dataObject.getString("license_plate")
                        register[9] = dataObject.getString("shifts_start") + " sampai " + dataObject.getString("shifts_end")
                        register[10] = dataObject.getString("token")
                    } catch (t: Throwable) { }
                }
            }
        } else {
            val fetchData = FetchData(ApiRegister.REGISTRASI_DETAIL +
                    "?token=" + intent.getStringExtra("QRCode").toString())
            if (fetchData.startFetch()) {
                if (fetchData.onComplete()) {
                    val result: String = fetchData.result
                    try {
                        val data = JSONObject(result)
                        val dataObject = data.getJSONObject("data")
                        register[0] = dataObject.getString("registration_number")
                        register[2] = dataObject.getString("fullname")
                        register[3] = dataObject.getString("users")
                        register[4] = dataObject.getString("no_hp")
                        register[5] = dataObject.getString("jobs")
                        register[6] = dataObject.getString("vehicle_type")
                        register[7] = dataObject.getString("manufactures")
                        register[8] = dataObject.getString("license_plate")
                        register[9] = dataObject.getString("shifts_start") + " sampai " + dataObject.getString("shifts_end")
                        register[10] = dataObject.getString("token")
                    } catch (t: Throwable) { }
                }
            }
        }

        dbReg.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                regKonfirm.text = register[0]
                namaKonfirm.text = register[2]
                emailKonfirm.text = register[3]
                telpKonfirm.text = register[4]
                pekerjaanKonfirm.text = register[5]
                kendaraanKonfirm.text = register[6]
                merkKonfirm.text = register[7]
                nopolKonfirm.text = register[8]
                shiftKonfirm.text = register[9]

                register[1] = String.format("%03d", dataSnapshot.childrenCount.toInt() + 1)
                register[11] = formatDate.format(Date()).toString()
                register[12] = "Hadir"
                register[13] = register[12] + " | " + register[2]
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        validateLay()
    }

    private fun validateLay() {
        if(register[0] == "") {
            kosongKonfirm.visibility = View.VISIBLE
            layKonfirm.visibility = View.GONE
            btnRegister.visibility = View.GONE
        } else {
            kosongKonfirm.visibility = View.GONE
            layKonfirm.visibility = View.VISIBLE
            btnRegister.visibility = View.VISIBLE
        }
    }

    private fun validate(): Boolean {
        if(register[0] == "") {
            Toast.makeText(this@ActivityKonfirm, "Data invalid", Toast.LENGTH_SHORT).show()
            return false
        }
        if(register[1] == "") {
            Toast.makeText(this@ActivityKonfirm, "Data invalid", Toast.LENGTH_SHORT).show()
            return false
        }
        if(register[12] == "") {
            Toast.makeText(this@ActivityKonfirm, "Data invalid", Toast.LENGTH_SHORT).show()
            return false
        }
        if(register[13] == "") {
            Toast.makeText(this@ActivityKonfirm, "Data invalid", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveData() {
        val addData = User(register[0], register[1], register[2], register[3], register[4],
            register[5], register[6], register[7], register[8], register[9], register[10],
            register[11], "", register[12], register[13])
        dbReg.child(register[1]).setValue(addData).addOnSuccessListener {
            postData(register[1])
        }
    }

    private fun postData(noRegistration: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://event.fuboru.co.id/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val dataModal = DataScan(noRegistration)
        val call: Call<DataScan?>? = retrofitAPI.postData(dataModal)

        call!!.enqueue(object : Callback<DataScan?> {
            override fun onResponse(call: Call<DataScan?>?, response: Response<DataScan?>) {
                Toast.makeText(this@ActivityKonfirm, "Register Berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ActivityKonfirm, ActivityBeranda::class.java))
                finish()
            }
            override fun onFailure(call: Call<DataScan?>?, t: Throwable) {
                Toast.makeText(this@ActivityKonfirm, "Register Gagal", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityKonfirm, ActivityBeranda::class.java))
        finish()
    }
}