import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Distrito extends BaseComunicacion{
    private String nombre;
    private InetAddress ip_multicast;
    private int puerto_multicast;
    private InetAddress ip_unicast;
    private int puerto_unicast;

    public static final String prefijo = "[Distrito ";
    public static final String nuevo = "nuevo titan en el distrito";

    private DatagramSocket unisocket;
    private MulticastSocket multisocket;

    private MCServer enviar;

    private ArrayList<Titan> titanes = new ArrayList<Titan>();
    private RecibirPeticionesClientes recibir_peticiones;

    private Thread thread_peticiones;

    public Distrito(String nombre, InetAddress ip_multicast, int puerto_multicast,
                    InetAddress ip_unicast, int puerto_unicast) throws IOException {	
        this.nombre = nombre;
        this.ip_multicast = ip_multicast;
        this.puerto_multicast = puerto_multicast;
        this.ip_unicast = ip_unicast;
        this.puerto_unicast = puerto_unicast;

        enviar = new MCServer(ip_multicast, puerto_multicast, titanes);
        enviar.Inicio();
        recibir_peticiones = new RecibirPeticionesClientes(puerto_unicast, titanes);
    }


    /*
    public void TitanPorMulticast(Titan titan) throws IOException {
        DatagramSocket auxiliar = enviar.GetMulticastSocket();
        String[] datos = new String[5];
        datos[0] = nuevo;
        datos[1] = Integer.toString(titan.GetId());
        datos[2] = titan.GetTipo();
        datos[3] = titan.GetDistrito();
        datos[4] = titan.GetTipo();

        EnviarPaquete(datos, ip_multicast.toString().substring(1),
                Integer.toString(puerto_multicast), auxiliar);
    }*/

    public void IniciarSockets() throws IOException {
        //multisocket = new MulticastSocket(puerto_multicast);
        //multisocket.joinGroup(ip_multicast);

        Thread thread_titanes = new Thread(enviar, "Thread-EnviarTitanes-"+nombre);
        thread_titanes.start();



        thread_peticiones = new Thread(recibir_peticiones, "Thread-PeticionClientes-"+nombre);
        thread_peticiones.start();
    }
	
	public void InstanciarTitan(int idTitan, String nombreTitan, String tipoTitan) throws IOException {
        Titan titan = new Titan(idTitan, tipoTitan, nombre, nombreTitan);
        titanes.add(titan);
        //TitanPorMulticast(titan);
    }

	public String GetNombre(){
		return this.nombre;
	}

}