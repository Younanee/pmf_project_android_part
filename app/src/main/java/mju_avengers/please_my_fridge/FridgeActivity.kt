package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fridge.*
import mju_avengers.please_my_fridge.adapter.TabPagerAdapter

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        configureTabLayout()

    }

    private fun configureTabLayout(){
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_home_black_48dp),0)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_search_black_48dp),1)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_add_shopping_cart_black_48dp),2)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_kitchen_black_48dp),3)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_face_black_48dp),4)

        val tabAdapter = TabPagerAdapter(main_tabLayout.tabCount, supportFragmentManager)
        main_pager.adapter = tabAdapter

        main_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tabLayout))
        main_tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                main_pager.currentItem = tab!!.position
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_pager.currentItem = tab!!.position
            }
        })

    }
}
