package Modelo;

import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.util.ArrayList;

public class Cliente extends Persona {

    private String email;

    private ArrayList<Venta> ventas = new ArrayList<>();

    public Cliente(IdPersona id, Nombre nom, String telefono) {

        super(id, nom, telefono);

        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addVenta(Venta venta) {

        if (!ventas.contains(venta)) {
            ventas.add(venta);
        }
    }

    public Venta[] getVentas() {
        return ventas.toArray(new Venta[0]);
    }
}