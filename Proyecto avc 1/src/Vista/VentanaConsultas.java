package Vista;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaConsultas extends JFrame {
    private JPanel panelConsultas;
    private JButton btnGenerarReporte;
    private JTable tablaViajes;
    private JButton btnConsultarEmpresas;
    private JButton btnConsultarTerminales;
    private JButton salirButton;

    public VentanaConsultas() {
        setTitle("Módulo de Auditoría y Consultas");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(panelConsultas);

        btnGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] columnas = {
                        "Fecha",
                        "Hora salida",
                        "Hora llegada",
                        "Precio",
                        "Asientos disponibles",
                        "Patente",
                        "Origen",
                        "Destino"
                };

                String[][] datos =
                        Controlador.SistemaVentaPasajes
                                .getInstance()
                                .listViajes();

                javax.swing.table.DefaultTableModel modelo =
                        new javax.swing.table.DefaultTableModel(
                                datos,
                                columnas
                        ) {
                            @Override
                            public boolean isCellEditable(
                                    int fila,
                                    int columna
                            ) {
                                return false;
                            }
                        };

                tablaViajes.setModel(modelo);

                if (datos.length == 0) {
                    JOptionPane.showMessageDialog(
                            VentanaConsultas.this,
                            "No hay viajes cargados en el sistema.",
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        btnConsultarEmpresas.addActionListener(e -> {
            new VentanaConsultaEmpresas().setVisible(true);
        });

        btnConsultarTerminales.addActionListener(e -> {
            new VentanaConsultaTerminales().setVisible(true);
        });
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}