package mju_avengers.please_my_fridge

import android.content.Context
import android.os.Handler
import android.util.Log
import com.google.firebase.database.*
import mju_avengers.please_my_fridge.data.FoodData
import mju_avengers.please_my_fridge.data.SimpleFoodData
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread

class UseFirebaseDatabase() {
    companion object {
        private var instance: UseFirebaseDatabase? = null
        lateinit var simpleFoodData: SimpleFoodData
        fun getInstence(): UseFirebaseDatabase {
            if (instance == null) {
                instance = UseFirebaseDatabase()
            }
            return instance!!
        }
    }

    fun readFBData(childID: String, listener: OnGetDataListener) {
        listener.onStart()
        val mFBDatabase = FirebaseDatabase.getInstance().reference
        mFBDatabase.child(childID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(errorData: DatabaseError?) {
                listener.onFailed(errorData!!)
            }

            override fun onDataChange(data: DataSnapshot?) {
                listener.onSuccess(data!!)
            }
        })
    }

//    private fun mappingData(data : DataSnapshot) : SimpleFoodData{
//        var id = data!!.child("id").value.toString()
//        var url = data.child("url").child("0").value.toString()
//        var title = data!!.child("title").value.toString()
//        var percent = 100
//        var starRate = 4.5.toFloat()
//        return )
//    }
}

//var id = data!!.child("id").value.toString()
//var url = data.child("url").child("0").value.toString()
//var title = data!!.child("title").value.toString()
//var percent = 100
//var starRate = 4.5.toFloat()
//simpleFoodData.add(SimpleFoodData(id, url, title, percent, starRate))