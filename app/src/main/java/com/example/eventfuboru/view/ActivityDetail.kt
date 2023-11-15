package com.example.eventfuboru.view

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.eventfuboru.R
import com.example.eventfuboru.model.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class ActivityDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: Query
    var service: ArrayList<String> = arrayListOf()
    @SuppressLint("NewApi")
    var formatDate = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        alertDialog = AlertDialog.Builder(this)
        service = arrayListOf("", "", "", "", "", "", "", "", "", "", "", "", "", "")
        validateCard()

        btnService.setOnClickListener {
            if(validate()) {
                alertDialog.setMessage("Apakah data peserta sudah benar ?").setCancelable(false)
                    .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            saveData()
                            finish()
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

    private fun validateCard() {
        if(intent.getStringExtra("Flag").toString() == "NR") {
            ref = FirebaseDatabase.getInstance().getReference("no_register")
                .orderByChild("no_reg").equalTo(intent.getStringExtra("no_reg").toString())
            getData()
            cardUrutDetail.visibility = View.GONE
            cardTglscanDetail.visibility = View.GONE
            cardTglserviceDetail.visibility = View.GONE
            cardStatusDetail.visibility = View.GONE
            btnService.visibility = View.GONE
        } else if(intent.getStringExtra("Flag").toString() == "R") {
            ref = FirebaseDatabase.getInstance().getReference("register")
                .orderByChild("no_reg").equalTo(intent.getStringExtra("no_reg").toString())
            getData()
            cardTglserviceDetail.visibility = View.GONE
        } else {
            ref = FirebaseDatabase.getInstance().getReference("service")
                .orderByChild("no_reg").equalTo(intent.getStringExtra("no_reg").toString())
            getData()
            btnService.visibility = View.GONE
        }
    }

    private fun getData() {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val allocation = snapshot1.getValue(User::class.java)
                    service[0] = allocation!!.no_reg
                    service[1] = allocation.no_urut
                    service[2] = allocation.nama
                    service[3] = allocation.email
                    service[4] = allocation.telp
                    service[5] = allocation.pekerjaan
                    service[6] = allocation.kendaraan
                    service[7] = allocation.merk
                    service[8] = allocation.nopol
                    service[9] = allocation.shift
                    service[10] = allocation.token
                    service[11] = allocation.tgl_scan
                    service[12] = allocation.tgl_service
                    service[13] = allocation.status

                    regDetail.text = service[0]
                    urutDetail.text = service[1]
                    namaDetail.text = service[2]
                    emailDetail.text = service[3]
                    telpDetail.text = service[4]
                    pekerjaanDetail.text = service[5]
                    kendaraanDetail.text = service[6]
                    merkDetail.text = service[7]
                    nopolDetail.text = service[8]
                    shiftDetail.text = service[9]
                    tglscanDetail.text = service[11]
                    tglserviceDetail.text = service[12]
                    statusDetail.text = service[13]
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun validate(): Boolean {
        if(service[0] == "") {
            Toast.makeText(this@ActivityDetail, "Data invalid", Toast.LENGTH_SHORT).show()
            return false
        }
        if(formatDate.format(Date()).toString() == "") {
            Toast.makeText(this@ActivityDetail, "Data invalid", Toast.LENGTH_SHORT).show()
            return false
        }
        if(service[13] == "") {
            Toast.makeText(this@ActivityDetail, "Data invalid", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveData() {
        val addData = User(service[0], service[1], service[2], service[3], service[4], service[5],
            service[6], service[7], service[8], service[9], service[10], service[11],
            formatDate.format(Date()).toString(), "Selesai", "")
        FirebaseDatabase.getInstance().getReference("service").child(service[1])
            .setValue(addData).addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("register").child(service[1])
                    .child("status").setValue("Selesai")
                FirebaseDatabase.getInstance().getReference("register").child(service[1])
                    .child("search").setValue("")
            Toast.makeText(this@ActivityDetail, "Service Berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}