package Controlador;

import Modelo.*;
import Persistencia.IOSVP;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import Excepciones.SVPException;
import Utilidades.Rut;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Optional;

import java.io.FileNotFoundException;

//Trabajamos Benja Vivanco,Tellez y Jorge
public class SistemaVentaPasajes implements Serializable {
    private static SistemaVentaPasajes instance=new SistemaVentaPasajes();
    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Viaje> viajes = new ArrayList<>();
    ArrayList<Venta> ventas = new ArrayList<>();
    private ControladorEmpresas ctrlEmpresas=ControladorEmpresas.getInstance();
    public SistemaVentaPasajes() {
        this.clientes = new ArrayList<>();
        this.pasajeros = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.viajes = new ArrayList<>();
    }
    public static SistemaVentaPasajes getInstance(){
        return instance;
    }
    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SVPException {
        Cliente cliente = new Cliente(id, nom, fono, email);

        if (findCliente(id).isEmpty()) {
            clientes.add(cliente);
        } else{
            throw new SVPException("Ya existe cliente con el id indicado");
        }
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto)throws SVPException {

        Pasajero pasajero = new Pasajero(id, nom, fono, nomContacto, fonoContacto);
        if (findPasajero(id).isEmpty()) {
            pasajeros.add(pasajero);
        }else {
            throw new SVPException("Ya existe pasajero con el id indicado");
        }
    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion,  String patBus, IdPersona[] idTripulantes, String[] comunas) throws SVPException {
        Optional<Viaje> viajeOptional = findViaje(fecha, hora, patBus);
        if (viajeOptional.isPresent()) {
            throw new SVPException("Ya existe viaje con fecha, hora y patente indicados");
        }
        Optional<Bus> busOptional = ctrlEmpresas.findBus(patBus);

        Bus bus = busOptional.orElseThrow(() ->
                new SVPException("No existe un bus con la patente indicada"));
        String rutEmpresa = bus.getEmpresa().getRut() + "";
        Auxiliar auxiliar = ctrlEmpresas.findAuxliar(idTripulantes[0], Rut.of(rutEmpresa))
                .orElseThrow(() -> new SVPException("No existe Auxiliar con el id indicado en la empresa con el rut indicado"));
        Conductor[] conductores = new Conductor[idTripulantes.length - 1];
        for (int i = 1; i < idTripulantes.length; i++) {
            conductores[i - 1] = ctrlEmpresas.findConductor(idTripulantes[i], Rut.of(rutEmpresa))
                    .orElseThrow(() -> new SVPException("No existe conductor con el id indicado en la empresa con el rut indicado"));
        }
        Terminal salida  = ctrlEmpresas.findTerminalPorComuna(comunas[0]).orElseThrow(() -> new SVPException("No existe terminal de salida en la comuna indicada"));
        Terminal llegada = ctrlEmpresas.findTerminalPorComuna(comunas[1]).orElseThrow(() -> new SVPException("No existe terminal de llegada en la comuna indicada"));

        Viaje viaje = new Viaje(
                fecha,
                hora,
                precio,
                duracion,
                bus,
                auxiliar,
                conductores,
                salida,
                llegada
        );
        viajes.add(viaje);
    }


    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaViaje,
                            String comSalida, String comLlegada, IdPersona idCliente,
                            int nroPasajes) throws SVPException {

        if (findVenta(idDoc, tipo).isPresent()) {
            throw new SVPException("Ya existe venta con el id y tipo de documento indicados");
        }

        Cliente cliente = findCliente(idCliente)
                .orElseThrow(() -> new SVPException("No existe cliente con id indicado"));

        ArrayList<Viaje> viajesDisponibles = new ArrayList<>();

        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje)
                    && v.getTerminalSalida().getDireccion().getComuna().equalsIgnoreCase(comSalida)
                    && v.getTerminalLlegada().getDireccion().getComuna().equalsIgnoreCase(comLlegada)) {
                viajesDisponibles.add(v);
            }
        }

        if (viajesDisponibles.isEmpty()) {
            throw new SVPException("No existen viajes disponibles en la fecha y con terminales en las comunas de salida y llegada indicados");
        }

        for (Viaje viaje : viajesDisponibles) {
            if (viaje.existeDisponibilidad(nroPasajes)) {
                Venta venta = new Venta(idDoc, tipo, LocalDate.now(), cliente);
                ventas.add(venta);
                return;
            }
        }

        throw new SVPException("No hay asientos disponibles suficientes para la cantidad de pasajes solicitada");
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

    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha,
                            LocalTime hora, String patBus, int asiento,
                            IdPersona idPasajero) throws SVPException {

        Venta venta = findVenta(idDoc, tipo)
                .orElseThrow(() -> new SVPException("No existe venta con el id y tipo de documento indicados"));

        Viaje viaje = findViaje(fecha, hora, patBus)
                .orElseThrow(() -> new SVPException("No existe viaje con la fecha, hora y patente de bus indicados"));

        Pasajero pasajero = findPasajero(idPasajero)
                .orElseThrow(() -> new SVPException("No existe pasajero con el id indicado"));

        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, venta);

        venta.addPasaje(pasaje);
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo) throws SVPException {
        Venta venta = findVenta(idDocumento, tipo)
                .orElseThrow(() -> new SVPException("No existe venta con el id y tipo de documento indicados"));

        Pago pago = new PagoEfectivo(venta.getMonto());
        venta.setPago(pago);
    }

    public void pagaVenta(String idDocumento,
                          TipoDocumento tipo,
                          long nroTarjeta) {

        for (Venta v : ventas) {

            if (v.getIdDocumento().equals(idDocumento)
                    && v.getTipo().equals(tipo)) {

                Pago pago = new PagoTarjeta(v.getMonto(), nroTarjeta);

                return;
            }
        }
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

    public String getNombrePasajero(IdPersona idPasajero){
        for (Pasajero p: pasajeros){
            if (p.getIdPersona().equals(idPasajero)){
                return String.valueOf(p.getNombreCompleto());
            }
        }
        return "";
    }



    public String[][] listPasajeros(LocalDate fecha,LocalTime hora,String patente)throws SVPException {
        Viaje v = findViaje(fecha, hora, patente)
                .orElseThrow(() -> new SVPException("No existe viaje con la fecha, hora y patente"));

        return v.getListaPasajeros();
    }


    public String getNombreCliente(IdPersona idPersona) {

        for (Cliente c : clientes) {

            if (c.getIdPersona().equals(idPersona)) {
                return c.getNombreCompleto().toString();
            }
        }

        return null;
    }

    public String[][] listPasajerosViaje(LocalDate fecha,
                                         LocalTime hora,
                                         String patenteBus) {

        Optional<Viaje> viajeOptional =
                findViaje(fecha, hora, patenteBus);

        if (viajeOptional.isPresent()) {

            Viaje viaje = viajeOptional.get();

            return viaje.getListaPasajeros();
        }

        return new String[0][0];
    }

    public void generatePasajesVenta(String idDocumento, TipoDocumento tipo) throws SVPException {
        Venta venta = findVenta(idDocumento, tipo)
                .orElseThrow(() -> new SVPException("No existe venta con el id y tipo de documento indicados"));

        String nombreArchivo = idDocumento + tipo.toString().toLowerCase() + ".txt";

        try {
            IOSVP.getInstance().savePasajesDeVenta(venta.getPasajes(), nombreArchivo);
        } catch (FileNotFoundException e) {
            throw new SVPException("No se puede abrir o crear el archivo " + nombreArchivo);
        }
    }



    public void readDatosIniciales() throws SVPException {
        try {
            Object[] datos = IOSVP.getInstance().readDatosIniciales();

            for (Object obj : datos) {
                if (obj instanceof Cliente cliente) {
                    if (findCliente(cliente.getIdPersona()).isEmpty()) {
                        clientes.add(cliente);
                    }
                }

                if (obj instanceof Pasajero pasajero) {
                    if (findPasajero(pasajero.getIdPersona()).isEmpty()) {
                        pasajeros.add(pasajero);
                    }
                }

                if (obj instanceof Viaje viaje) {
                    if (findViaje(viaje.getFecha(), viaje.getHora(), viaje.getBus().getPatente()).isEmpty()) {
                        viajes.add(viaje);
                    }
                }
            }

            ctrlEmpresas.setDatosIniciales(datos);

        } catch (FileNotFoundException e) {
            throw new SVPException("No existe o no se puede abrir el archivo SVPDatosIniciales.txt");
        }
    }

    public void saveDatosSistema() throws SVPException {
        IOSVP.getInstance().saveControladores(new Object[]{this, ctrlEmpresas});
    }


    public void readDatosSistema() throws SVPException {
        Object[] controladores = IOSVP.getInstance().readControladores();

        for (Object controlador : controladores) {
            if (controlador instanceof SistemaVentaPasajes svp) {
                this.ventas = svp.ventas;
                this.clientes = svp.clientes;
                this.viajes = svp.viajes;
                this.pasajeros = svp.pasajeros;
            }

            if (controlador instanceof ControladorEmpresas ce) {
                ctrlEmpresas.setInstanciPersistente(ce);
            }
        }
    }

    private Optional<Cliente> findCliente(IdPersona id) {
        return clientes.stream()
                .filter(c -> c.getIdPersona().equals(id))
                .findFirst();
    }


    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        return ventas.stream()
                .filter(v -> v.getIdDocumento().equals(idDocumento)
                        && v.getTipo().equals(tipoDocumento))
                .findFirst();
    }


    private Optional<Viaje> findViaje(LocalDate fecha, LocalTime hora, String patenteBus) {
        return viajes.stream()
                .filter(v -> v.getFecha().equals(fecha)
                        && v.getHora().equals(hora)
                        && v.getBus().getPatente().equals(patenteBus))
                .findFirst();
    }

    private Optional<Pasajero> findPasajero(IdPersona idPersona) {
        return pasajeros.stream()
                .filter(p -> p.getIdPersona().equals(idPersona))
                .findFirst();
    }

    /*
    =========
    Metodos extra
    ========
    */



    public String[] pasajesAImprimir(String idDocumento, TipoDocumento tipoDocumento) {

        ArrayList<String> lista = new ArrayList<>();

        for (Venta v : ventas) {

            if (v.getIdDocumento().equals(idDocumento)
                    && v.getTipo().equals(tipoDocumento)) {

                lista.add("===== BOLETA =====");

                for (Pasaje p : v.getPasajes()) {

                    lista.add("Asiento: " + p.getAsiento());

                    lista.add("Bus: " +
                            p.getViaje().getBus().getPatente());

                    lista.add("Hora: " +
                            p.getViaje().getHora());

                    lista.add("Precio: $" +
                            p.getViaje().getPrecio());
                }

                return lista.toArray(new String[0]);
            }
        }

        return new String[0];
    }

    public String[][] getHorariosDisponibles(LocalDate fechaV,
                                             String origen,
                                             String destino,
                                             int cant) {

        ArrayList<Viaje> encontrados = new ArrayList<>();

        for (Viaje v : viajes) {

            boolean mismaFecha =
                    v.getFecha().equals(fechaV);

            boolean mismoOrigen =
                    v.getTerminalSalida()
                            .getDireccion()
                            .getComuna()
                            .equalsIgnoreCase(origen);

            boolean mismoDestino =
                    v.getTerminalLlegada()
                            .getDireccion()
                            .getComuna()
                            .equalsIgnoreCase(destino);

            boolean disponible =
                    v.existeDisponibilidad(cant);

            if (mismaFecha
                    && mismoOrigen
                    && mismoDestino
                    && disponible) {

                encontrados.add(v);
            }
        }

        String[][] matriz =
                new String[encontrados.size()][4];

        for (int i = 0; i < encontrados.size(); i++) {

            Viaje v = encontrados.get(i);

            matriz[i][0] =
                    v.getBus().getPatente();

            matriz[i][1] =
                    v.getHora().toString();

            matriz[i][2] =
                    String.valueOf(v.getPrecio());

            matriz[i][3] =
                    String.valueOf(
                            v.getNroAsientosDisponibles()
                    );
        }

        return matriz;
    }
    //metodo para la tabla gui
    public String[][] buscarViajes(java.time.LocalDate fecha, String origen, String destino, int cantidadAsientos) {
        return new String[0][0];
    }
}