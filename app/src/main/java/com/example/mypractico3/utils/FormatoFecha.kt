package com.example.mypractico3.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatearFecha(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault())
    return formato.format(Date(timestamp))
}