package mju_avengers.please_my_fridge.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_setting.*
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.FoodInfoRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodData


class SettingTab : Fragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var foodDatas : ArrayList<FoodData>
    lateinit var foodInfoDataAdapter: FoodInfoRecyclerAdapter
    lateinit var mFoodDatabase: DatabaseReference
    lateinit var slideInfoRecyclerAdapter : SlideInLeftAnimationAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setFoodDataAdapter()
        mFoodDatabase = FirebaseDatabase.getInstance().reference


        setting_test_btn.setOnClickListener {
            mFoodDatabase.child("241").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    setFoodItem(p0!!)
                    setting_test_rv.adapter.notifyDataSetChanged()
                }
            })
        }

    }
    private fun setFoodDataAdapter() {

        foodDatas = ArrayList()
        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, foodDatas)
        foodInfoDataAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(foodInfoDataAdapter)
        setting_test_rv.layoutManager = LinearLayoutManager(context)
//        setting_test_rv.itemAnimator = OvershootInLeftAnimator()
//        setting_test_rv.itemAnimator.addDuration = 500
        setting_test_rv.adapter = slideInfoRecyclerAdapter
        //search_food_rv.adapter.
    }

    private fun setFoodItem(data : DataSnapshot){
        var id = data!!.child("id").value.toString()
        var url = 0
        var title = data!!.child("title").value.toString()
        var percent = 100
        var starRate = 4.5.toFloat()
        var ingredients: ArrayList<String> = ArrayList()
        data!!.child("ingredients").children.forEach {
            ingredients.add(it.value.toString())
        }
        var directions: ArrayList<String> = ArrayList()
        data!!.child("directions").children.forEach {
            directions.add(it.value.toString())
        }
        foodDatas.add(FoodData(id, url, title, percent, starRate, ingredients, directions))
    }
}
