package Persistencia;

import Controlador.ControladorEmpresas;
import Controlador.SistemaVentaPasajes;
import Modelo.*;
import Excepciones.*;
import Utilidades.IdPersona;
import Utilidades.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Hecha totalmente por benja Vivanco
public class IOSVP {
    private static IOSVP instance;

    private IOSVP() {
        this.empresas = new ArrayList<>();
        this.tripulantes = new ArrayList<>();
    }

    public static IOSVP getInstance() {
        if (instance == null) {
            instance = new IOSVP();
        }
        return instance;
    }

    private List<Empresa> empresas = new ArrayList<>();
    private List<Tripulante> tripulantes = new ArrayList<>();
    private List<Bus> buses = new ArrayList<>();
    private List<Terminal> terminales = new ArrayList<>();
    private List<Object> out = new ArrayList<>();

    public Object[] readDatosIniciales() throws FileNotFoundException {
        out.clear();
        empresas.clear();
        tripulantes.clear();
        buses.clear();
        terminales.clear();

        File archivo = new File("SVPDatosIniciales.txt");

        Scanner sc = new Scanner(archivo)
                .useDelimiter("[\t\r\n]+");

        int secc = 1;

        while (sc.hasNext()) {
            String linea = sc.next();

            if (linea.equals("+")) {
                secc++;
                continue;
            }

            switch (secc) {
                case 1 -> clientePasajeros(linea);
                case 2 -> empresas(linea);
                case 3 -> tripulantes(linea);
                case 4 -> terminales(linea);
                case 5 -> buses(linea);
                case 6 -> viajes(linea);
            }
        }

        sc.close();

        try {
            Controlador.ControladorEmpresas ce = Controlador.ControladorEmpresas.getInstance();

            java.lang.reflect.Field fEmp = ce.getClass().getDeclaredField("empresas");
            fEmp.setAccessible(true);
            ((java.util.ArrayList<Modelo.Empresa>) fEmp.get(ce)).addAll(this.empresas);

            java.lang.reflect.Field fBus = ce.getClass().getDeclaredField("buses");
            fBus.setAccessible(true);
            ((java.util.ArrayList<Modelo.Bus>) fBus.get(ce)).addAll(this.buses);

            java.lang.reflect.Field fTer = ce.getClass().getDeclaredField("terminales");
            fTer.setAccessible(true);
            ((java.util.ArrayList<Modelo.Terminal>) fTer.get(ce)).addAll(this.terminales);

            java.lang.reflect.Field fTrip = ce.getClass().getDeclaredField("tripulaciones");
            fTrip.setAccessible(true);
            ((java.util.ArrayList<Modelo.Tripulante>) fTrip.get(ce)).addAll(this.tripulantes);
        } catch (Exception e) {
            System.out.println("Sincronización CE: " + e.getMessage());
        }

        try {
            Controlador.SistemaVentaPasajes svp = Controlador.SistemaVentaPasajes.getInstance();
            java.util.ArrayList<Modelo.Viaje> viajesList = new java.util.ArrayList<>();

            for (Object obj : out) {
                if (obj instanceof Modelo.Viaje) viajesList.add((Modelo.Viaje) obj);
            }

            java.lang.reflect.Field fViajes = svp.getClass().getDeclaredField("viajes");
            fViajes.setAccessible(true);

            ((java.util.ArrayList<Modelo.Viaje>) fViajes.get(svp)).addAll(viajesList);

        } catch (Exception e) {
            System.out.println("Sincronización SVP: " + e.getMessage());
        }

        return out.toArray(new Object[0]);
    }


    public void saveControladores(Object[] controladores) throws SVPException {
        File file = new File("SVPObjetos.obj");

        try {
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
            outStream.writeObject(controladores);
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new SVPException("No se puede abrir o crear el archivo SVPObjetos.obj");

        } catch (IOException e) {
            e.printStackTrace();
            throw new SVPException("No se puede grabar en el archivo SVPObjetos.obj");
        }
    }

