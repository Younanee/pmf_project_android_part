package mju_avengers.please_my_fridge

import android.content.Context
import mju_avengers.please_my_fridge.data.FoodData

object SharedPreferenceController {

    private val DETAILD_FOOD = "detailed_food"

    private val COMPONENTS = "components"
    private val URLS = "urls"
    private val DIRECTIONS = "directions"

    fun setComponents(context: Context, data : ArrayList<String>){
        val pref = context.getSharedPreferences(DETAILD_FOOD, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putStringSet(COMPONENTS, data.toSet())
        editor.commit()
    }
    fun getComponents(context: Context) : Set<String> {
        val pref = context.getSharedPreferences(DETAILD_FOOD, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getStringSet(COMPONENTS, setOf())
    }
    fun setUrls(context: Context, data : ArrayList<String>){
        val pref = context.getSharedPreferences(DETAILD_FOOD, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putStringSet(URLS, data.toSet())
        editor.commit()
    }
    fun getUrls(context: Context) : Set<String> {
        val pref = context.getSharedPreferences(DETAILD_FOOD, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getStringSet(URLS, setOf())
    }
    fun setDirections(context: Context, data : ArrayList<String>){
        val pref = context.getSharedPreferences(DETAILD_FOOD, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putStringSet(DIRECTIONS, data.toSet())
        editor.commit()
    }
    fun getDirections(context: Context) : Set<String> {
        val pref = context.getSharedPreferences(DETAILD_FOOD, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getStringSet(DIRECTIONS, setOf())
    }

    fun clearSPC(context: Context){
        val pref = context.getSharedPreferences(DETAILD_FOOD, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }
}