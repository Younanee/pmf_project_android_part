package mju_avengers.please_my_fridge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_grocery_category.*
import mju_avengers.please_my_fridge.adapter.AddGroceryRecyclerAdapter
import mju_avengers.please_my_fridge.adapter.GroceryCategoryRecyclerAdapter
import mju_avengers.please_my_fridge.data.GroceryData
import mju_avengers.please_my_fridge.db.DataOpenHelper

class GroceryCategoryActivity : AppCompatActivity() {

    lateinit var groceryDatas: ArrayList<GroceryData>
    lateinit var groceryCategoryRecyclerAdapter: GroceryCategoryRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_category)

        val categoryName = intent.getStringExtra("CategoryName")
        grocery_category_name_tv.text = "# 나의 냉장고 속 $categoryName"
        setGroceryCategoryAdapter(categoryName)

        grocery_category_remove_selected_items_btn.setOnClickListener {
            //바꾸기?!?! 뭐로바꾸지
        }

    }

    private fun setGroceryCategoryAdapter(categoryName : String){
        groceryDatas = ArrayList()
        groceryDatas = DataOpenHelper.getInstance(this).getGroceryDatasFromCategory(categoryName)

        groceryCategoryRecyclerAdapter = GroceryCategoryRecyclerAdapter(this, groceryDatas)

        grocery_category_rv.layoutManager = LinearLayoutManager(applicationContext)
        //grocery_category_rv.layoutManager = GridLayoutManager(applicationContext, 2)
        //갯수가 너무 많을것을 대비해서 2줄씩 보여주기 등... 메뉴 추가시키기
        grocery_category_rv.adapter = groceryCategoryRecyclerAdapter

    }
}
