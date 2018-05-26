package mju_avengers.please_my_fridge.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import mju_avengers.please_my_fridge.fragment.*

class DetailedFoodTabPagerAdapter(var tabCount : Int, fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position){
            0 -> return FoodHomeTab()
            1 -> return FoodComponentTab()
            2 -> return FoodRecipeTab()
            else -> return null
        }
    }
    override fun getCount(): Int {
        return tabCount
    }

}