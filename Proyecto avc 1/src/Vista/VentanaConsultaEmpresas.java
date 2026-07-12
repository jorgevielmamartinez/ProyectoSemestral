package Vista;

import Controlador.ControladorEmpresas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaConsultaEmpresas extends JFrame {

    private JTable tablaEmpresas;

    public VentanaConsultaEmpresas() {
        setTitle("Consulta de Empresas");
        setSize(750, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tablaEmpresas = new JTable();

        String[] columnas = {
                "RUT",
                "Nombre",
                "Sitio web",
                "Tripulantes",
                "Buses",
                "Ventas"
        };

        String[][] datos =
                ControladorEmpresas
                        .getInstance()
                        .listEmpresas();

        DefaultTableModel modelo =
                new DefaultTableModel(datos, columnas) {
                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna
                    ) {
                        return false;
                    }
                };

        tablaEmpresas.setModel(modelo);

        JLabel titulo =
                new JLabel(
                        "Empresas registradas",
                        SwingConstants.CENTER
                );

        add(titulo, BorderLayout.NORTH);
        add(
                new JScrollPane(tablaEmpresas),
                BorderLayout.CENTER
        );

        if (datos.length == 0) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                            this,
                            "No hay empresas cargadas.",
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE
                    )
            );
        }
    }
}