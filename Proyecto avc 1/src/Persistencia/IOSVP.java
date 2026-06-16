package Persistencia;

import Controlador.*;
import Modelo.*;
import Excepciones.*;
import Utilidades.IdPersona;
import Utilidades.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IOSVP {
    private static IOSVP instance;
    private List<Empresa> empresas;
    private ArrayList<Tripulante> tripulantes;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    public Object[] readDatosIniciales() throws SistemaVentaPasajesException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Bus> buses = new ArrayList<>();
        ArrayList<Empresa> empresasLocales = new ArrayList<>();
        ArrayList<Terminal> terminales = new ArrayList<>();
        ArrayList<Viaje> viajes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("SVPIniciales.txt"))) {
            String linea;
            int bloque = 0;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                if (linea.equals("+")) {
                    bloque++;
                    continue;
                }
                String[] datos = linea.split(";");

                switch (bloque) {

                    case 1:
                        Rut rutEmpresa = Rut.of(datos[0]);

                        Optional<Empresa> nuevaEmpresa = findEmpresa(empresas,rutEmpresa);
                        empresasLocales.add(nuevaEmpresa.orElse(null));
                        break;

                    case 2:



                        break;

                    case 3:
                        String nombre = datos[1];
                        Optional<Terminal> terminal = findTerminal(terminales,nombre);
                        terminales.add(terminal.orElse(null));
                        break;

                    case 4:

                      String patente = datos[1];
                      Optional<Bus> bus=findBus(buses,patente);
                      buses.add(bus.orElse(null));
                        break;

                    case 5:
                        dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        df=DateTimeFormatter.ofPattern("HH:mm");
                        LocalDate fecha = LocalDate.parse(datos[0], dtf);
                        LocalTime hora = LocalTime.parse(datos[1], df);
                        String precio = datos[2];
                        String duracion = datos[3];
                        String pate = datos[4];
                        String rut= datos[5];
                        String rutEmpres=datos[6];
                        String terminalIda=datos[7];
                        String terminalLlegada =datos[8];
                        Viaje v=new Viaje()

                        break;
                }
            }
        } catch (IOException e) {
            throw new SistemaVentaPasajesException("Error leyendo archivo inicial: " + e.getMessage());
        } catch (Exception e) {
            throw new SistemaVentaPasajesException("Error en el formato de los datos: " + e.getMessage());
        }

        this.empresas = empresasLocales;

        return new Object[]{
                clientes,
                buses,
                empresasLocales,
                terminales,
                viajes
        };
    }


    private Optional<Tripulante> findTripulante(Empresa empresa, IdPersona id) {
        for (Tripulante t : empresa.getTripulantes()) {
            if (t.getIdPersona().equals(id)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    private Optional<Bus> findBus(List<Bus> buses, String patente) {
        for (Bus b : buses) {
            if (b.getPatente().equals(patente)) {
                return Optional.of(b);
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

    private Optional<Empresa> findEmpresa(List<Empresa> listaEmpresas, Rut rut) {
        for (Empresa e : listaEmpresas) {
            if (e.getRut().equals(rut)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}