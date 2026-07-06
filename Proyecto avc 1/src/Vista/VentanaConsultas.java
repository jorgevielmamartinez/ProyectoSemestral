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
                Controlador.ControladorEmpresas ce = Controlador.ControladorEmpresas.getInstance();
                StringBuilder sb = new StringBuilder();

                try {
                    java.util.ArrayList<Modelo.Bus> misBusesActivos = ce.getBuses();

                    if (misBusesActivos == null || misBusesActivos.isEmpty()) {
                        sb.append("No hay registros activos en memoria actualmente.\n");
                    } else {
                        sb.append("Cantidad de buses operativos: ").append(misBusesActivos.size()).append("\n\n");

                        for (Modelo.Bus b : misBusesActivos) {
                            if (b != null) {
                                sb.append("Patente: ").append(b.getPatente()).append("\n");
                                sb.append("-----------------------------------\n");
                            }
                        }
                    }

                    txtAreaReporte.setText(sb.toString());

                } catch (Exception ex) {
                    txtAreaReporte.setText("Ocurrió un problema al procesar el reporte: " + ex.getMessage());
                }
            }
        });
    };
}