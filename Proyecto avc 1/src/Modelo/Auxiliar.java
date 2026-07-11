package Modelo;

import Utilidades.Direccion;
import Utilidades.*;
import Utilidades.Nombre;

import java.io.Serializable;
import java.util.ArrayList;
//hecho por benjamín vivanco
public class Auxiliar extends Tripulante implements Serializable {
    private ArrayList<Viaje> auxiliarViajes;

    public Auxiliar(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
        this.auxiliarViajes = new ArrayList<>();
    }

    @Override
    public void addViaje(Viaje viaje){
        auxiliarViajes.add(viaje);
    }

    @Override
    public int getNroViajes(){
        return auxiliarViajes.size();
    }
}