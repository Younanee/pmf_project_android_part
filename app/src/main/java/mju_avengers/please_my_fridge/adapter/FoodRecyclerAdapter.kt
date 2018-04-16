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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.food_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return testFoodDatas.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(testFoodDatas[position], ctx)
    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val foodName = itemView.findViewById<TextView>(R.id.tv_food_list_item_name)

        fun bind(testFoodData: testFoodData, ctx: Context){
            foodName?.text = testFoodData.name
        }
    }
}