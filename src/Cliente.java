import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends BaseComunicacion {
    //listas
    private ArrayList<Titan> titanes_capturados = new ArrayList<Titan>();
    private ArrayList<Titan> titanes_asesinados = new ArrayList<Titan>();
    //parametros
    private final String prefijo = "[CLIENTE] ";
    private final String comando = "> ";
    private final int largo = 1000;
    private BufferedReader input;
    private DatagramSocket socket;
    private MulticastSocket socket_multicast;
    private boolean cambiar_distrito = false;
    private boolean primera = true;


    private MCClient receptor;
    //static
    public static final String cambio = "cambiar de distrito";
    // ip: 127.0.0.1
    //puerto central = 6001

    public Cliente() throws SocketException {
        input = new BufferedReader(new InputStreamReader(System.in));
        socket = new DatagramSocket();
    }

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente();
        cliente.Inicio();
    }

    private void InicializarHilo(String[] parametros) throws IOException {
        if(primera){
            InetAddress ip_mult_aux = InetAddress.getByName(parametros[1]);
            int puerto_multi_aux = Integer.parseInt(parametros[2]);
            receptor = new MCClient(ip_mult_aux,puerto_multi_aux);
            receptor.Iniciar();
            new Thread(receptor,"Thread-Receptor").start();
            primera = false;
        }
    }

    private void Inicio() throws IOException {
        String[] parametros;
        while (true) {
            parametros = RecibirParametros();
            EnviarPaquete(parametros[2],parametros[0],parametros[1],socket);

            String[] mensaje_recibido = RecibirPaquete(5,socket);
            if (mensaje_recibido[0].equals(ServidorCentral.rechazado)) {
                System.out.println("Fue rechazado, puede intentarlo de nuevo");
            } else {
                while (!cambiar_distrito) {
                    Opciones(mensaje_recibido);
                }
                cambiar_distrito = false;
            }
        }
    }

    //SOLO PARA TESTEO
    private String[] RecibirParametrosTESTEO() throws IOException {
        String[] mensajes = new String[3];
        String[] resultados = new String[3];
        mensajes[0] = "Ingresar IP Servidor Central";
        mensajes[1] ="Ingresar Puerto Servidor Central";
        mensajes[2] = "Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina";
        String texto_usuario;
        int i;
        resultados[0] = "127.0.0.1";
        resultados[1] = "6001";
        for ( i = 2; i< mensajes.length; i++){
            System.out.println(prefijo+mensajes[i]);
            System.out.print(comando);
            texto_usuario = input.readLine();
            resultados[i] = texto_usuario;
        }
        return resultados;
    }

    private String[] RecibirParametros() throws IOException {
        String[] mensajes = new String[3];
        String[] resultados = new String[3];
        String texto_usuario;
        mensajes[0] = "Ingresar IP Servidor Central";
        mensajes[1] ="Ingresar Puerto Servidor Central";
        mensajes[2] = "Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina";
        int i;
        for ( i = 0; i< mensajes.length; i++){
            System.out.println(prefijo+mensajes[i]);
            System.out.print(comando);
            texto_usuario = input.readLine();
            resultados[i] = texto_usuario;
        }
        return resultados;
    }

    private void Opciones(String[] parametros) throws IOException {
        //System.out.println(parametros[3]);
        //int puerto_multicast = Integer.parseInt(parametros[2]);
        //socket_multicast = new MulticastSocket(puerto_multicast);
        //deberia ir un thread?
        InicializarHilo(parametros);

        int i;
        String texto_usuario;
        String[] mensajes = new String[7];
        mensajes[0] = "Consola";
        mensajes[1] = "(1) Listar Titanes";
        mensajes[2] = "(2) Cambiar Distrito";
        mensajes[3] = "(3) Capturar Titan";
        mensajes[4] = "(4) Asesinar Titan";
        mensajes[5] = "(5) Listar Titanes Capturados";
        mensajes[6] = "(6) Listar Titanes Asesinados";
        for ( i = 0; i< mensajes.length; i++){
            System.out.println(prefijo+mensajes[i]);
        }
        System.out.print(comando);
        texto_usuario = input.readLine();
        AccionCliente(Integer.parseInt(texto_usuario),parametros);
    }

    private void AccionCliente(int eleccion, String[] parametros) throws IOException {
        switch (eleccion){
            case 1:
                ListarTitanes(parametros);
                break;
            case 2:
                CambiarDistrito(parametros);
                break;
            case 3:
                CapturarTitan(parametros);
                break;
            case 4:
                AsesinarTitan(parametros);
                break;
            case 5:
                ListarTitanesCapturados();
                break;
            case 6:
                ListarTitanesAsesinados();
                break;
            default:
                System.out.println("NO DEBERIA PASAR, POR FAVOR INTENTE EJECUTAR DE NUEVO");
                break;
        }
    }

    private void ListarTitanes(String[] parametros) throws IOException {
        String[] datos = new String[2];
        datos[0] = "1";
        datos[1] = parametros[0]; //nombre del distrito
        EnviarPaquete(datos, parametros[3], parametros[4], socket);

        byte[] buffer_salida = new byte[largo];
        ByteArrayInputStream input_stream = new ByteArrayInputStream(buffer_salida);
        DataInput input = new DataInputStream(input_stream);
        DatagramPacket mensaje_entrante = new DatagramPacket(buffer_salida, largo);

        //System.out.println(parametros[3]+" / "+parametros[4]);


        socket.receive(mensaje_entrante);

        //System.out.println("PERO EL CLIENTE PASO DEL RECIBE");

        //debo recibir como primer parametro el numero de titanes

        int numero_titanes = input.readInt();

        if(numero_titanes!= 0) {
            System.out.println(prefijo+"Titanes actualmente en el Distrito:");
            for (int i = 0; i < numero_titanes; i++) {
                System.out.println("");
                System.out.println("Id: "+ input.readInt());
                System.out.println("Nombre: "+ input.readUTF());
                System.out.println("Distrito: "+ input.readUTF());
                System.out.println("Tipo: "+ input.readUTF());
                System.out.println("");
            }
        } else {
            System.out.println(prefijo+"No hay Titanes actualemente");
        }
    }

    private void CambiarDistrito(String[] parametros) throws IOException {
        cambiar_distrito = true;
        EnviarPaquete(cambio, parametros[3], parametros[4], socket);
        //solo le aviso al servidor central
    }

    private void CapturarTitan(String[] parametros) throws IOException {
        String[] datos = new String[2];
        datos[0] = "3";
        //datos[1] = parametros[0];
        System.out.println(prefijo+"¿nombre del titan que desea capturar?");
        System.out.print(comando);
        datos[1] = input.readLine();
        EnviarPaquete(datos,parametros[3], parametros[4], socket);
        String[] respuesta;
        respuesta = RecibirPaquete(4,socket);
        Titan titan_capturado = new Titan(Integer.parseInt(respuesta[0]),
                respuesta[1], respuesta[2], respuesta[3]);
        titanes_capturados.add(titan_capturado);
    }

    private void AsesinarTitan(String[] parametros) throws IOException {
        String[] datos = new String[3];
        datos[0] = "4";
        //datos[1] = parametros[0];
        System.out.println(prefijo+"¿nombre del titan que desea matar?");
        System.out.print(comando);
        datos[1] = input.readLine();
        EnviarPaquete(datos,parametros[3], parametros[4], socket);
        String[] respuesta;
        respuesta = RecibirPaquete(4,socket);
        Titan titan_capturado = new Titan(Integer.parseInt(respuesta[0]),
                respuesta[1], respuesta[2], respuesta[3]);
        titanes_asesinados.add(titan_capturado);
    }

    private void ListarTitanesAsesinados(){
        ListarTitanes(prefijo, titanes_asesinados);
    }

    private void ListarTitanesCapturados() {
        ListarTitanes(prefijo,titanes_capturados);
    }


}