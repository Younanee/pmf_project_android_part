package mju_avengers.please_my_fridge.fragment

import android.app.ProgressDialog
import android.os.Bundle
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
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.OnGetDataListener
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.UseFirebaseDatabase
import mju_avengers.please_my_fridge.adapter.FoodInfoRecyclerAdapter
import mju_avengers.please_my_fridge.data.SimpleFoodData
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.startActivity


class HomeTab : Fragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        val idx : Int = home_food_rv.getChildAdapterPosition(v)
        val foodName : String = simpleFoodItems[idx].title
        startActivity<DetailedFoodInfoActivity>("title" to foodName)
    }
    lateinit var simpleFoodItems : ArrayList<SimpleFoodData>
    lateinit var foodInfoDataAdapter : FoodInfoRecyclerAdapter
    lateinit var hoomAnimationAdapter : AlphaInAnimationAdapter
    //lateinit var mProgressDialog : ProgressDialogHolder


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHomeFoodAdapter()
        arrayListOf("11","22","33").forEach {
            getFoodDataFromDB(it)
        }

    }

    private fun getFoodDataFromDB(childID : String){
        val mProgressDialog = indeterminateProgressDialog("Fetching Data...")
        UseFirebaseDatabase.getInstence().readFBData(childID, object : OnGetDataListener{
            override fun onStart() {
                mProgressDialog.show()
            }
            override fun onSuccess(data: DataSnapshot) {

                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = 100
                var starRate = 4.5.toFloat()
                simpleFoodItems.add(SimpleFoodData(id, url, title, percent, starRate))
                foodInfoDataAdapter.notifyItemInserted(simpleFoodItems.size)
                foodInfoDataAdapter.notifyDataSetChanged()
                if (mProgressDialog.isShowing){
                    mProgressDialog.dismiss()
                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }

    private fun setHomeFoodAdapter(){
        simpleFoodItems = ArrayList()
        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, simpleFoodItems)
        foodInfoDataAdapter.setOnItemClickListener(this)
        hoomAnimationAdapter = AlphaInAnimationAdapter(foodInfoDataAdapter)
        home_food_rv.layoutManager = LinearLayoutManager(context)
        home_food_rv.adapter = hoomAnimationAdapter
    }
}