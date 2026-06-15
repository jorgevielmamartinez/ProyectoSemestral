package Modelo;

import java.util.ArrayList;
import java.util.List;
//Clase hecha por Jacob Mercado
public class Bus {
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;
    private List<Viaje> viajes;
    private Empresa empresa;

    // Constructor
    public Bus(String patente, int nroAsientos, Empresa empresa) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.empresa = empresa;
        empresa.addBus(this);
        this.viajes = new ArrayList<>();
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getNroAsientos() {
        return nroAsientos;
    }

    public void addViaje(Viaje viaje) {
       for (Viaje v : viajes) {
           if (viaje.getBus().getPatente().equals(v.getBus().getPatente())) {
          return;
           }
       }
       this.viajes.add(viaje);
    }
    public Empresa getEmpresa() {
        return empresa;
    }
    public Viaje[] getViajes() {
        return viajes.toArray(new Viaje[0]);
    }
}
