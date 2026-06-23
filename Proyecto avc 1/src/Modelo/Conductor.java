package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.io.Serializable;
import java.util.ArrayList;
public class Conductor extends Tripulante implements Serializable {
    public Conductor(IdPersona id, Nombre nombre, Direccion direccion){
        super(id,nombre,direccion);
    }
    ArrayList<Viaje>viajesArrayConductor=new ArrayList<>();
    @Override
    public void addViaje(Viaje viaje) {
        viajesArrayConductor.add(viaje);
    }

    @Override
    public int getNroViajes() {
        return viajesArrayConductor.size();
    }
}
