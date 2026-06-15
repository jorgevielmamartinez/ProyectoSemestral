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
            BufferedReader br = new BufferedReader(new FileReader("SVPIniciales.txt"));

            String linea;
            int bloque = 0;

            while ((linea = br.readLine()) != null) {

                if (linea.equals("+")) {
                    bloque++;
                    continue;
                }

                String[] datos = linea.split(";");

                switch (bloque) {

                    case 0:
                        // clientes y pasajeros
                        break;

                    case 1:
                        // empresas
                        Empresa empresa = new Empresa(
                                Rut.of(datos[0]),
                                datos[1],
                                datos[2]
                        );
                        empresas.add(empresa);
                        break;

                    case 2:
                        // tripulantes
                        break;

                    case 3:
                        // terminales
                        Terminal terminal = new Terminal(
                                new Direccion(datos[1],
                                        Integer.parseInt(datos[2]),
                                        datos[3]),
                                datos[0]
                        );
                        terminales.add(terminal);
                        break;

                    case 4:
                        // buses
                        Empresa emp = findEmpresa(empresas, Rut.of(datos[4]))
                                .orElseThrow(() -> new SistemaVentaPasajesException("Empresa no encontrada"));

                        Bus bus = new Bus(datos[0], Integer.parseInt(datos[3]), emp);
                        bus.setMarca(datos[1]);
                        bus.setModelo(datos[2]);

                        emp.addBus(bus);
                        break;

                    case 5:
                        // viajes
                        break;
                }
            }

            br.close();

        } catch (IOException e) {
            throw new SistemaVentaPasajesException("Error leyendo archivo inicial");
        }

        return new Object[] {
                clientes,
                pasajeros,
                empresas,
                terminales,
                viajes
        };
    }

    private Optional<Empresa> findEmpresa(List<Empresa> empresas, Rut rut) {
        for (Empresa e : empresas) {
            if (e.getRut().equals(rut)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
    public void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivo) {
    }

    public void saveControladores(Object[] controladores) {
    }

    public Object[] readControladores() {
        return new Object[0];
    }
}