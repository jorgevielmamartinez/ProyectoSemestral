package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import Utilidades.Rut;

import java.util.ArrayList;

public class Empresa {
    private Rut rut;
    private String nombre;
    private String url;
    ArrayList<Bus>buses=new ArrayList();
    ArrayList<Conductor>conductores=new ArrayList();
    ArrayList<Auxiliar>auxiliares=new ArrayList();
    public Empresa(Rut rut, String nombre, String url) {
        this.rut = rut;
        this.nombre = nombre;
        this.url = url;
        this.buses=new ArrayList();
        this.conductores=new ArrayList();
        this.auxiliares=new ArrayList();
    }

    public Rut getRut(){
        return this.rut;
    }
    public String getNombre(){
        return nombre;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void addBus(Bus bus){
        this.buses.add(bus);
    }
    public Bus[] getBuses(){
        return this.buses.toArray(new Bus[0]);
    }
    public boolean addConductor(IdPersona id,Nombre nombre,Direccion direccion){
        for (Conductor c:conductores){
            if (c.getIdPersona().equals(id)){
                return false;
            }
        }
        for (Auxiliar a:auxiliares){
          if (a.getIdPersona().equals(id)){
              return false;
          }
        }
        Conductor conductor=new Conductor(id,nombre,direccion);
        conductores.add(conductor);
        return true;
    }
    public boolean addAuxiliar(IdPersona idPersona, Nombre nombre, Direccion direccion){
         for (Auxiliar a:auxiliares){
             if(a.getIdPersona().equals(idPersona)){
                 return false;
             }
         }
         for(Conductor c:conductores){
           if(c.getIdPersona().equals(idPersona)){
               return false;
           }
         }
         Auxiliar auxiliar=new Auxiliar(idPersona,nombre,direccion);
         auxiliares.add(auxiliar);
         return true;
    }
    ArrayList<Tripulante>tripulantes=new ArrayList<>();
    public Tripulante [] getTripulantes(){
       for (Conductor c:conductores){
           if (c instanceof Conductor){
               tripulantes.add((Tripulante) c);
           }
       }
       for(Auxiliar a:auxiliares){
           if(a instanceof Auxiliar){
               tripulantes.add((Tripulante) a);
           }
       }
       return tripulantes.toArray(new Tripulante[0]);
    }
    public Venta[] getVentas(){
        ArrayList<Venta>ventasEmpresa=new ArrayList<>();
        for (Bus b:buses){
            for(Viaje v:b.getViajes()){
                for (Venta ven:v.getVentas()){
                    ventasEmpresa.add(ven);
                }
            }
        }
        return ventasEmpresa.toArray(new Venta[0]);
    }
}
