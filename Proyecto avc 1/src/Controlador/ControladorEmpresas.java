package Controlador;
import Modelo.*;
import Utilidades.*;
import Excepciones.SVPException;

import java.util.Arrays;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
public class ControladorEmpresas implements Serializable {
    private ArrayList<Empresa> empresas=new ArrayList<>();
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Terminal> terminales=new ArrayList<>();
    private ArrayList<Tripulante>tripulaciones=new ArrayList<>();

    // Instanciar
    private static ControladorEmpresas instance = null;


    public static ControladorEmpresas getInstance() {
        if (instance == null) {
            instance = new ControladorEmpresas();
        }
        return instance;
    }

    public void createEmpresa(Rut rut,String nombre, String url)throws SVPException {
        Empresa em=new Empresa(rut, nombre);
        em.setUrl(url);

        if(!findEmpresa(rut).equals(rut)){
            empresas.add(em);
        }else{
            throw new SVPException("Ya existe una empresa");
        }
    }

    public String[][] listLlegadasSalidasTerminal(String nombreTerminal, String fecha) {
        return new String[0][0];
    }



    public void createBus(String patente, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SVPException {

        Empresa empresa = findEmpresa(rutEmp).orElseThrow(() -> new SVPException("No existe empresa con el rut indicado"));

        Bus bus = new Bus(patente, nroAsientos, empresa);
        bus.setMarca(marca);
        bus.setModelo(modelo);

        if (findBus(patente).isEmpty()) {
            //logica para bus que ya existe
            buses.add(bus);
        } else {
            throw new SVPException("Ya existe bus con la patente indicada");
        }
    }





    public void createTerminal(String nombre,Direccion direccion)throws SVPException {

        if(findTerminal(nombre).isPresent()) {
            throw new SVPException("Ya existe Terminal con el nombre indicado");
        }

        if(findTerminalPorComuna(direccion.getComuna()).isPresent()) {
            throw new SVPException("Ya existe terminal en la comuna indicada");
        }

        Terminal terminal =new Terminal(direccion, nombre);
        terminales.add(terminal);

    }

    public void hireConductorForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir ) throws SVPException {
        Optional<Empresa> empresa=findEmpresa(rutEmp);
        if(empresa.isEmpty()){
            throw new SVPException("No existe empresa con el rut indicado ");
        }
        if(findConductor(id, rutEmp).isPresent()){
            throw new SVPException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }
        empresa.get().addConductor(id,nom,dir);
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp,IdPersona id,Nombre nom,Direccion dir) throws SVPException {
        Optional<Empresa> empresa = findEmpresa(rutEmp);


        if(empresa.isEmpty()){
            throw new SVPException("No existe empresa con el rut indicado ");
        }


        if(findAuxliar(id, rutEmp).isPresent()) {
            throw new SVPException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada ");
        }

        empresa.get().addAuxiliar(id,nom,dir);
    }

    public String[][] listEmpresas(){
        if(empresas.size()==0){return new String[0][0];}

        String[][] listEmpresas=new String[empresas.size()][6];


        for (int i=0; i<empresas.size(); i++) {
            listEmpresas[i][0]=(empresas.get(i).getRut())+"";
            listEmpresas[i][1]=(empresas.get(i).getNombre());
            listEmpresas[i][2]=empresas.get(i).getUrl();
            listEmpresas[i][3]=""+((empresas.get(i).getTripulantes()).length);
            listEmpresas[i][4]=""+((empresas.get(i).getBuses()).length);
            listEmpresas[i][5]=""+((empresas.get(i).getVentas()).length);

        }

        return listEmpresas;

    }

