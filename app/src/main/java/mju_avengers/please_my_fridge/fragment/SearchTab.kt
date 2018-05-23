package mju_avengers.please_my_fridge.fragment

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.*
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.SearchFoodRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import org.jetbrains.anko.support.v4.toast


class SearchTab : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        val idx: Int = search_food_rv.getChildAdapterPosition(v)
        val simpleFoodItems: SimpleFoodData = simpleFoodItems[idx]
        val intent = Intent(context, DetailedFoodInfoActivity::class.java)
        intent.putExtra("title", simpleFoodItems.title)
        intent.putExtra("url", simpleFoodItems.url)
        startActivity(intent)
    }

    //Firebase database
    lateinit var mFoodDatabase: DatabaseReference

    //recyclerView
    lateinit var simpleFoodItems: ArrayList<SimpleFoodData>
    lateinit var searchFoodRecyclerAdapter: SearchFoodRecyclerAdapter
    lateinit var slideInfoRecyclerAdapter : SlideInLeftAnimationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        setFoodDataAdapter()
//        mFoodDatabase = FirebaseDatabase.getInstance().reference
        //최상위
        search_bottom_navi.setOnNavigationItemSelectedListener {
            setFoodDataAdapter()
            mFoodDatabase = FirebaseDatabase.getInstance().reference
            when (it.itemId) {
                R.id.action_grocery -> {
                    if (simpleFoodItems.isNotEmpty()) {
                        clearRecyclerView()
                    }
                    ReadDatabaseTask().execute(arrayListOf("511","1244","124"))
                    true
                }
                R.id.action_fridge -> {
                    if (simpleFoodItems.isNotEmpty()) {
                        clearRecyclerView()
                    }
                    ReadDatabaseTask().execute(arrayListOf("1"))
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
    private fun clearRecyclerView(){
        var size = simpleFoodItems.size
        simpleFoodItems.clear()
        search_food_rv.adapter.notifyItemRangeRemoved(0, size)
    }


    private fun setFoodDataAdapter() {
        simpleFoodItems = ArrayList()
        searchFoodRecyclerAdapter = SearchFoodRecyclerAdapter(context!!, simpleFoodItems)
        searchFoodRecyclerAdapter.setOnItemClickListener(this)
        slideInfoRecyclerAdapter = SlideInLeftAnimationAdapter(searchFoodRecyclerAdapter)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.itemAnimator = SlideInLeftAnimator()
        search_food_rv.adapter = slideInfoRecyclerAdapter
    }

    private fun getFoodItem(id: String){
        mFoodDatabase.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("해당 데이터가 없습니다.")
            }
            override fun onDataChange(data: DataSnapshot?) {
                setFoodItem(data!!)
                search_food_rv.adapter.notifyDataSetChanged()
            }
        })
    }
    private fun setFoodItem(data : DataSnapshot){
        var id = data.child("id").value.toString()
        var url = data.child("url").child("0").value.toString()
        var title = data.child("title").value.toString()
        var percent = 100
        var starRate = 4.5.toFloat()
        simpleFoodItems.add(SimpleFoodData(id, url, title, percent, starRate))
    }
    private inner class ReadDatabaseTask : AsyncTask<ArrayList<String>, String, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            //foodItems.clear()
            search_progressbar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: ArrayList<String>): Boolean {
            val temp : ArrayList<String> = params[0]
            for (id_str in temp) {
                getFoodItem(id_str)
            }
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if(result!!){
                search_food_rv.adapter.notifyDataSetChanged()
                search_progressbar.visibility = View.GONE
            } else {
                toast("아직")
            }
        }
    }
}