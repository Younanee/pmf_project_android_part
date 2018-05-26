package mju_avengers.please_my_fridge.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_food_recipe.*
import mju_avengers.please_my_fridge.DetailedFoodActivity
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.adapter.RecipeRecyclerAdapter


class FoodRecipeTab : Fragment() {
    lateinit var directions : ArrayList<String>
    lateinit var urls : ArrayList<String>
    lateinit var recipeRecyclerAdapter: RecipeRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_food_recipe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSearchFoodRecyclerAdapter()
    }

    private fun setSearchFoodRecyclerAdapter() {
        directions = ArrayList((activity as DetailedFoodActivity).foodData.directions)
        urls = ArrayList((activity as DetailedFoodActivity).foodData.urls)
        recipeRecyclerAdapter = RecipeRecyclerAdapter(context!!, directions, urls)
        food_detail_recipe_list_rv.layoutManager = LinearLayoutManager(context)
        food_detail_recipe_list_rv.adapter = recipeRecyclerAdapter
    }
}
