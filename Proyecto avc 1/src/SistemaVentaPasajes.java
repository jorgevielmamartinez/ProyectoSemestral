import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
public class SistemaVentaPasajes {
     ArrayList<Cliente> clientes=new ArrayList<>();
     ArrayList<Pasajero> pasajeros=new ArrayList<>();
     ArrayList<Bus>buses=new ArrayList<>();
     ArrayList<Viaje>viajes=new ArrayList<>();
     ArrayList<Venta>ventas=new ArrayList<>();
    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        for (Cliente c : clientes) {
            if (c.getIdPersona().equals(id)) {
                System.out.println(">>No se puede agregar un cliente con el mismo id<<");
                return false;
            }
        }
            Cliente cliente = new Cliente(id, nom, fono, email);
            clientes.add(cliente);
            System.out.println(">>Cliente agregado exitosamente<<");
            return true;
        }

    public boolean createPasajero(IdPersona id,Nombre nom,String fono,Nombre nomContacto,String fonoContacto){
      for(Pasajero p:pasajeros){
          if(p.getIdPersona().equals(id)){
              System.out.println(">>No se puede agregar un pasajero con el mismo id<<");
              return false;
          }
      }
        Pasajero pasajero=new Pasajero(id,nom,fono,nomContacto,fonoContacto);
        pasajeros.add(pasajero);
        System.out.println(">>Pasajero agreagado exitosamente<<");
        return true;
    }
    public boolean createBus(String patente,String marca,String modelo,String nroAsientos){
    for(Bus b:buses){
        if(b.getPatente().equals(patente)){
            System.out.println(">>No se puede agregar un bus con la misma patente<<");
            return false;
        }
      }
     Bus bus=new Bus(patente,marca,modelo,nroAsientos);
    buses.add(bus);
        System.out.println(">>Bus agregado exitosamente<<");
        return true;
    }
    public boolean createViaje(LocalDate fecha,LocalTime hora,int precio,String patBus){
      for(Viaje v:viajes){
          if(v.getFecha().equals(fecha) && v.getHora().equals(hora)){
              System.out.println(">>No se puede agregar un viaje con la misma fecha y hora<<");
              return false;
          }
      }
      Viaje viaje=new Viaje(fecha,hora,precio,patBus);
      viajes.add(viaje);
      System.out.println("<<Viaje agregado correctamente<<");
      return true;
    }
    public boolean iniciaVenta(String idDoc,TipoDocumento tipo,LocalDate fechaVenta,IdPersona idCliente){
     Cliente bandera=null;
        for (Cliente cliente:clientes){
         if(cliente.getIdPersona().equals(idCliente)){
             bandera=cliente;
             break;
         }
     }
        if (bandera==null){
            System.out.println(">>No se encontro al cliente<<");
            return false;
        }
        for (Venta v:ventas){
            if(v.getIdDocumento().equals(idDoc) ){
                System.out.println(">>No se puede iniciar la venta,ya existe una con este Id<<");
                return false;
            }
        }
     Venta venta=new Venta(idDoc,tipo,fechaVenta,bandera);
     ventas.add(venta);
        System.out.println("Venta iniciada exitosamente");
     return true;
    }
    public String[][] getHorariosDisponibles(LocalDate fechaViaje){

    }
    public String[][] listAsientosDeViaje(LocalDate fecha,LocalTime hora,String patBus){

    }
    public int getMontoVenta(String idDocumento,TipoDocumento tipo){

    }
    public String getNombrePasajero(IdPersona idPasajero){

    }
    public boolean vendePasaje(String idDoc,LocalDate fecha,LocalTime hora,String patBus,int asiento,idPasajero){

    }
    public String[][] listVentas(){

    }
    public String[][] listViajes(){

    }
    public String[][] listPasajeros(LocalDate fecha,LocalTime hora,String patBus){

    }
    private Cliente findCliente(IdPersona id){

    }
    private Venta findVenta(String idDocumento,TipoDocumento tipoDocumento){

    }
    private Bus findBus(String patente){

    }
    private Viaje findViaje(String fecha,String hora,String patenteBus){

    }
    private Pasajero findPasajero(IdPersona){

    }
}
