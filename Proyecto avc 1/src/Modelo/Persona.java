package Modelo;

import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.io.Serializable;

public abstract class Persona implements Serializable {

    private IdPersona idPersona;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(IdPersona id, Nombre nombre, String telefono) {
        this.idPersona = id;
        this.nombreCompleto = nombre;
        this.telefono = telefono;
    }

    public IdPersona getIdPersona() {
        return idPersona;
    }

    public Nombre getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(Nombre nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return idPersona + " " + nombreCompleto + " " + telefono;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }

        if (!(otro instanceof Persona persona)) {
            return false;
        }

        return idPersona.equals(persona.idPersona);
    }
}