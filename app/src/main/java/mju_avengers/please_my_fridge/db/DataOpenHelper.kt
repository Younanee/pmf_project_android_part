package mju_avengers.please_my_fridge.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import mju_avengers.please_my_fridge.data.EatenFoodData
import mju_avengers.please_my_fridge.data.GroceryData
import org.jetbrains.anko.db.*

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

    fun getGroceryDatasFromCategory(categoryName : String) : ArrayList<GroceryData> {
        val result : ArrayList<GroceryData> = ArrayList()
        instance!!.use {
            select(GroceryData.TABLE_NAME, GroceryData.COLUMN_ID, GroceryData.COLUMN_NAME, GroceryData.COLUMN_CATEGORY)
                    .whereArgs(GroceryData.COLUMN_CATEGORY + " = {categoryName}", "categoryName" to categoryName)
                    .parseList(object : MapRowParser<List<GroceryData>>{
                        override fun parseRow(columns: Map<String, Any?>): List<GroceryData> {
                            val id = columns.getValue(GroceryData.COLUMN_ID).toString().toInt()
                            val name = columns.getValue(GroceryData.COLUMN_NAME).toString()
                            val category = columns.getValue(GroceryData.COLUMN_CATEGORY).toString()
                            val data = GroceryData(id,category,name)

                            result.add(data)

                            return result
                        }

                    })
        }
        return result
    }

    fun getEatenFoodDatas() : ArrayList<EatenFoodData>{
        val result : ArrayList<EatenFoodData> = ArrayList()
        instance!!.use {
            select(EatenFoodData.TABLE_NAME, EatenFoodData.COLUMN_ID, EatenFoodData.COLUMN_FOOD_ID)
                    .parseList(object :MapRowParser<List<EatenFoodData>>{
                        override fun parseRow(columns: Map<String, Any?>): List<EatenFoodData> {
                            val id = columns.getValue(EatenFoodData.COLUMN_ID).toString().toInt()
                            val foodId = columns.getValue(EatenFoodData.COLUMN_FOOD_ID).toString()
                            result.add(EatenFoodData(id,foodId))
                            return result
                        }
                    })
        }
        return result
    }

    fun insertGroceryDatas(groceryDatas : ArrayList<GroceryData>){
        groceryDatas.forEach {
            instance!!.use {
                insert(GroceryData.TABLE_NAME,
                        GroceryData.COLUMN_CATEGORY to it.category,
                        GroceryData.COLUMN_NAME to it.name)
            }
        }
    }
    fun insertEatenFoodDatas(eatenFoodData: ArrayList<EatenFoodData>){
        eatenFoodData.forEach {
            instance!!.use {
                insert(EatenFoodData.TABLE_NAME,
                        EatenFoodData.COLUMN_FOOD_ID to it.foodId)
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(GroceryData.TABLE_NAME, true,
                GroceryData.COLUMN_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                GroceryData.COLUMN_CATEGORY to TEXT + NOT_NULL,
                GroceryData.COLUMN_NAME to TEXT + NOT_NULL)
        db.createTable(EatenFoodData.TABLE_NAME, true,
                EatenFoodData.COLUMN_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                EatenFoodData.COLUMN_FOOD_ID to TEXT + NOT_NULL)
        //db.createTable()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(GroceryData.TABLE_NAME, true)
        db.dropTable(EatenFoodData.TABLE_NAME, true)
    }
}
val Context.database : DataOpenHelper
    get() = DataOpenHelper.getInstance(applicationContext)