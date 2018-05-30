package mju_avengers.please_my_fridge.fragment


import android.content.res.AssetManager
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting.*
import mju_avengers.please_my_fridge.R
import mju_avengers.please_my_fridge.data.InitFoodGroceryData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import org.jetbrains.anko.support.v4.toast
import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader


class SettingTab : Fragment(){

    private val FOODID = 0
    private val FOOD_GROCERY_NAME = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
