package com.example.shopping_list

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.shopping_list.dao.DBHelper
import com.example.shopping_list.models.ListItem
import com.example.shopping_list.models.Priority
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.fragment_add_item.*

class EditProduct : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val id = intent.getIntExtra("id", -1)
        val dbHelper = DBHelper(this)
        val item = dbHelper.getItem(id)
        edit_product_name.setText(item.name)
        edit_product_max_price.setText(item.maxPrice.toString())
        edit_radioGroup.check(
            when (item.priority) {
                Priority.HIGH -> edit_radioButtonHigh.id
                Priority.MEDIUM -> edit_radioButtonNormal.id
                Priority.LOW -> edit_radioButtonLow.id
            }
        )
        edit_radio_bought.check(
            when (item.bought) {
                true -> radio_true.id
                false -> radio_false.id
            }
        )
        confirm_edit.setOnClickListener {
            val product = ListItem(
                edit_product_name.text.toString(),
                when (edit_radioGroup.checkedRadioButtonId) {
                    R.id.edit_radioButtonHigh -> Priority.HIGH
                    R.id.edit_radioButtonNormal -> Priority.MEDIUM
                    R.id.edit_radioButtonLow -> Priority.LOW
                    else -> Priority.LOW
                },
                edit_product_max_price.text.toString().toDouble(),
                when (edit_radio_bought.checkedRadioButtonId) {
                    R.id.radio_true -> true
                    R.id.radio_false -> false
                    else -> false
                }, id
                )
            dbHelper.updateItem(product)
            this.finish()
        }
        cancel_edit.setOnClickListener{
            this.finish()
        }
    }
}
