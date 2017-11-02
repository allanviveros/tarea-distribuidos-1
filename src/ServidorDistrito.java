import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ServidorDistrito extends BaseComunicacion {
    ArrayList<Distrito> distritos = new ArrayList<Distrito>();

    private BufferedReader input;
    private int idUltimoTitan = 0;
	private final int largo = 1000;
    private final String prefijo = "[ServidorDistrito] ";
    private final String comando = "> ";
    public static final String rechazado = "No fue aceptado";
	private DatagramSocket socket;

	private String puerto_central;
	private String ip_central;
	private String puerto_central_distrito;
    
	public ServidorDistrito() throws SocketException {
        socket = new DatagramSocket();
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) throws IOException {
        ServidorDistrito servidorDistrito = new ServidorDistrito();
        servidorDistrito.RecibirParametros();
        servidorDistrito.Ciclo();
    }

    private void RecibirParametros() throws IOException {
        String[] mensajes = new String[3];
        String[] resultados = new String[3];
        mensajes[0] = "Ingresar IP Servidor Central";
        mensajes[1] = "Ingresar Puerto Servidor Central (peticiones clientes)";
        mensajes[2] = "Ingresar Puerto Servidor Central (actualizar distritos)";
        String texto_usuario;
        int i;
        for ( i = 0; i< mensajes.length; i++){
            System.out.println(prefijo+mensajes[i]);
            System.out.print(comando);
            texto_usuario = input.readLine();
            resultados[i] = texto_usuario;
        }
        ip_central = resultados[0];
        puerto_central = resultados[1];
        puerto_central_distrito = resultados[2];
    }

    private void Ciclo() throws IOException {
        while (true){
            System.out.println(prefijo+"Eliga una de las opciones:");
            System.out.println(prefijo+"(1) Crear distrito:");
            System.out.println(prefijo+"(2) Crear titan:");
            System.out.print(comando);
            String eleccion = input.readLine();
            switch (Integer.parseInt(eleccion)){
                case 1:
                    CrearDistrito();
                    break;
                case 2:
                    CrearTitan();
                    break;
                default:
                    System.out.println("Opcion incorrecta, intente de nuevo");
                    System.out.println("");
                    break;
            }
        }
    }
/*
    private void CrearDistritoTESTEO() throws IOException {
        String[] datos = new String[5];
	    datos[0] = "trost";
        datos[1] = "224.1.1.1";
        datos[2] = "32768";
        datos[3] = "192.168.1.10";
        datos[4] = "6002";
	    InetAddress ip_multicast = InetAddress.getByName(datos[1]);
	    int puerto_multicast = Integer.parseInt(datos[2]);
        InetAddress ip_peticiones = InetAddress.getByName(datos[3]);
        int puerto_peticiones = Integer.parseInt(datos[4]);
        Distrito distrito = new Distrito("trost", ip_multicast, puerto_multicast,
                ip_peticiones, puerto_peticiones);
        System.out.println("AQUI INICIO LOS SOCKKETS Y EL THERDA");
        distrito.IniciarSockets();


        distritos.add(distrito);
        String puerto = Integer.toString(ServidorCentral.puerto_crear_distrito);
        EnviarPaquete(datos, ServidorCentral.ip, puerto,socket);
    }
*/
    private void CrearDistrito() throws IOException {


        //String puerto = Integer.toString(ServidorCentral.puerto_crear_distrito);

        //System.out.println(ServidorCentral.ip+" / "+puerto);

        String[] datos = new String[5];
        String[] mensajes = new String[5];
        mensajes[0] = prefijo+"Inserte nombre servidor:";
        mensajes[1] = prefijo+"Inserte ip multicast:";
        mensajes[2] = prefijo+"Inserte puerto multicast: ";
        mensajes[3] = prefijo+"Inserte ip peticiones: ";
        mensajes[4] = prefijo+"Inserte puerto peticiones: ";;
        int i;
        for (i=0;i<datos.length; i++){
            System.out.println(mensajes[i]);
            System.out.print(comando);
            datos[i] = input.readLine();
        }
        InetAddress ip_multicast = InetAddress.getByName(datos[1]);
        int puerto_multicast = Integer.parseInt(datos[2]);
        InetAddress ip_peticiones = InetAddress.getByName(datos[3]);
        int puerto_peticiones = Integer.parseInt(datos[4]);
        Distrito distrito = new Distrito(datos[0], ip_multicast, puerto_multicast,
                ip_peticiones, puerto_peticiones);
        distritos.add(distrito);
        EnviarPaquete(datos, ip_central, puerto_central_distrito,socket);
        distrito.IniciarSockets();
    }

    private void CrearTitan() throws IOException {
        System.out.println(prefijo+"Inserte nombre del distrito:");
        System.out.print(comando);
        String nombre_distrito = input.readLine();
        int resultado_busqueda = BuscarDistrito(nombre_distrito);
        if(resultado_busqueda >= 0){
            Distrito distrito = distritos.get(resultado_busqueda);
            String prefijo_menu = Distrito.prefijo+distrito.GetNombre();
            System.out.println(prefijo_menu+"] Inserte nombre del titan:");
            System.out.print(comando);
            String nombre_titan = input.readLine();
            System.out.println(prefijo_menu+"] Inserte tipo del titan:");
            System.out.println(prefijo+"(1) Normal:");
            System.out.println(prefijo+"(2) Exentrico:");
            System.out.println(prefijo+"(3) Cambiante:");
            System.out.print(comando);
            String tipo_titan = input.readLine();

            switch (Integer.parseInt(tipo_titan)){
                case 1:
                    tipo_titan = "Normal";
                    break;
                case 2:
                    tipo_titan = "Excentrico";
                    break;
                case 3:
                    tipo_titan = "Cambiante";
                    break;
                default:
                    System.out.println("Opcion incorrecta, de dejo como tipo: Normal");
                    tipo_titan = "Normal";
                    break;
            }
            distrito.InstanciarTitan(idUltimoTitan,nombre_titan, tipo_titan);
            idUltimoTitan +=1;
        } else {
            String respuesta = "";
            if(resultado_busqueda == -1){
                respuesta = "no hay distritos existentes";
            }else if(resultado_busqueda == -2){
                respuesta = "no existe un distrito con ese nombre";
            }
            System.out.println("No se pudo: "+respuesta);
            System.out.println("");
        }
    }

    private int BuscarDistrito(String nombre){
        if(distritos.size() != 0){
            int i;
            for (i= 0; i < distritos.size(); i++){
                Distrito distrito = distritos.get(i);
                if (nombre.equals(distrito.GetNombre())){
                    return i;
                }
            }
            return -2; //no existe en la lista
        } else {
            return -1; //no hay elementos en la lista
        }
    }
}
