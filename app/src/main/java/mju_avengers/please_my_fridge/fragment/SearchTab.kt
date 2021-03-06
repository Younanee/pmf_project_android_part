package mju_avengers.please_my_fridge.fragment

import android.app.Activity
import android.content.Intent
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
import mju_avengers.please_my_fridge.adapter.SearchFoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodComponentsData
import mju_avengers.please_my_fridge.data.FoodPersentData
import mju_avengers.please_my_fridge.data.FoodPointData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import mju_avengers.please_my_fridge.match_persent.CalculateMatchPercent
import mju_avengers.please_my_fridge.recipe_model.TensorflowRecommend
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread


class SearchTab : Fragment(), View.OnClickListener {
    companion object {
        private val PARAM_NAME = "param1"
        fun newInstance(param1: ArrayList<SimpleFoodData>): SearchTab {
            val searchTabFragment = SearchTab()
            val args = Bundle()
            args.putSerializable(PARAM_NAME, param1)
            searchTabFragment.arguments = args

            return searchTabFragment
        }
    }

    private val REQUEST_CODE_SEARCH_TAB = 3332
    private var isLoading: Boolean = false
    private var mParam: ArrayList<SimpleFoodData>? = null
    private var dataSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam = arguments!!.getSerializable(SearchTab.PARAM_NAME) as ArrayList<SimpleFoodData>
        }
    }

    override fun onClick(v: View?) {
        val idx: Int = search_food_rv.getChildAdapterPosition(v)
        var childId: String
        var matchPercent: Float
        childId = searchFoodRecyclerAdapter.simpleFoodItems!![idx].id
        matchPercent = searchFoodRecyclerAdapter.simpleFoodItems!![idx].percent
        val intent = Intent(context, DetailedFoodActivity::class.java)
        intent.putExtra("childId", childId)
        intent.putExtra("matchPercent", String.format("%.2f", matchPercent))
        startActivityForResult(intent, REQUEST_CODE_SEARCH_TAB)
        //startActivity<DetailedFoodActivity>("childId" to childId, "matchPercent" to String.format("%.2f", matchPercent))
    }

    private lateinit var searchFoodRecyclerAdapter: SearchFoodRecyclerAdapter
    private lateinit var slideInfoRecyclerAdapter: SlideInLeftAnimationAdapter
    lateinit var newSampleFoodDatas: ArrayList<SimpleFoodData>
    private var isNotSearched: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        mParam!!.sortByDescending { it.percent }
        searchFoodRecyclerAdapter = SearchFoodRecyclerAdapter(context!!, mParam!!)
        searchFoodRecyclerAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(searchFoodRecyclerAdapter)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.itemAnimator = SlideInLeftAnimator()
        search_food_rv.adapter = slideInfoRecyclerAdapter
        search_food_refresh_srl.setOnRefreshListener {
            if (isNotSearched) {
                refreshSearchFoodData()
            } else {
                search_food_refresh_srl.isRefreshing = false
            }
        }

        search_bottom_navi.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_fridge -> {
                    isNotSearched = true
                    refreshSearchFoodData()
                    true
                }
                R.id.action_grocery -> {
                    if (!isLoading) {
                        MaterialDialog.Builder(activity!!)
                                .title("식재료 검색")
                                .positiveText("검색")
                                .negativeText("취소")
                                .customView(R.layout.dialog_search_food, true)
                                .onPositive { dialog, which ->
                                    var keyword: TextInputEditText = dialog.findViewById(R.id.dialog_search_food_name_tv) as TextInputEditText
                                    if (keyword.text.isNotEmpty()) {
                                        isLoading = true
                                        search_food_refresh_srl.isRefreshing = true
                                        searchFoodData(keyword.text.toString())
                                    }
                                }
                                .show()
                    }
                    true
                }
                R.id.action_info -> {
                    MaterialDialog.Builder(activity!!)
                            .title("기반 검색 설명")
                            .items(arrayListOf("#보유 식재료 기반\n\n즐겨찾기된 음식 제외, 내가 가진 식재료 기반으로 추천합니다.",
                                    "#식재료 검색 기반\n\n즐겨찾기된 음식 포함, 검색한 식재료 + 내가 가진 식재료 기반으로 추천합니다."))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SEARCH_TAB) {
            if (resultCode == Activity.RESULT_OK) {
                if (isNotSearched) {
                    refreshSearchFoodData()
                }
            }
        }
    }

    private fun refreshSearchFoodData() {
        if (!isLoading) {
            isLoading = true
            search_food_refresh_srl.isRefreshing = true
            newSampleFoodDatas = ArrayList()
            setSearchFoodRecyclerAdapter(newSampleFoodDatas)
            doAsync {
                val newFoodDataIds = (activity as MainActivity).getNewMatchPercentData()
                dataSize = newFoodDataIds.size
                newFoodDataIds.forEach {
                    getNewSimpleFoodData(it)
                }
            }
        }
    }

    private fun searchFoodData(keyword: String) {
        newSampleFoodDatas = ArrayList()
        setSearchFoodRecyclerAdapter(newSampleFoodDatas)
        doAsync {
            val newFoodDataIds: ArrayList<FoodPersentData>? = getSearchedComponentData(keyword)
            if (newFoodDataIds == null) {
                uiThread {
                    isLoading = false
                    search_food_refresh_srl.isRefreshing = false
                    toast("추천 레시피 중 \"$keyword\"를 재료로 쓰는 요리가 없습니다.")
                }
            } else {
                dataSize = newFoodDataIds.size
                newFoodDataIds.forEach {
                    getSearchedSimpleFoodData(it)
                }
            }
        }
    }

    private fun setSearchFoodRecyclerAdapter(data: ArrayList<SimpleFoodData>?) {
        searchFoodRecyclerAdapter = SearchFoodRecyclerAdapter(context!!, data!!)
        searchFoodRecyclerAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(searchFoodRecyclerAdapter)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.itemAnimator = SlideInLeftAnimator()
        search_food_rv.adapter = slideInfoRecyclerAdapter
    }

    private fun getNewSimpleFoodData(childData: FoodPersentData) {
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener {
            override fun onStart() {
            }

            override fun onSuccess(data: DataSnapshot) {
                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = childData.persent
                var starRate = data!!.child("id").value.toString()

                newSampleFoodDatas!!.add(SimpleFoodData(id, url, title, percent, starRate))

                if (dataSize == newSampleFoodDatas!!.size) {
                    dataSize = 0
                    //newSampleFoodDatas!!.sortByDescending { it.percent }

                    setSearchFoodRecyclerAdapter(newSampleFoodDatas)
                    isNotSearched = true
                    isLoading = false
                    search_food_refresh_srl.isRefreshing = false
                }
            }

            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }

    private fun getSearchedSimpleFoodData(childData: FoodPersentData) {
        UseFirebaseDatabase.getInstence().readFBData(childData.id, object : OnGetDataListener {
            override fun onStart() {
            }

            override fun onSuccess(data: DataSnapshot) {
                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = childData.persent
                var starRate = data!!.child("id").value.toString()

                newSampleFoodDatas!!.add(SimpleFoodData(id, url, title, percent, starRate))

                if (dataSize == newSampleFoodDatas!!.size) {
                    dataSize = 0
                    newSampleFoodDatas!!.sortByDescending { it.percent }
                    setSearchFoodRecyclerAdapter(newSampleFoodDatas)
                    isLoading = false
                    isNotSearched = false
                    search_food_refresh_srl.isRefreshing = false
                    longToast("검색 완료")
                }
            }

            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }

    private fun loadModelByKeyword(keyword: String): ArrayList<FoodPointData> {
        var mRecommeders = TensorflowRecommend.create(context!!.assets, "Keras",
                "opt_recipe.pb", "label.txt", "embedding_1_input", "embedding_2_input",
                "merge_1/ExpandDims")
        val ids: ArrayList<Int> = DataOpenHelper.getInstance(activity!!).searchInitDataByKeyword(keyword)
        var foodPoints: ArrayList<FoodPointData> = ArrayList()
        //먹었던것 빼나?
        return if (ids.size != 0) {
            val id = 0
            ids.forEach {
                val rec = mRecommeders.recognize(id, it)
                foodPoints.add(FoodPointData(rec.label, rec.conf))
            }
            foodPoints.sortByDescending { foodPointData -> foodPointData.point }

            if (foodPoints.size > 30) {
                foodPoints.take(30) as ArrayList<FoodPointData>
            } else {
                foodPoints
            }

        } else {
            foodPoints
        }
    }

    fun getSearchedComponentData(keyword: String): ArrayList<FoodPersentData>? {
        var pointData: ArrayList<FoodPointData> = loadModelByKeyword(keyword)
        return if (pointData.size == 0) {
            null
        } else {
            var childIds: ArrayList<String> = ArrayList()
            pointData.forEach {
                childIds.add(it.id)
            }
            val foodComponentsDataList = DataOpenHelper.getInstance(activity!!).mappingDBDataToFoodComponentsData(childIds)
            var temp: ArrayList<FoodComponentsData> = ArrayList()
            foodComponentsDataList.forEach {
                if (it.components.contains(keyword)) {
                    temp.add(it)
                }
            }
            //return
            CalculateMatchPercent(activity!!).matchPercentCalculator(temp)
        }
    }


}