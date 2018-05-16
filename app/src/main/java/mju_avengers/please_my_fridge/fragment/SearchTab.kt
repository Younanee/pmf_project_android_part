package mju_avengers.please_my_fridge.fragment

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

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


    lateinit var foodItems: ArrayList<FoodData>
    lateinit var foodInfoDataAdapter: FoodInfoRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        foodItems = ArrayList()
        mFoodDatabase = FirebaseDatabase.getInstance().reference
        getFoodData("3")
        getFoodData("692")
        getFoodData("3299")
        getFoodData("299")
        getFoodData("99")
        getFoodData("35")
        setFoodDataAdapter()

        search_bottom_navi.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_grocery -> {
                    //addFoodItems(arrayListOf("22"))
//                    getFoodData("0")
//                    getFoodData("692")
//                    getFoodData("3299")
//                    setFoodDataAdapter()
                    doAsync {

                        uiThread {
                            search_progressbar.visibility = View.VISIBLE

                        }
                        Thread.sleep(5000)

                        onComplete {
                            search_progressbar.visibility = View.GONE
                        }
                    }
                    true
                }
                R.id.action_fridge -> {
                    toast("냉장고 기반")
                    //clearRecyclerView()

                    getFoodData("35")
                    setFoodDataAdapter()

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

    private fun clearRecyclerView(){
        var size = foodItems.size
        foodItems.clear()
        foodInfoDataAdapter.notifyItemRangeRemoved(0, size)
    }

    private fun setFoodDataAdapter() {
        foodInfoDataAdapter = FoodInfoRecyclerAdapter(context!!, foodItems)
        foodInfoDataAdapter.setOnItemClickListener(this)
        search_food_rv.layoutManager = LinearLayoutManager(context)
        search_food_rv.adapter = foodInfoDataAdapter
    }

    private fun addFoodItems(ids: ArrayList<String>) {
        foodItems.clear()
    }

    private fun getFoodData(id: String) {
        mFoodDatabase.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("해당 데이터가 없습니다.")
            }

            override fun onDataChange(data: DataSnapshot?) {
                val components = data!!.child("components").children
                components.forEach {
                    Log.d(it.key, it.value.toString())
                }
                val id = data!!.child("id").value.toString()
                val url = 0
                val title = data!!.child("title").value.toString()
                val percent = 100
                val starRate = 4.5.toFloat()
                val ingredients: ArrayList<String> = ArrayList()
                data!!.child("ingredients").children.forEach {
                    ingredients.add(it.value.toString())
                }
                val directions: ArrayList<String> = ArrayList()
                data!!.child("directions").children.forEach {
                    directions.add(it.value.toString())
                }

                foodItems.add(FoodData(id, url, title, percent, starRate, ingredients, directions))
            }
        })
    }
//    private inner class ReadDatabaseTask : AsyncTask<Void, Void, String>() {
//        override fun onProgressUpdate(vararg values: Void?) {
//            super.onProgressUpdate(*values)
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//        }
//
//        override fun onCancelled(result: String?) {
//            super.onCancelled(result)
//        }
//
//        override fun onCancelled() {
//            super.onCancelled()
//        }
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//        }
//
//        override fun doInBackground(vararg params: Void?): String {
//            return ""
//        }
//    }
}