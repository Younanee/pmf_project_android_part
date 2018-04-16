package mju_avengers.please_my_fridge.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.testFoodData

class FoodRecyclerAdapter(val ctx :Context, val testFoodDatas: ArrayList<testFoodData>): RecyclerView.Adapter<FoodRecyclerAdapter.Holder>() {
    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.food_item, parent, false)
        view.setOnClickListener(onItemClick)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return testFoodDatas.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.foodName.text = testFoodDatas[position].name
        holder.recipe.text = testFoodDatas[position].recipe
        //holder.thumbnail.setImageResource()
    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val foodName : TextView = itemView.findViewById(R.id.food_item_tv_name) as TextView
        //val thumbnail : ImageView = itemView.findViewById(R.id.food_item_thumbnail_image) as ImageView
        val recipe : TextView = itemView.findViewById(R.id.food_item_tv_recipe) as TextView
//        fun bind(testFoodData: testFoodData, ctx: Context){
//            foodName?.text = testFoodData.name
//        }
    }
}