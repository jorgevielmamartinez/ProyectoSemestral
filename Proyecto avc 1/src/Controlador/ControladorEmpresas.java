package Controlador;
import Modelo.*;
import Utilidades.*;
import Excepciones.SVPException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class ControladorEmpresas {
    private ArrayList<Empresa> empresas = new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Terminal> terminales = new ArrayList<>();
    private ArrayList<Tripulante> tripulaciones = new ArrayList<>();

    private static ControladorEmpresas instance = null;

    public static ControladorEmpresas getInstance() {
        if (instance == null) {
            instance = new ControladorEmpresas();
        }
        return instance;
    }

    public void createEmpresa(Rut rut, String nombre, String url) throws SVPException {
        findEmpresa(rut).ifPresentOrElse(
                em -> {
                    throw new SVPException("Ya existe una empresa");
                },
                () -> {
                    Empresa em = new Empresa(rut, nombre);
                    em.setUrl(url);
                    empresas.add(em);
                }
        );
    }

    public void createBus(String patente, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SVPException {
        Empresa empresa = findEmpresa(rutEmp)
                .orElseThrow(() -> new SVPException("No existe empresa con el rut indicado"));

        findBus(patente).ifPresentOrElse(
                b -> {
                    throw new SVPException("Ya existe bus con la patente indicada");
                },
                () -> {
                    Bus bus = new Bus(patente, nroAsientos, empresa);
                    bus.setMarca(marca);
                    bus.setModelo(modelo);
                    buses.add(bus);
                }
        );
    }

    public void createTerminal(String nombre, Direccion direccion) throws SVPException {
        findTerminal(nombre).ifPresent(t -> {
            throw new SVPException("Ya existe Terminal con el nombre indicado");
        });

        findTerminalPorComuna(direccion.getComuna()).ifPresent(t -> {
            throw new SVPException("Ya existe terminal en la comuna indicada");
        });

        terminales.add(new Terminal(direccion, nombre));
    }

    public void hireConductorForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SVPException {
        Empresa empresa = findEmpresa(rutEmp)
                .orElseThrow(() -> new SVPException("No existe empresa con el rut indicado "));

        findConductor(id, rutEmp).ifPresentOrElse(
                c -> {
                    throw new SVPException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
                },
                () -> empresa.addConductor(id, nom, dir)
        );
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SVPException {
        Empresa empresa = findEmpresa(rutEmp)
                .orElseThrow(() -> new SVPException("No existe empresa con el rut indicado "));

        findAuxliar(id, rutEmp).ifPresentOrElse(
                a -> {
                    throw new SVPException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
                },
                () -> empresa.addAuxiliar(id, nom, dir)
        );
    }

    public String[][] listEmpresas() {
        return empresas.stream()
                .map(e -> new String[]{
                        String.valueOf(e.getRut()),
                        e.getNombre(),
                        e.getUrl(),
                        String.valueOf(e.getTripulantes().length),
                        String.valueOf(e.getBuses().length),
                        String.valueOf(e.getVentas().length)
                })
                .toArray(String[][]::new);
    }

    public String[][] listLlegadaSalidasTerminal(String nombre, LocalDate fecha) throws SVPException {
        Terminal terminal = findTerminal(nombre)
                .orElseThrow(() -> new SVPException("No existe terminal con el nombre indicado"));

        Stream<String[]> salidas = Arrays.stream(terminal.getSalidas())
                .filter(viaje -> !viaje.getFecha().isBefore(fecha))
                .map(viaje -> new String[]{
                        "Salida",
                        String.valueOf(viaje.getHora()),
                        viaje.getBus().getPatente(),
                        viaje.getBus().getEmpresa().getNombre(),
                        String.valueOf(viaje.getListaPasajeros().length)
                });

        Stream<String[]> llegadas = Arrays.stream(terminal.getLlegadas())
                .filter(viaje -> !viaje.getFecha().isBefore(fecha))
                .map(viaje -> new String[]{
                        "Llegada",
                        String.valueOf(viaje.getHora()),
                        viaje.getBus().getPatente(),
                        viaje.getBus().getEmpresa().getNombre(),
                        String.valueOf(viaje.getListaPasajeros().length)
                });

        return Stream.concat(salidas, llegadas).toArray(String[][]::new);
    }

    public String[][] listVentasEmpresa(Rut rut) throws SVPException {
        Empresa empresa = findEmpresa(rut)
                .orElseThrow(() -> new SVPException("No existe empresa con el rut indicado "));

        return Arrays.stream(empresa.getVentas())
                .map(v -> new String[]{
                        String.valueOf(v.getFecha()),
                        String.valueOf(v.getTipo()),
                        String.valueOf(v.getMontoPagado()),
                        v.getTipoPago()
                })
                .toArray(String[][]::new);
    }

    protected Optional<Empresa> findEmpresa(Rut rut) {
        return empresas.stream()
                .filter(n -> n.getRut().equals(rut))
                .findFirst();
    }

    protected Optional<Terminal> findTerminal(String nombre) {
        return terminales.stream()
                .filter(n -> n.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        return terminales.stream()
                .filter(n -> n.getDireccion().getComuna().equalsIgnoreCase(comuna))
                .findFirst();
    }

    protected Optional<Bus> findBus(String patente) {
        return buses.stream()
                .filter(n -> n.getPatente().equalsIgnoreCase(patente))
                .findFirst();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        return findEmpresa(rutEmpresa)
                .map(Empresa::getTripulantes)
                .flatMap(tripulantes -> Arrays.stream(tripulantes)
                        .filter(n -> n.getIdPersona().equals(id) && n instanceof Conductor)
                        .map(n -> (Conductor) n)
                        .findFirst());
    }

    protected Optional<Auxiliar> findAuxliar(IdPersona id, Rut rutEmpresa) {
        return findEmpresa(rutEmpresa)
                .map(Empresa::getTripulantes)
                .flatMap(tripulantes -> Arrays.stream(tripulantes)
                        .filter(n -> n.getIdPersona().equals(id) && n instanceof Auxiliar)
                        .map(n -> (Auxiliar) n)
                        .findFirst());
    }

    protected void setInstanciPersistente(ControladorEmpresas instanciPersistente) {
        instance = instanciPersistente;
    }

    protected void setDatosIniciales(Object[] objetos) {
        empresas.clear();
        terminales.clear();
        buses.clear();
        tripulaciones.clear();

        for (Object o : objetos) {
            if (o instanceof Empresa) empresas.add((Empresa) o);
            else if (o instanceof Terminal) terminales.add((Terminal) o);
            else if (o instanceof Bus) buses.add((Bus) o);
            else if (o instanceof Tripulante) tripulaciones.add((Tripulante) o);
        }
    }
}