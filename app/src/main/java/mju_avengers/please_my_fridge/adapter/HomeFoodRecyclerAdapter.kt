package mju_avengers.please_my_fridge.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.HomeFoodData

class HomeFoodRecyclerAdapter(val ctx: Context, val homeFoodData: ArrayList<HomeFoodData>) : RecyclerView.Adapter<HomeFoodRecyclerAdapter.Holder>() {
    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.main_food_item, parent, false)
        view.setOnClickListener(onItemClick)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return homeFoodData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.thumbnail.setImageResource(homeFoodData[position].thumbnail)
        holder.foodName.text = homeFoodData[position].name
        holder.percent.text = "재료 보유율 " + homeFoodData[position].percent.toString() + "%"
        holder.starRate.text = "별점 "+homeFoodData[position].starRate.toString()
    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val foodName : TextView = itemView.findViewById(R.id.home_food_cv_name_tv) as TextView
        val thumbnail : ImageView = itemView.findViewById(R.id.home_food_cv_thumbnail_iv) as ImageView
        val percent : TextView = itemView.findViewById(R.id.home_food_cv_percent_tv) as TextView
        val starRate : TextView = itemView.findViewById(R.id.home_food_cv_star_rate_tv) as TextView
    }
}