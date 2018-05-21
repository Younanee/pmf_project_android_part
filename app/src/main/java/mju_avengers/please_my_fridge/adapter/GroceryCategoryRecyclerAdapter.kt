package mju_avengers.please_my_fridge.adapter

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import kotlinx.android.synthetic.main.grocery_category_item.view.*
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.GroceryData
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

class GroceryCategoryRecyclerAdapter(val ctx : Context, val datas: ArrayList<GroceryData>) : RecyclerView.Adapter<GroceryCategoryRecyclerAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.grocery_category_item, parent, false)
        return Holder(view)
    }


    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.groceryName.text = datas[position].name
        holder.groceryRemoveBtn.setOnClickListener {
            removeGrocery(position)
            //실제 데이터베이스에서도 지우는 기능 넣기
        }
    }

    private fun removeGrocery(position: Int){
        datas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, datas.size)
    }


    inner class Holder(itemView : View): RecyclerView.ViewHolder(itemView){
        val groceryName : TextView = itemView.findViewById(R.id.grocery_category_item_name_tv) as TextView
        val groceryRemoveBtn : TextView = itemView.findViewById(R.id.grocery_category_remove_tv) as TextView
    }
}