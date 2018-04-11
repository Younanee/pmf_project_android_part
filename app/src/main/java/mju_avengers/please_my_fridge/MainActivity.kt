package mju_avengers.please_my_fridge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(HomeTab())

        btn_main_home.setOnClickListener {
            replaceFragment(HomeTab())
        }
        btn_main_search.setOnClickListener {
            replaceFragment(SearchTab())
        }
        btn_main_add.setOnClickListener {
            replaceFragment(AddTab())
        }
        btn_main_fridge.setOnClickListener {
            replaceFragment(FavoriteTab())
        }
        btn_main_setting.setOnClickListener {
            replaceFragment(SettingTab())
        }

    }

    fun addFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_frame, fragment)
        transaction.commit()

    }

    fun replaceFragment(fragment: Fragment){
        val transition = supportFragmentManager.beginTransaction()
        transition.replace(R.id.main_frame, fragment)
        transition.commit()
    }
}
