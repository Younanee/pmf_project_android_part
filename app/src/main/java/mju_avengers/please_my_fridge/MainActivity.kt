package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import mju_avengers.please_my_fridge.adapter.TabPagerAdapter
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureTabLayout()
        val user = FirebaseAuth.getInstance().currentUser
        longSnackbar(main_ll,user.toString(), "확인", { view ->
            toast("dmd?")
        }).setDuration(5000).show()

    }

    private fun configureTabLayout() {
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_home_unclicked_black_24dp), 0)
        main_tabLayout.getTabAt(0)!!.icon!!.alpha = 255
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_search_black_24dp), 1)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_kitchen_black_24dp), 2)
        main_tabLayout.addTab(main_tabLayout.newTab().setIcon(R.drawable.ic_person_black_24dp), 3)

        val tabAdapter = TabPagerAdapter(main_tabLayout.tabCount, supportFragmentManager)
        main_pager.adapter = tabAdapter

        main_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tabLayout))
        main_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 63
                main_pager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 255
                main_pager.currentItem = tab!!.position
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_tabLayout.getTabAt(main_pager.currentItem)!!.icon!!.alpha = 63
                main_pager.currentItem = tab!!.position
            }
        })

    }


}
