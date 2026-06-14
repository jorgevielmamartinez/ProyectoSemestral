package Modelo;

import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.util.ArrayList;

public class Cliente extends Persona {
    private String email;
    ArrayList<Venta> ventas = new ArrayList();
    public Cliente(IdPersona id, Nombre nom, String telefono, String email){
        super(id,nom,telefono);
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void addVenta(Venta venta){
        for (Venta v:ventas){
            if(v.getCliente().equals(getIdPersona())){
                addVenta(v);
            }
        }
    }
    public Venta[] getVentas() {
        return ventas.toArray(new Venta[0]);
    }
}
