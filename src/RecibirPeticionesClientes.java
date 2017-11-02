import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class RecibirPeticionesClientes extends BaseComunicacion implements Runnable{

    private final int largo = 1000;
    private DatagramSocket socket;
    Boolean thread_cerrado = false;
    ArrayList<Titan> titanes;

    public RecibirPeticionesClientes(int puerto, ArrayList<Titan> titanes) throws SocketException {
        this.socket = new DatagramSocket(puerto);
        this.titanes = titanes;
    }

    @Override
    public void run() {
        while(true) {
            try {
            Eleccion();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }

    private void Eleccion() throws IOException {
        String datos[] = RecibirPaqueteInfo(2, socket);



        //      0  ,  1,    2
        //datos(ip, puerto, opcion, nombre titan)

        switch (Integer.parseInt(datos[2])) {
            case 1:
                ListarTitanes(datos);
                break;
            case 3:
                Titan(datos);
                break;
            case 4:
                Titan(datos);
                break;
            default:
                System.out.print("Accion incorrecta, se considera rechazado");
                break;
        }
    }

    private void Titan(String[] datos) throws IOException {
        int i;
        String[] info_titan;
        if(titanes.size() != 0) {
            for (i= 0; i< titanes.size(); i++) {
                Titan titan = titanes.get(i);
                if(titan.GetNombre().equals(datos[3])){
                    info_titan = titan.ObtenerInformacion();
                    EnviarPaquete(info_titan, datos[0], datos[1], socket);
                    titanes.remove(i);
                    break;
                }
            }
            //no existe
        }
        //no hay titanes
    }

    private void ListarTitanes(String[] datos) throws IOException {
        InetAddress ip = InetAddress.getByName(datos[0]);
        int puerto = Integer.parseInt(datos[1]);
        System.out.println("LLEGA AQUI EN LISTAR TITANES");
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(output_stream);
        int numero_titanes = titanes.size();
        int i;

        output.writeInt(numero_titanes);

        if(titanes.size() != 0) {
            for (i= 0; i <titanes.size();i++) {
                Titan titan = titanes.get(i);
                output.writeInt(titan.GetId());
                output.writeUTF(titan.GetNombre());
                output.writeUTF(titan.GetDistrito());
                output.writeUTF(titan.GetTipo());
            }
        }
        byte[] mensaje = output_stream.toByteArray();
        DatagramPacket mensaje_enviar = new DatagramPacket(mensaje, mensaje.length,
                ip, puerto);

        socket.send(mensaje_enviar);
    }
}
