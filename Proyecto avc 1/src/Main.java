import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private Scanner sc;
    private SistemaVentaPasajes sistema;

    public Main() {
        this.sc = new Scanner(System.in);
        this.sistema = new SistemaVentaPasajes();
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
            System.out.println("---------------------------------");
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
}