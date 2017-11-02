import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class BaseComunicacion {
    private final int largo= 1000;

    public BaseComunicacion(){

    }
    //ENVIAR CON MULTIPLES PARAMETROS

    private byte[] ContenidoPaquete(String[] parametros) throws IOException {
        ByteArrayOutputStream stream_output = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(stream_output);
        int i;
        byte[] mensaje;
        for(i = 0; i< parametros.length; i++){
            output.writeUTF(parametros[i]);
        }
        mensaje = stream_output.toByteArray();
        return mensaje;
    }

    protected void EnviarPaquete(String[] parametros, String ip, String puerto, DatagramSocket socket) throws IOException {
        InetAddress ip_aux = InetAddress.getByName(ip);
        int puerto_aux = Integer.parseInt(puerto);
        byte[] mensaje = ContenidoPaquete(parametros);
        DatagramPacket enviar = new DatagramPacket(mensaje, mensaje.length, ip_aux, puerto_aux);
        socket.send(enviar);
    }

    //ENVIAR CON 1 parametro

    private byte[] ContenidoPaquete(String parametro) throws IOException {
        ByteArrayOutputStream stream_output = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(stream_output);
        int i;
        byte[] mensaje;
        output.writeUTF(parametro);
        mensaje = stream_output.toByteArray();
        return mensaje;
    }

    protected void EnviarPaquete(String parametro, String ip, String puerto, DatagramSocket socket) throws IOException {
        InetAddress ip_aux = InetAddress.getByName(ip);
        int puerto_aux = Integer.parseInt(puerto);
        byte[] mensaje = ContenidoPaquete(parametro);
        DatagramPacket enviar = new DatagramPacket(mensaje, mensaje.length, ip_aux, puerto_aux);
        socket.send(enviar);
    }

    //multiples parametros
    protected String[] RecibirPaquete(int parametros, DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[largo];
        ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer);
        DatagramPacket mensaje_recibido = new DatagramPacket(buffer, buffer.length);
        socket.receive(mensaje_recibido);
        //tomamos los parametros del datagrama
        DataInput input = new DataInputStream(stream_input);
        int i;
        String[] resultado = new String[parametros];
        for(i=0; i<parametros;i++){
            resultado[i] = input.readUTF();
        }
        return resultado;
    }

    //para 1 solo parametro
    protected String RecibirPaquete(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[largo];
        ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer);
        DatagramPacket mensaje_recibido = new DatagramPacket(buffer, buffer.length);
        socket.receive(mensaje_recibido);
        //tomamos los parametros del datagrama
        DataInput input = new DataInputStream(stream_input);
        String resultado = input.readUTF();
        return resultado;
    }

    //para 1 solo parametro
    //[ip, puerto, mensaje]
    protected String[] RecibirPaqueteInfo(DatagramSocket socket) throws IOException {
        String[] resultado = new String[3];
        byte[] buffer = new byte[largo];
        ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer);
        DatagramPacket mensaje_recibido = new DatagramPacket(buffer, buffer.length);
        socket.receive(mensaje_recibido);
        //tomamos los parametros del datagrama
        DataInput input = new DataInputStream(stream_input);
        resultado[0] = mensaje_recibido.getAddress().toString().substring(1);
        resultado[1] = Integer.toString(mensaje_recibido.getPort());
        resultado[2] = input.readUTF();
        return resultado;
    }

    //para n solo parametro
    //[ip, puerto, mensaje, ...]
    protected String[] RecibirPaqueteInfo(int parametros, DatagramSocket socket) throws IOException {

        String[] resultado = new String[2+parametros];
        byte[] buffer = new byte[largo];
        ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer);
        DatagramPacket mensaje_recibido = new DatagramPacket(buffer, buffer.length);

        socket.receive(mensaje_recibido);

        //tomamos los parametros del datagrama
        DataInput input = new DataInputStream(stream_input);
        resultado[0] = mensaje_recibido.getAddress().toString().substring(1);
        resultado[1] = Integer.toString(mensaje_recibido.getPort());
        //resultado[2] = input.readUTF();
        int i;
        for(i=2; i<parametros;i++){
            resultado[i] = input.readUTF();
        }
        return resultado;
    }

    protected void ListarTitanes(String prefijo, ArrayList<Titan> lista_titanes){
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
}
