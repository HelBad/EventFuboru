package com.example.eventfuboru.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.example.eventfuboru.R
import com.example.eventfuboru.adapter.AdapterRegister
import com.example.eventfuboru.api.ApiRegister
import com.example.eventfuboru.model.Register
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import kotlin.collections.ArrayList

class ActivityRegister : AppCompatActivity() {
    lateinit var adapter: AdapterRegister
    lateinit var dataArrayList: ArrayList<Register>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        (this as AppCompatActivity).setSupportActionBar(toolbar1Register)
        loadKeterangan()

        adapter = AdapterRegister(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerRegister.layoutManager = layoutManager
        recyclerRegister.adapter = adapter

        lanjutRegister.setOnClickListener {
            if(namaRegister.text.toString() == "") {
                toolbar1Register.visibility = View.VISIBLE
                toolbar2Register.visibility = View.GONE
                Toast.makeText(this@ActivityRegister, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(namaRegister.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbar1Register.visibility = View.VISIBLE
                toolbar2Register.visibility = View.GONE
                Toast.makeText(this@ActivityRegister, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivityRegister, ActivityRegister::class.java)
                intent.putExtra("nama", namaRegister.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalRegister.setOnClickListener {
            namaRegister.setText("")
        }
    }

    private fun loadKeterangan() {
        val fetchData = FetchData(ApiRegister.REGISTRASI_COUNT)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    totalRegister.setText(String.format("%03d", dataObject.getString("COUNT(*)").toInt() + 1))
                } catch (t: Throwable) { }
            }
        }

        namaRegister.setText(intent.getStringExtra("nama").toString())
        if(namaRegister.text.toString() == "null") {
            namaRegister.setText("")
            loadRegister()
        } else {
            namaRegister.setText(intent.getStringExtra("nama").toString())
            searchRegister()
        }
    }

    private fun loadRegister() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiRegister.REGISTRASI)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Register(
                            dataArray.getJSONObject(i).getString("id"),
                            dataArray.getJSONObject(i).getString("registration_number"),
                            dataArray.getJSONObject(i).getString("fullname")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchRegister() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiRegister.REGISTRASI +
                "?fullname=" + intent.getStringExtra("nama").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Register(
                            dataArray.getJSONObject(i).getString("id"),
                            dataArray.getJSONObject(i).getString("registration_number"),
                            dataArray.getJSONObject(i).getString("fullname")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_cari_2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.cari) {
            toolbar1Register.visibility = View.GONE
            toolbar2Register.visibility = View.VISIBLE
            loadKeterangan()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityRegister, ActivityBeranda::class.java))
        finish()
    }
}