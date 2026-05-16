package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.util.ArrayList;

public abstract class Tripulante extends Persona {
    Direccion direccion;
    public  Tripulante(IdPersona idPersona, Nombre nombre, Direccion direccion){
        super(idPersona,nombre, String.valueOf(direccion));
    }
    public Direccion getDireccion() {
        return direccion;
    }
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public abstract void addViaje(Viaje viaje);
    public abstract int getNroViajes();
}
