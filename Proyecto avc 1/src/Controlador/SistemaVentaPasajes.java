package Controlador;

import Modelo.*;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import Excepciones.SistemaVentaPasajesException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

//Trabajamos Benja Vivanco,Tellez y Jorge
public class SistemaVentaPasajes{
    private static SistemaVentaPasajes instance=new SistemaVentaPasajes();
    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Viaje> viajes = new ArrayList<>();
    ArrayList<Venta> ventas = new ArrayList<>();
    private ControladorEmpresas ctrlEmpresas=ControladorEmpresas.getInstance();
   private DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yy");
    public SistemaVentaPasajes() {
        this.clientes = new ArrayList<>();
        this.pasajeros = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.viajes = new ArrayList<>();
    }
    public static SistemaVentaPasajes getInstance(){
        return instance;
    }
    public void createCliente(IdPersona id, Nombre nom, String fono, String email) {
        Cliente cliente = new Cliente(id, nom,fono,email);

        if (findCliente(id).isEmpty()) {
            clientes.add(cliente);
        } else{
            throw new SistemaVentaPasajesException("Ya existe cliente con el id indicado");
        }
        }

    public boolean createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto){
          if(findPasajero(id)!=null){
              System.out.println(">>No se puede agregar un pasajero con el mismo id<<");
              return false;
          }
        Pasajero pasajero=new Pasajero(id,nom,fono,nomContacto,fonoContacto);
        pasajeros.add(pasajero);
        return true;
    }

    public boolean createViaje(LocalDate fecha,LocalTime hora,int precio,String patBus){
        if (findViaje(fecha.toString(),hora.toString(),patBus)!=null){
            System.out.println(">>No se puede agregar un viaje con la misma fecha y hora<<");
            return false;
        }
        Bus bus=findBus(patBus);
        if (bus==null){
            System.out.println(">>Modelo.Bus no existente<<");
            return false;
        }
      Viaje viaje=new Viaje(fecha,hora,precio);
      viajes.add(viaje);
      return true;
    }
    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta, IdPersona idCliente){
     Cliente bandera=findCliente(idCliente);
        if (bandera==null){
            System.out.println(">>No se encontro al cliente<<");
            return false;
        }
        Venta venta=findVenta(idDoc,tipo);
        if (venta!=null){
            System.out.println(">>No se puede iniciar la venta,ya existe una con este Id");
            return false;
        }
     venta=new Venta(idDoc,tipo,fechaVenta,bandera);
     ventas.add(venta);
     return true;
    }
    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {
        ArrayList<Viaje> encontrados = new ArrayList<>();
        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje)) {
                encontrados.add(v);
            }
        }
        String[][] matriz = new String[encontrados.size()][4];
        for (int i = 0; i < encontrados.size(); i++) {
            Viaje v = encontrados.get(i);
            matriz[i][0] = v.getBus().getPatente();
            matriz[i][1] = v.getHora().toString();
            matriz[i][2] = String.valueOf(v.getPrecio());
            matriz[i][3] = String.valueOf(v.getNroAsientosDisponibles());
        }
        return matriz;
    }
    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patente){
        for (Viaje v : viajes) {
            if (v.getFecha().equals(fecha) &&
                    v.getHora().equals(hora) &&
                    v.getBus().getPatente().equals(patente)) {

                return v.getAsientos();
            }
        }
        return new String[0][0];
    }
    public int getMontoVenta(String idDocumento, TipoDocumento tipo){
        for(Venta v:ventas){
            if(v.getIdDocumento().equals(idDocumento)&& v.getTipo().equals(tipo)){
                return v.getMonto();
            }
        }
        return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero){
       for (Pasajero p: pasajeros){
           if (p.getIdPersona().equals(idPasajero)){
               return String.valueOf(p.getNombreCompleto());
           }
       }
       return null;
    }
    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha, LocalTime hora, String patBus, int asiento, IdPersona idPasajero){
     Venta venta=findVenta(idDoc,tipo).orElseThrow(()->new SistemaVentaPasajesException("No existe una venta con el id y tipo de documento indicado"));

        Viaje viaje = findViaje("" + fecha, "" + hora, patBus).orElseThrow(() ->
                new SistemaVentaPasajesException("No existe viaje con la fecha, hora y patente del bus indicados"));
        Pasajero pasajero = findPasajero(idPasajero).orElseThrow(() ->
                new SistemaVentaPasajesException("No existe pasajero con el id indicado"));

        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, venta);
        //venta.addPasaje(pasaje);
        viaje.addPasaje(pasaje);
    }
    public String[][] listVentas(){
        String[][] lista = new String[ventas.size()][7];
        for (int i = 0; i < ventas.size(); i++) {
            Venta v = ventas.get(i);
            lista[i][0]=""+ventas.get(i).getIdDocumento();
            lista[i][1]=""+ventas.get(i).getTipo();
            lista[i][2]=""+ventas.get(i).getFecha();
            lista[i][3]=""+ventas.get(i).getCliente().getIdPersona();
            lista[i][4]=""+ventas.get(i).getCliente().getNombreCompleto();
            lista[i][5]=""+ventas.get(i).getPasajes().length;
            lista[i][6]=""+ventas.get(i).getMonto();
        }
        return lista;
    }
    public String[][] listViajes(){
      if (viajes.size()==0){
          return new String[0][0];
      }
      String[][]lista=new String[viajes.size()][8];
      int i;
      for (i=0;i<viajes.size();i++){
          Viaje v=viajes.get(i);
          lista[i][0]=""+viajes.get(i).getFecha();
          lista[i][1]=""+viajes.get(i).getHora();
          LocalDateTime horaLlegada = viajes.get(i).getFechaHoraTermino();
          lista[i][2]=String.format("%02d:%02d", horaLlegada.getHour(), horaLlegada.getMinute());
          lista[i][3]=""+viajes.get(i).getPrecio();
          lista[i][4]=""+viajes.get(i).getNroAsientosDisponibles();
          lista[i][5]=viajes.get(i).getBus().getPatente();
          lista[i][6]=viajes.get(i).getTerminalSalida().getDireccion().getComuna();
          lista[i][7]=viajes.get(i).getTerminalLlegada().getDireccion().getComuna();
      }
      return lista;
    }
    public String[][] listPasajeros(LocalDate fecha,LocalTime hora,String patente)throws SistemaVentaPasajesException {
        Viaje v = findViaje(fecha.toString(), hora.toString(), patente).orElseThrow(()->new SistemaVentaPasajesException("No existe viaje con la fecha,hora y patente");
        if (v == null) return new String[0][0];

        return v.getListaPasajeros();
    }
    private Optional<Cliente> findCliente(IdPersona id){
   for (Cliente c:clientes){
       if (c.getIdPersona().equals(id)){
           return Optional.of(c);
       }
   }
   return Optional.empty();
    }
    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento){
     for (Venta v:ventas){
         if (v.getIdDocumento().equals(idDocumento)&&v.getTipo().equals(tipoDocumento)){
             return Optional.of(v);
         }
     }
     return Optional.empty();
    }

    private Optional<Viaje> findViaje(String fecha, String hora, String patenteBus){
    for (Viaje v:viajes){
        if (v.getFecha().equals(fecha)&& v.getHora().equals(hora)&& v.getBus().getPatente().equals(patenteBus)){
            return Optional.of(v);
        }
    }
    return Optional.empty();
    }
    private Optional<Pasajero> findPasajero(IdPersona idPersona){
        for (Pasajero p:pasajeros){
            if (p.getIdPersona().equals(idPersona)){
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
