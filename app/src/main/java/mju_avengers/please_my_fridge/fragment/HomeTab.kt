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
    override fun onClick(v: View?) {
        val idx : Int = home_food_rv.getChildAdapterPosition(v)
        val childId = simpleFoodItems[idx].id
        startActivity<DetailedFoodInfoActivity>("childId" to childId)
    }
    lateinit var simpleFoodItems : ArrayList<SimpleFoodData>
    lateinit var foodComponentsDatas: ArrayList<FoodComponentsData>
    lateinit var foodPersentData: ArrayList<FoodPersentData>
    lateinit var foodInfoDataAdapter : FoodInfoRecyclerAdapter
    lateinit var hoomAnimationAdapter : AlphaInAnimationAdapter
    var mProgressDialog : ProgressDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHomeFoodAdapter()
        val recipeid : IntArray = IntArray(5023, {i -> i})
        var recieveDatas : ArrayList<FoodPointData> = loadModel(recipeid)
        recieveDatas.forEach {
            getFoodComponentsData(it.id)
        }
        val match : ArrayList<FoodPersentData> = MakeMatchRate(arrayListOf("쇠고기","다진 양파","달걀","밥","우유","넛맥","버터","파슬리","소금", "파슬리가루")).getDirectory(foodComponentsDatas)
        Log.e("일치율", match.toString())

        match.forEach {
            getFoodDataFromDB(it, recieveDatas.size)
        }


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
    }

    override fun onStart() {
        super.onStart()

    }
    fun loadModel(recipeid : IntArray) : ArrayList<FoodPointData>{
        var mRecommeders = TensorflowRecommend.create(context!!.assets, "Keras",
                "opt_recipe.pb", "label.txt", "embedding_1_input", "embedding_2_input",
                "merge_1/ExpandDims")
        var foodPoints : ArrayList<FoodPointData> = ArrayList()
        val id = 0
        //val recipeid : IntArray = IntArray(5023, {i -> i})
        for (i in recipeid.indices) {
            val rec = mRecommeders.recognize(id, recipeid[i])
            foodPoints.add(FoodPointData(rec.label, rec.conf))
        }
        foodPoints.sortByDescending { foodPointData -> foodPointData.point }
        return foodPoints.take(20) as ArrayList<FoodPointData>
    }

    private fun getFoodDataFromDB(childData : FoodPersentData, count : Int){
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener{
            override fun onStart() {
            }
            override fun onSuccess(data: DataSnapshot) {

                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = childData.persent
                var starRate = 4.5.toFloat()
                simpleFoodItems.add(SimpleFoodData(id, url, title, percent, starRate))
                foodInfoDataAdapter.notifyItemInserted(simpleFoodItems.size)
                foodInfoDataAdapter.notifyDataSetChanged()
                if (count==simpleFoodItems.size && mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }

    private fun getFoodComponentsData(childID : String){
        UseFirebaseDatabase.getInstence().readFBData(childID, object : OnGetDataListener{
            override fun onStart() {
                if (mProgressDialog == null) {
                    mProgressDialog = indeterminateProgressDialog("데이터 불러오기...")
                    mProgressDialog!!.show()
                } else {
                    if (!mProgressDialog!!.isShowing) {
                        mProgressDialog!!.show()
                    }
                }
            }
            override fun onSuccess(data: DataSnapshot) {
                val id = data!!.child("id").value.toString()
                val components: ArrayList<String> = ArrayList()
                data!!.child("components").children.forEach {
                    components.add(it.value.toString())
                }
                foodComponentsDatas.add(FoodComponentsData(id,components))
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }

    private fun setHomeFoodAdapter(){
        simpleFoodItems = ArrayList()
        foodComponentsDatas = ArrayList()
        foodPersentData = ArrayList()
        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, simpleFoodItems)
        foodInfoDataAdapter.setOnItemClickListener(this)
        hoomAnimationAdapter = AlphaInAnimationAdapter(foodInfoDataAdapter)
        home_food_rv.layoutManager = LinearLayoutManager(context)
        home_food_rv.adapter = hoomAnimationAdapter
    }
}