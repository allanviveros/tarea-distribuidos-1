import java.io.*;
import java.net.*;
import java.util.List;

public class ServidorCentral extends BaseComunicacion{
    //listas
    private ListaServidorCliente lista_servidor_cliente;
    //variables
    public  int puerto; //recibe mensajes de los clientes sobre obtener info de distritos
    public  int puerto_crear_distrito; //reciibe mensajes para cuando crean distritos
    private final int largo = 1000;
    public static String ip ;
    private final String prefijo = "[ServidorCentral] ";
    private final String comando = "> ";
    public static final String rechazado = "No fue aceptado";
    private DatagramSocket socket;
    private BufferedReader input;
    private ActualizarDistritos actualizador;
    private Thread thread_actualizador;

    public static void main(String[] args) throws IOException {
        ServidorCentral servidor_central = new ServidorCentral();
        servidor_central.Inicio();
    }

    public ServidorCentral() throws IOException {
        lista_servidor_cliente = new ListaServidorCliente(prefijo);
        input = new BufferedReader(new InputStreamReader(System.in));
        RecibirParametros();

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
        ip = resultados[0];
        puerto = Integer.parseInt(resultados[1]);
        puerto_crear_distrito = Integer.parseInt(resultados[2]);
        actualizador = new ActualizarDistritos(puerto_crear_distrito,
                lista_servidor_cliente, prefijo);
    }

    private void Inicio() throws IOException {
        socket = new DatagramSocket(puerto);
        thread_actualizador = new Thread(actualizador, "Thread-ActualizadorDistrito");
        thread_actualizador.start();
        while (true) {
            lista_servidor_cliente.DesplegarInformacion();
            String[] mensaje_info = RecibirPaqueteInfo(socket);
            Eleccion(mensaje_info);
        }
    }

    private void Eleccion(String[] mensaje_info) throws IOException {
        String[] mensajes = new String[3];
        String resultado;
        mensajes[0] = "Autorizar a ";
        mensajes[1] = " con el distrito: ";
        mensajes[2] = "\n1) SI\n2) NO";
        String texto_usuario;
        System.out.println(prefijo + mensajes[0] + mensaje_info[0] +
                mensajes[1] + mensaje_info[2] + mensajes[2]);
        System.out.print(comando);
        texto_usuario = input.readLine();
        resultado = texto_usuario;
        int indice = Integer.parseInt(resultado);
        if (indice == 1) {
            Aceptar(mensaje_info);
        } else if (indice == 2) {
            Rechazar(mensaje_info);
        } else {
            System.out.print("Accion incorrecta, se considera rechazado");
            Rechazar(mensaje_info);
        }
    }

    private void Aceptar(String[] mensaje_info) throws IOException {
        int i;
        String[] parametros_distrito;
        parametros_distrito = lista_servidor_cliente.AñadirCliente(
                mensaje_info[0], mensaje_info[2]);
        if (parametros_distrito[0].equals(ListaServidorCliente.error)) {
            System.out.println("No existe el distrito al cual desea conectarse, es rechazado");
            Rechazar(mensaje_info);
        } else {
            EnviarPaquete(parametros_distrito, mensaje_info[0], mensaje_info[1], socket);
        }
    }

    private void Rechazar(String[] mensaje_info) throws IOException {
        EnviarPaquete(rechazado, mensaje_info[0], mensaje_info[1], socket);
    }
}