package com.example.eventfuboru.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventfuboru.R
import com.example.eventfuboru.model.Register

class AdapterRegister(dataList: ArrayList<Register>?): RecyclerView.Adapter<AdapterRegister.RegisterViewHolder>() {
    private val dataList: ArrayList<Register>?

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_register, parent, false)
        return RegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
        holder.namaRegister.text = dataList!![position].fullname
        holder.regRegister.text = "No. Register : " + dataList[position].registration_number
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class RegisterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaRegister: TextView
        val regRegister: TextView

        init {
            namaRegister = itemView.findViewById<View>(R.id.namaRegister) as TextView
            regRegister = itemView.findViewById<View>(R.id.regRegister) as TextView
        }
    }
}