package mju_avengers.please_my_fridge.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import mju_avengers.please_my_fridge.data.SimpleFoodData
import mju_avengers.please_my_fridge.fragment.*
import java.io.Serializable

class TabPagerAdapter(var tabCount : Int, fm : FragmentManager, private val datas : ArrayList<SimpleFoodData>) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        val homeTab : Fragment = HomeTab.newInstance(datas)
        val searchTab : Fragment = SearchTab.newInstance(datas)
        return when (position){
            0 -> homeTab
            1 -> searchTab
            2 -> FridgeTab()
            3 -> SettingTab()
            else -> null
        }
    }
    override fun getCount(): Int {
        return tabCount
    }
}