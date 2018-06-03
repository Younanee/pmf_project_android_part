package mju_avengers.please_my_fridge.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import mju_avengers.please_my_fridge.data.*
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PMF_DB", null, 1){
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

    fun getGroceryDatasPlusFromCategory(categoryName : String) : ArrayList<GroceryDataPlusDate> {
        val result : ArrayList<GroceryDataPlusDate> = ArrayList()
        instance!!.use {
            select(GroceryData.TABLE_NAME, GroceryData.COLUMN_ID, GroceryData.COLUMN_NAME, GroceryData.COLUMN_CATEGORY, GroceryData.COLUMN_DATE)
                    .whereArgs(GroceryData.COLUMN_CATEGORY + " = {categoryName}", "categoryName" to categoryName)
                    .parseList(object : MapRowParser<List<GroceryDataPlusDate>>{
                        override fun parseRow(columns: Map<String, Any?>): List<GroceryDataPlusDate> {
                            val id = columns.getValue(GroceryData.COLUMN_ID).toString().toInt()
                            val name = columns.getValue(GroceryData.COLUMN_NAME).toString()
                            val category = columns.getValue(GroceryData.COLUMN_CATEGORY).toString()
                            val date = columns.getValue(GroceryData.COLUMN_DATE).toString()
                            val data = GroceryDataPlusDate(id,category,name, date)

                            result.add(data)

                            return result
                        }
                    })
        }
        return result
    }
    fun getAllGrocerisNameInFridge() : ArrayList<String> {
        val result : ArrayList<String> = ArrayList()
        instance!!.use {
            select(GroceryData.TABLE_NAME, GroceryData.COLUMN_NAME)
                    .distinct()
                    .parseList(object : MapRowParser<List<String>>{
                        override fun parseRow(columns: Map<String, Any?>): List<String> {
                            val name = columns.getValue(GroceryData.COLUMN_NAME).toString()
                            result.add(name)
                            return result
                        }
                    })
        }
        return result
    }
    fun getEatenFoodDatas() : ArrayList<String>{
        val result : ArrayList<String> = ArrayList()
        instance!!.use {
            select(EatenFoodData.TABLE_NAME, EatenFoodData.COLUMN_ID, EatenFoodData.COLUMN_FOOD_ID)
                    .parseList(object :MapRowParser<List<String>>{
                        override fun parseRow(columns: Map<String, Any?>): List<String> {
                            val foodId = columns.getValue(EatenFoodData.COLUMN_FOOD_ID).toString()
                            result.add(foodId)
                            return result
                        }
                    })
        }
        return result
    }


    fun insertGroceryDatas(groceryDatas : ArrayList<GroceryData>){
        val nowDate : String = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
        groceryDatas.forEach {
            instance!!.use {
                insert(GroceryData.TABLE_NAME,
                        GroceryData.COLUMN_CATEGORY to it.category,
                        GroceryData.COLUMN_NAME to it.name,
                        GroceryData.COLUMN_DATE to nowDate
                )
            }
        }
    }

    fun insertEatenFoodDatas(foodId : String){
        instance!!.use {
            insert(EatenFoodData.TABLE_NAME,
                    EatenFoodData.COLUMN_FOOD_ID to foodId)
        }
    }
    fun removeEatenFoodData(foodId : String){
        instance!!.use {
            delete(EatenFoodData.TABLE_NAME,
                    "${EatenFoodData.COLUMN_FOOD_ID}={${EatenFoodData.COLUMN_FOOD_ID}}",
                    EatenFoodData.COLUMN_FOOD_ID to foodId)
        }
    }
    fun removeGroceryDatas(groceryIdDatas : ArrayList<Int>){
        groceryIdDatas.forEach {
            instance!!.use {
                delete(GroceryData.TABLE_NAME,
                        GroceryData.COLUMN_ID+"={"+GroceryData.COLUMN_ID+"}",
                        GroceryData.COLUMN_ID to it)
            }
        }
    }
    fun insertInitFoodGroceryData(datas : ArrayList<InitFoodGroceryData>){
        writableDatabase.beginTransaction()
        try {
            for(i in 0..datas.size-1){
                var cv : ContentValues = ContentValues()
                cv.put(InitFoodGroceryData.COLUMN_FOOD_ID, datas[i].id)
                cv.put(InitFoodGroceryData.COLUMN_GROCERY_NAME, datas[i].groceryName)
                writableDatabase.insert(InitFoodGroceryData.TABLE_NAME, null, cv)
            }
            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }
    }
    fun mappingDBDataToFoodComponentsData(foodIds : ArrayList<String> ) : ArrayList<FoodComponentsData> {
        var result: ArrayList<FoodComponentsData> = ArrayList()

        foodIds.forEach {
            var cursor : Cursor = readableDatabase.query(InitFoodGroceryData.TABLE_NAME,
                    arrayOf(InitFoodGroceryData.COLUMN_GROCERY_NAME), "${InitFoodGroceryData.COLUMN_FOOD_ID} = ?",
                    arrayOf(it),null,null,null)
            cursor.moveToFirst()
            var components : ArrayList<String> = ArrayList()
            while (!cursor.isAfterLast){
                components.add(cursor.getString(0))
                cursor.moveToNext()
            }
            result.add(FoodComponentsData(it, components))
        }
        return result
    }
    fun searchInitDataByKeyword(keyword : String ) : ArrayList<Int> {
        var result: ArrayList<Int> = ArrayList()
        var cursor : Cursor = readableDatabase.query(true, InitFoodGroceryData.TABLE_NAME,
                arrayOf(InitFoodGroceryData.COLUMN_FOOD_ID),"${InitFoodGroceryData.COLUMN_GROCERY_NAME} = ?", arrayOf(keyword),null,null,null,null)

        if (cursor!=null && cursor.moveToFirst())
        while (!cursor.isAfterLast){
            var temp = cursor.getString(0).toInt()
            if (temp in 1..5022) {
                result.add(temp)
            }
            cursor.moveToNext()
        }
        return result
    }

    fun isCompletedInitDataSetting() : Boolean{
        var count : Long = DatabaseUtils.queryNumEntries(instance!!.readableDatabase, InitFoodGroceryData.TABLE_NAME)
        if (count.toInt() == 32944) {
            return true
        }
        return false
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(GroceryData.TABLE_NAME, true,
                GroceryData.COLUMN_ID to INTEGER + PRIMARY_KEY ,
                GroceryData.COLUMN_CATEGORY to TEXT + NOT_NULL,
                GroceryData.COLUMN_NAME to TEXT + NOT_NULL,
                GroceryData.COLUMN_DATE to TEXT + NOT_NULL)

        db.createTable(EatenFoodData.TABLE_NAME, true,
                EatenFoodData.COLUMN_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                EatenFoodData.COLUMN_FOOD_ID to TEXT + NOT_NULL + UNIQUE)
        db.createTable(InitFoodGroceryData.TABLE_NAME, true,
                InitFoodGroceryData.COLUMN_FOOD_ID to TEXT,
                InitFoodGroceryData.COLUMN_GROCERY_NAME to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(GroceryData.TABLE_NAME, true)
        db.dropTable(EatenFoodData.TABLE_NAME, true)
    }
}
val Context.database : DataOpenHelper
    get() = DataOpenHelper.getInstance(applicationContext)
