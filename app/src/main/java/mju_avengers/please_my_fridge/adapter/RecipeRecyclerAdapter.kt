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

class RecipeRecyclerAdapter(val ctx: Context, val directions : ArrayList<String>, var urls : ArrayList<String>) : RecyclerView.Adapter<RecipeRecyclerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.recipe_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return directions.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (itemCount > position) {
            holder.direction.text = directions[position]

            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_image_black_24dp)
            requestOptions.error(R.drawable.ic_clear_black_36dp)
//            requestOptions.centerCrop()
            requestOptions.fitCenter()
            Glide.with(ctx)
                    .setDefaultRequestOptions(requestOptions)
                    .load(urls[position])
                    .thumbnail(0.5f)
                    .into(holder.url)
            holder.index.text = (position + 1).toString()
        }
    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val direction : TextView = itemView.findViewById(R.id.recipe_direction_tv) as TextView
        val url : ImageView = itemView.findViewById(R.id.recipe_url_iv) as ImageView
        val index : TextView = itemView.findViewById(R.id.recipe_index_tv) as TextView
    }

}