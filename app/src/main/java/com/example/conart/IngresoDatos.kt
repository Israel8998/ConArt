package com.example.conart

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_ingreso_datos.*


class IngresoDatos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_datos)

        btnConfiguracion.setOnClickListener {
            val ConfiguracionIntent = Intent(this, ConfiguracionPerfil::class.java)
            startActivity(ConfiguracionIntent)
        }
    }
}