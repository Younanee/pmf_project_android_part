package mju_avengers.please_my_fridge

import android.app.Application

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