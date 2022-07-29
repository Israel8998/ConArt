package com.example.conart

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ingreso_datos.*
import kotlinx.android.synthetic.main.cuadro_dialogo.view.*
import java.text.SimpleDateFormat
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

        //Mantener la sesión iniciada
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()

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

        //función del botón para colocar ingresos
        btnIngresos.setOnClickListener {
            //Asignando valores
            val ventana = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.cuadro_dialogo, null)
            
            //pasando vista al builder
            ventana.setView(view)

            //creando dialog
            val dialog = ventana.create()
            dialog.show()

            //Guardar datos en base de datos al dar click en guardar
            view.btnGuardarDA.setOnClickListener {
                if (email != null) {
                    if(view.txtIngresoValorDA.text.isNotEmpty()) {
                        db.collection("Ingresos").document(email).set(
                            hashMapOf(
                                "Fecha" to Timestamp(Date()),
                                "Valor" to view.txtIngresoValorDA.text.toString(),
                                "Descripción" to view.txtDescripcionDA.text.toString()
                            )
                        )
                    } else {
                        view.txtIngresoValorDA.setError("Llenar este campo para poder guardar")
                    }
                }
                dialog.hide()
                Toast.makeText(this, "Dato Ingreso guardado", Toast.LENGTH_LONG).show()
            }

            //Acción al dar click en cancelar
            view.btnCancelarDA.setOnClickListener {
                dialog.hide()
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            }
        }

        //función del botón para colocar egresos
        btnEgresos.setOnClickListener {
            //Asignando valores
            val ventana = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.cuadro_dialogo, null)

            //pasando vista al builder
            ventana.setView(view)

            //creando dialog
            val dialog = ventana.create()
            dialog.show()

            //Guardar datos en base de datos
            view.btnGuardarDA.setOnClickListener {
                if(view.txtIngresoValorDA.text.isNotEmpty()) {
                    if (email != null) {
                        db.collection("Egresos").document(email).set(
                            hashMapOf(
                                "Fecha" to Timestamp(Date()),
                                "Valor" to view.txtIngresoValorDA.text.toString(),
                                "Descripción" to view.txtDescripcionDA.text.toString()
                            )
                        )
                    }
                } else {
                    view.txtIngresoValorDA.setError("Llenar este campo para poder guardar")
                }
                dialog.hide()
                Toast.makeText(this, "Dato Egreso guardado", Toast.LENGTH_LONG).show()
            }

            //Acción al dar click en cancelar
            view.btnCancelarDA.setOnClickListener {
                dialog.hide()
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            }
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