public class Pasajero extends Persona{
    private Nombre nomContacto;
    private String fonoContacto;

    public Pasajero(IdPersona id, Nombre nombre) {
        super(id, nombre);
    }

    public Nombre getNomContacto(){
        return nomContacto;
    }

    public void setNomContacto(Nombre nom){
        this.nomContacto = nom;
    }
    public String getFonoContacto(){
        return fonoContacto;
    }

    public void setFonoContacto(String fono){
        this.fonoContacto = fono;
    }
}
