package com.example.shopping_list.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.contentValuesOf
import com.example.shopping_list.models.ListItem
import com.example.shopping_list.models.Priority
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATA_BASE_NAME, null, DATA_BASE_VERSION) {

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COL_ID INTEGER PRIMARY KEY," +
                    "$COL_NAME TEXT NOT NULL," +
                    "$COL_PRIORITY TEXT NOT NULL," +
                    "$COL_MINPRICE REAL NOT NULL," +
                    "$COL_DATE TEXT NOT NULL," +
                    "$COL_BOUGHT INTEGER NOT NULL)"
        )
    }

    companion object {
        internal const val DATA_BASE_VERSION = 1
        internal const val DATA_BASE_NAME = "productsx.db"
        internal const val TABLE_NAME = "productsx"
        internal const val COL_ID = "id"
        internal const val COL_NAME = "name"
        internal const val COL_PRIORITY = "priority"
        internal const val COL_MINPRICE = "price"
        internal const val COL_DATE = "date"
        internal const val COL_BOUGHT = "bought"
    }

    val allItems: MutableList<ListItem>
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val query = "SELECT * FROM $TABLE_NAME"
            val items = mutableListOf<ListItem>()
            val db = this.readableDatabase
            val cursor = db.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                    val priority = cursor.getString(cursor.getColumnIndex(COL_PRIORITY))
                    val price = cursor.getDouble(cursor.getColumnIndex(COL_MINPRICE))
                    val date = cursor.getString(cursor.getColumnIndex(COL_DATE))
                    val bought = cursor.getInt(cursor.getColumnIndex(COL_BOUGHT))
                    val item = ListItem(
                        name, priority, price, LocalDate.parse(
                            date,
                            DateTimeFormatter.ISO_DATE
                        ), bought, id
                    )
                    items.add(item)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return items
        }

    fun deleteItem(id: Int?): Long {

        return if (id != null) {
            val db = this.writableDatabase
            val result = db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
            db.close()
            return result.toLong()
        } else {
            -1L
        }

    }

    fun addItem(item: ListItem): Long {
        val db = this.writableDatabase
        val value = contentValuesOf()
        value.put(COL_NAME, item.name)
        value.put(
            COL_PRIORITY, when (item.priority) {
                Priority.HIGH -> "high"
                Priority.MEDIUM -> "normal"
                Priority.LOW -> "low"
            }
        )
        value.put(COL_MINPRICE, item.maxPrice)
        value.put(COL_DATE, item.date.toString())
        value.put(
            COL_BOUGHT, when (item.bought) {
                false -> 0
                true -> 1
            }
        )
        val result = db.insert(TABLE_NAME, null, value)
        item.id = result.toInt()
        db.close()
        return result
    }

    fun updateItem(item: ListItem): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, item.id)
        values.put(COL_NAME, item.name)
        values.put(COL_PRIORITY, when(item.priority){
            Priority.LOW -> "low"
            Priority.MEDIUM -> "normal"
            Priority.HIGH -> "high"
        })
        values.put(COL_MINPRICE, item.maxPrice)
        values.put(
            COL_BOUGHT, when (item.bought) {
                false -> 0
                true -> 1
            }
        )
        val result = db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(item.id.toString()))
        db.close()
        return result.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getItem(id: Int): ListItem {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID =?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        cursor.moveToFirst()
        val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
        val priority = cursor.getString(cursor.getColumnIndex(COL_PRIORITY))
        val price = cursor.getDouble(cursor.getColumnIndex(COL_MINPRICE))
        val date = cursor.getString(cursor.getColumnIndex(COL_DATE))
        val bought = cursor.getInt(cursor.getColumnIndex(COL_BOUGHT))
        cursor.close()
        return ListItem(
            name, priority, price, LocalDate.parse(
                date,
                DateTimeFormatter.ISO_DATE
            ), bought
        )
    }
}