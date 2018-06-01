package mju_avengers.please_my_fridge.adapter

import android.content.Context
import android.graphics.Color

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.db.DataOpenHelper
import org.jetbrains.anko.textColor

class ComponentRecyclerAdapter(val ctx: Context, val components : ArrayList<String>, val myGroceries : ArrayList<String>) : RecyclerView.Adapter<ComponentRecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.detail_component_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return components.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (myGroceries.contains(components[position])){
            holder.name.text = components[position]
            holder.isKeeping.visibility = View.GONE
        } else {
            holder.name.text = components[position]
            holder.name.setTextColor(Color.parseColor("#FF4081"))
        }

    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.food_detail_component_name) as TextView
        val isKeeping : TextView = itemView.findViewById(R.id.food_detail_component_is_keeping_tv) as TextView
    }

}