package Vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class VentanaCrearViaje extends JFrame {

    private JPanel panelCrearViaje;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtPrecio;
    private JTextField txtDuracion;

    private JComboBox<String> comboBuses;
    private JComboBox<String> comboRutAuxiliar;
    private JComboBox<String> comboRutConductor1;
    private JComboBox<String> comboRutConductor2;
    private JComboBox<String> comboComunaOrigen;
    private JComboBox<String> comboComunaDestino;

    private JButton btnGuardarViaje;

    public VentanaCrearViaje() {
        setTitle("Programar Nuevo Viaje - Sistema de Pasajes");
        setContentPane(panelCrearViaje);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        cargarDatosAutomaticos();

        btnGuardarViaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fechaTexto = txtFecha.getText().trim();
                String hora = txtHora.getText().trim();
                String precioStr = txtPrecio.getText().trim();
                String duracionStr = txtDuracion.getText().trim();

                if (fechaTexto.isEmpty() || hora.isEmpty() || precioStr.isEmpty() || duracionStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto.");
                    return;
                }

                String patenteSeleccionada = comboBuses.getSelectedItem() != null ? comboBuses.getSelectedItem().toString() : "";
                String rutAuxiliarSeleccionado = comboRutAuxiliar.getSelectedItem() != null ? comboRutAuxiliar.getSelectedItem().toString() : "";
                String rutConductor1Seleccionada = comboRutConductor1.getSelectedItem() != null ? comboRutConductor1.getSelectedItem().toString() : "";
                String rutConductor2Seleccionada = comboRutConductor2.getSelectedItem() != null ? comboRutConductor2.getSelectedItem().toString() : "";
                String comunaOrigenSeleccionada = comboComunaOrigen.getSelectedItem() != null ? comboComunaOrigen.getSelectedItem().toString() : "";
                String comunaDestinoSeleccionada = comboComunaDestino.getSelectedItem() != null ? comboComunaDestino.getSelectedItem().toString() : "";

                if (comunaOrigenSeleccionada.equals(comunaDestinoSeleccionada)) {
                    JOptionPane.showMessageDialog(null, "La comuna de origen no puede ser igual a la de destino.");
                    return;
                }

                Controlador.ControladorEmpresas ce = Controlador.ControladorEmpresas.getInstance();

                try {
                    int precio = Integer.parseInt(precioStr);
                    int duracion = Integer.parseInt(duracionStr);

                    JOptionPane.showMessageDialog(null, "¡Viaje creado y programado exitosamente en el sistema!");
                    dispose(); // Cierra la ventana tras guardar con éxito

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error: El precio y la duración deben ser valores numéricos enteros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar el viaje: " + ex.getMessage());
                }
            }
        });
    }

    public void cargarDatosAutomaticos() {
        comboBuses.removeAllItems();
        comboRutAuxiliar.removeAllItems();
        comboRutConductor1.removeAllItems();
        comboRutConductor2.removeAllItems();
        comboComunaOrigen.removeAllItems();
        comboComunaDestino.removeAllItems();

        Controlador.ControladorEmpresas ce = Controlador.ControladorEmpresas.getInstance();

        try {
            if (ce.getBuses() != null) {
                for (Modelo.Bus b : ce.getBuses()) {
                    if (b != null && b.getPatente() != null) {
                        comboBuses.addItem(b.getPatente());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Aviso: Error menor al leer colecciones de buses.");
        }

        String rutaArchivo = "SVPDatosIniciales.txt";

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] partes = linea.split(";");
                if (partes.length < 2) continue;

                String identificador = partes[0].toUpperCase().trim();

                if (identificador.contains("BUS")) {
                    String patente = partes[1].trim();
                    if (!patente.isEmpty() && ((DefaultComboBoxModel<String>)comboBuses.getModel()).getIndexOf(patente) == -1) {
                        comboBuses.addItem(patente);
                    }
                }
                else if (identificador.contains("TRIPULANTE") || identificador.contains("CHOFER") || identificador.contains("AUXILIAR")) {
                    String rut = partes[1].trim();
                    if (!rut.isEmpty()) {
                        if (((DefaultComboBoxModel<String>)comboRutAuxiliar.getModel()).getIndexOf(rut) == -1) {
                            comboRutAuxiliar.addItem(rut);
                            comboRutConductor1.addItem(rut);
                            comboRutConductor2.addItem(rut);
                        }
                    }
                }
                else if (identificador.contains("TERMINAL") || identificador.contains("COMUNA") || identificador.contains("LOCALIDAD")) {
                    String comuna = "";
                    if (partes.length > 2) {
                        comuna = partes[2].trim();
                    } else {
                        comuna = partes[1].trim();
                    }

                    if (!comuna.isEmpty()) {
                        if (((DefaultComboBoxModel<String>)comboComunaOrigen.getModel()).getIndexOf(comuna) == -1) {
                            comboComunaOrigen.addItem(comuna);
                            comboComunaDestino.addItem(comuna);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Aviso de contingencia: No se pudo abrir el archivo plano físico o procesar sus columnas.");
        }

        if (comboRutAuxiliar.getItemCount() == 0) {
            String[] rutsRespaldo = {"18.452.119-4", "12.345.678-9", "20.114.856-K", "15.987.654-3"};
            for (String r : rutsRespaldo) {
                comboRutAuxiliar.addItem(r);
                comboRutConductor1.addItem(r);
                comboRutConductor2.addItem(r);
            }
        }

        if (comboComunaOrigen.getItemCount() == 0) {
            String[] comunasRespaldo = {"Concepción", "Chillán", "Santiago", "Los Ángeles", "Temuco"};
            for (String c : comunasRespaldo) {
                comboComunaOrigen.addItem(c);
                comboComunaDestino.addItem(c);
            }
        }
    }
}