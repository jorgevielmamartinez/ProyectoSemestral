package Vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

                try {
                    int precio = Integer.parseInt(precioStr);
                    int duracion = Integer.parseInt(duracionStr);

                    if (precio <= 0 || duracion <= 0) {
                        JOptionPane.showMessageDialog(
                                VentanaCrearViaje.this,
                                "El precio y la duración deben ser mayores que cero.",
                                "Valores inválidos",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    java.time.LocalDate fecha =
                            java.time.LocalDate.parse(
                                    fechaTexto,
                                    java.time.format.DateTimeFormatter
                                            .ofPattern("dd/MM/yyyy")
                            );

                    java.time.LocalTime horaViaje =
                            java.time.LocalTime.parse(
                                    hora,
                                    java.time.format.DateTimeFormatter
                                            .ofPattern("HH:mm")
                            );

                    java.util.ArrayList<Utilidades.IdPersona> tripulantes =
                            new java.util.ArrayList<>();

                    tripulantes.add(
                            Utilidades.Rut.of(rutAuxiliarSeleccionado)
                    );

                    tripulantes.add(
                            Utilidades.Rut.of(rutConductor1Seleccionada)
                    );

                    if (!rutConductor2Seleccionada
                            .equals("Sin segundo conductor")) {

                        if (rutConductor1Seleccionada
                                .equals(rutConductor2Seleccionada)) {

                            JOptionPane.showMessageDialog(
                                    VentanaCrearViaje.this,
                                    "Los conductores no pueden ser la misma persona.",
                                    "Conductores repetidos",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }

                        tripulantes.add(
                                Utilidades.Rut.of(rutConductor2Seleccionada)
                        );
                    }

                    String[] comunas = {
                            comunaOrigenSeleccionada,
                            comunaDestinoSeleccionada
                    };

                    Controlador.SistemaVentaPasajes
                            .getInstance()
                            .createViaje(
                                    fecha,
                                    horaViaje,
                                    precio,
                                    duracion,
                                    patenteSeleccionada,
                                    tripulantes.toArray(
                                            new Utilidades.IdPersona[0]
                                    ),
                                    comunas
                            );

                    JOptionPane.showMessageDialog(
                            VentanaCrearViaje.this,
                            "¡Viaje creado correctamente!",
                            "Viaje creado",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                } catch (java.time.format.DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                            VentanaCrearViaje.this,
                            "La fecha debe usar DD/MM/AAAA y la hora HH:MM.",
                            "Fecha u hora inválida",
                            JOptionPane.WARNING_MESSAGE
                    );

                } catch (Excepciones.SVPException ex) {
                    JOptionPane.showMessageDialog(
                            VentanaCrearViaje.this,
                            ex.getMessage(),
                            "No se pudo crear el viaje",
                            JOptionPane.WARNING_MESSAGE
                    );
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
        comboRutConductor2.addItem("Sin segundo conductor");

        comboComunaOrigen.removeAllItems();
        comboComunaDestino.removeAllItems();

        Controlador.ControladorEmpresas ce =
                Controlador.ControladorEmpresas.getInstance();

        try {
            for (Modelo.Bus bus : ce.getBuses()) {
                comboBuses.addItem(bus.getPatente());
            }

            for (Modelo.Empresa empresa : ce.getEmpresas()) {
                for (Modelo.Tripulante tripulante : empresa.getTripulantes()) {
                    if (tripulante instanceof Modelo.Auxiliar) {
                        comboRutAuxiliar.addItem(
                                tripulante.getIdPersona().toString()
                        );
                    }

                    if (tripulante instanceof Modelo.Conductor) {
                        comboRutConductor1.addItem(
                                tripulante.getIdPersona().toString()
                        );
                        comboRutConductor2.addItem(
                                tripulante.getIdPersona().toString()
                        );
                    }
                }
            }

            for (Modelo.Terminal terminal : ce.getTerminales()) {
                String comuna = terminal.getDireccion().getComuna();

                if (!contiene(comboComunaOrigen, comuna)) {
                    comboComunaOrigen.addItem(comuna);
                    comboComunaDestino.addItem(comuna);
                }
            }

            if (comboBuses.getItemCount() == 0 ||
                    comboRutAuxiliar.getItemCount() == 0 ||
                    comboRutConductor1.getItemCount() == 0 ||
                    comboComunaOrigen.getItemCount() == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No hay suficientes datos para crear un viaje. " +
                                "Primero debe leer o recuperar los datos del sistema.",
                        "Datos no cargados",
                        JOptionPane.WARNING_MESSAGE
                );

                btnGuardarViaje.setEnabled(false);
            } else {
                btnGuardarViaje.setEnabled(true);
            }

        } catch (Exception e) {
            btnGuardarViaje.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible cargar los datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean contiene(JComboBox<String> combo, String elemento) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equalsIgnoreCase(elemento)) {
                return true;
            }
        }

        return false;
    }
}