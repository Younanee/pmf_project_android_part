package mju_avengers.please_my_fridge.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.R

class SearchTab : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addFragment(GrocerySearchTab())
        search_grocery_btn.setBackgroundColor(resources.getColor(R.color.searchButton))


        search_grocery_btn.setOnClickListener {
            search_grocery_btn.setBackgroundColor(resources.getColor(R.color.searchButton))
            search_fridge_btn.setBackgroundColor(resources.getColor(R.color.white))
            replaceFragment(GrocerySearchTab())
        }
        search_fridge_btn.setOnClickListener {
            search_grocery_btn.setBackgroundColor(resources.getColor(R.color.white))
            search_fridge_btn.setBackgroundColor(resources.getColor(R.color.searchButton))
            replaceFragment(FridgeSearchTab())
        }



    }

    fun addFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.search_frame, fragment)
        transaction.commit()
    }
    fun replaceFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.search_frame, fragment)
        transaction.commit()
    }
}