    public String[][] listLlegadaSalidasTerminal(String nombre, LocalDate fecha) throws SVPException {
        Optional<Terminal> terminal = findTerminal(nombre);

        if(terminal.isEmpty()) {
            throw new SVPException("No existe terminal con el nombre indicado");
        }

        Viaje[]llegadas= terminal.get().getLlegadas();
        Viaje[]salidas= terminal.get().getSalidas();


        ArrayList<Viaje> salida= new ArrayList<>(Arrays.asList(salidas));
        ArrayList<Viaje> llegada= new ArrayList<>(Arrays.asList(llegadas));


        salida.removeIf(viaje -> viaje.getFecha().isBefore(fecha));
        llegada.removeIf(viaje -> viaje.getFecha().isBefore(fecha));

        String[][] ArrayViajes=new String[salida.size()+llegada.size()][5];
        int index = 0;


        for (Viaje viaje : salida) {
            ArrayViajes[index][0] = "Salida";
            ArrayViajes[index][1] = "" + viaje.getHora();
            ArrayViajes[index][2] = viaje.getBus().getPatente();
            ArrayViajes[index][3] = viaje.getBus().getEmpresa().getNombre();
            ArrayViajes[index][4] = "" + viaje.getListaPasajeros().length;
            index++;
        }

        for (Viaje viaje : llegada) {
            ArrayViajes[index][0] = "Llegada";
            ArrayViajes[index][1] = "" + viaje.getHora();
            ArrayViajes[index][2] = viaje.getBus().getPatente();
            ArrayViajes[index][3] = viaje.getBus().getEmpresa().getNombre();
            ArrayViajes[index][4] = "" + viaje.getListaPasajeros().length;
            index++;
        }

        return ArrayViajes;

    }



    public String[][] listVentasEmpresa(Rut rut) throws SVPException {
        Optional<Empresa> up=findEmpresa(rut);

        if (up.isEmpty()) {
            throw new SVPException("No existe empresa con el rut indicado ");
        }


        Venta[] ArregloEmpresa=up.get().getVentas();

        if(ArregloEmpresa.length==0){
            return new String[0][0];
        }

        String[][] fast=new String[ArregloEmpresa.length][4];


        for (int i=0;i<ArregloEmpresa.length;i++){
            fast[i][0]=""+ArregloEmpresa[i].getFecha();
            fast[i][1]=""+ArregloEmpresa[i].getTipo();
            fast[i][2]=""+ArregloEmpresa[i].getMontoPagado();
            fast[i][3]=ArregloEmpresa[i].getTipoPago();
        }


        return fast;

    }

    protected void setInstanciPersistente(ControladorEmpresas instanciPersistente){
        instance=instanciPersistente;
    }
    protected void setDatosIniciales(Object[] objetos){
        empresas.clear();
        terminales.clear();
        buses.clear();
        tripulaciones.clear();
        for (Object o:objetos){
            if(o instanceof Empresa){
                empresas.add((Empresa) o);
            }if (o instanceof Terminal){
                terminales.add((Terminal) o);
            }if (o instanceof Bus) {
                buses.add((Bus) o);
            }if (o instanceof Tripulante){
                tripulaciones.add((Tripulante) o);
            }
        }
    }


    protected Optional<Empresa> findEmpresa(Rut rut) {
        return empresas.stream()
                .filter(e -> e.getRut().equals(rut))
                .findFirst();
    }




    protected Optional<Terminal> findTerminal(String nombre) {
        return terminales.stream()
                .filter(t -> t.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        return terminales.stream()
                .filter(t -> t.getDireccion().getComuna().equalsIgnoreCase(comuna))
                .findFirst();
    }


    protected Optional<Bus> findBus(String patente) {
        return buses.stream()
                .filter(b -> b.getPatente().equalsIgnoreCase(patente))
                .findFirst();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        return findEmpresa(rutEmpresa)
                .stream()
                .flatMap(e -> Arrays.stream(e.getTripulantes()))
                .filter(t -> t instanceof Conductor)
                .map(t -> (Conductor) t)
                .filter(c -> c.getIdPersona().equals(id))
                .findFirst();
    }

    protected Optional<Auxiliar> findAuxliar(IdPersona id, Rut rutEmpresa) {
        return findEmpresa(rutEmpresa)
                .stream()
                .flatMap(e -> Arrays.stream(e.getTripulantes()))
                .filter(t -> t instanceof Auxiliar)
                .map(t -> (Auxiliar) t)
                .filter(a -> a.getIdPersona().equals(id))
                .findFirst();
    }

    public void setInstanciaPersistente(ControladorEmpresas ce) {
        this.empresas = ce.empresas;
        this.terminales = ce.terminales;
    }

}