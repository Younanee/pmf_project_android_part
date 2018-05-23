package mju_avengers.please_my_fridge.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.SimpleFoodData

class SearchFoodRecyclerAdapter(val ctx : Context, var simpleFoodItems : ArrayList<SimpleFoodData>): RecyclerView.Adapter<SearchFoodRecyclerAdapter.Holder>() {
    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.search_food_item, parent, false)
        view.setOnClickListener(onItemClick)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return simpleFoodItems.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_image_black_24dp)
        requestOptions.error(R.drawable.ic_clear_black_36dp)
        requestOptions.centerCrop()
        Glide.with(ctx)
                .setDefaultRequestOptions(requestOptions)
                .load(simpleFoodItems[position].url)
                .thumbnail(0.5f)
                .into(holder.url)
        holder.name.text = simpleFoodItems[position].title
        holder.percent.text = "재료 보유율 " + simpleFoodItems[position].percent.toString() + "%"
        holder.starRate.text = "별점 "+simpleFoodItems[position].starRate.toString()    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.search_food_item_name_tv) as TextView
        val url : ImageView = itemView.findViewById(R.id.search_food_item_url_iv) as ImageView
        val percent : TextView = itemView.findViewById(R.id.search_food_item_percent_tv) as TextView
        val starRate : TextView = itemView.findViewById(R.id.search_food_item_star_tv) as TextView
    }
}