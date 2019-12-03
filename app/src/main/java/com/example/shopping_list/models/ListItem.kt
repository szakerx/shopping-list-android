package com.example.shopping_list.models

import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class ListItem @TargetApi(Build.VERSION_CODES.O)
@RequiresApi(Build.VERSION_CODES.O) constructor(
    var name: String,
    var priority: Priority,
    var maxPrice: Double,
    var date: LocalDate,
    var bought: Boolean = false,
    var id: Int? = null
) {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        name: String,
        priority: String,
        maxPrice: Double,
        date: LocalDate,
        bought: Int,
        id: Int? = null
    ) : this(
        name, when (priority) {
            "normal" -> Priority.MEDIUM
            "high" -> Priority.HIGH
            "low" -> Priority.LOW
            else -> Priority.LOW
        }, maxPrice, date, when (bought) {
            0 -> false
            1 -> true
            else -> false
        }, id
    )

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        name: String,
        priority: Priority,
        maxPrice: Double,
        bought: Boolean,
        id: Int
    ) : this(
        name, priority, maxPrice, LocalDate.now(), bought, id
    )

    override fun toString(): String {
        return "$name $priority $maxPrice $date $bought $id"
    }
}