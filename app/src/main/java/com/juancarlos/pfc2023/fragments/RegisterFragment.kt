package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.fragments.DatePickerFragment


class RegisterFragment() : Fragment(R.layout.fragment_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Ocultar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()
        mainActivity.setStatusBarColor("#214F95")
        view.findViewById<EditText>(R.id.etDate).setOnClickListener() {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        getActivity()?.let { datePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        var numberMonth = month + 1;
        view?.findViewById<EditText>(R.id.etDate)
            ?.setText("$day/$numberMonth/$year")
    }

}