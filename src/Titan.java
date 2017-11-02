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
    public String GetNombre(){ return nombre; }
    public String GetDistrito() {return distrito ;}
    public String GetTipo() { return tipo; }

    public String[] ObtenerInformacion(){
        String[] datos = new String[4];
        datos[0] = Integer.toString(id);
        datos[1] = tipo;
        datos[2] = distrito;
        datos[3] = nombre;
        return  datos;
    }
}
