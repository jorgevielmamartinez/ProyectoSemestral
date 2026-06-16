package Persistencia;

import Modelo.*;
import Utilidades.*;
import Excepciones.SistemaVentaPasajesException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IOSVP {

    private static IOSVP instance = new IOSVP();

    private IOSVP() {}

    public static IOSVP getInstance() {
        return instance;
    }

    public Object[] readDatosIniciales() {


        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Pasajero> pasajeros = new ArrayList<>();
        ArrayList<Empresa> empresas = new ArrayList<>();
        ArrayList<Terminal> terminales = new ArrayList<>();
        ArrayList<Viaje> viajes = new ArrayList<>();

        try {
            InputStream is = IOSVP.class.getResourceAsStream("SVPDatosIniciales.txt");

            if (is == null) {
                throw new SistemaVentaPasajesException("No se encontró SVPDatosIniciales.txt");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String linea;
            int bloque = 0;
            DateTimeFormatter fechaFormato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while ((linea = br.readLine()) != null) {

                if (linea.equals("+")) {
                    bloque++;
                    continue;
                }

                String[] datos = linea.split(";");

                switch (bloque) {

                    case 0 -> {
                        Nombre nombre = crearNombre(datos[2], datos[3], datos[4], datos[5]);
                        IdPersona id = Rut.of(datos[1]);

                        if (datos[0].equals("C") || datos[0].equals("CP")) {
                            Cliente cliente = new Cliente(id, nombre, datos[6], datos[7]);
                            clientes.add(cliente);
                        }

                        if (datos[0].equals("P") || datos[0].equals("CP")) {
                            Nombre contacto;

                            if (datos[0].equals("CP")) {
                                contacto = crearNombre(datos[8], datos[9], datos[10], datos[11]);
                                Pasajero pasajero = new Pasajero(id, nombre, datos[6], contacto, datos[12]);
                                pasajeros.add(pasajero);
                            } else {
                                contacto = crearNombre(datos[7], datos[8], datos[9], datos[10]);
                                Pasajero pasajero = new Pasajero(id, nombre, datos[6], contacto, datos[11]);
                                pasajeros.add(pasajero);
                            }
                        }
                    }

                    case 1 -> {
                        Empresa empresa = new Empresa(
                                Rut.of(datos[0]),
                                datos[1],
                                datos[2]
                        );
                        empresas.add(empresa);
                    }

                    case 2 -> {
                        Empresa empresa = findEmpresa(empresas, Rut.of(datos[9]))
                                .orElseThrow(() -> new SistemaVentaPasajesException("Empresa no encontrada"));

                        Nombre nombre = crearNombre(datos[2], datos[3], datos[4], datos[5]);

                        Direccion direccion = new Direccion(
                                datos[6],
                                Integer.parseInt(datos[7]),
                                datos[8]
                        );

                        if (datos[0].equals("A")) {
                            empresa.addAuxiliar(Rut.of(datos[1]), nombre, direccion);
                        } else if (datos[0].equals("C")) {
                            empresa.addConductor(Rut.of(datos[1]), nombre, direccion);
                        }
                    }

                    case 3 -> {
                        Terminal terminal = new Terminal(
                                new Direccion(
                                        datos[1],
                                        Integer.parseInt(datos[2]),
                                        datos[3]
                                ),
                                datos[0]
                        );
                        terminales.add(terminal);
                    }

                    case 4 -> {
                        Empresa empresa = findEmpresa(empresas, Rut.of(datos[4]))
                                .orElseThrow(() -> new SistemaVentaPasajesException("Empresa no encontrada"));

                        Bus bus = new Bus(datos[0], Integer.parseInt(datos[3]), empresa);
                        bus.setMarca(datos[1]);
                        bus.setModelo(datos[2]);

                        empresa.addBus(bus);
                    }

                    case 5 -> {
                        Bus bus = findBus(empresas, datos[4])
                                .orElseThrow(() -> new SistemaVentaPasajesException("Bus no encontrado"));

                        Empresa empresa = bus.getEmpresa();

                        Auxiliar auxiliar = (Auxiliar) findTripulante(empresa, Rut.of(datos[5]))
                                .orElseThrow(() -> new SistemaVentaPasajesException("Auxiliar no encontrado"));

                        Conductor conductor = (Conductor) findTripulante(empresa, Rut.of(datos[6]))
                                .orElseThrow(() -> new SistemaVentaPasajesException("Conductor no encontrado"));

                        Terminal salida = findTerminal(terminales, datos[7])
                                .orElseThrow(() -> new SistemaVentaPasajesException("Terminal salida no encontrado"));

                        Terminal llegada = findTerminal(terminales, datos[8])
                                .orElseThrow(() -> new SistemaVentaPasajesException("Terminal llegada no encontrado"));

                        Viaje viaje = new Viaje(
                                LocalDate.parse(datos[0], fechaFormato),
                                LocalTime.parse(datos[1]),
                                Integer.parseInt(datos[2]),
                                Integer.parseInt(datos[3]),
                                bus,
                                auxiliar,
                                new Conductor[]{conductor},
                                salida,
                                llegada
                        );

                        viajes.add(viaje);
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            throw new SistemaVentaPasajesException(e.getMessage());
        }

        imprimirDatosIniciales(clientes, pasajeros, empresas, terminales, viajes);

        return new Object[]{
                clientes,
                pasajeros,
                empresas,
                terminales,
                viajes
        };
    }

    // imprime lo de arriba
    private void imprimirDatosIniciales(ArrayList<Cliente> clientes,
                                        ArrayList<Pasajero> pasajeros,
                                        ArrayList<Empresa> empresas,
                                        ArrayList<Terminal> terminales,
                                        ArrayList<Viaje> viajes) {

        System.out.println("\n==============================");
        System.out.println("   DATOS INICIALES CARGADOS");
        System.out.println("==============================");

        System.out.println("\n--- CLIENTES ---");
        for (Cliente c : clientes) {
            System.out.printf("%-15s | %-35s | %-18s | %s%n",
                    c.getIdPersona(),
                    c.getNombreCompleto(),
                    c.getTelefono(),
                    c.getEmail());
        }

        System.out.println("\n--- PASAJEROS ---");
        for (Pasajero p : pasajeros) {
            System.out.printf("%-15s | %-35s | Tel: %-18s | Contacto: %-35s | %s%n",
                    p.getIdPersona(),
                    p.getNombreCompleto(),
                    p.getTelefono(),
                    p.getNomContacto(),
                    p.getFonoContacto());
        }

        System.out.println("\n--- EMPRESAS ---");
        for (Empresa e : empresas) {
            System.out.printf("%-15s | %-20s | %-30s | Buses: %-2d | Tripulantes: %-2d%n",
                    e.getRut(),
                    e.getNombre(),
                    e.getUrl(),
                    e.getBuses().length,
                    e.getTripulantes().length);
        }

        System.out.println("\n--- TERMINALES ---");
        for (Terminal t : terminales) {
            System.out.printf("%-20s | %s%n",
                    t.getNombre(),
                    t.getDireccion());
        }

        System.out.println("\n--- VIAJES ---");
        for (Viaje v : viajes) {
            System.out.printf("%-10s | %-5s | $%-6d | Bus: %-8s | %-20s -> %-20s%n",
                    v.getFecha(),
                    v.getHora(),
                    v.getPrecio(),
                    v.getBus().getPatente(),
                    v.getTerminalSalida().getNombre(),
                    v.getTerminalLlegada().getNombre());
        }

        System.out.println("\n==============================\n");
    }

    private Nombre crearNombre(String tratamiento, String nombres,
                               String apellidoPaterno, String apellidoMaterno) {
        Nombre nombre = new Nombre();
        nombre.setTratamiento(Tratamiento.valueOf(tratamiento));
        nombre.setNombres(nombres);
        nombre.setApellidoPaterno(apellidoPaterno);
        nombre.setApellidoMaterno(apellidoMaterno);
        return nombre;
    }

    private Optional<Empresa> findEmpresa(List<Empresa> empresas, Rut rut) {
        for (Empresa e : empresas) {
            if (e.getRut().equals(rut)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    private Optional<Bus> findBus(List<Empresa> empresas, String patente) {
        for (Empresa e : empresas) {
            for (Bus b : e.getBuses()) {
                if (b.getPatente().equals(patente)) {
                    return Optional.of(b);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Tripulante> findTripulante(Empresa empresa, IdPersona id) {
        for (Tripulante t : empresa.getTripulantes()) {
            if (t.getIdPersona().equals(id)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    private Optional<Terminal> findTerminal(List<Terminal> terminales, String nombre) {
        for (Terminal t : terminales) {
            if (t.getNombre().equals(nombre)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }



    /*
    =================
    SIN COMPLETAR
    =================
    */
    public void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivo) {
    }

    public void saveControladores(Object[] controladores) {
    }

    public Object[] readControladores() {
        return new Object[0];
    }
}