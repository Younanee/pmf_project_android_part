package mju_avengers.please_my_fridge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.grocery_item.*
import mju_avengers.please_my_fridge.adapter.AddGroceryRecyclerAdapter
import mju_avengers.please_my_fridge.data.GroceryCategory
import mju_avengers.please_my_fridge.data.GroceryData
import org.jetbrains.anko.*

class AddActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        var idx : Int = add_grocery_item_rv.getChildAdapterPosition(v)
    }

    lateinit var groceryDatas : ArrayList<GroceryData>
    lateinit var addGroceryDataAdapter : AddGroceryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setGroceryDataAdapter()
        add_grocery_add_btn.setOnClickListener {
            alert{
                title = "식료품 추가하기"
                positiveButton("추가"){
                    addGroceryDataAdapter.addGrocery(GroceryData(groceryDatas.size.toString(), GroceryCategory.MEAT))
                    if (groceryDatas.size > 0) {
                        add_notice_tv.visibility = View.INVISIBLE
                    }
                }
            }.show()
        }
    }

    fun setGroceryDataAdapter(){
        groceryDatas = ArrayList()
        addGroceryDataAdapter = AddGroceryRecyclerAdapter(this, groceryDatas)
        addGroceryDataAdapter.setOnItemClickListener(this)

        add_grocery_item_rv.layoutManager = LinearLayoutManager(applicationContext)
        add_grocery_item_rv.adapter = addGroceryDataAdapter
    }


}
