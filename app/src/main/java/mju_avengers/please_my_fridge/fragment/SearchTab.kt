package mju_avengers.please_my_fridge.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.*
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.SearchFoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.SimpleFoodData
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            mParam = arguments!!.getSerializable(SearchTab.PARAM_NAME) as ArrayList<SimpleFoodData>

        }
    }
    override fun onClick(v: View?) {
        val idx: Int = search_food_rv.getChildAdapterPosition(v)
        val childId = mParam!![idx].id
        val matchPercent = mParam!![idx].percent
        startActivity<DetailedFoodActivity>("childId" to childId, "matchPercent" to matchPercent)
    }

    lateinit var searchFoodRecyclerAdapter: SearchFoodRecyclerAdapter
    lateinit var slideInfoRecyclerAdapter : SlideInLeftAnimationAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSearchFoodRecyclerAdapter()
        search_bottom_navi.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_grocery -> {
                    setSearchFoodRecyclerAdapter()
                    true
                }
                R.id.action_fridge -> {
                    setSearchFoodRecyclerAdapter()
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

//    private fun clearRecyclerView(){
//        var size = simpleFoodItems.size
//        simpleFoodItems.clear()
//        search_food_rv.adapter.notifyDataSetChanged()
//        search_food_rv.adapter.notifyItemRangeRemoved(0, size)
//    }

    private fun setSearchFoodRecyclerAdapter() {
        searchFoodRecyclerAdapter = SearchFoodRecyclerAdapter(context!!, mParam!!)
        searchFoodRecyclerAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(searchFoodRecyclerAdapter)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.itemAnimator = SlideInLeftAnimator()
        search_food_rv.adapter = slideInfoRecyclerAdapter
    }

}