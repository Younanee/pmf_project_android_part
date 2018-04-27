package mju_avengers.please_my_fridge.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.HomeFoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.HomeFoodData
import org.jetbrains.anko.support.v4.startActivity

class HomeTab : Fragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        val idx : Int = home_food_rv.getChildAdapterPosition(v)
        val foodName : String = homeFoodItems[idx].name
        startActivity<DetailedFoodInfoActivity>("foodName" to foodName)
    }


    lateinit var homeFoodItems : ArrayList<HomeFoodData>
    lateinit var homeFoodDataAdapter : HomeFoodRecyclerAdapter
    lateinit var hoomAnimationAdapter : AlphaInAnimationAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeFoodItems = setTestFoodData()
        setHomeFoodAdapter()


    }

    fun setTestFoodData() : ArrayList<HomeFoodData>{
        val mainFoodItems = ArrayList<HomeFoodData>()
        mainFoodItems.add(HomeFoodData(R.drawable.test01, "삼겹살 덮밥",95, 4.5.toFloat()))
        mainFoodItems.add(HomeFoodData(R.drawable.test02, "차돌박이 찌개",82, 4.toFloat()))
        mainFoodItems.add(HomeFoodData(R.drawable.test03, "양배추피 만두",79, 3.7.toFloat()))
        return mainFoodItems
    }

    fun setHomeFoodAdapter(){
        homeFoodDataAdapter = HomeFoodRecyclerAdapter(context!!, homeFoodItems)
        homeFoodDataAdapter.setOnItemClickListener(this)
        hoomAnimationAdapter = AlphaInAnimationAdapter(homeFoodDataAdapter)


        home_food_rv.layoutManager = LinearLayoutManager(context)
        home_food_rv.adapter = hoomAnimationAdapter

    }


}