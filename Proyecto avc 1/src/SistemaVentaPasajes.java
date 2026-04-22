import java.util.ArrayList;

public class SistemaVentaPasajes {
    ArrayList<Cliente> clientes;
    ArrayList<Pasajero> pasajeros;
    ArrayList<Venta> ventas;
    ArrayList<Viaje> viajes;
    ArrayList<Bus>  buses;
    public SistemaVentaPasajes() {
        this.clientes = new ArrayList<>();
        this.pasajeros = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.viajes = new ArrayList<>();
        this.buses = new ArrayList<>();
    }
    public boolean createCliente(IdPersona id,Nombre nom, String fono,String email) {
       for (Cliente c : clientes) {
           if (c.getIdPersona().equals(id)) {
               return false;
           }
       }
        Cliente cliente=new Cliente(id,nom,fono,email);
       clientes.add(cliente);
       return true;
    }
    public boolean createPasajero(IdPersona id,Nombre nom, String fono,Nombre nomContacto,String fonoContacto){
        for (Pasajero p : pasajeros) {
            if (p.getIdPersona().equals(id)) {
                return false;
            }
        }
        Pasajero pasajero=new Pasajero(id,nom,fono,nomContacto,fonoContacto);
        pasajeros.add(pasajero);
        return true;
    }
}
