package com.example.conart

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_configuracion_perfil.*
import kotlinx.android.synthetic.main.activity_ingreso_datos.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class IngresoDatos : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_datos)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")

        //Cargar nombre y apellido al inicio de la pantalla
        if(email != null) {
            db.collection("Usuarios").document(email).get().addOnSuccessListener {
                lblNombreUsuario.setText(it.get("Nombre") as String?)
                lblApellidoUsuario.setText(it.get("Apellido") as String?)
            }
        }

        //Cargar la fecha actual
        lblCalendario.text = SimpleDateFormat("dd/MM/yyyy").format(Date())

        //botón para ir a la pantalla de configuración
        btnConfiguracion.setOnClickListener {
            showConfigPerfil(email ?: "")
        }
    }

    //Enviar el email ingresado al ir a la pantalla de configuración del perfil
    private fun showConfigPerfil(email: String) {
        val ConfiguracionIntent = Intent(this, ConfiguracionPerfil::class.java).apply{
            putExtra("email", email)
        }
        startActivity(ConfiguracionIntent)
    }
}