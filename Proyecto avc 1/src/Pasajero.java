public class Pasajero {
    private Nombre nomContacto;
    private String fonoContacto;

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
