package mju_avengers.please_my_fridge.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fridge.*
import mju_avengers.please_my_fridge.GroceryCategoryActivity
import mju_avengers.please_my_fridge.R
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
            startActivity<GroceryCategoryActivity>("CategoryName" to "meat")
        }
        fridge_category_seafood_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "seafood")
        }
        fridge_category_vegetable_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "vegetable")
        }
        fridge_category_fruit_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "fruit")
        }
        fridge_category_sauce_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "sauce")
        }
        fridge_category_grain_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "grain")
        }
        fridge_category_etc_btn.setOnClickListener {
            startActivity<GroceryCategoryActivity>("CategoryName" to "etc")
        }
    }


}