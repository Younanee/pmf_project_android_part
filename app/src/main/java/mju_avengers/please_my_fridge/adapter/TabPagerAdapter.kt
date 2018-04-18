package mju_avengers.please_my_fridge.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import mju_avengers.please_my_fridge.fragment.*

class TabPagerAdapter(var tabCount : Int, fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position){
            0 -> return HomeTab()
            1 -> return SearchTab()
            2 -> return AddTab()
            3 -> return FavoriteTab()
            4 -> return SettingTab()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

}