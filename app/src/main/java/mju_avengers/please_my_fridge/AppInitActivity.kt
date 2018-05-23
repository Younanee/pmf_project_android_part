package mju_avengers.please_my_fridge

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_join.*
import mju_avengers.please_my_fridge.data.FoodData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class AppInitActivity : AppCompatActivity() {

    companion object {
        private val RC_SIGN_IN = 1091
    }

    private lateinit var mAuth: FirebaseAuth
    lateinit var mFoodDatabase: DatabaseReference
    lateinit var simpleFoodItems: ArrayList<SimpleFoodData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        mAuth = FirebaseAuth.getInstance()
        mFoodDatabase = FirebaseDatabase.getInstance().reference
        simpleFoodItems = ArrayList()

        //테스트
        app_init_test_btn.setOnClickListener {
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        startSignIn()

    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    fun startSignIn(){
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(listOf(AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            var response : IdpResponse = IdpResponse.fromResultIntent(data)!!
            if (resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                var intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else {
                //로그인 실패
            }
        }
    }
}
