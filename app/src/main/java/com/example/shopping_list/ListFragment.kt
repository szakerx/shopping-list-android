package com.example.shopping_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping_list.dao.DBHelper
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private var dbHelper: DBHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())
        setRecyclerView(dbHelper!!)
    }

    fun setRecyclerView(dbHelper: DBHelper) {

        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = ListItemAdapter(dbHelper.allItems, this, dbHelper, requireContext())

    }

    override fun onResume() {
        super.onResume()
        setRecyclerView(dbHelper!!)
    }

}
