package modelo;/*
No se pueden añadir constantes, enumeraciones, atributos, relaciones,
constructores o métodos a las clases, ni tampoco agregar clases. Solo se permite agregar métodos privados,
si ello lleva a una mejora del código o se logra mayor reúso, simplicidad y/o legibilidad.
*/

import utilidades.IdPersona;
import utilidades.Nombre;

public class Persona {
    private IdPersona id;
    private Nombre nombreCompleto;
    private String telefono;

    // Constructor
    public Persona(IdPersona id, Nombre nombre, String telefono) {
        this.id = id;
        this.nombreCompleto = nombre;
        this.telefono = telefono;
    }

    // Getters y Setters
    public IdPersona getIdPersona() {
        return id;
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

    public String toString() {
        return  id +" "+ nombreCompleto +" "+ telefono;
    }

    public boolean equals(Object otro) {
        if (id.equals(((Persona) otro).id)) {
            return true;
        }
        return false;
    }
}