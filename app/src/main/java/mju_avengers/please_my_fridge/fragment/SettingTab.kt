package mju_avengers.please_my_fridge.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_setting.*
import mju_avengers.please_my_fridge.*
import mju_avengers.please_my_fridge.adapter.SearchFoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodComponentsData
import mju_avengers.please_my_fridge.data.FoodPersentData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import mju_avengers.please_my_fridge.match_persent.CalculateMatchPercent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity


class SettingTab : Fragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        val idx: Int = setting_eaten_food_list_rv.getChildAdapterPosition(v)
        var childId : String
        var matchPercent : Float
        childId = myFoodRecyclerAdapter.simpleFoodItems!![idx].id
        matchPercent = myFoodRecyclerAdapter.simpleFoodItems!![idx].percent

        startActivity<DetailedFoodActivity>("childId" to childId, "matchPercent" to String.format("%.2f", matchPercent))
    }

    lateinit var myFoodRecyclerAdapter : SearchFoodRecyclerAdapter
    private lateinit var slideInfoRecyclerAdapter : SlideInLeftAnimationAdapter
    lateinit var myEatenFoodData : ArrayList<SimpleFoodData>
    private var dataSize = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLoginView()
        myEatenFoodData = ArrayList()
        setEatenFoodRecyclerAdapter(myEatenFoodData)
        setting_show_eaten_food_list_btn_tv.setOnClickListener {
            setting_refresh_srl.isRefreshing = true
            refreshEatenFoodData()
        }
        setting_refresh_srl.setOnRefreshListener {
            refreshEatenFoodData()
            setting_refresh_srl.isRefreshing = false
        }
        setting_user_logout_tv.setOnClickListener {
            MaterialDialog.Builder(context!!)
                    .title("로그아웃 하시겠습니까?")
                    .positiveText("로그아웃")
                    .negativeText("취소")
                    .onPositive { dialog, which ->
                        AuthUI.getInstance().signOut(context!!).addOnCompleteListener {
                            startActivity<AppInitActivity>()
                            activity!!.finish()
                        }
                    }.show()
        }
    }
    fun setLoginView(){
        var user = FirebaseAuth.getInstance().currentUser
        if (user!=null){
            val name : String = user.displayName!!.toString()
            val email : String = user.email!!.toString()
            val photoUrl : String = user.photoUrl!!.toString()
            setting_user_name_tv.text = name
            setting_user_email_tv.text = email
            val requestOptions = RequestOptions()
            requestOptions.error(R.drawable.ic_clear_black_36dp)
            requestOptions.centerCrop()
            Glide.with(activity!!)
                    .setDefaultRequestOptions(requestOptions)
                    .load(photoUrl)
                    .into(setting_user_img_iv)
        }

    }
    fun refreshEatenFoodData(){
        myEatenFoodData = ArrayList()
        setting_refresh_srl.isRefreshing = true
        doAsync {
            val newFoodDataIds = DataOpenHelper.getInstance(activity!!).getEatenFoodDatas()
            val foodComponentsData : ArrayList<FoodComponentsData>? = DataOpenHelper.getInstance(activity!!).mappingDBDataToFoodComponentsData(newFoodDataIds)
            val foodPercentData : ArrayList<FoodPersentData> = CalculateMatchPercent(context!!).matchPercentCalculator(foodComponentsData!!)
            dataSize = foodPercentData.size
            foodPercentData.forEach {
                getNewEatenFoodData(it)
            }
        }
    }
    private fun setEatenFoodRecyclerAdapter(data : ArrayList<SimpleFoodData>) {
        myFoodRecyclerAdapter = SearchFoodRecyclerAdapter(context!!, data!!)//nullpoint에러뜸
        myFoodRecyclerAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(myFoodRecyclerAdapter)
        setting_eaten_food_list_rv.layoutManager = LinearLayoutManager(context)
        setting_eaten_food_list_rv.itemAnimator = SlideInLeftAnimator()
        setting_eaten_food_list_rv.adapter = myFoodRecyclerAdapter
    }


    private fun getNewEatenFoodData(childData : FoodPersentData){
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener {
            override fun onStart() {
            }
            override fun onSuccess(data: DataSnapshot) {
                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = childData.persent
                var starRate = data!!.child("id").value.toString()

                myEatenFoodData!!.add(SimpleFoodData(id, url, title, percent, starRate))

                if (dataSize == myEatenFoodData!!.size){
                    dataSize = 0
                    //myEatenFoodData!!.sortByDescending { it.percent }
                    setEatenFoodRecyclerAdapter(myEatenFoodData)
                    setting_refresh_srl.isRefreshing = false
                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }
}
