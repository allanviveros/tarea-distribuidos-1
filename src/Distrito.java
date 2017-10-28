import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Distrito {
    private String nombre;
    private InetAddress ip_multicast;
    private int puerto_multicast;
    private InetAddress ip_unicast;
    private int puerto_unicast;

    private DatagramSocket unisocket;
    private MulticastSocket multisocket;

    private ArrayList<Titan> titanes = new ArrayList<Titan>();

    public Distrito(String nombre, InetAddress ip_multicast, int puerto_multicast,
                    InetAddress ip_unicast, int puerto_unicast) throws IOException {
        this.nombre = nombre;
        this.ip_multicast = ip_multicast;
        this.puerto_multicast = puerto_multicast;
        this.ip_unicast = ip_unicast;
        this.puerto_unicast = puerto_unicast;
        IniciarSockets();
    }

    public void IniciarSockets() throws IOException {
        unisocket = new DatagramSocket(puerto_unicast);
        multisocket = new MulticastSocket(puerto_multicast);
        multisocket.joinGroup(ip_multicast);
    }


}
