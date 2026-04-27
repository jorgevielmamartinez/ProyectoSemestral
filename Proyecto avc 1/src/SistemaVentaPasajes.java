import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//Trabajamos Benja Vivanco, Téllez y Jorge
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
        return true;
    }

    public boolean createPasajero(IdPersona id,Nombre nom,String fono,Nombre nomContacto,String fonoContacto){
        if(findPasajero(id)!=null){
            System.out.println(">>No se puede agregar un pasajero con el mismo id<<");
            return false;
        }
        Pasajero pasajero=new Pasajero(id,nom,fono,nomContacto,fonoContacto);
        pasajeros.add(pasajero);
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

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patente) {
        for (Viaje v : viajes) {
            if (v.getFecha().equals(fecha) && v.getHora().equals(hora) && v.getBus().getPatente().equals(patente)) {
                String[][] asientosMatriz = v.getAsientos();
                String[] resultado = new String[asientosMatriz.length];
                for (int i = 0; i < asientosMatriz.length; i++) {
                    resultado[i] = asientosMatriz[i][1];
                }
                return resultado;
            }
        }
        return new String[0];
    }

    public String[][] listViajes() {
        String[][] lista = new String[viajes.size()][6];
        for (int i = 0; i < viajes.size(); i++) {
            Viaje v = viajes.get(i);
            lista[i][0] = v.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            lista[i][1] = v.getHora().toString();
            lista[i][2] = v.getBus().getPatente();
            lista[i][3] = String.valueOf(v.getBus().getNroAsientos());
            lista[i][4] = String.valueOf(v.getNroAsientosOcupados());
            lista[i][5] = String.valueOf(v.getNroAsientosDisponibles());
        }
        return lista;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patente) {
        Viaje v = findViaje(fecha.toString(), hora.toString(), patente);
        if (v == null) return new String[0][0];

        return v.getListaPasajeros();
    }


    public String[][] listVentas() {
        String[][] lista = new String[ventas.size()][4];
        for (int i = 0; i < ventas.size(); i++) {
            Venta v = ventas.get(i);
            lista[i][0] = v.getIdDocumento();
            lista[i][1] = v.getCliente().getNombreCompleto().toString();
            lista[i][2] = String.valueOf(v.getTotalVenta());
            lista[i][3] = v.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return lista;
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