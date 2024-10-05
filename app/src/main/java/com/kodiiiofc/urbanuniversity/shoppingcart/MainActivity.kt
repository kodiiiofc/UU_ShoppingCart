package com.kodiiiofc.urbanuniversity.shoppingcart

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var statusBar: View

    private lateinit var idET: EditText
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText

    private lateinit var saveBTN: Button
    private lateinit var removeBTN: Button
    private lateinit var itemsLV: ListView

    private var adapter: MyListAdapter? = null

    private val db = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        statusBar = findViewById(R.id.status_bar)

        val statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBar.layoutParams.height = resources.getDimensionPixelSize(statusBarHeight)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        idET = findViewById(R.id.et_id)
        nameET = findViewById(R.id.et_name)
        weightET = findViewById(R.id.et_weight)
        priceET = findViewById(R.id.et_price)
        saveBTN = findViewById(R.id.btn_save)
        removeBTN = findViewById(R.id.btn_remove_db)
        itemsLV = findViewById(R.id.lv_items)

        loadDatabase()

        saveBTN.setOnClickListener {
            val itemID = idET.text.toString().trim()
            val itemName = nameET.text.toString().trim()
            val itemWeight = weightET.text.toString().trim()
            val itemPrice = priceET.text.toString().trim()

            if (itemID.isNotEmpty() && itemName.isNotEmpty() && itemWeight.isNotEmpty() && itemPrice.isNotEmpty()) {
                val item =
                    Item(itemID.toInt(), itemName, itemWeight.toDouble(), itemPrice.toDouble())
                db.addItem(item)

            }
            loadDatabase()
        }

        removeBTN.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Внимание!")
                .setMessage("Вы уверены, что хотите удалить базу данных?")
                .setPositiveButton("Да") { _, _ ->
                    db.removeAll()
                    loadDatabase()
                }
                .setNegativeButton("Нет", null)
                .create()
                .show()
        }

        itemsLV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item = itemsLV.getItemAtPosition(position) as Item
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Элемент ${item.itemID}")
                .setMessage("Выбран элемент ${item.itemName}.\nЧто вы хотите сделать?")
                .setNegativeButton("Удалить") { _, _ ->
                    db.deleteItem(item)
                    loadDatabase()
                    Toast.makeText(this@MainActivity, "Позиция ${item.itemID} удалена", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("Отмена", null)
                .setPositiveButton("Редактировать") { _, _ ->
                    updateItem(item)
                }
                .create()
                .show()
        }

    }

    private fun updateItem(item: Item) {
        val dialog = AlertDialog.Builder(this)
        val editableFields = layoutInflater.inflate(R.layout.item_update, null)
        val idET = editableFields.findViewById<EditText>(R.id.et_id)
        val nameET = editableFields.findViewById<EditText>(R.id.et_name)
        val weightET = editableFields.findViewById<EditText>(R.id.et_weight)
        val priceET = editableFields.findViewById<EditText>(R.id.et_price)

        idET.setText(item.itemID.toString())
        nameET.setText(item.itemName)
        weightET.setText(item.itemWeight.toString())
        priceET.setText(item.itemPrice.toString())

        dialog.setTitle("Редактирование")
            .setView(editableFields)
            .setPositiveButton("Сохранить") { _, _ ->
                val updatedID = idET.text.toString().trim()
                val updatedName = nameET.text.toString().trim()
                val updatedWeight = weightET.text.toString().trim()
                val updatedPrice = priceET.text.toString().trim()

                if (updatedID.isNotEmpty() && updatedName.isNotEmpty() && updatedWeight.isNotEmpty() && updatedPrice.isNotEmpty()) {
                    val updatedItem = Item(
                        updatedID.toInt(),
                        updatedName,
                        updatedWeight.toDouble(),
                        updatedPrice.toDouble()
                    )
                    db.updateItem(updatedItem)
                    loadDatabase()
                    Toast.makeText(this@MainActivity, "Данные позциции ${item.itemID} обновлены", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    private fun loadDatabase() {
        adapter = MyListAdapter(this, db.readItems())
        itemsLV.adapter = adapter
        itemsLV.deferNotifyDataSetChanged()
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