package com.kodiiiofc.urbanuniversity.shoppingcart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(private val context: Context, private val itemList: List<Item>) :
    ArrayAdapter<Item>(context, R.layout.list_item, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val nameTV = view?.findViewById<TextView>(R.id.tv_name)
        val weightTV = view?.findViewById<TextView>(R.id.tv_weight)
        val priceTV = view?.findViewById<TextView>(R.id.tv_price)

        nameTV?.text = item?.itemName
        weightTV?.text = item?.itemWeight.toString()
        priceTV?.text = item?.itemPrice.toString()

        return view!!
    }
}