package mju_avengers.please_my_fridge.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.*
import org.jetbrains.anko.uiThread


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
    private var dataSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            mParam = arguments!!.getSerializable(PARAM_NAME) as ArrayList<SimpleFoodData>

        }

    }

    override fun onClick(v: View?) {
        val idx : Int = home_food_rv.getChildAdapterPosition(v)
        val childId = mParam!![idx].id
        val matchPercent = mParam!![idx].percent
        startActivity<DetailedFoodActivity>("childId" to childId, "matchPercent" to matchPercent)
    }

    lateinit var foodInfoDataAdapter : FoodInfoRecyclerAdapter
    lateinit var hoomAnimationAdapter : AlphaInAnimationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        return v
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


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
//        home_food_refresh_srl.setOnRefreshListener {
//            home_food_refresh_srl.isRefreshing = false
//            mParam!!.clear()
//            home_food_rv.adapter.notifyDataSetChanged()
//
//            home_food_refresh_srl.isRefreshing = true
//            val newFoodDataIds = (activity as MainActivity).getNewMatchPercentData()
//            dataSize = newFoodDataIds.size
//            newFoodDataIds.forEach {
//                getNewSimpleFoodData(it)
//            }
//        }
        home_food_refresh_srl.setOnRefreshListener {
            doAsync {
                foodInfoDataAdapter.clear()
                val newFoodDataIds = (activity as MainActivity).getNewMatchPercentData()
//                newFoodDataIds.sortByDescending { foodPersentData: FoodPersentData -> foodPersentData.persent }
                dataSize = newFoodDataIds.size
                newFoodDataIds.forEach {
                    getNewSimpleFoodData(it)
                }
            }
        }
    }
    private fun getNewSimpleFoodData(childData : FoodPersentData){
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener{
            override fun onStart() {
            }
            override fun onSuccess(data: DataSnapshot) {
                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = childData.persent
                var starRate = data!!.child("id").value.toString()

                mParam!!.add(SimpleFoodData(id, url, title, percent, starRate))

                if (dataSize == mParam!!.size) {
                    dataSize = 0
                    foodInfoDataAdapter.addAll(mParam!!)
                    home_food_refresh_srl.isRefreshing = false
                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }

}
