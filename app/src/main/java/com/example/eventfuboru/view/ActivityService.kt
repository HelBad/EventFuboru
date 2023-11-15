package com.example.eventfuboru.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventfuboru.R
import com.example.eventfuboru.adapter.ViewholderBeranda
import com.example.eventfuboru.model.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_service.*

class ActivityService : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        (this as AppCompatActivity).setSupportActionBar(toolbarService)
        ref = FirebaseDatabase.getInstance().getReference("service")

        mLayoutManager = LinearLayoutManager(this)
        recyclerService.setHasFixedSize(true)
        recyclerService.layoutManager = mLayoutManager
        listData()
        getCount()
    }

    private fun getCount() {
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                totalService.setText(dataSnapshot.childrenCount.toInt().toString())
                validateLay()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun validateLay() {
        if(totalService.text.toString() == "0") {
            kosongService.visibility = View.VISIBLE
            recyclerService.visibility = View.GONE
        } else {
            kosongService.visibility = View.GONE
            recyclerService.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_cari, menu)
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
                        intent.putExtra("Flag", "S")
                        intent.putExtra("no_reg", viewHolder.user.no_reg)
                        startActivity(intent)
                    }
                    override fun onItemLongClick(view: View, position: Int) {}
                })
                return viewHolder
            }
        }
        recyclerService.adapter = firebaseRecyclerAdapter
    }

    private fun dataSearch(searchText:String) {
        val query = ref.orderByChild("nama").startAt(searchText).endAt(searchText + "\uf8ff")
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
                        intent.putExtra("Flag", "NR")
                        intent.putExtra("no_reg", viewHolder.user.no_reg)
                        startActivity(intent)
                    }
                    override fun onItemLongClick(view: View, position: Int) {}
                })
                return viewHolder
            }
        }
        recyclerService.adapter = firebaseRecyclerAdapter
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityService, ActivityBeranda::class.java))
        finish()
    }
}