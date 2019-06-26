package com.ankit.mishra.Data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter



class SpinnerAdapter<T>(private val listOfItems:MutableList<T>,private val resourceId:Int, private val dataBinding:SpinnerBinding<T> ) : BaseAdapter()
{
    override fun isEmpty(): Boolean =listOfItems.isEmpty()

    override fun getItem(position: Int): T =listOfItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean =true

    override fun getCount(): Int = listOfItems.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view= convertView ?: LayoutInflater.from(parent?.context).inflate(resourceId,parent, false)
        dataBinding.bindData(listOfItems[position],view)
        return view
    }
}