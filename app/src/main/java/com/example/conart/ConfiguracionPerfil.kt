package com.example.conart

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_configuracion_perfil.*

enum class ProviderType {
    BASIC,
    GOOGLE
}

class ConfiguracionPerfil : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion_perfil)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")

        //Guardar
        btnGuardar.setOnClickListener {
            if (email != null) {
                db.collection("Usuarios").document(email).set(
                    hashMapOf("Nombre" to txtNombreConf,
                    "Apellido" to txtApellidoConf,
                    "Número celular" to txtCelularConf,
                    "Contraseña" to txtContraseñaConf,
                    "Provider" to lblProveedor)
                )
            }
        }
    }

    private fun setup(email: String, provider: String){
        title = "Configuración de perfil"
        lblCorreo.text = email
        lblProveedor.text = provider

        btnCerrarSesion.setOnClickListener {
            //Borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            CerrarSesion() //Implementación la función cerrar sesión
        }
    }

    private fun CerrarSesion() { //Función del botón cerrar sesión
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
    }
}