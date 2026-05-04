import java.util.ArrayList;

public class Tripulante extends Persona{
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
    ArrayList<Viaje>viajesTripulante;

    public void addViaje(Viaje viaje){
        viajesTripulante.add(viaje);
    }
    public int getNroViajes(){
      return viajesTripulante.size();
    }
}
