package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.adapter.TabPagerAdapter
import mju_avengers.please_my_fridge.data.FoodData
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {
    private val FINISH_INTERVAL_TIME : Long = 2000
    private var backPressedTime : Long = 0
//    lateinit var mFoodDatabase : DatabaseReference
//    lateinit var mainFoodItems : ArrayList<FoodData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        mFoodDatabase = FirebaseDatabase.getInstance().reference
        configureTabLayout()
//        mainFoodItems = ArrayList()
//        arrayListOf("1241", "122", "333").forEach {
//            getFoodItem(it)
//        }
        //val user = FirebaseAuth.getInstance().currentUser
//        longSnackbar(main_ll,user.toString(), "확인", { view ->
//        }).setDuration(5000).show()

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
    override fun onBackPressed() {
        var tempTime : Long = System.currentTimeMillis()
        var intervalTime : Long = tempTime - backPressedTime
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            longToast("한번 더 뒤로가기를 누르면 종료됩니다.")
        }
    }
}
