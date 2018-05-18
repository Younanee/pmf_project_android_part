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
import kotlinx.android.synthetic.main.fragment_search.*
import mju_avengers.please_my_fridge.DetailedFoodInfoActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.FoodInfoRecyclerAdapter
import mju_avengers.please_my_fridge.data.FoodData
import org.jetbrains.anko.support.v4.toast


class SearchTab : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        val idx: Int = search_food_rv.getChildAdapterPosition(v)
        val foodItem: FoodData = foodItems[idx]
        val intent = Intent(context, DetailedFoodInfoActivity::class.java)
        intent.putExtra("title", foodItem.title)
        intent.putExtra("url", foodItem.thumbnail)
        intent.putExtra("ingredients", foodItem.ingredients)
        intent.putExtra("directions", foodItem.directions)
        startActivity(intent)

    }

    //Firebase database
    lateinit var mFoodDatabase: DatabaseReference


    var foodItems: ArrayList<FoodData> = ArrayList()
    lateinit var foodInfoDataAdapter: FoodInfoRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //최상위
        mFoodDatabase = FirebaseDatabase.getInstance().reference

        search_bottom_navi.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_grocery -> {
                    toast("식재료 기반")
                    true
                }
                R.id.action_fridge -> {
                    toast("냉장고 기반")
                    ReadDatabaseTask().execute(arrayListOf("2","22","222"))
                    true
                }
                R.id.action_info -> {
                    MaterialDialog.Builder(activity!!)
                            .title("기반 검색 설명")
                            .checkBoxPrompt("a", true, null)
                            .items(arrayListOf("a", "b", "c"))
                            .negativeText("닫기")
                            .show()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }



    private fun setFoodDataAdapter() {
        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, foodItems)
        foodInfoDataAdapter.setOnItemClickListener(this)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.adapter = foodInfoDataAdapter
    }

    private fun getFoodItem(id: String){
        mFoodDatabase.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("해당 데이터가 없습니다.")
            }
            override fun onDataChange(data: DataSnapshot?) {
                if (data != null) {
                    setFoodItems(data)
                }
            }
        })
    }
    private fun setFoodItems(data : DataSnapshot){
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
        foodItems.add(FoodData(id, url, title, percent, starRate, ingredients, directions))
    }
    private inner class ReadDatabaseTask : AsyncTask<ArrayList<String>, String, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
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
            setFoodDataAdapter()
            search_progressbar.visibility = View.GONE
        }
    }
}