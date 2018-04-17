package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mju_avengers.please_my_fridge.data.testFoodData

class HomeTab : Fragment(){


    lateinit var foodItems : ArrayList<testFoodData>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        foodItems = ArrayList()


    }


}