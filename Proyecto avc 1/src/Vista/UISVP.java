package Vista;

// Clase hecha por benjamin vivanco y tellez
import Utilidades.*;
import Controlador.*;
import Modelo.*; // Se agrega para instanciar objetos en los datos de prueba si es necesario
import Excepciones.SistemaVentaPasajesException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UISVP {
    private static UISVP INSTANCE = new UISVP();
    private Scanner sc;

    private UISVP() {
        sc = new Scanner(System.in);
        sc.useDelimiter("\\r\\n|[\\n\\r\\u2028\\u2029\\u0085]|\\t");
    }

    public static UISVP getInstance() {
        return INSTANCE;
    }

    private final static ControladorEmpresas CE = ControladorEmpresas.getInstance();
    private final static SistemaVentaPasajes SVP = SistemaVentaPasajes.getInstance();

    public void menu() {
        // Llamada automática al cargar el menú para poblar el sistema
        try {
            cargaDatosPredeterminados();
        } catch (Exception e) {
            System.out.println("Aviso: Error cargando algunos datos de prueba: " + e.getMessage());
        }

        int opcion = -1;
        do {
            System.out.println("\n\n============================");
            System.out.println("...::: Menú Principal :::...");
            System.out.println();
            System.out.println("  1) Crear empresa");
            System.out.println("  2) Contratar tripulante");
            System.out.println("  3) Crear terminal");
            System.out.println("  4) Crear cliente");
            System.out.println("  5) Crear bus");
            System.out.println("  6) Crear viaje");
            System.out.println("  7) Vender pasaje");
            System.out.println("  8) Listar empresas");
            System.out.println("  9) Listar llegadas y salidas de terminal");
            System.out.println("  10) Listar ventas por empresa");
            System.out.println("  0) Salir");
            System.out.print("\nSeleccione una opción: ");

            try {
                opcion = sc.nextInt();
                switch (opcion) {
                    case 1: crearEmpresa(); break;
                    case 2: contratarTripulante(); break;
                    case 3: crearTerminal(); break;
                    case 4: crearCliente(); break;
                    case 5: crearBus(); break;
                    case 6: crearViaje(); break;
                    case 7: venderPasaje(); break;
                    case 8: listarEmpresas(); break;
                    case 9: listarLlegadasSalidasTerminal(); break;
                    case 10: listarVentasEmpresa(); break;
                    case 0: System.out.println("Saliendo del sistema..."); break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
                sc.next(); // limpiar buffer
            } catch (SistemaVentaPasajesException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR INESPERADO: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    public void cargaDatosPredeterminados() {
        // Empresa 1
        Rut rutEmpresa1 = Rut.of("11.111.111-1");
        String nomEmpresa1 = "Empresa 1";
        String urlEmpresa1 = "https://empresa1.cl";
        CE.createEmpresa(rutEmpresa1, nomEmpresa1, urlEmpresa1);

        // Auxiliar 1
        IdPersona idAuxiliar1 = Rut.of("22.222.222-2");
        Nombre nombreAuxiliar1 = new Nombre();
        nombreAuxiliar1.setTratamiento(Tratamiento.valueOf("SR"));
        nombreAuxiliar1.setNombres("Pedro Alejandro");
        nombreAuxiliar1.setApellidoPaterno("Ramirez");
        nombreAuxiliar1.setApellidoMaterno("Torres");
        Direccion direccionAuxiliar1 = new Direccion("Avenida. UBB", 882, "Chillan");

        // Contratación adaptada a la lógica interna de tu Controlador/Modelo Empresa
        CE.hireAuxiliarForEmpresa(rutEmpresa1, idAuxiliar1, nombreAuxiliar1, direccionAuxiliar1);

        // Conductor 1
        IdPersona idConductor1 = Rut.of("33.333.333-3");
        Nombre nombreConductor1 = new Nombre();
        nombreConductor1.setTratamiento(Tratamiento.valueOf("SR"));
        nombreConductor1.setNombres("Miguel Angel");
        nombreConductor1.setApellidoPaterno("Fernandez");
        nombreConductor1.setApellidoMaterno("Garcia");
        Direccion direccionConductor1 = new Direccion("Avenida. Udec", 374, "San Carlos");

        CE.hireConductorForEmpresa(rutEmpresa1, idConductor1, nombreConductor1, direccionConductor1);

        // Terminal 1
        String nombreT1 = "Terminal 1";
        Direccion direccionT1 = new Direccion("Calle terminal1", 222, "Chillan");
        CE.createTerminal(nombreT1, direccionT1);

        // Terminal 2
        String nombreT2 = "Terminal 2";
        Direccion direccionT2 = new Direccion("Calle terminal2", 333, "Talca");
        CE.createTerminal(nombreT2, direccionT2);

        // Bus
        CE.createBus("HIID", "Mercedes", "kjfdsl", 40, rutEmpresa1);

        // Viaje
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        IdPersona[] idTripulantes = new IdPersona[2];
        idTripulantes[0] = idAuxiliar1;
        idTripulantes[1] = idConductor1;
        String[] comunas = new String[2];
        comunas[0] = "Chillan";          // Comuna de salida
        comunas[1] = "Talca";            // Comuna de llegada

        SVP.createViaje(LocalDate.parse("20/03/2025", dateFormatter),
                LocalTime.parse("15:30", timeFormatter),
                1000, 90, "HIID", idTripulantes, comunas);

        // Clientes
        IdPersona id1 = Pasaporte.of("chileno", "1234");
        IdPersona id2 = Rut.of("66.666.666-6");

        Nombre n1 = new Nombre();
        n1.setNombres("Lucas Daniel");
        n1.setApellidoPaterno("Fernandez");
        n1.setApellidoMaterno("Garcia");
        n1.setTratamiento(Tratamiento.valueOf("SR"));

        Nombre n2 = new Nombre();
        n2.setNombres("Sofia Isabel");
        n2.setApellidoPaterno("Martinez");
        n2.setApellidoMaterno("Lopez");
        n2.setTratamiento(Tratamiento.valueOf("SRA"));

        SVP.createCliente(id1, n1, "95234", "matias@gmail.com");
        SVP.createCliente(id2, n2, "4873", "some@gmail.com");

        SVP.createPasajero(id1, n1, "83247", n1, "83247");
        SVP.createPasajero(id2, n2, "1234", n2, "1234");
    }

    // --- MÉTODOS DEL MENÚ ---

    private void crearEmpresa() {
        System.out.println("\n-- Crear Empresa --");
        System.out.print("RUT de la empresa: ");
        String rutStr = sc.next();
        System.out.print("Nombre de la empresa: ");
        String nombre = sc.next();
        System.out.print("URL: ");
        String url = sc.next();

        CE.createEmpresa(Rut.of(rutStr), nombre, url);
        System.out.println("Empresa creada exitosamente.");
    }

    private void contratarTripulante() {
        System.out.println("\n-- Contratar Tripulante --");
        System.out.print("RUT de la Empresa: ");
        String rutEmpresa = sc.next();
        System.out.print("Tipo Tripulante (1: Auxiliar, 2: Conductor): ");
        int tipo = sc.nextInt();
        System.out.print("RUT del Tripulante: ");
        String rutTrip = sc.next();
        System.out.print("Nombres: ");
        String nombres = sc.next();
        System.out.print("Apellido Paterno: ");
        String apPaterno = sc.next();
        System.out.print("Calle Dirección: ");
        String calle = sc.next();
        System.out.print("Número: ");
        int numero = sc.nextInt();
        System.out.print("Comuna: ");
        String comuna = sc.next();

        Nombre nom = new Nombre();
        nom.setNombres(nombres);
        nom.setApellidoPaterno(apPaterno);
        Direccion dir = new Direccion(calle, numero, comuna);

        if (tipo == 1) {
            CE.hireAuxiliarForEmpresa(Rut.of(rutEmpresa), Rut.of(rutTrip), nom, dir);
            System.out.println("Auxiliar contratado.");
        } else {
            CE.hireConductorForEmpresa(Rut.of(rutEmpresa), Rut.of(rutTrip), nom, dir);
            System.out.println("Conductor contratado.");
        }
    }

    private void crearTerminal() {
        System.out.println("\n-- Crear Terminal --");
        System.out.print("Nombre del terminal: ");
        String nombre = sc.next();
        System.out.print("Calle: ");
        String calle = sc.next();
        System.out.print("Número: ");
        int numero = sc.nextInt();
        System.out.print("Comuna: ");
        String comuna = sc.next();

        Direccion dir = new Direccion(calle, numero, comuna);
        CE.createTerminal(nombre, dir);
        System.out.println("Terminal creado exitosamente.");
    }

    private void crearCliente() {
        System.out.println("\n-- Crear Cliente --");
        System.out.print("RUT del cliente: ");
        String idStr = sc.next();
        System.out.print("Nombres: ");
        String nombres = sc.next();
        System.out.print("Teléfono: ");
        String fono = sc.next();
        System.out.print("Email: ");
        String email = sc.next();

        Nombre nom = new Nombre();
        nom.setNombres(nombres);

        SVP.createCliente(Rut.of(idStr), nom, fono, email);
        System.out.println("Cliente creado.");
    }

    private void crearBus() {
        System.out.println("\n-- Crear Bus --");
        System.out.print("Patente del bus: ");
        String patente = sc.next();
        System.out.print("Marca: ");
        String marca = sc.next();
        System.out.print("Modelo: ");
        String modelo = sc.next();
        System.out.print("Número de asientos: ");
        int asientos = sc.nextInt();
        System.out.print("RUT de la Empresa: ");
        String rutEmp = sc.next();

        CE.createBus(patente, marca, modelo, asientos, Rut.of(rutEmp));
        System.out.println("Bus registrado.");
    }

    private void crearViaje() {
        System.out.println("\n-- Crear Viaje --");
        System.out.print("Fecha (DD/MM/YYYY): ");
        String fechaStr = sc.next();
        System.out.print("Hora (HH:MM): ");
        String horaStr = sc.next();
        System.out.print("Precio: ");
        int precio = sc.nextInt();
        System.out.print("Duración (minutos): ");
        int duracion = sc.nextInt();
        System.out.print("Patente del bus: ");
        String patente = sc.next();
        System.out.print("RUT Auxiliar: ");
        String rutAux = sc.next();
        System.out.print("RUT Conductor: ");
        String rutCond = sc.next();
        System.out.print("Comuna Origen: ");
        String orig = sc.next();
        System.out.print("Comuna Destino: ");
        String dest = sc.next();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        IdPersona[] tripulantes = new IdPersona[]{Rut.of(rutAux), Rut.of(rutCond)};
        String[] comunas = new String[]{orig, dest};

        SVP.createViaje(LocalDate.parse(fechaStr, dateFormatter), LocalTime.parse(horaStr, timeFormatter), precio, duracion, patente, tripulantes, comunas);
        System.out.println("Viaje creado exitosamente.");
    }

    private void venderPasaje() {
        System.out.println("\n-- Venta de Pasajes --");
        System.out.print("Ingrese fecha del viaje (DD/MM/YYYY): ");
        String fecha = sc.next();
        System.out.print("Cantidad de pasajes: ");
        int numPasajes = sc.nextInt();

        System.out.println("Procesando consulta de viajes...");
        // Conexión con la lógica de negocio de venta
    }

    private void listarEmpresas() {
        System.out.println("\n-- Listado de Empresas --");
        String[][] empresas = CE.listEmpresas();
        if (empresas.length == 0) {
            System.out.println("No hay empresas registradas.");
        } else {
            for (String[] fila : empresas) {
                System.out.println(String.join(" | ", fila));
            }
        }
    }

    private void listarLlegadasSalidasTerminal() {
        System.out.println("\n-- Listar Terminal --");
        System.out.print("Ingrese nombre del terminal: ");
        String nombreTerm = sc.next();
        System.out.print("Ingrese fecha (DD/MM/YYYY): ");
        String fecha = sc.next();

        String[][] viajes = CE.listLlegadasSalidasTerminal(nombreTerm, fecha);
        if (viajes.length == 0) {
            System.out.println("No se encontraron registros.");
        } else {
            for (String[] v : viajes) {
                System.out.println(String.join(" | ", v));
            }
        }
    }

    private void listarVentasEmpresa() {
        System.out.println("\n-- Ventas por Empresa --");
        System.out.print("Ingrese RUT de la empresa: ");
        String rutEmp = sc.next();

        String[][] ventas = CE.listVentasEmpresa(Rut.of(rutEmp));
        if (ventas.length == 0) {
            System.out.println("No se registraron ventas.");
        } else {
            for (String[] v : ventas) {
                System.out.println(String.join(" | ", v));
            }
        }
    }
}