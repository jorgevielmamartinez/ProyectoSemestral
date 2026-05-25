package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import java.util.ArrayList;
public class Auxiliar extends Tripulante{
    public Auxiliar(IdPersona id, Nombre nombre, Direccion direccion){
        super(id,nombre,direccion);
    }
    ArrayList<Viaje> viajesArrayAuxiliar = new ArrayList<>();
    @Override
    public void addViaje(Viaje viaje) {
        viajesArrayAuxiliar.add(viaje);
    }
    @Override
    public int getNroViajes() {
        return viajesArrayAuxiliar.size();
    }
}
