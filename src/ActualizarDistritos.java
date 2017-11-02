import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ActualizarDistritos extends BaseComunicacion implements Runnable {
    /*esta clase debe ser ejecutada mediante un thread para que funciones
    es parte de la funcionalidad del servidor central, para la actualizacion de distritos
     */

    ListaServidorCliente lista;
    int puerto;
    private DatagramSocket socket;
    String prefijo;

    public ActualizarDistritos(int puerto, ListaServidorCliente lista, String prefijo) throws SocketException {
        this.puerto = puerto;
        this.lista = lista;
        this.prefijo = prefijo;
        socket = new DatagramSocket(puerto);
    }

    @Override
    public void run() {
        String[] datos;
        while(true){
            try {
                datos = RecibirPaquete(5,socket);
                lista.AñadirDistritos(datos[0], datos[1], datos[2], datos[3], datos[4]);
                System.out.println(prefijo+"Se agrego un nuevo distrito: "+datos[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
