import java.io.*;
import java.net.*;
 
//Para probarlo, lanzar el MCServer y despues crear tantas 
//instancias de este como se quiera, espera a que haga el 
//join al grupo tarda unos segundos, y comienza a escribir 
//en la instancia del servidor (lo escrito saldrá en todos 
//estos clientes creados).
 
public class MCClient implements Runnable {

    int puerto_multicast;
    InetAddress ip_multicast;
    private MulticastSocket socket_multi;

    public  MCClient(InetAddress ip_multicast, int puerto_multicast){
        this.ip_multicast = ip_multicast;
        this.puerto_multicast = puerto_multicast;
    }

    public void Iniciar() throws IOException {
        socket_multi = new MulticastSocket(puerto_multicast);
        socket_multi.joinGroup(ip_multicast);
    }

    @Override
    public void run() {
        while (true) {
            byte[] buffer = new byte[1000];
            ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer);
            DatagramPacket mensaje_recibido = new DatagramPacket(buffer, buffer.length);

            try {
                socket_multi.receive(mensaje_recibido);
                //tomamos los parametros del datagrama
                DataInput input = new DataInputStream(stream_input);


                //Primer input me dice cuantos titanes a listar
                int numero_titanes = input.readInt();
                if(numero_titanes != 0) {
                    for (int i = 0; i < numero_titanes; i++) {
                        System.out.println("");
                        System.out.println("Id: " + input.readInt());
                        System.out.println("Nombre: " + input.readUTF());
                        System.out.println("Distrito: " + input.readUTF());
                        System.out.println("Tipo: " + input.readUTF());
                        System.out.println("");
                    }
                } else {
                    System.out.println("No hay titanes");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
/*
        //Salimos del grupo multicast
        socket_multi.leaveGroup(group);

        // Cerramos el socket:
        socket_multi.close();
*/

    }
}
