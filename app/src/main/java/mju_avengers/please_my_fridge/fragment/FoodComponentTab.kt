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


class FoodComponentTab : Fragment() {
    lateinit var components : ArrayList<String>
    lateinit var componentRecyclerAdapter: ComponentRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_food_component, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setSearchFoodRecyclerAdapter()

    }
    private fun setSearchFoodRecyclerAdapter() {
        components = ArrayList((activity as DetailedFoodActivity).foodData.ingredients)
        componentRecyclerAdapter = ComponentRecyclerAdapter(context!!, components)
        detailed_food_component_list_rv.layoutManager = LinearLayoutManager(context)
        detailed_food_component_list_rv.adapter = componentRecyclerAdapter
    }

}
