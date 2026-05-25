package Vista;

import Controlador.SistemaVentaPasajes;
import java.util.Scanner;

public class UISVP {

    private static UISVP instance;

    private Scanner sc;
    private SistemaVentaPasajes sistema;

    private UISVP() {

        sc = new Scanner(System.in);
        sistema = new SistemaVentaPasajes();

    }

    public static UISVP getInstance() {

        if (instance == null) {
            instance = new UISVP();
        }

        return instance;
    }

    public void menu() {

        // menú principal

    }

    private void createCliente() {

    }

    private void createBus() {

    }

    private void createViaje() {

    }

    // etc...
}