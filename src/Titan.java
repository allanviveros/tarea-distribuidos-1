public class Titan {
    private int id;
    private String tipo;
    private String distrito;
    private String nombre;

    public Titan(int id, String tipo,String distrito,String nombre){
        this.id = id;
        this.tipo = tipo;
        this.distrito = distrito;
        this.nombre = nombre;
    }

    public int GetId(){
        return id;
    }
}
