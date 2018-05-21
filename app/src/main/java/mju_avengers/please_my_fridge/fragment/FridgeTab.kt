package mju_avengers.please_my_fridge.fragment

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fridge.*
import mju_avengers.please_my_fridge.GroceryCategoryActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.GroceryData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.startActivity

class FridgeTab : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fridge, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fridge_grocery_add_btn.setOnClickListener {
            val dialog : DialogFragment = SelectedAddOptionDialog()
            dialog.show(childFragmentManager, "SelectedAddOption")
        }

        fridge_category_meat_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "육류")
        }
        fridge_category_seafood_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "어패류")
        }
        fridge_category_vegetable_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "채소류")
        }
        fridge_category_fruit_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "과일류")
        }
        fridge_category_sauce_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "소스류")
        }
        fridge_category_grain_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "곡류")
        }
        fridge_category_etc_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "기타")
        }
    }

    private fun setViewCountGrocery(){
        val categories = arrayListOf("육류", "어패류", "채소류", "과일류", "소스류", "곡류", "기타")
        var arrays : ArrayList<Int> = ArrayList()
        categories.forEach {
            val groceryCount : Int = DataOpenHelper.getInstance(activity!!).use {
                select(GroceryData.TABLE_NAME)
                        .whereArgs(GroceryData.COLUMN_CATEGORY + " = {categoryName}", "categoryName" to it)
                        .exec { parseList(classParser<GroceryData>()).size }
            }
            arrays.add(groceryCount)
        }
        fridge_category_meat_count_tv.text = arrays[0].toString()
        fridge_category_seafood_count_tv.text = arrays[1].toString()
        fridge_category_vegetable_count_tv.text = arrays[2].toString()
        fridge_category_fruit_count_tv.text = arrays[3].toString()
        fridge_category_sauce_count_tv.text = arrays[4].toString()
        fridge_category_grain_count_tv.text = arrays[5].toString()
        fridge_category_etc_count_tv.text = arrays[6].toString()
    }

    override fun onStart() {
        super.onStart()
        setViewCountGrocery()


    }


}