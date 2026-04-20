import java.time.LocalDate;
import java.time.LocalTime;
//clase hecha por Jorge Vielma
public class SistemaVentaPasajes {
    public boolean createCliente(IdPersona id,Nombre nom,String fono,String email){

    }
    public boolean createPasajero(IdPersona id,Nombre nom,String fono,Nombre nomContacto,String fonoContacto){

    }
    public boolean createBus(String patente,String marca,String modelo,String nroAsientos){

    }
    public boolean createViaje(LocalDate fecha,LocalTime hora,int precio,String patBus){

    }
    public boolean iniciaVenta(String idDoc,TipoDocumento tipo,LocalDate fechaVenta,IdPersona idCliente){

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
