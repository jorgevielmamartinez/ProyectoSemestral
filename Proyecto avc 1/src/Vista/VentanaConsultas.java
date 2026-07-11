package Vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Controlador.ControladorEmpresas;
import Modelo.Bus;

public class VentanaConsultas extends JFrame {
    private JPanel panelConsultas;
    private JButton btnGenerarReporte;
    private JTextArea txtAreaReporte;

    public VentanaConsultas() {
        setTitle("Módulo de Auditoría y Consultas");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(panelConsultas);

        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                StringBuilder sb = new StringBuilder();
                sb.append("========== REPORTE COMPLETO DE VIAJES ==========\n\n");

                try {
                    Controlador.ControladorEmpresas ce = Controlador.ControladorEmpresas.getInstance();

                    java.lang.reflect.Field fieldT = ce.getClass().getDeclaredField("terminales");
                    fieldT.setAccessible(true);

                    java.util.ArrayList<Modelo.Terminal> terminales = (java.util.ArrayList<Modelo.Terminal>) fieldT.get(ce);

                    boolean encontro = false;
                    for (Modelo.Terminal t : terminales) {
                        for (Modelo.Viaje v : t.getSalidas()) {
                            encontro = true;

                            sb.append("Ruta: ").append(t.getDireccion().getComuna()).append(" -> ")
                                    .append(v.getLlegada().getDireccion().getComuna()).append("\n");
                            sb.append("Fecha: ").append(v.getFecha()).append(" | Hora: ").append(v.getHora()).append("\n");
                            sb.append("Precio: $").append(v.getPrecio()).append(" | Duración: ").append(v.getDuracion()).append(" min\n");
                            sb.append("Bus: ").append(v.getBus().getMarca()).append(" (Patente: ").append(v.getBus().getPatente()).append(")\n");
                            sb.append("Asientos Disponibles: ").append(v.getNroAsientosDisponibles()).append("\n");
                            sb.append("Tripulación:\n");

                            for (Modelo.Tripulante trip : v.getTripulantes()) {
                                if (trip != null) sb.append("  - ").append(trip.getNombreCompleto()).append("\n");
                            }
                            sb.append("--------------------------------------------------\n");
                        }
                    }
                    if (!encontro) sb.append("No hay viajes programados en el sistema actualmente.");

                } catch (Exception ex) {
                    sb.append("Error al generar el reporte: ").append(ex.getMessage());
                }
                txtAreaReporte.setText(sb.toString());
            }
        });
    }
}