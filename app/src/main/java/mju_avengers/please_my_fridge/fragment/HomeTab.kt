package mju_avengers.please_my_fridge.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import mju_avengers.please_my_fridge.*
import mju_avengers.please_my_fridge.adapter.FoodInfoRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodComponentsData
import mju_avengers.please_my_fridge.data.FoodPersentData
import mju_avengers.please_my_fridge.data.FoodPointData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import mju_avengers.please_my_fridge.match_persent.MakeMatchRate
import mju_avengers.please_my_fridge.recipe_model.TensorflowRecommend
import org.jetbrains.anko.support.v4.*


class HomeTab : Fragment(), View.OnClickListener{
    companion object {
        private val PARAM_NAME = "param1"

        fun newInstance(param1 : ArrayList<SimpleFoodData>) : HomeTab{
            val homeTabFragment = HomeTab()
            val args = Bundle()
            args.putSerializable(PARAM_NAME, param1)
            homeTabFragment.arguments = args

            return homeTabFragment
        }
    }

    private var mParam : ArrayList<SimpleFoodData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            mParam = arguments!!.getSerializable(PARAM_NAME) as ArrayList<SimpleFoodData>

        }
    }

    override fun onClick(v: View?) {
        val idx : Int = home_food_rv.getChildAdapterPosition(v)
        val childId = mParam!![idx].id
        startActivity<DetailedFoodActivity>("childId" to childId)
    }

    lateinit var foodInfoDataAdapter : FoodInfoRecyclerAdapter
    lateinit var hoomAnimationAdapter : AlphaInAnimationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        return v
    }


    override fun onStart() {
        super.onStart()
        setHomeFoodAdapter()

    }

    private fun setHomeFoodAdapter(){
        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, mParam!!)
        foodInfoDataAdapter.setOnItemClickListener(this)
        hoomAnimationAdapter = AlphaInAnimationAdapter(foodInfoDataAdapter)
        home_food_rv.layoutManager = LinearLayoutManager(context)
        home_food_rv.adapter = hoomAnimationAdapter
    }
}

//        val recipeid : IntArray = IntArray(5023, {i -> i})
//        var recieveDatas : ArrayList<FoodPointData> = loadModel(recipeid)
//        recieveDatas.forEach {
//            getFoodComponentsData(it.id)
//        }
//        val match : ArrayList<FoodPersentData> = MakeMatchRate(arrayListOf("쇠고기","다진 양파","달걀","밥","우유","넛맥","버터","파슬리","소금", "파슬리가루")).getDirectory(foodComponentsDatas)
//        Log.e("일치율", match.toString())
//
//        match.forEach {
//            getFoodDataFromDB(it, recieveDatas.size)
//        }


//longToast("size는 " + datas.size.toString())
//임시테스트
//var mFridgeGroceries : ArrayList<String> = arrayListOf("쇠고기","다진 양파","달걀","밥","우유","넛맥","버터","파슬리","소금", "파슬리가루")

//MakeMatchRate(model.getRecommendFoodIds(), mFridgeGroceries).directory
//        var cutting : ArrayList<String> = ArrayList()
//        cutting.add(temp[0])
//        cutting.add(temp[1])
//        cutting.add(temp[2])

//        arrayListOf("584","1428", "1828", "91", "3").forEach {
//            getFoodDataFromDB(it)
//        }
//        cutting.forEach {
//            getFoodDataFromDB(it)
//        }
//