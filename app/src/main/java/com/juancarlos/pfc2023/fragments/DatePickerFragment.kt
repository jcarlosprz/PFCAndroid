package com.juancarlos.pfc2023.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Outline
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment

class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener(day, month, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val picker = DatePickerDialog(activity as Context, this, year, month, day)
        picker.datePicker.maxDate = c.timeInMillis
        return picker
    }


}
