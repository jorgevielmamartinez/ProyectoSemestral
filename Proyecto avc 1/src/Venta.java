import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;
    private ArrayList<Pasaje> pasajes;
    public Venta(String id,TipoDocumento tipo,LocalDate fec,Cliente cliente){
        this.idDocumento=id;
        this.tipo=tipo;
        this.fecha=fec;
        this.cliente=cliente;
        this.pasajes=new ArrayList<>();
    }
    public String getIdDocumento(){
        return idDocumento;
    }
    public TipoDocumento getTipo(){
        return tipo;
    }
    public LocalDate getFecha(){
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public void createPasaje(int asiento,Viaje viaje,Pasajero pasajero){
        Pasaje p=new Pasaje(asiento,viaje,pasajero,this);  //el this es para pasar la venta que pide el constructor de pasaje.
        pasajes.add(p);
    }
    public Pasaje[] getPasajes(){
        return pasajes.toArray(new Pasaje[0]);
    }
    public int getMonto(){
     int total=0;
     for(Pasaje p: pasajes){
         total+=p.getViaje().getPrecio();   //pasaje conecta con viaje,de ahi que uso esto
     }
     return total;
    }
}
