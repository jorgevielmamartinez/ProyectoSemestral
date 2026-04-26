import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class SistemaVentaPasajes {
    ArrayList<Cliente> clientes=new ArrayList<>();
    ArrayList<Pasajero> pasajeros=new ArrayList<>();
    ArrayList<Bus>buses=new ArrayList<>();
    ArrayList<Viaje>viajes=new ArrayList<>();
    ArrayList<Venta>ventas=new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public SistemaVentaPasajes() {
        this.clientes = new ArrayList<>();
        this.pasajeros = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.viajes = new ArrayList<>();
        this.buses = new ArrayList<>();
    }
    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        if ( findCliente(id)!=null ) {
            System.out.println(">>Ya existe cliente con esa id<<");
            return false;
        }
        Cliente cliente = new Cliente(id, nom, fono, email);
        clientes.add(cliente);
        System.out.println(">>Cliente agregado exitosamente<<");
        return true;
    }

    public boolean createPasajero(IdPersona id,Nombre nom,String fono,Nombre nomContacto,String fonoContacto){
        if(findPasajero(id)!=null){
            System.out.println(">>No se puede agregar un pasajero con el mismo id<<");
            return false;
        }
        Pasajero pasajero=new Pasajero(id,nom,fono,nomContacto,fonoContacto);
        pasajeros.add(pasajero);
        System.out.println(">>Pasajero agreagado exitosamente<<");
        return true;
    }
    public boolean createBus(String patente,String marca,String modelo,int nroAsientos){
        if(findBus(patente)!=null){
            System.out.println(">>Ya existe un bus con esa patente<<");
            return false;
        }
        Bus bus=new Bus(patente,nroAsientos);
        bus.setMarca(marca);
        bus.setModelo(modelo);
        buses.add(bus);
        System.out.println(">>Bus agregado exitosamente<<");
        return true;
    }
    public boolean createViaje(LocalDate fecha,LocalTime hora,int precio,String patBus){
        if (findViaje(fecha.toString(),hora.toString(),patBus)!=null){
            System.out.println(">>No se puede agregar un viaje con la misma fecha y hora<<");
            return false;
        }
        Bus bus=findBus(patBus);
        if(bus==null){
            System.out.println(">>Bus no existente<<");
            return false;
        }
        Viaje viaje=new Viaje(fecha,hora,precio,bus);
        viajes.add(viaje);
        System.out.println("<<Viaje agregado correctamente<<");
        return true;
    }
    public boolean iniciaVenta(String idDoc,TipoDocumento tipo,LocalDate fechaVenta,IdPersona idCliente){
        Cliente bandera=findCliente(idCliente);
        if (bandera==null){
            System.out.println(">>No se encontro al cliente<<");
            return false;
        }
        Venta venta=findVenta(idDoc,tipo);
        if( venta!=null){
            System.out.println(">>No se puede iniciar la venta,ya existe una con este Id<<");
            return false;
        }
        venta=new Venta(idDoc,tipo,fechaVenta,bandera);
        ventas.add(venta);
        System.out.println("Venta iniciada exitosamente");
        return true;
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {
        int contadorViajes = 0;
        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje)) {
                contadorViajes++;
            }
        }
        if (contadorViajes == 0) {
            return new String[0][0];
        }
        int llenadoMatriz = 0;
        String[][] viajesDisponibles = new String[contadorViajes][4];
        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje)) {
                viajesDisponibles[llenadoMatriz][0] = v.getBus().getPatente();
                viajesDisponibles[llenadoMatriz][1] = v.getHora().toString();
                viajesDisponibles[llenadoMatriz][2] = String.valueOf(v.getPrecio());
                viajesDisponibles[llenadoMatriz][3] = String.valueOf(v.getNroAsientosDisponibles());
                llenadoMatriz++;
            }
        }
        return viajesDisponibles;
    }

    public String[][] listAsientosDeViaje(LocalDate fecha,LocalTime hora,String patBus){
        Viaje v=findViaje(fecha.toString(),hora.toString(),patBus);
        if(v==null){
            return new String[0][0];
        }
        return v.getAsientos();
    }

    public int getMontoVenta(String idDocumento,TipoDocumento tipo){
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

    public boolean vendePasaje(String idDoc,TipoDocumento tipo,LocalDate fecha,LocalTime hora,String patBus,int asiento,IdPersona idPasajero){
        Venta venta=findVenta(idDoc,tipo);
        if (venta==null){
            return false;
        }
        Viaje viaje=findViaje(fecha.toString(),hora.toString(),patBus);
        if (viaje==null){
            return false;
        }
        Pasajero pasajero=findPasajero(idPasajero);
        if (pasajero==null){
            return false;
        }
        if(!viaje.existeDisponibilidad()){
            return false;
        }
        String[][]asientos=viaje.getAsientos();
        if (asientos[asiento-1][1].equals("Ocupado")){
            return false;
        }
        Pasaje pasaje=new Pasaje(asiento,viaje,pasajero,venta);
        viaje.addPasaje(pasaje);
        venta.createPasaje(asiento,viaje,pasajero);
        return true;
    }
    public String[][] listVentas(){
        if (ventas.size()==0){
            return new String[0][0];
        }
        String[][] lista =new String[ventas.size()][7];
        for (int i = 0; i < ventas.size(); i++) {
            Venta venta=ventas.get(i);
            lista[i][0] = venta.getIdDocumento();
            lista[i][1] = venta.getTipo().toString();
            lista[i][2]= venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            lista[i][3]= venta.getCliente().getIdPersona().toString();
            lista[i][4] = venta.getCliente().getNombreCompleto().toString();
            lista[i][5] = venta.getPasajes().toString();
            lista[i][6] = String.valueOf(venta.getMonto());
        }
        return lista;
    }
        public String[][] listViajes(){
            if (viajes.size()==0){
                return new String[0][0];
            }
            String[][]lista=new String[viajes.size()][4];
            int i;
            for (i=0;i<viajes.size();i++){
                Viaje viaje=viajes.get(i);
                lista[i][0]= String.valueOf(viaje.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
                lista[i][1]=String.valueOf(viaje.getHora());
                lista[i][2]= String.valueOf(viaje.getPrecio());
                lista[i][3]=String.valueOf(viaje.getNroAsientosDisponibles());
                lista[i][4]=viaje.getBus().getPatente();
            }
            return lista;
    }
    public String[][] listPasajeros(LocalDate fecha,LocalTime hora,String patBus){
        Viaje viaje=findViaje(fecha.toString(),hora.toString(),patBus);
        if (viaje==null){
            return new String[0][0];
        }
        String[][] lista=new String[viajes.size()][2];
        for (int i = 0; i < viajes.size(); i++) {
            lista[i][0] =viaje.getAsientos().toString();
            lista[i][1] =viaje.getListaPasajeros().toString();
        }
        return lista;
    }
    private Cliente findCliente(IdPersona id){
        for (Cliente c: clientes){
            if (c.getIdPersona().equals(id)){
                return c;
            }
        }
        return null;
    }
    private Venta findVenta(String idDocumento,TipoDocumento tipoDocumento){
        for (Venta v: ventas){
            if (v.getIdDocumento().equals(idDocumento)&& v.getTipo().equals(tipoDocumento)){
                return v;
            }
        }
        return null;
    }
    private Bus findBus(String patente){
        for (Bus b:buses){
            if (b.getPatente().equals(patente)){
                return b;
            }
        }
        return null;
    }
    private Viaje findViaje(String fecha,String hora,String patenteBus){
        for (Viaje v: viajes){
            if (v.getFecha().toString().equals(fecha) && v.getHora().toString().equals(hora) && v.getBus().getPatente().equals(patenteBus)){
                return v;
            }
        }
        return null;
    }
    private Pasajero findPasajero(IdPersona idPersona){
        for (Pasajero p: pasajeros){
            if (p.getIdPersona().equals(idPersona)){
                return p;
            }
        }
        return null;
    }

}