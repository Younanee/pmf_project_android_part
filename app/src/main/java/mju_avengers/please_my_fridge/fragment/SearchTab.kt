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

        search_fridge_btn.setOnClickListener {

        }


    }

    fun addFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.search_frame, fragment)
        transaction.addToBackStack("")
        transaction.commit()
    }
    fun replaceFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.search_frame, fragment)
        transaction.addToBackStack("")
        transaction.commit()
    }
}