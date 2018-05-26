package mju_avengers.please_my_fridge.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.*
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.OnGetDataListener
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.UseFirebaseDatabase
import mju_avengers.please_my_fridge.adapter.SearchFoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.SimpleFoodData
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class SearchTab : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        val idx: Int = search_food_rv.getChildAdapterPosition(v)
        val childId = simpleFoodItems[idx].id
        startActivity<DetailedFoodInfoActivity>("childId" to childId)
    }

    lateinit var simpleFoodItems: ArrayList<SimpleFoodData>
    lateinit var searchFoodRecyclerAdapter: SearchFoodRecyclerAdapter
    lateinit var slideInfoRecyclerAdapter : SlideInLeftAnimationAdapter
    var mProgressDialog : ProgressDialog? = null
    private var dataCount = 0

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
                    if (simpleFoodItems.isNotEmpty()) {
                        clearRecyclerView()
                    }
                    //이곳은 검색어 기반으로 추천 리스트를 받는곳
                    //내가 이해한게 맞다면 이곳에 모델에 대한 output값을 받는다. 배열이나 arraylist등으로, (아래 숫자들은 임의로 id값을 넣은것임)
                    //바로 아래 64 코드 줄 중에서 arrayListOf("1234", "33", "4124", "3331", "121", "7", "110", "400")이 부분을 output값에 대응해서 바꾼다.
                    arrayListOf("1234", "33", "4124", "3331", "121", "7", "110", "400").forEach {
                        readAndFetchFoodItems(it, 8)
                    }
                    true
                }
                R.id.action_fridge -> {
                    if (simpleFoodItems.isNotEmpty()) {
                        clearRecyclerView()
                    }
                    //이곳은 냉장고 기반으로 추천 리스트를 받는곳
                    //내가 이해한게 맞다면 이곳에 모델에 대한 output값을 받는다. 배열이나 arraylist등으로,(아래 숫자들은 임의로 id값을 넣은것임)
                    //바로 아래 64 코드 줄 중에서 arrayListOf("1234", "33", "4124", "3331", "121", "7", "110", "400")이 부분을 output값에 대응해서 바꾼다.
                    arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10").forEach {
                        readAndFetchFoodItems(it, 10)
                    }
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

    override fun onStart() {
        super.onStart()
        //search_bottom_navi.selectedItemId = R.id.action_fridge
    }

    override fun onStop() {
        super.onStop()
        clearRecyclerView()
    }

    private fun clearRecyclerView(){
        var size = simpleFoodItems.size
        simpleFoodItems.clear()
        search_food_rv.adapter.notifyDataSetChanged()
        search_food_rv.adapter.notifyItemRangeRemoved(0, size)
    }

    private fun readAndFetchFoodItems(childID : String, count : Int) {

        UseFirebaseDatabase.getInstence().readFBData(childID, object : OnGetDataListener {
            override fun onStart() {
                if (mProgressDialog == null) {
                    mProgressDialog = indeterminateProgressDialog("데이터 불러오기...")
                    mProgressDialog!!.show()
                } else {
                    if (!mProgressDialog!!.isShowing){
                        mProgressDialog!!.show()
                    }
                }
            }
            override fun onSuccess(data: DataSnapshot) {
                dataCount++

                var id = data!!.child("id").value.toString()
                var url = data.child("url").child("0").value.toString()
                var title = data!!.child("title").value.toString()
                var percent = "100"
                var starRate = 4.5.toFloat()
                simpleFoodItems.add(SimpleFoodData(id, url, title, percent, starRate))
                searchFoodRecyclerAdapter.notifyItemInserted(simpleFoodItems.size)
                searchFoodRecyclerAdapter.notifyDataSetChanged()

                if (count==simpleFoodItems.size && mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                }

            }

            override fun onFailed(databaseError: DatabaseError) {
                Log.e("FireBase DB Error", databaseError.toString())
            }
        })
    }
    private fun setSearchFoodRecyclerAdapter() {
        simpleFoodItems = ArrayList()
        searchFoodRecyclerAdapter = SearchFoodRecyclerAdapter(context!!, simpleFoodItems)
        searchFoodRecyclerAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(searchFoodRecyclerAdapter)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.itemAnimator = SlideInLeftAnimator()
        search_food_rv.adapter = slideInfoRecyclerAdapter
    }
}