package mju_avengers.please_my_fridge.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_food_component.*
import mju_avengers.please_my_fridge.DetailedFoodActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.ComponentRecyclerAdapter
import mju_avengers.please_my_fridge.db.DataOpenHelper


class FoodComponentTab : Fragment() {
    lateinit var components: ArrayList<String>
    lateinit var componentRecyclerAdapter: ComponentRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_food_component, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setSearchFoodRecyclerAdapter()

    }

    private fun setSearchFoodRecyclerAdapter() {
        components = ArrayList((activity as DetailedFoodActivity).foodData.components)
        val myGroceries: ArrayList<String> = DataOpenHelper.getInstance(activity!!).getAllGrocerisNameInFridge()
        componentRecyclerAdapter = ComponentRecyclerAdapter(context!!, components, myGroceries)
        detailed_food_component_list_rv.layoutManager = LinearLayoutManager(context)
        detailed_food_component_list_rv.adapter = componentRecyclerAdapter
    }

}
