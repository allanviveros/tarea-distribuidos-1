import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorDistrito {
    //necesito una lista de distritos
    ArrayList<Distrito> distritos = new ArrayList<Distrito>();


    public static void main(String[] args) throws IOException {
        ServidorDistrito servidor_central = new ServidorDistrito();
        servidor_central.Inicio();
    }

    public ServidorDistrito(){

    }

    private void Inicio(){

    }

    private void AccionCliente(int eleccion) throws IOException {
        switch (eleccion){
            case 1:
                CrearDistrito();
                //ConexionDistrito();
                break;
            case 2:
                ListarDistritos();
                //ConexionDistrito();
                break;
            case 3:
                CapturarTitan();
                //ConexionDistrito();
                break;
            default:
                System.out.println("NO DEBERIA PASAR");
                //ConexionDistrito();
                break;
        }
    }

    private void CrearDistrito(){
        //crear distrito y mandarselo al servidor central
    }

    private void ListarDistritos(){

    }


}
