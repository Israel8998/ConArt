package com.example.conart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_crear_cuenta.*
import kotlinx.android.synthetic.main.activity_iniciar_sesion.*

class IniciarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        setup()
    }
    private fun setup() {
        title = "Iniciar sesión"

        aRegistrar.setOnClickListener {
            val intent = Intent(this, CrearCuenta::class.java)
            startActivity(intent)
        }

        btnIngresar.setOnClickListener {
            if (txtCorreo.text.isNotEmpty() &&
                txtContraseña.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtCorreo.text.toString(),
                    txtContraseña.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        showIngresarDatos()
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Correo o contraseña incorrectos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showIngresarDatos() {
        val InicioSesion = Intent(this, IngresoDatos::class.java)
        startActivity(InicioSesion)
    }
}