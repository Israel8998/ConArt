package com.example.conart.modelo;

public class Movimiento {
    String Descripción, Fecha, Valor;
    public Movimiento() {}

    public Movimiento(String descripción, String fecha, String valor) {
        Descripción = descripción;
        Fecha = fecha;
        Valor = valor;
    }

    public String getDescripción() {
        return Descripción;
    }

    public void setDescripción(String descripción) {
        Descripción = descripción;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }
}
