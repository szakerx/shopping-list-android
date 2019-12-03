package com.example.shopping_list

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.shopping_list.dao.DBHelper
import com.example.shopping_list.models.ListItem
import com.example.shopping_list.models.Priority
import kotlinx.android.synthetic.main.fragment_add_item.*
import java.time.LocalDate

class AddItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_item, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        cancel_button.setOnClickListener{
            (context as MainActivity).killFragment(this)
        }
        add_product_button.setOnClickListener{
            val dbHelper = DBHelper(context!!)
            val item = ListItem(product_name.text.toString(), when(radioGroup.checkedRadioButtonId) {
                R.id.radioButtonHigh -> Priority.HIGH
                R.id.radioButtonNormal -> Priority.MEDIUM
                R.id.radioButtonLow -> Priority.LOW
                else -> Priority.LOW
            }, product_max_price.text.toString().toDouble(), LocalDate.now())
            dbHelper.addItem(item)
            (context as MainActivity).setFragment(ListFragment())
        }
    }
}
