package Vista;

import Controlador.ControladorEmpresas;
import Excepciones.SVPException;
import Modelo.Terminal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaConsultaTerminales extends JFrame {

    private JComboBox<String> comboTerminales;
    private JTextField txtFecha;
    private JButton btnConsultar;
    private JTable tablaMovimientos;

    public VentanaConsultaTerminales() {
        setTitle("Llegadas y salidas de un terminal");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        comboTerminales = new JComboBox<>();
        txtFecha = new JTextField(10);
        btnConsultar = new JButton("Consultar");
        tablaMovimientos = new JTable();

        JPanel panelFiltros = new JPanel(new FlowLayout());

        panelFiltros.add(new JLabel("Terminal:"));
        panelFiltros.add(comboTerminales);
        panelFiltros.add(new JLabel("Desde fecha (dd/MM/yyyy):"));
        panelFiltros.add(txtFecha);
        panelFiltros.add(btnConsultar);

        add(panelFiltros, BorderLayout.NORTH);
        add(
                new JScrollPane(tablaMovimientos),
                BorderLayout.CENTER
        );

        cargarTerminales();

        btnConsultar.addActionListener(e ->
                consultarMovimientos()
        );
    }

    private void cargarTerminales() {
        comboTerminales.removeAllItems();

        for (Terminal terminal :
                ControladorEmpresas
                        .getInstance()
                        .getTerminales()) {

            comboTerminales.addItem(
                    terminal.getNombre()
            );
        }

        if (comboTerminales.getItemCount() == 0) {
            btnConsultar.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    "No hay terminales cargados.",
                    "Sin terminales",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void consultarMovimientos() {
        String terminal =
                (String) comboTerminales.getSelectedItem();

        String fechaTexto =
                txtFecha.getText().trim();

        if (terminal == null || fechaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un terminal e ingrese una fecha.",
                    "Datos faltantes",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(
                    fechaTexto,
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy"
                    )
            );

            String[][] datos =
                    ControladorEmpresas
                            .getInstance()
                            .listLlegadaSalidasTerminal(
                                    terminal,
                                    fecha
                            );

            String[] columnas = {
                    "Movimiento",
                    "Hora",
                    "Patente",
                    "Empresa",
                    "Pasajeros"
            };

            DefaultTableModel modelo =
                    new DefaultTableModel(
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

            tablaMovimientos.setModel(modelo);

            if (datos.length == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No existen llegadas o salidas "
                                + "desde la fecha indicada.",
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe usar el formato DD/MM/AAAA.",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error de consulta",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}