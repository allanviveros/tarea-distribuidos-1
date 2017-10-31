import java.io.*;
import java.net.*;
import java.util.List;

public class Cliente {

    private List<Titan> titanes_capturados;
    private List<Titan> titanes_asesinados;

    private final String prefijo = "[CLIENTE] ";
    private final String comando = "> ";

    private String servidor_central_ip;
    private String servidor_central_puerto;
    private String distrito;

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
        } //USAR WHILE
    }

    private void ConexionDistrito() throws IOException {
        /* si la conexion es un exito llamar a esta funcion*/
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
        AccionCliente(Integer.parseInt(texto_usuario));
    }

    private void AccionCliente(int eleccion) throws IOException {
        switch (eleccion){
            case 1:
                ListarTitanes();
                ConexionDistrito();
                break;
            case 2:
                CambiarDistrito();
                ConexionDistrito();
                break;
            case 3:
                CapturarTitan();
                ConexionDistrito();
                break;
            case 4:
                AsesinarTitan();
                ConexionDistrito();
                break;
            case 5:
                ListarTitanesCapturados();
                ConexionDistrito();
                break;
            case 6:
                ListarTitanesAsesinados();
                ConexionDistrito();
                break;
            default:
                System.out.println("NO DEBERIA PASAR");
                ConexionDistrito();
                break;
        }
    }

    private void ListarTitanes(){

    }

    private void CambiarDistrito(){

    }

    private void CapturarTitan(){

    }

    private void AsesinarTitan(){

    }

    private void ListarTitanesCapturados(){

    }

    private void ListarTitanesAsesinados(){

    }
}