package mju_avengers.please_my_fridge.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.GroceryData

class AddGroceryRecyclerAdapter(val ctx : Context, val groceryDatas : ArrayList<GroceryData>) : RecyclerView.Adapter<AddGroceryRecyclerAdapter.Holder>(){
    private lateinit var onItemClick : View.OnClickListener
    private lateinit var onRemoveClick : View.OnClickListener

    fun setOnRemoveClickListener(l : View.OnClickListener){
        onRemoveClick = l
    }
    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.grocery_item, parent, false)
        setOnItemClickListener(onItemClick)
        return Holder(view)
    }


    override fun getItemCount(): Int = groceryDatas.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.category.text = "#"+groceryDatas[position].category.toString()
        holder.foodName.text = groceryDatas[position].name
        holder.removeBtn.setOnClickListener {
            removeGrocery(position)
        }
    }

    fun addGrocery(data : GroceryData){
        groceryDatas.add(data)
        notifyItemInserted(groceryDatas.size)
    }
    private fun removeGrocery(position: Int){
        groceryDatas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, groceryDatas.size)
    }
    fun getCurrentGroceryDatas() : ArrayList<GroceryData> = groceryDatas

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val category : TextView = itemView.findViewById(R.id.grocery_item_category_tv) as TextView
        val foodName : TextView = itemView.findViewById(R.id.grocery_item_name_tv) as TextView
        val removeBtn : ImageView = itemView.findViewById(R.id.grocery_item_remove_btn) as ImageView
    }
}