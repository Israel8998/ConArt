package com.example.conart

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_crear_cuenta.*
import kotlinx.android.synthetic.main.activity_iniciar_sesion.*

class IniciarSesion : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        setup()
        session()
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        if(email != null && provider != null){
            showIngresoDatos(email, ProviderType.valueOf(provider))
        }
    }

    //Inicio de la función de la interfaz
    private fun setup() {
        title = "Iniciar sesión" //Título de la pantalla

        //Función del botón para ir a la ventana registrar
        aRegistrar.setOnClickListener {
            val intent = Intent(this, CrearCuenta::class.java)
            startActivity(intent)
        }

        //Función del botón para validar cuenta e iniciar sesión
        btnIngresar.setOnClickListener {
            if (txtCorreo.text.isNotEmpty()) { //Comprobar que no esté vacío el texto del correo
                if(txtContraseña.text.isNotEmpty()) { //Comprobar que no esté vacío el texto de la contraseña
                    if (txtContraseña.length() >= 6) { //Comprobar que tenga el mínimo de caracteres en contraseña
                        FirebaseAuth.getInstance().signInWithEmailAndPassword( //Función para validación de la BDD
                            txtCorreo.text.toString(), txtContraseña.text.toString() //Envío de valores
                            ).addOnCompleteListener {
                            if (it.isSuccessful) { //Condición en caso de tener éxito
                                showIngresoDatos(it.result?.user?.email?:"", ProviderType.BASIC)
                            } else { //Condición en caso de estar algo mal
                                showAlert()
                            }
                        }
                    } else { //Mensaje error para la cantidad mínima de caracteres en contraseña
                        txtContraseña.setError("La contraseña debe tener mínimo 6 caracteres")
                    }
                } else { //Mensaje error si está vacío el campo de la contraseña
                    txtContraseña.setError("La contraseña no puede estar vacía")
                }
            } else { //Mensaje error si está vacío el campo del correo
                txtCorreo.setError("El correo no puede estar vacío")
            }
        }

        btnGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
            requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    //Creación de poop up en caso de existir error al iniciar sesión
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error") //Título del poop up del error
        builder.setMessage("Correo o contraseña incorrectos") //Descripción del error
        builder.setPositiveButton("Aceptar", null) //botón para cerra el poop up
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Envío a otra pantalla en caso de tener éxito al iniciar sesión
    private fun showIngresoDatos(email: String, provider: ProviderType) {
        val IngresoDatosIntent = Intent(this, IngresoDatos::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(IngresoDatosIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

            try {
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) { //Condición en caso de tener éxito
                                showIngresoDatos(account.email ?: "", ProviderType.GOOGLE)
                            } else {
                                showAlert()
                            }
                        }
                }
            } catch(e: ApiException){
                showAlert()
            }
        }
    }
}