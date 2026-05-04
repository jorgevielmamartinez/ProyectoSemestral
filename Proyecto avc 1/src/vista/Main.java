package vista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import
import controlador.SistemaVentaPasajes;

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
        int opcion=0 ;
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

        } while (opcion !=8);
    }

    private void createCliente() {
        System.out.println("...... Creación de un nuevo modelo.Cliente ......");

        System.out.print("Tipo documento (1.utilidades.Rut, 2.utilidades.Pasaporte): ");
        int tipo = Integer.parseInt(sc.nextLine());

        IdPersona id = null;
        String rut = "", num = "", nac = "";

        if (tipo == 1) {
            System.out.print("Ingrese utilidades.Rut: ");
            rut = sc.nextLine();
            id = Rut.of(rut);
        } else {
            System.out.print("Numero utilidades.Pasaporte: ");
            num = sc.nextLine();

            System.out.print("Nacionalidad: ");
            nac = sc.nextLine();

            id = Pasaporte.of(num, nac);
        }

        Nombre nombre = new Nombre();

        System.out.print("Sr.[1] o Sra.[2]: ");
        int trat = Integer.parseInt(sc.nextLine());
        nombre.setTratamiento(trat == 1 ? Tratamiento.SR : Tratamiento.SRA);

        System.out.print("Nombres: ");
        nombre.setNombres(sc.nextLine());

        System.out.print("Apellido Paterno: ");
        nombre.setApellidoPaterno(sc.nextLine());

        System.out.print("Apellido Materno: ");
        nombre.setApellidoMaterno(sc.nextLine());

        System.out.print("Telefono movil: ");
        String fono = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        boolean ok = sistema.createCliente(id, nombre, fono, email);

        if (ok) {
            System.out.println("...... modelo.Cliente guardado exitosamente ......");
        } else {
            System.out.println(">> Error: modelo.Cliente ya existe <<");
        }
    }

    private void createBus() {
        System.out.println("...... Creación de un nuevo BUS ......");

        System.out.print("Patente (ej:xx.xx.00): ");
        String patente = sc.nextLine();

        System.out.print("Marca: ");
        String marca = sc.nextLine();

        System.out.print("Modelo: ");
        String modelo = sc.nextLine();

        System.out.print("Numero de asientos: ");
        int asientos = Integer.parseInt(sc.nextLine());

        boolean ok = sistema.createBus(patente, marca, modelo, asientos);

        if (ok) {
            System.out.println("Patente: " + patente);
            System.out.println("Marca: " + marca);
            System.out.println("Modelo: " + modelo);
            System.out.println("Numero de asientos: " + asientos);
            System.out.println("...... modelo.Bus guardado exitosamente ......");
        } else {
            System.out.println(">> Error: modelo.Bus ya existe <<");
        }
    }

    private void createViaje() {
        System.out.println("...... Creación de un nuevo modelo.Viaje ......");

        DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formHora = DateTimeFormatter.ofPattern("HH:mm");

        System.out.print("Fecha(dd/mm/yyyy): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), formFecha);

        System.out.print("Hora(hh:mm): ");
        LocalTime hora = LocalTime.parse(sc.nextLine(), formHora);

        System.out.print("Precio: ");
        int precio = Integer.parseInt(sc.nextLine());

        System.out.print("Patente modelo.Bus: ");
        String patente = sc.nextLine();

        boolean ok = sistema.createViaje(fecha, hora, precio, patente);

        if (ok) {
            System.out.println("Fecha: " + fecha);
            System.out.println("Hora: " + hora);
            System.out.println("Precio: " + precio);
            System.out.println("Patente modelo.Bus: " + patente);
            System.out.println("...... modelo.Viaje guardado exitosamente ......");
        } else {
            System.out.println(">> Error: modelo.Bus no existe o conflicto <<");
        }
    }

    private void vendePasajes() {
        System.out.println("...... modelo.Venta de pasajes ......");

        System.out.print("ID Documento: ");
        String idDoc = sc.nextLine();

        System.out.print("Tipo documento (1.Boleta, 2.Factura): ");
        int tipoD = Integer.parseInt(sc.nextLine());

        TipoDocumento tipoDoc = (tipoD != 1) ? TipoDocumento.FACTURA : TipoDocumento.BOLETA;

        DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.print("Fecha de venta: ");
        LocalDate fechaVenta = LocalDate.parse(sc.nextLine(), formFecha);

        System.out.print("utilidades.Rut[1] o utilidades.Pasaporte[2]: ");
        int tipoCliente = Integer.parseInt(sc.nextLine());

        IdPersona idCliente;

        if (tipoCliente == 1) {
            System.out.print("R.U.T.: ");
            String rut = sc.nextLine();
            idCliente = Rut.of(rut);
        } else {
            System.out.print("Numero utilidades.Pasaporte: ");
            String num = sc.nextLine();

            System.out.print("Nacionalidad: ");
            String nac = sc.nextLine();

            idCliente = Pasaporte.of(num, nac);
        }

        boolean iniciada = sistema.iniciaVenta(idDoc, tipoDoc, fechaVenta, idCliente);

        if (!iniciada) {
            System.out.println(">> Error venta <<");
            return;
        }

        System.out.println("::: Datos de la modelo.Venta :::");
        System.out.println("ID Documento: " + idDoc);
        System.out.println("Tipo: " + tipoDoc);
        System.out.println("Fecha: " + fechaVenta.format(formFecha));

        System.out.print("Cantidad de pasajes: ");
        int cant = Integer.parseInt(sc.nextLine());

        System.out.print("Fecha viaje: ");
        LocalDate fechaViaje = LocalDate.parse(sc.nextLine(), formFecha);

        String[][] horarios = sistema.getHorariosDisponibles(fechaViaje);

        System.out.println("::: Listado de horarios disponibles :::");
        System.out.println("| BUS | SALIDA | VALOR | ASIENTOS |");

        for (int i = 0; i < horarios.length; i++) {
            System.out.printf("%2d | %-6s | %-5s | %-6s | %-8s |\n",
                    i + 1,
                    horarios[i][0],
                    horarios[i][1],
                    horarios[i][2],
                    horarios[i][3]);
        }

        System.out.print("Seleccione viaje: ");
        int opcViaje = Integer.parseInt(sc.nextLine()) - 1;

        String patente = horarios[opcViaje][0];
        LocalTime hora = LocalTime.parse(horarios[opcViaje][1]);

        String[][] asientos = sistema.listAsientosDeViaje(fechaViaje, hora, patente);

        System.out.println("::: Asientos disponibles :::");

        for (int i = 0; i < asientos.length; i++) {
            String valor = asientos[i][1].equals("Ocupado") || asientos[i][1].equals("O") ? "*" : asientos[i][0];

            System.out.printf("|%2s| ", valor);

            if ((i + 1) % 4 == 2) System.out.print("   ");
            if ((i + 1) % 4 == 0) System.out.println();
        }

        for (int i = 0; i < cant; i++) {
            System.out.print("\nAsiento: ");
            int asiento = Integer.parseInt(sc.nextLine());

            System.out.print("RUT pasajero: ");
            IdPersona idPasajero = Rut.of(sc.nextLine());

            if (sistema.getNombrePasajero(idPasajero) == null) {
                Nombre n = new Nombre();
                n.setTratamiento(Tratamiento.SR);

                System.out.print("utilidades.Nombre: ");
                n.setNombres(sc.nextLine());

                System.out.print("Apellido: ");
                n.setApellidoPaterno(sc.nextLine());

                System.out.print("Telefono: ");
                String fono = sc.nextLine();

                Nombre nc = new Nombre();
                nc.setTratamiento(Tratamiento.SR);

                System.out.print("Contacto emergencia: ");
                nc.setNombres(sc.nextLine());

                System.out.print("Fono contacto emergencia: ");
                String fonoC = sc.nextLine();

                sistema.createPasajero(idPasajero, n, fono, nc, fonoC);
            }

            sistema.vendePasaje(idDoc, tipoDoc, fechaViaje, hora, patente, asiento, idPasajero);

            System.out.println("::: modelo.Pasaje agregado exitosamente :::");
        }

        int total = sistema.getMontoVenta(idDoc, tipoDoc);

        System.out.println("::: Monto total de la venta: $" + total);
        System.out.println("::: modelo.Venta cerrada exitosamente :::");
    }

    private void listVentas() {
        String[][] ventas = sistema.listVentas();
        System.out.println("___ Lista de Ventas ___");
        System.out.println("Doc | modelo.Cliente | Total | Fecha");
        for (String[] v : ventas) {
            System.out.println(String.join(" | ", v));
        }
    }

    private void listViajes() {
        String[][] datos = sistema.listViajes();
        System.out.println("___ Lista de Viajes ___");
        System.out.println("Fecha | Hora | Patente | Asientos Totales | Asientos Libres");
        for (String[] d : datos) {
            System.out.println(String.join(" | ", d));
        }
    }

    private void listPasajerosViaje() {
        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("HH:mm");

        System.out.print("Fecha (dd/MM/yyyy): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), f1);

        System.out.print("Hora (HH:mm): ");
        LocalTime hora = LocalTime.parse(sc.nextLine(), f2);

        System.out.print("Patente: ");
        String pat = sc.nextLine();

        String[][] lista = sistema.listPasajeros(fecha, hora, pat);
        System.out.println("___ Lista de Pasajeros de un modelo.Viaje ___");
        System.out.println("ID | utilidades.Nombre | Contacto | Fono Contacto");
        for (String[] p : lista) {
            System.out.println(String.join(" | ", p));
        }
    }
}