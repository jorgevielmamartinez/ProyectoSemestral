package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class VentanaVentas extends JFrame {

    private JPanel panelVentas;
    private JLabel txtBuscaOrigen;
    private JLabel txtBuscaDestino;
    private JTextField txtBuscaFecha;
    private JTextField txtBuscaCantidad;
    private JButton btnBuscarViajes;
    private JTable tablaViajes;
    private JTextField txtRutCliente;
    private JTextField txtRutPasajero;
    private JTextField txtNomPasajero;
    private JTextField txtNroAsiento;
    private JComboBox<String> comboPago;
    private JTextField txtTarjeta;
    private JButton btnFinalizarVenta;
    private JComboBox<String> comboComunaOrigen;
    private JComboBox<String> comboComunaDestino;
    private JLabel txtFecha;

    // Lista interna para mapear las filas de la tabla con los objetos Viaje reales
    private ArrayList<Modelo.Viaje> viajesList = new ArrayList<>();

    public VentanaVentas() {
        setTitle("Sistema de Buses - Consulta y Venta de Pasajes");
        setContentPane(panelVentas);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        btnFinalizarVenta.setEnabled(false);

        cargarComunasBusqueda();

        // Listener para activar el botón de venta solo cuando se seleccione un viaje válido
        tablaViajes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaViajes.getSelectedRow() != -1) {
                btnFinalizarVenta.setEnabled(true);
            } else {
                btnFinalizarVenta.setEnabled(false);
            }
        });

        // ==========================================
        // OPCIÓN: CONSULTAR VIAJES (INFORME DETALLADO)
        // ==========================================
        btnBuscarViajes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablaViajes.clearSelection();
                viajesList.clear(); // Limpiamos mapeo previo

                String origen = comboComunaOrigen.getSelectedItem() != null ? comboComunaOrigen.getSelectedItem().toString() : "";
                String destino = comboComunaDestino.getSelectedItem() != null ? comboComunaDestino.getSelectedItem().toString() : "";
                String textoFecha = txtBuscaFecha.getText().trim();
                String textoCantidad = txtBuscaCantidad.getText().trim();

                if (textoFecha.isEmpty() || textoCantidad.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese una fecha y la cantidad de asientos.", "Dato faltante", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int cantidadAsientos = Integer.parseInt(textoCantidad);
                    if (cantidadAsientos <= 0) {
                        JOptionPane.showMessageDialog(null, "La cantidad de asientos debe ser mayor a 0.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "La cantidad de asientos debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (origen.equals(destino)) {
                    JOptionPane.showMessageDialog(null, "El origen y el destino no pueden ser iguales.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    DateTimeFormatter formatoChile = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaBusqueda = LocalDate.parse(textoFecha, formatoChile);

                    // Inicialización segura del modelo de la tabla
                    DefaultTableModel modeloTabla = new DefaultTableModel(
                            new Object[][]{},
                            new String[]{"Origen", "Terminal Salida", "Destino", "Terminal Llegada", "Fecha", "Hora", "Patente", "Precio", "Asientos Disp."}
                    ) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    tablaViajes.setModel(modeloTabla);

                    // Mediante Reflexión obtenemos la lista privada de terminales directamente de la instancia activa del controlador
                    java.lang.reflect.Field fieldTerminales = Controlador.ControladorEmpresas.class.getDeclaredField("terminales");
                    fieldTerminales.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    ArrayList<Modelo.Terminal> listaTerminales = (ArrayList<Modelo.Terminal>) fieldTerminales.get(Controlador.ControladorEmpresas.getInstance());

                    boolean encontrados = false;

                    if (listaTerminales != null) {
                        for (Modelo.Terminal t : listaTerminales) {
                            // Filtro 1: Buscamos el terminal que pertenezca a la comuna de origen seleccionada
                            if (t.getDireccion().getComuna().equalsIgnoreCase(origen)) {
                                Modelo.Viaje[] salidas = t.getSalidas();
                                if (salidas != null) {
                                    for (Modelo.Viaje v : salidas) {
                                        // Filtro 2: Validamos la fecha del viaje y que la comuna del terminal de llegada coincida con el destino
                                        if (v.getFecha().equals(fechaBusqueda) &&
                                                v.getLlegada().getDireccion().getComuna().equalsIgnoreCase(destino)) {

                                            // Agregamos la fila con la información completa requerida
                                            modeloTabla.addRow(new Object[]{
                                                    origen,
                                                    v.getSalida().getNombre(),
                                                    destino,
                                                    v.getLlegada().getNombre(),
                                                    textoFecha,
                                                    v.getHora(),
                                                    v.getBus().getPatente(),
                                                    "$" + v.getPrecio(),
                                                    v.getNroAsientosDisponibles()
                                            });

                                            viajesList.add(v); // Guardamos la referencia real del objeto viaje
                                            encontrados = true;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!encontrados) {
                        JOptionPane.showMessageDialog(null, "No se encontraron viajes para la ruta y fecha seleccionada.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto. Por favor use: DD/MM/AAAA", "Error de fecha", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al procesar la consulta del sistema: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ==========================================
        // OPCIÓN: FINALIZAR VENTA + COMPROBANTE DE TICKET
        // ==========================================
        btnFinalizarVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rutCliente = txtRutCliente.getText().trim();
                String rutPasajero = txtRutPasajero.getText().trim();
                String nomPasajero = txtNomPasajero.getText().trim();
                String nroAsiento = txtNroAsiento.getText().trim();
                String tarjeta = txtTarjeta.getText().trim();
                String medioPago = comboPago.getSelectedItem() != null ? comboPago.getSelectedItem().toString() : "";

                if (rutCliente.isEmpty() || rutPasajero.isEmpty() || nomPasajero.isEmpty() || nroAsiento.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos obligatorios del pasajero y cliente.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if ((medioPago.equalsIgnoreCase("Crédito") || medioPago.equalsIgnoreCase("Débito")) && tarjeta.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Para pagos con tarjeta, el número de tarjeta es obligatorio.", "Validación de Pago", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (tarjeta.isEmpty()) {
                    tarjeta = "Efectivo/NA";
                }

                int filaSeleccionada = tablaViajes.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un viaje de la tabla para realizar la venta.", "Viaje no seleccionado", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    DefaultTableModel modelo = (DefaultTableModel) tablaViajes.getModel();
                    String origenViaje = modelo.getValueAt(filaSeleccionada, 0).toString();
                    String termSalida = modelo.getValueAt(filaSeleccionada, 1).toString();
                    String destinoViaje = modelo.getValueAt(filaSeleccionada, 2).toString();
                    String termLlegada = modelo.getValueAt(filaSeleccionada, 3).toString();
                    String fechaViaje = modelo.getValueAt(filaSeleccionada, 4).toString();
                    String horaViaje = modelo.getValueAt(filaSeleccionada, 5).toString();
                    String patenteBus = modelo.getValueAt(filaSeleccionada, 6).toString();
                    String precioViaje = modelo.getValueAt(filaSeleccionada, 7).toString();

                    // Obtenemos el objeto Viaje correspondiente desde nuestro mapeo dinámico
                    Modelo.Viaje viajeSeleccionado = viajesList.get(filaSeleccionada);
                    int asientoNum = Integer.parseInt(nroAsiento);

                    // Validación lógica de negocio en caliente usando el objeto real de la consulta
                    if (viajeSeleccionado.getNroAsientosDisponibles() <= 0) {
                        JOptionPane.showMessageDialog(null, "No quedan asientos disponibles en este viaje.", "Capacidad Máxima", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // GENERACIÓN DEL INFORME DETALLADO DE VENTA EXITOSA (TICKET)
                    String informeVenta = "====== INFORME DE VENTA EXITOSA ======\n\n" +
                            "🔹 DATOS DEL PASAJERO\n" +
                            "Nombre: " + nomPasajero + "\n" +
                            "RUT Pasajero: " + rutPasajero + "\n" +
                            "RUT Cliente/Comprador: " + rutCliente + "\n\n" +
                            "🔹 DATOS DEL VIAJE\n" +
                            "Ruta: " + origenViaje + " ➔ " + destinoViaje + "\n" +
                            "Terminal Salida: " + termSalida + "\n" +
                            "Terminal Llegada: " + termLlegada + "\n" +
                            "Fecha de salida: " + fechaViaje + "\n" +
                            "Hora de salida: " + horaViaje + "\n" +
                            "Bus (Patente): " + patenteBus + "\n" +
                            "Asiento N°: " + asientoNum + "\n\n" +
                            "🔹 DETALLE DE PAGO\n" +
                            "Monto total: " + precioViaje + "\n" +
                            "Medio de pago: " + medioPago + "\n" +
                            "Tarjeta finalizada en: " + (tarjeta.length() > 4 ? "****" + tarjeta.substring(tarjeta.length() - 4) : tarjeta) + "\n\n" +
                            "======================================";

                    JOptionPane.showMessageDialog(null, informeVenta, "Comprobante de Venta", JOptionPane.INFORMATION_MESSAGE);

                    // Limpieza automática para una nueva venta limpia
                    txtRutCliente.setText("");
                    txtRutPasajero.setText("");
                    txtNomPasajero.setText("");
                    txtNroAsiento.setText("");
                    txtTarjeta.setText("");
                    btnBuscarViajes.doClick(); // Refresca dinámicamente el stock de asientos libres en la grilla

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El número de asiento debe ser un valor numérico válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al procesar la transacción: " + ex.getMessage(), "Excepción", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void cargarComunasBusqueda() {
        comboComunaOrigen.removeAllItems();
        comboComunaDestino.removeAllItems();

        String rutaArchivo = "SVPDatosIniciales.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] partes = linea.split(";");
                if (partes.length < 2) continue;

                String identificador = partes[0].toUpperCase().trim();

                if (identificador.contains("TERMINAL") || identificador.contains("COMUNA")) {
                    String comuna = (partes.length > 2) ? partes[2].trim() : partes[1].trim();

                    if (!comuna.isEmpty()) {
                        if (((DefaultComboBoxModel<String>)comboComunaOrigen.getModel()).getIndexOf(comuna) == -1) {
                            comboComunaOrigen.addItem(comuna);
                            comboComunaDestino.addItem(comuna);
                        }
                    }
                }
            }
        } catch (Exception e) {}

        if (comboComunaOrigen.getItemCount() == 0) {
            String[] comunasRespaldo = {"Concepción", "Chillán", "Santiago", "Los Ángeles", "Temuco"};
            for (String c : comunasRespaldo) {
                comboComunaOrigen.addItem(c);
                comboComunaDestino.addItem(c);
            }
        }
    }
}