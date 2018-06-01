package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_grocery_category.*
import mju_avengers.please_my_fridge.adapter.GroceryCategoryRecyclerAdapter
import mju_avengers.please_my_fridge.data.GroceryDataPlusDate
import mju_avengers.please_my_fridge.db.DataOpenHelper
import org.jetbrains.anko.toast

class GroceryCategoryActivity : AppCompatActivity() {

    lateinit var groceryDatas: ArrayList<GroceryDataPlusDate>
    lateinit var groceryCategoryRecyclerAdapter: GroceryCategoryRecyclerAdapter

    lateinit var categoryName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_category)

        categoryName = intent.getStringExtra("CategoryName")
        grocery_category_name_tv.text = "# 나의 냉장고 속 $categoryName"
        setGroceryCategoryAdapter(categoryName)

        grocery_category_remove_selected_items_btn.setOnClickListener {
            DataOpenHelper.getInstance(this).removeGroceryDatas(groceryCategoryRecyclerAdapter.removedDatas)
            toast("상태 저장 완료")
            finish()
        }

    }

    private fun setGroceryCategoryAdapter(categoryName: String) {
        groceryDatas = ArrayList()
        groceryDatas = DataOpenHelper.getInstance(this).getGroceryDatasPlusFromCategory(categoryName)

        groceryCategoryRecyclerAdapter = GroceryCategoryRecyclerAdapter(this, groceryDatas)

        grocery_category_rv.layoutManager = LinearLayoutManager(applicationContext)
        //grocery_category_rv.layoutManager = GridLayoutManager(applicationContext, 2)
        grocery_category_rv.adapter = groceryCategoryRecyclerAdapter

    }

    override fun onBackPressed() {
        if (groceryCategoryRecyclerAdapter.removedDatas.size != 0) {
            MaterialDialog.Builder(this)
                    .content("현재 냉장고 $categoryName 상태를 저장하시겠습니까?")
                    .positiveText("저장 후 닫기")
                    .positiveColor(resources.getColor(R.color.colorAccent))
                    .neutralText("계속 입력")
                    .negativeText("바로 닫기")
                    .negativeColor(resources.getColor(R.color.colorPrimaryDark))
                    .onPositive { dialog, which ->
                        //saveGroceryDatas()
                        DataOpenHelper.getInstance(this).removeGroceryDatas(groceryCategoryRecyclerAdapter.removedDatas)
                        toast("상태 저장 완료")
                        super.onBackPressed()
                    }
                    .onNegative { dialog, which ->
                        super.onBackPressed()
                    }
                    .onNeutral { dialog, which ->

                    }
                    .show()
        } else {
            super.onBackPressed()
        }

    }
}
