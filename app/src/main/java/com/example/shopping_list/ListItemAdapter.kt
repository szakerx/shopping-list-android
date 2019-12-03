package com.example.shopping_list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping_list.dao.DBHelper
import com.example.shopping_list.models.ListItem
import com.example.shopping_list.models.Priority

class ListItemAdapter(
    private val items: MutableList<ListItem>,
    private val frag: ListFragment,
    private val dbHelper: DBHelper,
    private val context: Context
) : RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        var deleteButton: Button = itemView.findViewById(R.id.delete_button)
        var itemPriority: TextView = itemView.findViewById(R.id.priority)
        var itemMaxPrice: TextView = itemView.findViewById(R.id.price)
        var itemDate: TextView = itemView.findViewById(R.id.date)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemPriority.text = when (item.priority) {
            Priority.LOW -> "Low"
            Priority.MEDIUM -> "Normal"
            Priority.HIGH -> "High"
        }
        holder.itemMaxPrice.text = item.maxPrice.toString()
        holder.itemDate.text = item.date.toString()
        holder.deleteButton.setOnClickListener {
            dbHelper.deleteItem(item.id)
            frag.setRecyclerView(dbHelper)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditProduct::class.java).apply {
                putExtra("id", item.id)
            }
            (context as? MainActivity)?.startActivity(intent)
        }
        if (item.bought){
            holder.itemName.setTextColor(Color.GREEN)
        }
    }
}