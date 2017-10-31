import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private ArrayList<Titan> titanes_capturados = new ArrayList<Titan>();
    private ArrayList<Titan> titanes_asesinados = new ArrayList<Titan>();

    private final String prefijo = "[CLIENTE] ";
    private final String comando = "> ";
    private final int largo = 1000;

    // ip: 127.0.0.1
    //puerto central = 6001

    public Cliente() { }

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente();
        cliente.Inicio();
        //cliente.ConexionDistrito(); //solo para testeo
    }

    private void Inicio() throws IOException {
        String[] parametros = RecibirParametrosTESTEO();
        EnviarMensajeServidorCentral(parametros);
        Flujo();
    }

    private void Flujo(){


    }
    //SOLO PARA TESTEO
    private String[] RecibirParametrosTESTEO() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
            texto_usuario = br.readLine();
            resultados[i] = texto_usuario;
        }
        return resultados;
    }

    private String[] RecibirParametros() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] mensajes = new String[3];
        String[] resultados = new String[3];
        mensajes[0] = "Ingresar IP Servidor Central";
        mensajes[1] ="Ingresar Puerto Servidor Central";
        mensajes[2] = "Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina";
        String texto_usuario;
        int i;
        for ( i = 0; i< mensajes.length; i++){
            System.out.println(prefijo+mensajes[i]);
            System.out.print(comando);
            texto_usuario = br.readLine();
            resultados[i] = texto_usuario;
        }
        return resultados;
    }

    private void EnviarMensajeServidorCentral(String[] parametros) throws IOException {
        //Preparar datos para datagrama
        InetAddress ip_central = InetAddress.getByName(parametros[0]);
        int puerto_central = Integer.parseInt(parametros[1]);
        byte[] mensaje;
        ByteArrayOutputStream stream_output = new ByteArrayOutputStream(1000);
        DataOutput output = new DataOutputStream(stream_output);
        output.writeUTF(parametros[2]);
        mensaje = stream_output.toByteArray();
        //creacion socket, datagrama
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, ip_central,puerto_central);
        socket.send(paquete);
        //espero respuesta
        byte[] buffer = new byte[1000];
        ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer);
        DatagramPacket mensaje_recibido = new DatagramPacket(buffer, buffer.length);
        socket.receive(mensaje_recibido);
        //tomamos los parametros del datagrama
        DataInput input = new DataInputStream(stream_input);
        String distrito = input.readUTF();

        String rechazado = ServidorCentral.rechazado;
        if (distrito.equals(rechazado)) {
            System.out.println("Este Cliente fue rechazado");
        } else {
            String ip_multicast  = input.readUTF();
            String puerto_multicast = input.readUTF();
            String ip_unicast = input.readUTF();
            String puerto_unicast = input.readUTF();
            //System.out.println(ip_multicast+puerto_multicast+ip_unicast+puerto_unicast+mensaje);
            //Multicast();
            System.out.println(puerto_multicast);
            ConexionDistrito(distrito, ip_multicast, puerto_multicast, ip_unicast, puerto_unicast, socket);
        } //USAR WHILE
    }

    private void Multicast(){
        //NO USAR
    }

    private void ConexionDistrito(String distrito, String ip_multicast, String puerto_multicast,
                                  String ip_unicast, String puerto_unicast, DatagramSocket unicast_socket) throws IOException {
        /* si la conexion es un exito llamar a esta funcion*/


        //---
        InetAddress multicast_ip = InetAddress.getByName(ip_multicast);
        MulticastSocket multicast_socket = new MulticastSocket(Integer.parseInt(puerto_multicast));
        //deberia ir un thread?

        //---

        int i;
        String texto_usuario;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
        texto_usuario = br.readLine();
        AccionCliente(Integer.parseInt(texto_usuario), distrito, ip_multicast, puerto_multicast,
                ip_unicast, puerto_unicast,unicast_socket);
    }

    private void AccionCliente(int eleccion, String distrito, String ip_multicast, String puerto_multicast,
                               String ip_unicast, String puerto_unicast, DatagramSocket unicast_socket) throws IOException {
        switch (eleccion){
            case 1:
                ListarTitanes(ip_unicast, puerto_unicast, unicast_socket);
                //ConexionDistrito();
                break;
            case 2:
                CambiarDistrito();
                //ConexionDistrito();
                break;
            case 3:
                CapturarTitan(ip_unicast, puerto_unicast, unicast_socket);
                //ConexionDistrito();
                break;
            case 4:
                AsesinarTitan(ip_unicast, puerto_unicast, unicast_socket);
                //ConexionDistrito();
                break;
            case 5:
                ListarTitanesCapturados();
                //ConexionDistrito();
                break;
            case 6:
                ListarTitanesAsesinados();
                //ConexionDistrito();
                break;
            default:
                System.out.println("NO DEBERIA PASAR, POR FAVOR INTENTE EJECUTAR DE NUEVO");
                //ConexionDistrito();
                break;
        }
    }

    private void ListarTitanesCapturados() {
        ListarTitanes(titanes_capturados);
    }

    private void ListarTitanes(String ip_unicast, String puerto_unicast, DatagramSocket socket_unicast) throws IOException {
        //uso el ip unicast, y el puerto unicast
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(output_stream);
        String opcion = "1";
        //escribo la opcion
        ///output.writeInt(1);
        output.writeUTF(opcion);
        byte[] mensaje = output_stream.toByteArray();
        DatagramPacket enviar = new DatagramPacket(mensaje, mensaje.length,
                InetAddress.getByName(ip_unicast), Integer.parseInt(puerto_unicast));
        socket_unicast.send(enviar);
        //se lo envio al ip unicasr

        //me preparo para recibir un mensaje
        byte[] buffer_salida = new byte[largo];
        ByteArrayInputStream input_stream = new ByteArrayInputStream(buffer_salida);
        DataInput input = new DataInputStream(input_stream);
        DatagramPacket mensaje_entrante = new DatagramPacket(buffer_salida, largo);
        socket_unicast.receive(mensaje_entrante);

        //debo recibir como primer parametro el numero de titanes

        int numero_titanes = input.readInt();
        //Imprimir titanes
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

    private void CambiarDistrito(){

    }

    private void CapturarTitan(String ip_unicast, String puerto_unicast, DatagramSocket socket_unicast) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        ByteArrayOutputStream output_stream = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(output_stream);

        System.out.println(prefijo+"¿nombre del titan que desea capturar?");
        String titan = br.readLine();

        String opcion = "3";

        output.writeUTF(opcion);
        output.writeUTF(titan);

        byte[] mensaje = output_stream.toByteArray();

        DatagramPacket peticion = new DatagramPacket(mensaje, mensaje.length,
                InetAddress.getByName(ip_unicast), Integer.parseInt(puerto_unicast));
        socket_unicast.send(peticion);

        //preparo la entrada de la respuesta
        byte[] buffer = new byte[largo];
        ByteArrayInputStream input_stream = new ByteArrayInputStream(buffer);
        DataInput input = new DataInputStream(input_stream);
        DatagramPacket data = new DatagramPacket(buffer, largo);

        socket_unicast.receive(data);
        //obtengo los parametros de la respuesta
        //primero obtengo el id
        int id = input.readInt();
        String nombre = input.readUTF();
        String distrito = input.readUTF();
        String tipo = input.readUTF();
        //el titan es capturado (COLOCAR EN UNA LISTA
        Titan titan_capturado = new Titan(id,tipo, distrito, nombre);
        titanes_capturados.add(titan_capturado);
    }

    private void AsesinarTitan(String ip_unicast, String puerto_unicast, DatagramSocket socket_unicast) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        ByteArrayOutputStream output_stream = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(output_stream);

        System.out.println(prefijo+"¿nombre del titan que desea matar?");
        String opcion = "4";

        String titan = br.readLine();

        output.writeUTF(opcion);
        output.writeUTF(titan);

        byte[] mensaje = output_stream.toByteArray();

        DatagramPacket peticion = new DatagramPacket(mensaje, mensaje.length,
                InetAddress.getByName(ip_unicast), Integer.parseInt(puerto_unicast));
        socket_unicast.send(peticion);

        byte[] buffer = new byte[largo];
        ByteArrayInputStream input_stream = new ByteArrayInputStream(buffer);
        DataInput input = new DataInputStream(input_stream);
        DatagramPacket mensaje_recibido = new DatagramPacket(buffer, largo);

        socket_unicast.receive(mensaje_recibido);

        int id = input.readInt();
        String nombre = input.readUTF();
        String distrito = input.readUTF();
        String tipo = input.readUTF();
        //TitanLocalAS(id, nombre, distrito, tipo);

        Titan titan_asesinado = new Titan(id,tipo, distrito, nombre);
        titanes_asesinados.add(titan_asesinado);
    }

    private void ListarTitanes(ArrayList<Titan> lista_titanes){
        int i;
        if(lista_titanes.size() != 0) {
            System.out.println(prefijo+"Titanes Capturados");
            for (i= 0; i<lista_titanes.size(); i++) {
                Titan titan = lista_titanes.get(i);
                System.out.println("");
                System.out.println("Titan "+Integer.toString(i));
                System.out.println("Nombre: "+titan.GetNombre());
                System.out.println("Id: "+titan.GetId());
                System.out.println("Tipo: "+titan.GetTipo());
                System.out.println("Distrito: "+titan.GetDistrito());
                System.out.println("");
            }
        } else {
            System.out.println(prefijo+"No hay titanes ");
        }
    }

    private void ListarTitanesAsesinados(){
        ListarTitanes(titanes_asesinados);
    }
}