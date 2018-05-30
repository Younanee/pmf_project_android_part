package mju_avengers.please_my_fridge.adapter

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.grocery_category_item.view.*
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.GroceryData
import mju_avengers.please_my_fridge.data.GroceryDataPlusDate
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

class GroceryCategoryRecyclerAdapter(val ctx : Context, val datas: ArrayList<GroceryDataPlusDate>) : RecyclerView.Adapter<GroceryCategoryRecyclerAdapter.Holder>(){

    var removedDatas : ArrayList<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.grocery_category_item, parent, false)
        return Holder(view)
    }



    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.groceryName.text = datas[position].name
        holder.groceryDate.text = "추가일: " + datas[position].date
        holder.groceryRemoveBtn.setOnClickListener {
            removedDatas.add(datas[position].name)
            removeGrocery(position)
            //실제 데이터베이스에서도 지우는 기능 넣기
            //아예 리사이클뷰 다시달아버리면 인덱스 에러 안뜸
        }
    }

    private fun removeGrocery(position: Int){
        datas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, datas.size)
    }


    inner class Holder(itemView : View): RecyclerView.ViewHolder(itemView){
        val groceryName : TextView = itemView.findViewById(R.id.grocery_category_item_name_tv) as TextView
        val groceryRemoveBtn : ImageButton = itemView.findViewById(R.id.grocery_category_remove_ib) as ImageButton
        val groceryDate : TextView = itemView.findViewById(R.id.grocery_category_item_date_tv) as TextView
    }
}