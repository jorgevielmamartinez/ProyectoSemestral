import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private Scanner sc;
    private SistemaVentaPasajes sistema = new SistemaVentaPasajes();

    public Main() {
        this.sc = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.menu();
    }

    private void menu() {
        int opcion = 0;
        do {
            System.out.println("===========================");
            System.out.println("___ Menu principal ___");
            System.out.println("1) Crear cliente");
            System.out.println("2) Crear bus");
            System.out.println("3) Crear viaje");
            System.out.println("4) Vender pasajes");
            System.out.println("5) Lista de pasajeros");
            System.out.println("6) Lista de ventas");
            System.out.println("7) Lista de viajes");
            System.out.println("8) Salir");
            System.out.println("===========================");
            System.out.print(" Ingrese numero de opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {

                    case 1:
                        createCliente();
                        break;

                    case 2:
                        createBus();
                        break;

                    case 3:
                        createViaje();
                        break;
                    case 4:
                        vendePasajes();
                        break;
                    case 5:
                        listPasajerosViaje();
                        break;
                    case 6:
                        listVentas();
                        break;
                    case 7:
                        listViajes();
                        break;
                    case 8:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }

            } catch (Exception e) {
                System.out.println("Error, ingrese un numero valido.");
            }

        } while (opcion != 8);
    }

    public void createCliente() {
        System.out.println("___ Crear Cliente ___");

        System.out.print("Tipo documento (1.Rut, 2.Pasaporte): ");
        int tipo = Integer.parseInt(sc.nextLine());

        IdPersona id = null;
        if (tipo == 1) {
            System.out.print("Ingrese Rut: ");
            String rut = sc.nextLine();
            id = Rut.of(rut);

        } else {
            System.out.print("Numero Pasaporte: ");
            String num = sc.nextLine();

            System.out.print("Nacionalidad: ");
            String nac = sc.nextLine();

            id = Pasaporte.of(num, nac);
        }

        Nombre nombre = new Nombre();

        System.out.print("Tratamiento (1=SR, 2=SRA): ");
        int trat = Integer.parseInt(sc.nextLine());

        if (trat == 1) {
            nombre.setTratamiento(Tratamiento.SR);
        } else {
            nombre.setTratamiento(Tratamiento.SRA);
        }

        System.out.print("Nombres: ");
        nombre.setNombres(sc.next());

        System.out.print("Apellido Paterno: ");
        nombre.setApellidoPaterno(sc.next());

        System.out.print("Apellido Materno: ");
        nombre.setApellidoMaterno(sc.next());

        System.out.print("Telefono: ");
        String fono = sc.next();

        System.out.print("Email: ");
        String email = sc.next();
        sc.nextLine();

        boolean ok = sistema.createCliente(id, nombre, fono, email);

        if (ok) {
            System.out.println("Cliente creado con exito!");
        } else {
            System.out.println("Error al crear cliente. Posiblemente ya existe.");
        }
    }

    public void createBus() {
        System.out.println("___ Crear Bus ___");

        System.out.print("Patente: ");
        String patente = sc.next();

        System.out.print("Marca: ");
        String marca = sc.next();

        System.out.print("Modelo: ");
        String modelo = sc.next();

        System.out.print("Numero asientos: ");
        int asientos = Integer.parseInt(sc.next());
        sc.nextLine();

        boolean ok = sistema.createBus(patente, marca, modelo, asientos);
        if (ok) {
            System.out.println("Bus creado con exito!");
        } else {
            System.out.println("Error. Patente ya existe.");
        }
    }

    public void createViaje() {
        System.out.println("___ Crear Viaje ___");

        DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formHora = DateTimeFormatter.ofPattern("HH:mm");

        System.out.print("Fecha (dd/mm/yyyy): ");
        LocalDate fecha = LocalDate.parse(sc.next(), formFecha);

        System.out.print("Hora (hh:mm): ");
        LocalTime hora = LocalTime.parse(sc.next(), formHora);

        System.out.print("Precio pasaje: ");
        int precio = Integer.parseInt(sc.next());

        System.out.print("Patente del Bus: ");
        String patente = sc.next();
        sc.nextLine();
        boolean ok = sistema.createViaje(fecha, hora, precio, patente);

        if (ok) {
            System.out.println("Viaje creado con exito!");
        } else {
            System.out.println("Error. Bus no existe o choca horario.");
        }
    }

    public void vendePasajes() {
        System.out.println("___ Vender Pasajes ___");

        System.out.print("ID Documento: ");
        String idDoc = sc.next();

        System.out.print("Tipo (1.Boleta, 2.Factura): ");
        int tipoD = Integer.parseInt(sc.next());

        TipoDocumento tipoDoc = (tipoD != 1) ? TipoDocumento.FACTURA : TipoDocumento.BOLETA;

        DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Fecha de venta (dd/mm/yyyy): ");

        LocalDate fechaVenta = LocalDate.parse(sc.next(), formFecha);

        System.out.print("Ingrese RUT del cliente comprador (ej: 111-1): ");
        IdPersona idCliente = Rut.of(sc.next());
        boolean iniciada = sistema.iniciaVenta(idDoc, tipoDoc, fechaVenta, idCliente);

        if (!iniciada) {
            System.out.println("Error: Cliente no existe o documento repetido.");
            return;
        }
        System.out.print("Cuantos pasajes comprara?: ");
        int cant = sc.nextInt();

        System.out.print("Fecha del viaje a buscar (dd/mm/yyyy): ");
        LocalDate fechaViaje = LocalDate.parse(sc.next(), formFecha);

        String[][] horarios = sistema.getHorariosDisponibles(fechaViaje);
        if (horarios.length == 0) {
            System.out.println("No hay viajes para esa fecha.");
            return;
        }

        System.out.println("Viajes disponibles:");
        for (int i = 0; i < horarios.length; i++) {
            System.out.println((i+1) + " | Patente: " + horarios[i][0] + " | Hora: " + horarios[i][1] + " | Libres: " + horarios[i][3]);
        }

        System.out.print("Seleccione un viaje (ingrese el numero de lista): ");
        int opcViaje = Integer.parseInt(sc.next()) - 1;

        String patenteElegida = horarios[opcViaje][0];
        LocalTime horaElegida = LocalTime.parse(horarios[opcViaje][1]);

        String[][] matrizAsientos = new String[][]{sistema.listAsientosDeViaje(fechaViaje, horaElegida, patenteElegida)};

        for (int i = 0; i < matrizAsientos.length; i++) {
            System.out.print("[" + matrizAsientos[i][1] + "] A" + matrizAsientos[i][0] + "\t");

            if ((i + 1) % 4 == 0){
                System.out.println();
            }
        }

        for (int i = 0; i < cant; i++) {
            System.out.print("\nIngrese numero de asiento para el pasaje " + (i+1) + ": ");
            int asiento = Integer.parseInt(sc.next());

            System.out.print("Ingrese RUT del Pasajero que viaja: ");
            IdPersona idPasajero = Rut.of(sc.next());

            String nombreExiste = sistema.getNombrePasajero(idPasajero);

            if (nombreExiste == null) {

                System.out.println("Pasajero no existe. Ingrese sus datos.");
                Nombre nPas = new Nombre();

                nPas.setTratamiento(Tratamiento.SR);

                System.out.print("Nombres: ");
                nPas.setNombres(sc.next());

                System.out.print("Apellido Paterno: ");
                nPas.setApellidoPaterno(sc.next());
                System.out.println("Apellido Materno: ");
                nPas.setApellidoMaterno(sc.next());

                System.out.print("Telefono pasajero: ");
                String fonoP = sc.next();

                Nombre nCont = new Nombre();
                nCont.setTratamiento(Tratamiento.SR);

                System.out.print("Nombres Contacto Emergencia: ");
                nCont.setNombres(sc.next());

                System.out.print("Apellido Contacto Emergencia: ");
                nCont.setApellidoPaterno(sc.next());

                nCont.setApellidoMaterno("");

                System.out.print("Fono Contacto Emergencia: ");
                String fonoC = sc.next();

                sistema.createPasajero(idPasajero, nPas, fonoP, nCont, fonoC);
            }

            sistema.vendePasaje(idDoc, tipoDoc, fechaViaje, horaElegida, patenteElegida, asiento, idPasajero);
            System.out.println("Pasaje asignado.");
        }

        int total = sistema.getMontoVenta(idDoc, tipoDoc);

        System.out.println("*** Venta finalizada. Total a pagar: $" + total + " ***");
    }

    public void listPasajerosViaje() {
        System.out.println("___ Lista de Pasajeros de un Viaje ___");

        DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formHora = DateTimeFormatter.ofPattern("HH:mm");

        System.out.print("Fecha del viaje (dd/mm/yyyy): ");
        LocalDate fecha = LocalDate.parse(sc.next(), formFecha);

        System.out.print("Hora del viaje (hh:mm): ");
        LocalTime hora = LocalTime.parse(sc.next(), formHora);

        System.out.print("Patente del bus: ");
        String patente = sc.next();
        sc.nextLine();
        String[][] lista = sistema.listPasajeros(fecha, hora, patente);

        if (lista.length == 0) {
            System.out.println("No hay pasajeros o no existe el viaje.");
        } else {
            System.out.println("ID | Nombre | Contacto | Fono Contacto");

            for (int i = 0; i < lista.length; i++) {

                System.out.println(lista[i][0] + " | " + lista[i][1] + " | " + lista[i][2] + " | " + lista[i][3]);
            }
        }
    }

    public void listVentas() {
        System.out.println("___ Lista de Ventas ___");

        String[][] ventas = sistema.listVentas();

        if (ventas.length == 0) {
            System.out.println("No hay ventas registradas.");
        } else {
            System.out.println("Doc | Cliente | Total | Fecha");

            for (int i = 0; i < ventas.length; i++) {

                System.out.println(ventas[i][0] + " | " + ventas[i][1] + " | " + ventas[i][2] + " | " + ventas[i][3]);
            }
        }
    }

    public void listViajes() {
        System.out.println("--- LISTADO DE VIAJES ---");
        String[][] datos = sistema.listViajes();
        System.out.println("Fecha | Hora | Patente | Capacidad | Ocupados | Disponibles");
        for (String[] fila : datos) {
            System.out.println(String.join(" | ", fila));
        }
    }
}