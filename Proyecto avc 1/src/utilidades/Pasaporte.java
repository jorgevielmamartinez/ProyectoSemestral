package utilidades;

public class Pasaporte implements IdPersona {
    private String numero;
    private String nacionalidad;
    public Pasaporte(String numero, String nacionalidad) {
        this.numero = numero;
        this.nacionalidad = nacionalidad;
    }

    public String getNumero() {
        return numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }
    public static Pasaporte of(String nacionalidad, String numero) {
        if (nacionalidad==null || numero==null) return null;
        return new Pasaporte(numero,nacionalidad);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pasaporte pasaporte = (Pasaporte) obj;

        return numero.equals(pasaporte.numero) && nacionalidad.equals(pasaporte.nacionalidad);
    }

    public String toString(){
        return numero+"   "+nacionalidad;
    }
}
