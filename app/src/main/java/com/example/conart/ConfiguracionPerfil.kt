package com.example.conart

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_configuracion_perfil.*

class ConfiguracionPerfil : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion_perfil)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "")

        //Guardar en la base de datos
        btnGuardar.setOnClickListener {
            if (email != null) {
                db.collection("Usuarios").document(email).set(
                    hashMapOf("Nombre" to txtNombreConf.text.toString(),
                    "Apellido" to txtApellidoConf.text.toString(),
                    "Número celular" to txtCelularConf.text.toString())
                )
                showSatisfaction()
            }
        }

        //botón para ir a la pantalla principal
        btnPantallaPrincipal.setOnClickListener {
            showPantallaPrincipal(email ?: "")
        }

        //Cargar datos en los campos correspondientes al configurar perfil
        if(email != null) {
            db.collection("Usuarios").document(email).get().addOnSuccessListener {
                txtNombreConf.setText(it.get("Nombre") as String?)
                txtApellidoConf.setText(it.get("Apellido") as String?)
                txtCelularConf.setText(it.get("Número celular") as String?)
            }
        } else {
            showAlert()
        }
    }

    private fun showPantallaPrincipal(email: String) {
        val IngresoDatosIntent = Intent(this, IngresoDatos::class.java).apply {
            putExtra("email", email)
        }
        startActivity(IngresoDatosIntent)
    }

    private fun setup(email: String){
        title = "Configuración de perfil"
        lblCorreo.text = email

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
        val IniciarSesionIntent = Intent(this, IniciarSesion::class.java)
        startActivity(IniciarSesionIntent)
    }

    private fun showSatisfaction() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Guardado exitoso") //Título del poop up
        builder.setMessage("Los datos ingresados se han guardado con éxito") //Mensaje del Poop up
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error") //Título del poop up del error
        builder.setMessage("Los datos no se han podido guardar") //Descripción del error
        builder.setPositiveButton("Aceptar", null) //botón para cerra el poop up
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}