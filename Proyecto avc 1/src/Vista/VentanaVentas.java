package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JComboBox<Modelo.TipoDocumento> comboTipoDocumento;
    private JTextField txtIdDocumento;

    // Lista interna para mapear las filas de la tabla con los objetos Viaje reales
    private ArrayList<Modelo.Viaje> viajesList = new ArrayList<>();

    public VentanaVentas() {
        setTitle("Sistema de Buses - Consulta y Venta de Pasajes");
        setContentPane(panelVentas);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        btnFinalizarVenta.setEnabled(false);

        comboTipoDocumento.setModel(
                new DefaultComboBoxModel<>(
                        Modelo.TipoDocumento.values()
                )
        );

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

                int cantidadAsientos;

                try {
                    cantidadAsientos = Integer.parseInt(textoCantidad);

                    if (cantidadAsientos <= 0) {
                        JOptionPane.showMessageDialog(
                                VentanaVentas.this,
                                "La cantidad de asientos debe ser mayor a 0.",
                                "Dato inválido",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            VentanaVentas.this,
                            "La cantidad de asientos debe ser un número válido.",
                            "Error de formato",
                            JOptionPane.ERROR_MESSAGE
                    );
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
                                        if (v.getFecha().equals(fechaBusqueda)
                                                && v.getLlegada().getDireccion().getComuna()
                                                .equalsIgnoreCase(destino)
                                                && v.existeDisponibilidad(cantidadAsientos)) {

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
                        JOptionPane.showMessageDialog(null, "No se encontraron viajes que cumplan con la ruta, fecha " +
                                "y cantidad de asientos solicitada.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
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

                String idDocumento =
                        txtIdDocumento.getText().trim();

                Modelo.TipoDocumento tipoDocumento =
                        (Modelo.TipoDocumento)
                                comboTipoDocumento.getSelectedItem();

                if (idDocumento.isEmpty()
                        || tipoDocumento == null
                        || rutCliente.isEmpty()
                        || rutPasajero.isEmpty()
                        || nomPasajero.isEmpty()
                        || nroAsiento.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            VentanaVentas.this,
                            "Complete el documento, cliente, pasajero y asiento.",
                            "Faltan datos",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                if ((medioPago.equalsIgnoreCase("Crédito") || medioPago.equalsIgnoreCase("Débito")) && tarjeta.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Para pagos con tarjeta, el número de tarjeta es obligatorio.", "Validación de Pago", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String detalleTarjeta;

                if (medioPago.equalsIgnoreCase("Efectivo")) {
                    detalleTarjeta = "No aplica";
                } else {
                    detalleTarjeta = tarjeta.length() > 4
                            ? "****" + tarjeta.substring(tarjeta.length() - 4)
                            : tarjeta;
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
                    Modelo.Viaje viajeSeleccionado =
                            viajesList.get(filaSeleccionada);

                    int asientoNum =
                            Integer.parseInt(nroAsiento);

                    if (asientoNum < 1
                            || asientoNum > viajeSeleccionado.getBus().getNroAsientos()) {

                        JOptionPane.showMessageDialog(
                                VentanaVentas.this,
                                "El asiento debe estar entre 1 y "
                                        + viajeSeleccionado.getBus().getNroAsientos() + ".",
                                "Asiento inválido",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    if (!viajeSeleccionado.estaAsientoDisponible(asientoNum)) {
                        JOptionPane.showMessageDialog(
                                VentanaVentas.this,
                                "El asiento seleccionado ya está ocupado.",
                                "Asiento ocupado",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    
                    if (viajeSeleccionado.getNroAsientosDisponibles() <= 0) {
                        JOptionPane.showMessageDialog(
                                VentanaVentas.this,
                                "No quedan asientos disponibles en este viaje.",
                                "Capacidad máxima",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    /*
                     * DESDE AQUÍ COMIENZA LA VENTA REAL
                     */

                    Controlador.SistemaVentaPasajes sistema =
                            Controlador.SistemaVentaPasajes.getInstance();

// Convertir la fecha mostrada en la tabla
                    LocalDate fechaSeleccionada =
                            LocalDate.parse(
                                    fechaViaje,
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            );

// Convertir la hora mostrada en la tabla
                    java.time.LocalTime horaSeleccionada =
                            java.time.LocalTime.parse(horaViaje);

// Convertir los RUT a IdPersona
                    Utilidades.IdPersona idCliente =
                            Utilidades.Rut.of(rutCliente);

                    Utilidades.IdPersona idPasajero =
                            Utilidades.Rut.of(rutPasajero);

// 1. Iniciar la venta
                    sistema.iniciaVenta(
                            idDocumento,
                            tipoDocumento,
                            fechaSeleccionada,
                            origenViaje,
                            destinoViaje,
                            idCliente,
                            1
                    );

// 2. Registrar el pasaje
                    sistema.vendePasaje(
                            idDocumento,
                            tipoDocumento,
                            fechaSeleccionada,
                            horaSeleccionada,
                            patenteBus,
                            asientoNum,
                            idPasajero
                    );

// 3. Registrar el pago
                    if (medioPago.equalsIgnoreCase("Efectivo")) {

                        sistema.pagaVenta(
                                idDocumento,
                                tipoDocumento
                        );

                    } else {

                        String tarjetaLimpia =
                                tarjeta.replaceAll("[^0-9]", "");

                        long numeroTarjeta =
                                Long.parseLong(tarjetaLimpia);

                        sistema.pagaVenta(
                                idDocumento,
                                tipoDocumento,
                                numeroTarjeta
                        );
                    }

// 4. Generar el archivo del pasaje
                    sistema.generatePasajesVenta(
                            idDocumento,
                            tipoDocumento
                    );

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
                            "Tarjeta: " + detalleTarjeta + "\n\n" +
                            "Monto total: " + precioViaje + "\n" +
                            "Medio de pago: " + medioPago + "\n" +
                            "Tarjeta: " + detalleTarjeta + "\n\n" +
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

                    JOptionPane.showMessageDialog(
                            VentanaVentas.this,
                            "El asiento y el número de tarjeta deben contener solamente números.",
                            "Error de formato",
                            JOptionPane.ERROR_MESSAGE
                    );

                } catch (Excepciones.SVPException ex) {

                    JOptionPane.showMessageDialog(
                            VentanaVentas.this,
                            ex.getMessage(),
                            "No se pudo completar la venta",
                            JOptionPane.WARNING_MESSAGE
                    );

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            VentanaVentas.this,
                            "Error al procesar la transacción: "
                                    + ex.getMessage(),
                            "Error inesperado",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    public void cargarComunasBusqueda() {
        comboComunaOrigen.removeAllItems();
        comboComunaDestino.removeAllItems();

        Controlador.SistemaVentaPasajes sistema =
                Controlador.SistemaVentaPasajes.getInstance();

        String[] comunas = sistema.getComunasConViajes();

        if (comunas.length == 0) {
            btnBuscarViajes.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    "No hay viajes cargados. Primero debe leer los datos iniciales.",
                    "Datos no cargados",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        for (String comuna : comunas) {
            comboComunaOrigen.addItem(comuna);
            comboComunaDestino.addItem(comuna);
        }

        btnBuscarViajes.setEnabled(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}