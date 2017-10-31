//import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.List;

public class ServidorCentral {
    private ListaServidorCliente lista_servidor_cliente = new ListaServidorCliente();

    //constantes
    private final int puerto = 6001;
    private final int largo = 1000;
    private final String prefijo = "[ServidorCentral] ";
    public static final String rechazado = "No fue aceptado";



    public static void main(String[] args) throws IOException {
        ServidorCentral servidor_central = new ServidorCentral();
        servidor_central.Inicio();
    }

    public ServidorCentral(){
        lista_servidor_cliente.ValoresIniciales();
    }

    private void Inicio() throws IOException {
        //preparo socket y datagrama para los clientes
        DatagramSocket socket = new DatagramSocket(puerto);
        byte[] buffer_input = new byte[largo];
        ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer_input);
        DataInput input = new DataInputStream(stream_input);
        DatagramPacket paquete_recibido = new DatagramPacket(buffer_input, largo);
        while (true) {
            //muestro los distritos y usuarios
            lista_servidor_cliente.DesplegarInformacion();
            //infinitamente recibo datagramas
            socket.receive(paquete_recibido); //esto bloquea el while si no recibe un DG
            //obtengo parametros del paquete
            String mensaje = input.readUTF();
            InetAddress ip = paquete_recibido.getAddress();
            int port = paquete_recibido.getPort();
            //elijo si aceptar o no y genero una respuesta
            DatagramPacket respuesta = Eleccion(mensaje, ip, port);
            socket.send(respuesta);
        }
    }

    private DatagramPacket Eleccion(String distrito, InetAddress ip, int port) throws IOException {
        //muestro las elecciones y espero a que el servidor elija
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] mensajes = new String[3];
        String resultado;
        mensajes[0] = "Autorizar a ";
        mensajes[1] = " con el distrito: ";
        mensajes[2] = "\n1) SI\n2) NO";
        String texto_usuario;
        System.out.println(prefijo + mensajes[0] + ip + mensajes[1] + distrito + mensajes[2]);
        texto_usuario = br.readLine();
        resultado = texto_usuario;
        int indice = Integer.parseInt(resultado);
        DatagramPacket respuesta;
        ByteArrayOutputStream stream_output = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(stream_output);
        if (indice == 1) {
            Aceptar(output, distrito, ip);
        } else if (indice == 2) {
            Rechazar(output);
        } else {
            System.out.print("Accion incorrecta, se considera rechazado");
            Rechazar(output);
        }
        byte[] mensaje = stream_output.toByteArray();
        respuesta = new DatagramPacket(mensaje, mensaje.length, ip, port);
        return respuesta;
    }

    private void Aceptar(DataOutput output, String distrito, InetAddress ip) throws IOException {
        //inscribirlo en la estructura de datos
        String ips = ip.toString().substring(1);
        String[] parametros_distrito; //los almaceno sin el "/"
        parametros_distrito = lista_servidor_cliente.AñadirCliente(ips, distrito);
        int i;
        for (i = 0; i < parametros_distrito.length; i++){
            output.writeUTF(parametros_distrito[i]);
        }
    }

    private void Rechazar(DataOutput output) throws IOException {
        output.writeUTF(rechazado);
    }

    private void DesplegarInformacion(){

    }
}