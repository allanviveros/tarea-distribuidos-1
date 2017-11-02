
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MCServer implements Runnable {
    int puerto_multicast;
    InetAddress ip_multicast;
    private MulticastSocket socket_multi;
    private ArrayList<Titan> titanes;
    public MCServer(InetAddress ip_multicast, int puerto_multicast, ArrayList<Titan> titanes) {
        this.ip_multicast = ip_multicast;
        this.puerto_multicast = puerto_multicast;
        this.titanes = titanes;
    }

    public void Inicio() throws IOException {

        //Creamos el MulticastSocket sin especificar puerto.
        socket_multi = new MulticastSocket();

        }


    @Override
    public void run() {
        DatagramPacket mensaje_enviar;
        while (true) {
            try {
                //se prepara el mensaje
                ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
                DataOutput output = new DataOutputStream(output_stream);
                int numero_titanes = titanes.size();
                output.writeInt(numero_titanes);
                int i;
                if (titanes.size() != 0) {
                    for (i = 0; i < titanes.size(); i++) {
                        Titan titan = titanes.get(i);
                        output.writeInt(titan.GetId());
                        output.writeUTF(titan.GetNombre());
                        output.writeUTF(titan.GetDistrito());
                        output.writeUTF(titan.GetTipo());
                    }
                }
                byte[] mensaje = output_stream.toByteArray();
                mensaje_enviar = new DatagramPacket(mensaje, mensaje.length,
                        ip_multicast, puerto_multicast);
                socket_multi.send(mensaje_enviar);
                Thread.sleep(100000);

            } catch (InterruptedException ie) {
                ie.printStackTrace();

            } catch (IOException ioe) {
                ioe.printStackTrace();

            }
        }

    }
}