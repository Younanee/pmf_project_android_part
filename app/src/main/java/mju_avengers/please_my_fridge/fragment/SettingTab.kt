package mju_avengers.please_my_fridge.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_setting.*
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.SimpleFoodData
import org.jetbrains.anko.support.v4.toast


class SettingTab : Fragment(), View.OnClickListener{
    lateinit var mFoodDatabase: DatabaseReference
    lateinit var simpleFoodItems: ArrayList<SimpleFoodData>

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setting_test_btn.setOnClickListener {
            mFoodDatabase = FirebaseDatabase.getInstance().reference
            simpleFoodItems = ArrayList()
            mFoodDatabase.child("213").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    setFoodItem(p0!!)
                    toast(simpleFoodItems.toString())
                }
            })

        }

    }
//    private fun setFoodDataAdapter() {
//
//        foodDatas = ArrayList()
//        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, foodDatas)
//        foodInfoDataAdapter.setOnItemClickListener(this)
//        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(foodInfoDataAdapter)
//        setting_test_rv.layoutManager = LinearLayoutManager(context)
////        setting_test_rv.itemAnimator = OvershootInLeftAnimator()
////        setting_test_rv.itemAnimator.addDuration = 500
//        setting_test_rv.adapter = slideInfoRecyclerAdapter
//        //search_food_rv.adapter.
//    }
//
    private fun setFoodItem(data : DataSnapshot){
        var id = data!!.child("id").value.toString()
        var url = data!!.child("url").child("0").value.toString()
        var title = data!!.child("title").value.toString()
        var percent = "100"
        var starRate = 4.5.toFloat()
//        var ingredients: ArrayList<String> = ArrayList()
//        data!!.child("ingredients").children.forEach {
//            ingredients.add(it.value.toString())
//        }
//        var directions: ArrayList<String> = ArrayList()
//        data!!.child("directions").children.forEach {
//            directions.add(it.value.toString())
//        }
        simpleFoodItems.add(SimpleFoodData(id, url, title, percent, starRate))
    }
}