    public static Object[] readControladores() throws SVPException {
        File file = new File("SVPObjetos.obj");

        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            Object[] objetos = (Object[]) input.readObject();
            input.close();
            return objetos;
        } catch (IOException e) {
            e.printStackTrace();
            throw new SVPException("No se puede grabar en el archivo SVPObjetos.obj");
        } catch (ClassNotFoundException e) {
            throw new SVPException("No se puede leer el archivo SVPObjetos.obj");
        }
    }

    public void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivo) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(new FileOutputStream(nombreArchivo));

        for (Pasaje pasaje : pasajes) {
            printStream.println(pasaje.toString());
        }

        printStream.close();
    }


    private Optional<Empresa> findEmpresa(List<Empresa> empresas, Rut rut){
        return empresas.stream().filter(x -> x.getRut().equals(rut)).findFirst();
    }
    private Optional<Tripulante> findTripulante(List<Tripulante> tripulantes, IdPersona id){
        return tripulantes.stream().filter(x -> x.getIdPersona().equals(id)).findFirst();
    }
    private Optional<Bus> findBus(List<Bus> buses, String patente){
        return buses.stream().filter(x -> x.getPatente().equalsIgnoreCase(patente)).findFirst();
    }
    private Optional<Terminal> findTerminal(List<Terminal> terminals, String nombre ){
        return terminals.stream().filter(x -> x.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

    // METODOS EXTRAS

    private void clientePasajeros(String linea){
        String[] datos = linea.split(";");

        // LIMPIEZA CLAVE: Quitamos espacios en blanco de todos los elementos
        for (int i = 0; i < datos.length; i++) {
            datos[i] = datos[i].trim();
        }

        switch(datos[0]){
            case "C":
                Cliente cliente = new Cliente(
                        getIdpersona(datos[1]),
                        getNombre(Tratamiento.valueOf(datos[2]), datos[3], datos[4], datos[5]),
                        datos[6],
                        datos[7]
                );
                out.add(cliente);
                break;
            case "P":
                // public Pasajero(IdPersona id, Nombre nombre, String telefono, Nombre nomContacto, String fonoContacto) {
                Pasajero pasajero = new Pasajero( getIdpersona(datos[1]),getNombre(Tratamiento.valueOf(datos[2]), datos[3], datos[4], datos[5]),datos[6],getNombre(Tratamiento.valueOf(datos[7]), datos[8], datos[9], datos[10]),datos[11]);
                out.add(pasajero);
                break;
            case "CP":
                Cliente cliente2 = new Cliente(
                        getIdpersona(datos[1]),
                        getNombre(Tratamiento.valueOf(datos[2]), datos[3], datos[4], datos[5]),
                        datos[6],
                        datos[7]
                );
                out.add(cliente2);

                Pasajero pasajero2 = new Pasajero(
                        getIdpersona(datos[1]),
                        getNombre(Tratamiento.valueOf(datos[2]), datos[3], datos[4], datos[5]),
                        datos[6],
                        getNombre(Tratamiento.valueOf(datos[8]), datos[9], datos[10], datos[11]),
                        datos[12]
                );
                out.add(pasajero2);
                break;
        }
    }

    private void terminales(String linea){
        String[] datos = linea.split(";");

        for (int i = 0; i < datos.length; i++) {
            datos[i] = datos[i].trim();
        }

        Terminal ter = new Terminal( getDireccion(datos[1], Integer.parseInt(datos[2]), datos[3]),datos[0]);
        out.add(ter);
        terminales.add(ter);
    }

    private void viajes(String linea) {
        String[] datos = linea.split(";");

        for (int i = 0; i < datos.length; i++) {
            datos[i] = datos[i].trim();
        }

        Bus bus = findBus(buses, formatPatente(datos[4]))
                .orElseThrow(() -> new NoSuchElementException("Bus no encontrado: " + datos[4]));

        Auxiliar auxiliar = (Auxiliar) findTripulante(tripulantes, getIdpersona(datos[5]))
                .orElseThrow(() -> new NoSuchElementException("Auxiliar no encontrado: " + datos[5]));

        Conductor conductor = (Conductor) findTripulante(tripulantes, getIdpersona(datos[6]))
                .orElseThrow(() -> new NoSuchElementException("Conductor no encontrado: " + datos[6]));

        Terminal terminalSalida = findTerminal(terminales, datos[7])
                .orElseThrow(() -> new NoSuchElementException("Terminal origen no encontrada: " + datos[7]));

        Terminal terminalLlegada = findTerminal(terminales, datos[8])
                .orElseThrow(() -> new NoSuchElementException("Terminal destino no encontrada: " + datos[8]));

        Viaje viaje = new Viaje(
                LocalDate.parse(datos[0], DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                LocalTime.parse(datos[1], DateTimeFormatter.ofPattern("HH:mm")),
                Integer.parseInt(datos[2]),
                Integer.parseInt(datos[3]),
                bus,
                auxiliar,
                new Conductor[]{conductor},
                terminalSalida,
                terminalLlegada
        );

        out.add(viaje);
    }

    private void empresas(String linea) {
        String[] datos = linea.split(";");

        for (int i = 0; i < datos.length; i++) {
            datos[i] = datos[i].trim();
        }

        Empresa emp = new Empresa(Rut.of(datos[0]), datos[1]);
        emp.setUrl(datos[2]);
        empresas.add(emp);
        out.add(emp);
    }

    private void tripulantes(String linea) {
        String[] datos = linea.split(";");

        for (int i = 0; i < datos.length; i++) {
            datos[i] = datos[i].trim();
        }

        switch (datos[0]) {
            case "A":
                Auxiliar aux = new Auxiliar(
                        getIdpersona(datos[1]),
                        getNombre(Tratamiento.valueOf(datos[2]), datos[3], datos[4], datos[5]),
                        getDireccion(datos[6], Integer.parseInt(datos[7]), datos[8])
                );

                Empresa empA = findEmpresa(empresas, Rut.of(datos[9]))
                        .orElseThrow(() -> new NoSuchElementException("Empresa no encontrada: " + datos[9]));

                empA.addAuxiliar(aux.getIdPersona(), aux.getNombreCompleto(), aux.getDireccion());

                out.add(aux);
                tripulantes.add(aux);
                break;

            case "C":
                Conductor cond = new Conductor(
                        getIdpersona(datos[1]),
                        getNombre(Tratamiento.valueOf(datos[2]), datos[3], datos[4], datos[5]),
                        getDireccion(datos[6], Integer.parseInt(datos[7]), datos[8])
                );

                Empresa empC = findEmpresa(empresas, Rut.of(datos[9]))
                        .orElseThrow(() -> new NoSuchElementException("Empresa no encontrada: " + datos[9]));

                empC.addConductor(cond.getIdPersona(), cond.getNombreCompleto(), cond.getDireccion());

                out.add(cond);
                tripulantes.add(cond);
                break;
        }
    }



    private void buses(String linea) {
        String[] datos = linea.split(";");

        for (int i = 0; i < datos.length; i++) {
            datos[i] = datos[i].trim();
        }

        Bus bus  = new Bus(formatPatente(datos[0]), Integer.parseInt(datos[3]), findEmpresa(empresas, Rut.of(datos[4])).get());
        bus.setMarca(datos[1]);
        bus.setModelo(datos[2]);
        out.add(bus);
        buses.add(bus);
    }

    private String formatPatente(String patente){
        return patente.charAt(0) + "" + patente.charAt(1) + "." + patente.charAt(2) + patente.charAt(3) + "-" + patente.charAt(4) + patente.charAt(5);
    }

    private IdPersona getIdpersona(String dato){
        IdPersona idPersona = null;
        try {
            idPersona = Rut.of(dato);
        } catch (SVPException e) {
            String[] pasaporte = dato.split(" ");
            idPersona = Pasaporte.of(pasaporte[0], pasaporte[1]);
        }
        return idPersona;
    }

    private Direccion getDireccion(String calle, int numero, String comuna){
        return new Direccion(calle, numero, comuna);
    }

    private Nombre getNombre(Tratamiento tratamiento, String nombre, String apellidoPaterno, String apellidoMaterno){
        return new Nombre(tratamiento, nombre, apellidoPaterno, apellidoMaterno);
    }
}