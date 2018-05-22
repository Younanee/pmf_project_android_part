package mju_avengers.please_my_fridge

import android.content.Context
import android.os.Handler
import android.util.Log
import com.google.firebase.database.*
import mju_avengers.please_my_fridge.data.FoodData

class MyConnectFirebase(ctx : Context) {
    companion object {
        private var instance : MyConnectFirebase? = null
        lateinit var datas : ArrayList<FoodData>
        lateinit var mFirbaseDB : DatabaseReference

        fun getInstence(ctx:Context): MyConnectFirebase{
            if(instance == null){
                instance = MyConnectFirebase(ctx)
            }
            return instance!!
        }
    }
    fun getFoodDatas(ids : ArrayList<String>) : ArrayList<FoodData> {
        mFirbaseDB = FirebaseDatabase.getInstance().reference
        datas = ArrayList()
        ids.forEach {
            getFoodItemAtDB(it)
        }
        return datas
    }
    private fun getFoodItemAtDB(id: String){
        mFirbaseDB.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.e("FireBase", "Data is not token")
            }
            override fun onDataChange(data: DataSnapshot?) {
                datas.add(mappingData(data!!))
            }
        })
    }
    private fun mappingData(data : DataSnapshot) : FoodData{
        var id = data!!.child("id").value.toString()
        var urls : ArrayList<String> = ArrayList()
        data!!.child("url").children.forEach {
            urls.add(it.value.toString())
        }
        var title = data!!.child("title").value.toString()
        var percent = 100
        var starRate = 4.5.toFloat()
        var ingredients: ArrayList<String> = ArrayList()
        data!!.child("ingredients").children.forEach {
            ingredients.add(it.value.toString())
        }
        var directions: ArrayList<String> = ArrayList()
        data!!.child("directions").children.forEach {
            directions.add(it.value.toString())
        }
        Log.e(" 3-2번째", "mappingData-> 매핑 끝")
        return FoodData(id, urls, title, percent, starRate, ingredients, directions)
    }
}