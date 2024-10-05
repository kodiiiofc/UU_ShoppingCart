package com.kodiiiofc.urbanuniversity.shoppingcart

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var statusBar: View

    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText

    private lateinit var saveBTN: Button
    private lateinit var itemsLV: ListView

    private var items: List<Item> = listOf()
    private var adapter: MyListAdapter? = null

    private val db = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        statusBar = findViewById(R.id.status_bar)

        val statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBar.layoutParams.height= resources.getDimensionPixelSize(statusBarHeight)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        nameET = findViewById(R.id.et_name)
        weightET = findViewById(R.id.et_weight)
        priceET = findViewById(R.id.et_price)
        saveBTN = findViewById(R.id.btn_save)
        itemsLV = findViewById(R.id.lv_items)
        adapter = MyListAdapter(this, items)
        itemsLV.adapter = adapter


        saveBTN.setOnClickListener {
            try {
                val itemName = nameET.text.toString()
                val itemWeight = weightET.text.toString().toDouble()
                val itemPrice = priceET.text.toString().toDouble()

                val item = Item(itemName, itemWeight, itemPrice)
                db.addItem(item)
            } catch (e: Exception) {
                //if fields are empty just load data
            }
            finally {
                adapter = MyListAdapter(this, db.readItems())
                itemsLV.adapter = adapter
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}