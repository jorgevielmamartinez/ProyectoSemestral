package Modelo;

import Utilidades.Direccion;

import java.io.Serializable;
import java.util.ArrayList;

public class Terminal implements Serializable {
    private Direccion direccion;
    private String nombre;
    ArrayList<Viaje>Llegadas=new ArrayList();
    public Terminal(Direccion direccion) {}
    ArrayList<Viaje>salidas=new ArrayList<>();
    public Terminal(Direccion direccion, String nombre) {
        this.direccion = direccion;
        this.nombre = nombre;
        this.Llegadas=new ArrayList();
        this.salidas=new ArrayList();
    }
    public String getNombre(){
        return nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    public void addLlegada(Viaje viaje){
      this.Llegadas.add(viaje);
    }
    public void addSalida(Viaje viaje){
      this.salidas.add(viaje);
    }
    public Viaje[] getLlegadas(){
      return Llegadas.toArray(new Viaje[0]);
    }
    public Viaje[] getSalidas(){
      return salidas.toArray(new Viaje[0]);
    }
}
