

public class Rut implements IdPersona {
    private int num;
    private char dv;
    private Rut(int num, char dv) {
        this.num = num;
        this.dv = dv;

    }
    public int getNum() {
        return num;
    }
    public char getDv() {
        return dv;
    }
    public static Rut of(String rutConDv){
        rutConDv = rutConDv.replace(". ", "");
        String[] dvs = rutConDv.split("-");
        int num = Integer.parseInt(dvs[0]);
        char dv = dvs[1].toUpperCase().charAt(0);
        return new Rut(num, dv);
    }
    public String toString(){
            return num+"-"+dv;
    }
    public boolean equals(Rut rut){
        return this.num == rut.num && this.dv == rut.dv;
    }
}
