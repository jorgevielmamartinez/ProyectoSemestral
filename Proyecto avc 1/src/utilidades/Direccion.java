package utilidades;

public class Direccion {
    private String calle;
    private int numero;
    private String comuna;

    public Direccion(String calle, int numero, String comuna) {
        this.calle = calle;
        this.numero = numero;
    }
    public String getCalle() {
        return calle;
    }

    public int getNumero() {
        return numero;
    }

    public String getComuna() {
        return comuna;
    }

    @Override
    public String toString() {return "Calle: " + calle + " numero: " + numero + "comuna: " + comuna ;}

    @Override
    public boolean equals (Object otro){
        // 1. ¿Es el mismo objeto?
        if (this == otro) return true;

        // 2. ¿Es nulo o de otra clase?
        if (otro == null || getClass() != otro.getClass()) return false;

        // 3. Convertir para comparar atributos
        Direccion esaDireccion = (Direccion) otro;

        // 4. Comparar cada campo de la estructura
        return numero == esaDireccion.numero &&
                calle.equals(esaDireccion.calle) &&
                comuna.equals(esaDireccion.comuna);
    }
}
