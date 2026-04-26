

import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

public class Rut implements IdPersona {
    private final long numero;
    private final char dv;

    private Rut(long numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }
    public static Rut of(String rutStr) {
        String rutLimpio=rutStr.replace(".","").replace("-","");
        String rutLong= rutLimpio.substring(0,rutLimpio.length()-1);
        String rutDV=rutLimpio.substring(rutLimpio.length()-1);
        long numero;
        char dv;
            numero = Long.parseLong(rutLong);
            dv=rutDV.charAt(0);
            return new Rut(numero,dv);
    }
    @Override
    public String toString() {
        String numeroStr = String.valueOf(numero);
        String newNumeroStr = "";
        char digito;
        int conDigitos = 0;
        for (int x = numeroStr.length() - 1; x >= 0; x--) {
            digito = numeroStr.charAt(x);
            newNumeroStr = newNumeroStr + digito;
            conDigitos++;
            if (conDigitos % 3 == 0 && x !=0) {
                newNumeroStr = newNumeroStr + ".";
            }
        }
        newNumeroStr = new StringBuilder(newNumeroStr).reverse().toString();
        return newNumeroStr+"-"+dv;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rut rut = (Rut) o;
        return numero == rut.numero && dv == rut.dv;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, dv);
    }
}
