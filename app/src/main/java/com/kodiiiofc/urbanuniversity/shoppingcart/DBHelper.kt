package com.kodiiiofc.urbanuniversity.shoppingcart

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "SHOPPING_CART_DATABASE"
        private val DATABASE_VERSION = 2
        private val TABLE_NAME = "shopping_cart_table"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_WEIGHT = "weight"
        private val KEY_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val USER_TABLE = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_WEIGHT REAL, $KEY_PRICE REAL)"
        db?.execSQL(USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(item: Item) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, item.itemID)
        contentValues.put(KEY_NAME, item.itemName)
        contentValues.put(KEY_WEIGHT, item.itemWeight)
        contentValues.put(KEY_PRICE, item.itemPrice)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun readItems(): MutableList<Item> {
        val itemList = mutableListOf<Item>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return itemList
        }
        var itemID: Int
        var itemName: String
        var itemWeight: Double
        var itemPrice: Double
        if (cursor.moveToFirst()) {
            do {
                itemID = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                itemName = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                itemWeight = cursor.getDouble(cursor.getColumnIndex(KEY_WEIGHT))
                itemPrice = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                val item = Item(itemID, itemName, itemWeight, itemPrice)
                itemList.add(item)
            } while (cursor.moveToNext())
        }
        return itemList
    }

    fun updateItem(item: Item) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, item.itemID)
        contentValues.put(KEY_NAME, item.itemName)
        contentValues.put(KEY_WEIGHT, item.itemWeight)
        contentValues.put(KEY_PRICE, item.itemPrice)
        db.update(TABLE_NAME, contentValues, "$KEY_ID = ${item.itemID}", null)
        db.close()
    }

    fun deleteItem(item: Item) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"$KEY_ID = ${item.itemID}", null)
        db.close()
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

}