package com.example.eventfuboru.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventfuboru.R
import com.example.eventfuboru.model.User

class ViewholderBeranda(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var mView: View = itemView
    private var mClickListener: ClickListener? = null
    var user = User()

    init {
        itemView.setOnClickListener { view -> mClickListener!!.onItemClick(view, adapterPosition) }
        itemView.setOnLongClickListener { view ->
            mClickListener!!.onItemLongClick(view, adapterPosition)
            true
        }
    }

    fun setDetails(user: User) {
        this.user = user
        val noBeranda = mView.findViewById(R.id.noBeranda) as TextView
        val namaBeranda = mView.findViewById(R.id.namaBeranda) as TextView
        val regBeranda = mView.findViewById(R.id.regBeranda) as TextView

        noBeranda.text = user.no_urut
        namaBeranda.text = user.nama
        regBeranda.text = "No. Register : " + user.no_reg
    }

    interface ClickListener {
        fun onItemClick(view: View, position:Int)
        fun onItemLongClick(view: View, position:Int)
    }

    fun setOnClickListener(clickListener:ClickListener) {
        mClickListener = clickListener
    }
}