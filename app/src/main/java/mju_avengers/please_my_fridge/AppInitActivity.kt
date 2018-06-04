package mju_avengers.please_my_fridge

import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import mju_avengers.please_my_fridge.data.InitFoodGroceryData
import mju_avengers.please_my_fridge.db.DataOpenHelper
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class AppInitActivity : AppCompatActivity() {

    companion object {
        private val RC_SIGN_IN = 1091
    }

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        mAuth = FirebaseAuth.getInstance()
        //초기 데이터베이스 테이블 생성
        DataOpenHelper.getInstance(this)

        startSignIn()

    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    fun startSignIn() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(listOf(AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                if (!DataOpenHelper.getInstance(this).isCompletedInitDataSetting()) {
                    initDataBaseInsert()
                    Log.e("초기 데이터 작업중", "데이터 들어갔습니다.")
                }
                var intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.e("login failed", "firebase login fail")
            }
        }
    }

    fun initDataBaseInsert() {
        var datas: ArrayList<InitFoodGroceryData> = ArrayList()
        var assetManager: AssetManager
        var inputStream: InputStream? = null
        var inputStreamReader: InputStreamReader
        var fileReader: BufferedReader? = null
        try {
            assetManager = resources.assets
            inputStream = assetManager.open("id_with_ingredients.csv")
            inputStreamReader = InputStreamReader(inputStream, "euc-kr")
            fileReader = BufferedReader(inputStreamReader)
            var line: String?
            line = fileReader!!.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.isNotEmpty()) {
                    datas.add(InitFoodGroceryData(tokens[0], tokens[1]))
                }
                line = fileReader!!.readLine()
            }

        } catch (e: Exception) {
            Log.e("error", e.toString())
        } finally {
            try {
                fileReader!!.close()
                inputStream!!.close()
            } catch (e: Exception) {
                Log.e("error", e.toString())
            }
        }

        DataOpenHelper.getInstance(this!!).insertInitFoodGroceryData(datas)
    }


}
