class Nombre{
    private Tratamiento tratamiento;
    private String nombres;
    private String apellidoMaterno;
    private String apellidoPaterno;

    public Tratamiento getTratamiento() {
        return tratamiento;
    }
    public void setTratamiento(Tratamiento tratamiento){
        this.tratamiento=tratamiento;
    }
    public String getNombres(){
        return nombres;
    }
    public void setNombres(String nombres){
        this.nombres=nombres;
    }
    public String getApellidoPaterno(){
        return apellidoPaterno;
    }
    public void setApellidoPaterno(String apellidoPaterno){
        this.apellidoPaterno=apellidoPaterno;
    }
    public String getApellidoMaterno(){
        return apellidoMaterno;
    }
    public void setApellidoMaterno(String apellidoMaterno){
        this.apellidoMaterno=apellidoMaterno;
    }
    public String toString(){
        return nombres+","+tratamiento+","+apellidoPaterno+","+apellidoMaterno;
    }

    public boolean equals(Nombre nombre) {
        return nombres==nombre.nombres && tratamiento==nombre.tratamiento&&apellidoPaterno==nombre.apellidoPaterno && apellidoMaterno==nombre.apellidoMaterno;
    }
}