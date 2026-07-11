package Utilidades;

import java.io.Serializable;
import java.util.Objects;

public class Rut implements IdPersona, Serializable {

    private final int numero;
    private final char dv;

    private Rut(int numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public static Rut of(String rutConDv) {

        String rutLimpio = rutConDv.replace(".", "")
                .replace("-", "");

        String rutNumero = rutLimpio.substring(0, rutLimpio.length() - 1);
        char dv = rutLimpio.charAt(rutLimpio.length() - 1);

        int numero = Integer.parseInt(rutNumero);

        return new Rut(numero, dv);
    }

    @Override
    public String toString() {
        String numeroStr = String.valueOf(numero);
        String nuevoNumero = "";
        int contador = 0;

        for (int i = numeroStr.length() - 1; i >= 0; i--) {
            nuevoNumero += numeroStr.charAt(i);
            contador++;

            if (contador % 3 == 0 && i != 0) {
                nuevoNumero += ".";
            }
        }

        nuevoNumero = new StringBuilder(nuevoNumero).reverse().toString();

        return nuevoNumero + "-" + dv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rut rut)) return false;
        return numero == rut.numero && dv == rut.dv;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, dv);
    }
}