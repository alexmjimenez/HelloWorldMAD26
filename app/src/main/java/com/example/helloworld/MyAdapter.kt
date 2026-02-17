package com.example.helloworld

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val items: List<MyItem>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.name

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, OpenStreetMapsActivity::class.java)
            intent.putExtra("LAT", item.latitude)
            intent.putExtra("LON", item.longitude)
            intent.putExtra("NAME", item.name)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}