package mju_avengers.please_my_fridge.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

object DBManager{
    val Context.database : DataOpenHelper
        get() = DataOpenHelper.getInstance(applicationContext)



}


class DataOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDB", null, 1){
    companion object {
        private var instance : DataOpenHelper? = null
        @Synchronized
        fun getInstance(ctx: Context) : DataOpenHelper {
            if (instance == null){
                instance = DataOpenHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("foodTable", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "name" to TEXT,
                "photo" to TEXT,
                "content" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("foodRecipeTable", true)
    }
}