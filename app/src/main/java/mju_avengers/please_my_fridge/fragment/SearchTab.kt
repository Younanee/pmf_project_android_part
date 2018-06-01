package mju_avengers.please_my_fridge.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.*
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.SearchFoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodPersentData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class SearchTab : Fragment(), View.OnClickListener {
    companion object {
        private val PARAM_NAME = "param1"
        fun newInstance(param1 : ArrayList<SimpleFoodData>) : SearchTab{
            val searchTabFragment = SearchTab()
            val args = Bundle()
            args.putSerializable(PARAM_NAME, param1)
            searchTabFragment.arguments = args

            return searchTabFragment
        }
    }
    private var mParam : ArrayList<SimpleFoodData>? = null
    private var dataSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            mParam = arguments!!.getSerializable(SearchTab.PARAM_NAME) as ArrayList<SimpleFoodData>
        }
    }
    override fun onClick(v: View?) {
        val idx: Int = search_food_rv.getChildAdapterPosition(v)
        var childId : String
        var matchPercent : Float
//        if (isNotSearched){
//            childId = mParam!![idx].id
//            matchPercent = mParam!![idx].percent
//        } else {
//            childId = searchResultDatas!![idx].id
//            matchPercent = searchResultDatas!![idx].percent
//        }
        childId = searchFoodRecyclerAdapter.simpleFoodItems!![idx].id
        matchPercent = searchFoodRecyclerAdapter.simpleFoodItems!![idx].percent


        startActivity<DetailedFoodActivity>("childId" to childId, "matchPercent" to String.format("%.2f", matchPercent))
    }

    private lateinit var searchFoodRecyclerAdapter: SearchFoodRecyclerAdapter
    private lateinit var slideInfoRecyclerAdapter : SlideInLeftAnimationAdapter
    lateinit var searchResultDatas : ArrayList<SimpleFoodData>
    private var isNotSearched : Boolean = true
    //private var mProgressDialog : ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSearchFoodRecyclerAdapter()
        searchResultDatas = ArrayList()
        search_bottom_navi.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_fridge -> {
                    isNotSearched = true
                    search_food_refresh_srl.isRefreshing = true
                    mParam!!.clear()
                    searchFoodRecyclerAdapter.clear()
                    doAsync {
                        val newFoodDataIds = (activity as MainActivity).getNewMatchPercentData()
                        dataSize = newFoodDataIds.size
                        newFoodDataIds.forEach {
                            getNewSimpleFoodData(it)
                        }
                    }
                    true
                }
                R.id.action_grocery -> {
                    MaterialDialog.Builder(activity!!)
                            .positiveText("검색")
                            .negativeText("취소")
                            .customView(R.layout.dialog_search_food, true)
                            .onPositive { dialog, which ->
                                var keyword : TextInputEditText = dialog.findViewById(R.id.dialog_search_food_name_tv) as TextInputEditText
                                if (keyword.text.isNotEmpty()){
                                    searchFoodData(keyword.text.toString())
                                }
                            }
                            .show()

                    true
                }
                R.id.action_info -> {
                    MaterialDialog.Builder(activity!!)
                            .title("기반 검색 설명")
                            .items(arrayListOf("검색한 식재료 기반으로 추천합니다.", "기존 식재료 기반으로 추천합니다."))
                            .negativeText("닫기")
                            .show()
                    true
                }
                else -> {
                    toast("else?!?!?!")
                    false
                }
            }
        }
    }


    private fun searchFoodData(keyword : String){
        val newFoodDataIds : ArrayList<FoodPersentData>? = (activity!! as MainActivity).getSearchedComponentData(keyword)
        if (newFoodDataIds == null) {
            longToast("추천 레시피 중 \"$keyword\"를 재료로 쓰는 요리가 없습니다.")
        } else {
            search_food_refresh_srl.isRefreshing = true
            dataSize = newFoodDataIds.size
            newFoodDataIds.forEach {
                getSearchedSimpleFoodData(it)
            }
        }
    }

    private fun setSearchFoodRecyclerAdapter() {
        searchFoodRecyclerAdapter = SearchFoodRecyclerAdapter(context!!, mParam!!)
        searchFoodRecyclerAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(searchFoodRecyclerAdapter)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.itemAnimator = SlideInLeftAnimator()
        search_food_rv.adapter = slideInfoRecyclerAdapter
        search_food_refresh_srl.setOnRefreshListener {
            if (isNotSearched){
                mParam!!.clear()
                searchFoodRecyclerAdapter.clear()
                doAsync {
                    val newFoodDataIds = (activity as MainActivity).getNewMatchPercentData()
                    dataSize = newFoodDataIds.size
                    newFoodDataIds.forEach {
                        getNewSimpleFoodData(it)
                    }
                }
            } else {
                search_food_refresh_srl.isRefreshing = false
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
                    mParam!!.sortByDescending { it.percent }
                    isNotSearched = true
                    searchFoodRecyclerAdapter.addAll(mParam!!)
                    search_food_refresh_srl.isRefreshing = false
                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }

    private fun getSearchedSimpleFoodData(childData : FoodPersentData){
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener{
            override fun onStart() {
            }
            override fun onSuccess(data: DataSnapshot) {
                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = childData.persent
                var starRate = data!!.child("id").value.toString()

                searchResultDatas!!.add(SimpleFoodData(id, url, title, percent, starRate))

                if (dataSize == searchResultDatas!!.size) {
                    dataSize = 0
                    searchResultDatas!!.sortByDescending { it.percent }
                    isNotSearched = false
                    searchFoodRecyclerAdapter.clear()
                    searchFoodRecyclerAdapter.addAll(searchResultDatas!!)
                    search_food_refresh_srl.isRefreshing = false

                }
            }
            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }


}