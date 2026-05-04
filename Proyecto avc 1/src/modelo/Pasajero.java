package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.ArrayList;

public class Pasajero extends Persona{
    private Nombre nomContacto;
    private String fonoContacto;
    ArrayList<Pasaje> pasajeList;
    public Pasajero(IdPersona id, Nombre nombre, String telefono, Nombre nomContacto, String fonoContacto) {
        super(id, nombre,telefono);
        this.nomContacto=nomContacto;
        this.fonoContacto=fonoContacto;
        this.pasajeList=new ArrayList<>();

    }

    public Nombre getNomContacto(){
        return nomContacto;
    }

    public void setNomContacto(Nombre nom){
        this.nomContacto = nom;
    }
    public String getFonoContacto(){
        return fonoContacto;
    }

    public void setFonoContacto(String fono){
        this.fonoContacto = fono;
    }
}
