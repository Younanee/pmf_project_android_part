package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import mju_avengers.please_my_fridge.adapter.FoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.testFoodData
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

class HomeTab : Fragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        val idx : Int = home_tab_recycler_view.getChildAdapterPosition(v)
        alert(""+ idx + "번 인덱스 눌림!", "음식 상세보기"){
            yesButton { toast("Good!!!") }
            noButton {  }
        }.show()
        //그냥 엑티비티로 만드는게 나을듯?!?!
        //toast( ""+ idx + "번 인덱스 눌림!")
    }

    lateinit var foodItems : ArrayList<testFoodData>
    lateinit var foodRecyclerAdapter: FoodRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        foodItems = ArrayList()
        foodItems.add(testFoodData(0, "음식이름1", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름2", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름3", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름4", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름5", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름6", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름7", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름8", "돼지고기 넣고 등..."))
        foodItems.add(testFoodData(0, "음식이름9", "돼지고기 넣고 등..."))

        foodRecyclerAdapter = FoodRecyclerAdapter(context!!, foodItems)
        foodRecyclerAdapter.setOnItemClickListener(this)

        home_tab_recycler_view.layoutManager = LinearLayoutManager(context)
        home_tab_recycler_view.adapter = foodRecyclerAdapter


    }


}