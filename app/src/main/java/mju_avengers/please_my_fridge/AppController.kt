package mju_avengers.please_my_fridge

import android.app.Application
import com.mongodb.MongoException
//import com.mongodb.client.MongoClient
//import com.mongodb.client.MongoClients
//import com.mongodb.client.MongoCollection
//import com.mongodb.client.MongoDatabase
import org.bson.Document
import java.net.UnknownHostException

class AppController : Application() {
    companion object {
        //어플 전역에서 사용할 스태틱 변수 설정
        //lateinit var db :

    }

    override fun onCreate() {
        super.onCreate()
    }

    fun buildDatabase(){

    }

}