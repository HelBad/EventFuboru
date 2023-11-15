package com.example.eventfuboru.view

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventfuboru.adapter.ViewholderBeranda
import com.example.eventfuboru.model.User
import com.example.eventfuboru.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_beranda.*

class ActivityBeranda : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: Query
    lateinit var refSearch: DatabaseReference
    lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        (this as AppCompatActivity).setSupportActionBar(toolbarBeranda)
        alertDialog = AlertDialog.Builder(this)
        ref = FirebaseDatabase.getInstance().getReference("register")
            .orderByChild("status").equalTo("Hadir")
        refSearch = FirebaseDatabase.getInstance().getReference("register")

        mLayoutManager = LinearLayoutManager(this)
        recyclerBeranda.setHasFixedSize(true)
        recyclerBeranda.layoutManager = mLayoutManager
        listData()
        getCount()

        btnScan.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityScan::class.java))
            finish()
        }
        btnInput.setOnClickListener {
            showDialog()
        }
    }

    private fun getCount() {
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                totalBeranda.setText(dataSnapshot.childrenCount.toInt().toString())
                validateLay()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun validateLay() {
        if(totalBeranda.text.toString() == "0") {
            kosongBeranda.visibility = View.VISIBLE
            recyclerBeranda.visibility = View.GONE
        } else {
            kosongBeranda.visibility = View.GONE
            recyclerBeranda.visibility = View.VISIBLE
        }
    }

    private fun showDialog() {
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_beranda, null)
        val dialogBeranda = dialogLayout.findViewById(R.id.dialogBeranda) as EditText

        with(alertDialog) {
            setPositiveButton("OK") { dialog, which ->
                if(dialogBeranda.text.toString() == "") {
                    Toast.makeText(this@ActivityBeranda, "No. Register kosong", Toast.LENGTH_SHORT).show()
                    startActivityForResult(Intent(this@ActivityBeranda, ActivityBeranda::class.java), 1)
                } else {
                    val intent = Intent(this@ActivityBeranda, ActivityKonfirm::class.java)
                    intent.putExtra("QRCode", dialogBeranda.text.toString())
                    intent.putExtra("input", "Manual")
                    startActivity(intent)
                    finish()
                }
            }
            setNegativeButton("BATAL") { dialog, which ->
                startActivityForResult(Intent(this@ActivityBeranda, ActivityBeranda::class.java), 1)
            }
            setView(dialogLayout)
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_beranda, menu)
        val item = menu!!.findItem(R.id.cari)
        val searchView = item.actionView as SearchView
        searchView.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query:String):Boolean {
                dataSearch(query)
                return false
            }
            override fun onQueryTextChange(newText:String):Boolean {
                dataSearch(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.register) {
            startActivity(Intent(this@ActivityBeranda, ActivityRegister::class.java))
            finish()
            return true
        }
        if (id == R.id.service) {
            startActivity(Intent(this@ActivityBeranda, ActivityService::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listData() {
        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<User, ViewholderBeranda>(
            User::class.java,
            R.layout.list_beranda,
            ViewholderBeranda::class.java,
            ref
        ) {
            override fun populateViewHolder(viewHolder: ViewholderBeranda, model: User, position: Int) {
                viewHolder.setDetails(model)
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderBeranda {
                val viewHolder = super.onCreateViewHolder(parent, viewType)
                viewHolder.setOnClickListener(object : ViewholderBeranda.ClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val intent = Intent(view.context, ActivityDetail::class.java)
                        intent.putExtra("Flag", "R")
                        intent.putExtra("no_reg", viewHolder.user.no_reg)
                        startActivity(intent)
                    }
                    override fun onItemLongClick(view: View, position: Int) {}
                })
                return viewHolder
            }
        }
        recyclerBeranda.adapter = firebaseRecyclerAdapter
    }

    private fun dataSearch(searchText:String) {
        val query = refSearch.orderByChild("search").startAt("Hadir | $searchText")
            .endAt("Hadir | $searchText\uf8ff")
        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<User, ViewholderBeranda>(
            User::class.java,
            R.layout.list_beranda,
            ViewholderBeranda::class.java,
            query
        ) {
            override fun populateViewHolder(viewHolder: ViewholderBeranda, model: User, position: Int) {
                viewHolder.setDetails(model)
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderBeranda {
                val viewHolder = super.onCreateViewHolder(parent, viewType)
                viewHolder.setOnClickListener(object : ViewholderBeranda.ClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val intent = Intent(view.context, ActivityDetail::class.java)
                        intent.putExtra("Flag", "R")
                        intent.putExtra("no_reg", viewHolder.user.no_reg)
                        startActivity(intent)
                    }
                    override fun onItemLongClick(view: View, position: Int) {}
                })
                return viewHolder
            }
        }
        recyclerBeranda.adapter = firebaseRecyclerAdapter
    }

    override fun onBackPressed() {
        alertDialog.setTitle("Keluar Aplikasi")
        alertDialog.setMessage("Apakah anda ingin keluar aplikasi ?").setCancelable(false)
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    finishAffinity()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    startActivityForResult(Intent(this@ActivityBeranda, ActivityBeranda::class.java), 1)
                }
            }).create().show()
    }
}