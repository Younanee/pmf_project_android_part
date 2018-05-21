package mju_avengers.please_my_fridge.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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

    fun insertGroceryDatas(groceryDatas : ArrayList<GroceryData>){
        groceryDatas.forEach {
            instance!!.use {
                insert(GroceryData.TABLE_NAME,
                        GroceryData.COLUMN_CATEGORY to it.category,
                        GroceryData.COLUMN_NAME to it.name
                )
            }
        }
    }



    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(GroceryData.TABLE_NAME, true,
                GroceryData.COLUMN_ID to INTEGER + PRIMARY_KEY + UNIQUE,
                GroceryData.COLUMN_CATEGORY to TEXT + NOT_NULL,
                GroceryData.COLUMN_NAME to TEXT + NOT_NULL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("grocery_table", true)
    }
}
val Context.database : DataOpenHelper
    get() = DataOpenHelper.getInstance(applicationContext)