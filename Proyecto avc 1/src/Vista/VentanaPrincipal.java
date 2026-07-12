package Vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JButton btnCargarDatos;
    private JButton btnCrearViaje;
    private JButton btnVenderPasaje;
    private JButton btnConsultar;
    private JLabel txtTitulo;
    private JButton btnGuardarDatos;
    private JButton btnSalir;
    private JButton btnRecuperarDatos;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Pasajes de Buses");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Este cierra todo el programa
        setLocationRelativeTo(null);

        setContentPane(panelPrincipal);

        // Cargar Datos
        btnCargarDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Controlador.SistemaVentaPasajes
                            .getInstance()
                            .readDatosIniciales();
                    JOptionPane.showMessageDialog(null, "¡Datos cargados exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Abrir Crear Viaje
        btnCrearViaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaCrearViaje().setVisible(true);
            }
        });

        // Abrir Ventas
        btnVenderPasaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaVentas().setVisible(true);
            }
        });

        //  Abrir Consultas
        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaConsultas().setVisible(true);
            }
        });

        // Guardar datos del sistema
        btnGuardarDatos.addActionListener(e -> {
            try {
                Controlador.SistemaVentaPasajes
                        .getInstance()
                        .saveDatosSistema();

                JOptionPane.showMessageDialog(
                        VentanaPrincipal.this,
                        "Datos guardados correctamente.",
                        "Guardado exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Excepciones.SVPException ex) {
                JOptionPane.showMessageDialog(
                        VentanaPrincipal.this,
                        ex.getMessage(),
                        "Error al guardar",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

// Recuperar datos guardados
        btnRecuperarDatos.addActionListener(e -> {
            try {
                Controlador.SistemaVentaPasajes
                        .getInstance()
                        .readDatosSistema();

                JOptionPane.showMessageDialog(
                        VentanaPrincipal.this,
                        "Datos recuperados correctamente.",
                        "Recuperación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Excepciones.SVPException ex) {
                JOptionPane.showMessageDialog(
                        VentanaPrincipal.this,
                        ex.getMessage(),
                        "Error al recuperar",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

// Salir de la aplicación
        btnSalir.addActionListener(e -> {
            dispose();
            System.exit(0);
        });


    }
}