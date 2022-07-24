package com.example.conart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.util.PatternsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_crear_cuenta.*
import kotlinx.android.synthetic.main.activity_iniciar_sesion.*
import java.lang.NumberFormatException

class CrearCuenta : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)


        //Analytuc Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        //Setup
        setup()
    }

    private fun setup() { //Título de la pantalla
        title = "Registro"

        //Función del botón para crear cuenta
        btnRegistrar.setOnClickListener {
                if (txtCorreoUsuario.text.isNotEmpty()) { //Comprobar que no esté vacío el texto del correo
                    if (txtPassword.text.isNotEmpty()) { //Comprobar que no esté vacío el texto de la contraseña
                        if (txtPassword.length() >= 6) { //Comprobar que tenga el mínimo de caracteres en contraseña
                            if (PatternsCompat.EMAIL_ADDRESS.matcher(txtCorreoUsuario.text.toString()).matches()) {
                                FirebaseAuth.getInstance()
                                    .createUserWithEmailAndPassword( //Función para crear cuenta
                                        txtCorreoUsuario.text.toString(),
                                        txtPassword.text.toString() //Envío a la BDD
                                    ).addOnCompleteListener {
                                    if (it.isSuccessful) { //Condición en caso de tener éxito
                                        showSatisfaction()
                                        showConfiguracion(it.result?.user?.email?: "", ProviderType.BASIC)
                                    } else { //Condición en caso de estar algo mal
                                        showAlert()
                                    }
                                }
                            } else{
                                txtCorreoUsuario.setError("Ingrese un correo válido")
                            }
                        } else { //Mensaje error para la cantidad mínima de caracteres en contraseña
                            txtPassword.setError("La contraseña debe tener mínimo 6 caracteres")
                        }
                    } else { //Mensaje error si está vacío el campo de la contraseña
                        txtPassword.setError("La contraseña no puede estar vacía")
                    }
                } else { //Mensaje error si está vacío el campo del correo
                    txtCorreoUsuario.setError("El correo no puede estar vacío")
                }
        }

        btnIniciarSesion.setOnClickListener { //Función botón para regresar a pantalla de configuración perfil
            val confPerf = Intent(this, ConfiguracionPerfil::class.java)
            startActivity(confPerf)
        }
    }

    //Poop up en caso de tener problemas al crear cuenta
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error") //Título del poop up
        builder.setMessage("Se ha producido un error autenticando al usuario") //Mensaje del Poop up
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Poop up en caso de tener éxito al crear cuenta
    private fun showSatisfaction() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso") //Título del poop up
        builder.setMessage("Usuario registrado con éxito") //Mensaje del Poop up
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showConfiguracion(email: String, provider: ProviderType) {
        val ConfiguracionIntent = Intent(this, ConfiguracionPerfil::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(ConfiguracionIntent)
    }
}