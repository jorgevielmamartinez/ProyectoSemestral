import java.util.ArrayList;

public class Cliente extends Persona {
    private String email;
    ArrayList<Venta> ventas = new ArrayList();
    public Cliente(IdPersona id, Nombre nom, String email){
        super(id,nom);
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void addVenta(Venta venta){
        for (Venta v:ventas){
            if(v.getCliente().equals(getIdPersona())){
                addVenta(v);
            }
        }
    }
}
