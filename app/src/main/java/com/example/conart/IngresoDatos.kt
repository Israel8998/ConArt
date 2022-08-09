package com.example.conart

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ingreso_datos.*
import kotlinx.android.synthetic.main.cuadro_dialogo.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime.now
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
                lblBienvenida.setText("Bienvenido/a: " + it.get("Nombre") as String? + " " + it.get("Apellido") as String?)
            }
        }

        //Al aplastar el botón ir a pantalla de ver Datos
        btnVerDatos.setOnClickListener {
            val showDatos = Intent(this, ObtenerDatos::class.java).apply{
                putExtra("email", email)
            }
            startActivity(showDatos)
        }

        //Cargar la fecha actual
        lblCalendario.text = SimpleDateFormat("yyyy/MM/dd").format(Date())

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
                        db.collection("Movimientos").document(email).collection("Libro diario").document(now().toString()).set(
                            hashMapOf(
                                    "Fecha" to SimpleDateFormat("yyyy/MM/dd").format(Date()).toString(),
                                    "Valor" to view.txtIngresoValorDA.text.toString().toFloat(),
                                    "Descripción" to view.txtDescripcionDA.text.toString()
                            )
                        )
                        dialog.hide()
                        Toast.makeText(this, "Ingreso guardado", Toast.LENGTH_LONG).show()
                    } else {
                        dialog.hide()
                        Toast.makeText(this, "El valor no puede estar vacío", Toast.LENGTH_LONG).show()
                    }
                }
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
                if (email != null) {
                    if(view.txtIngresoValorDA.text.isNotEmpty()) {
                        db.collection("Movimientos").document(email).collection("Libro diario").document(now().toString()).set(
                            hashMapOf(
                                "Fecha" to SimpleDateFormat("yyyy/MM/dd").format(Date()).toString(),
                                "Valor" to view.txtIngresoValorDA.text.toString().toFloat() * -1,
                                "Descripción" to view.txtDescripcionDA.text.toString()
                            )
                        )
                        dialog.hide()
                        Toast.makeText(this, "Egreso guardado", Toast.LENGTH_LONG).show()
                    } else {
                        dialog.hide()
                        Toast.makeText(this, "El valor no puede estar vacío", Toast.LENGTH_LONG).show()
                    }
                }
            }

            //Acción al dar click en cancelar
            view.btnCancelarDA.setOnClickListener {
                dialog.hide()
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            }
        }

        //Cargar valor total ingresado
        var sumaTotal: Float = 0F //Crear variable acumulativa
        if (email != null) {
            db.collection("Movimientos").document(email).collection("Libro diario")
                .get().addOnSuccessListener { //búsqueda para calcular
                for (document in it) { //recorrer todos los documentos de la tabla "libro darios"
                    var valorTotal = document.get("Valor") //Almacenar variable deceada
                    sumaTotal += valorTotal.toString().toFloat() //Sumar variable traida de la base de datos
                }
                lblTotal.setText(String.format("%.2f", sumaTotal)) //Mostrar el resultado en el label
            }
        }

        //Cargar valor total diario ingresado
        var sumaDiario: Float = 0F //Crear variable acumulativa
        if (email != null) {
            db.collection("Movimientos").document(email).collection("Libro diario")
                .whereEqualTo("Fecha", lblCalendario.text).get().addOnSuccessListener { //búsqueda para calcular
                for (document in it) { //recorrer todos los documentos de la tabla "libro darios"
                    var valorDiario = document.get("Valor") //Almacenar variable deceada
                    sumaDiario += valorDiario.toString().toFloat() //Sumar variable traida de la base de datos
                }
                lblTotalDiario.setText(String.format("%.2f", sumaDiario)) //Mostrar el resultado en el label
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