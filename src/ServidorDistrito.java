import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorDistrito {
    //necesito una lista de distritos
    ArrayList<Distrito> distritos = new ArrayList<Distrito>();
	ArrayList<Titan> titanesAsesinados = new ArrayList<Titan>();
	ArrayList<Titan> titanesCapturados = new ArrayList<Titan>();

    private int idUltimoTitan = 0;
	private final int largo = 1000;

    private final String prefijo = "[ServidorDistrito] ";
    private final String comando = "> ";
    public static final String rechazado = "No fue aceptado";

	private String[] mensajes = new String[2];
	private String[] resultados = new String[2];
    
	public ServidorDistrito(){}

    public static void main(String[] args) throws IOException {
        ServidorDistrito servidorDistrito = new ServidorDistrito();
        servidorDistrito.Inicio();
    }

    private void Inicio() throws IOException {
        RecibirParametros();
	
	//preparo socket y datagrama para los clientes
        DatagramSocket socket = new DatagramSocket(resultados[1]);
        byte[] buffer_input = new byte[largo];
        ByteArrayInputStream stream_input = new ByteArrayInputStream(buffer_input);
        DataInput input = new DataInputStream(stream_input);
        DatagramPacket paquete_recibido = new DatagramPacket(buffer_input, largo);
        while (true) {
            //envio actualizaciones sobre los titanes
            
            //infinitamente recibo datagramas
            socket.receive(paquete_recibido); //esto bloquea el while si no recibe un DG
            //obtengo parametros del paquete
            String mensajeOpcion = input.readUTF();
			String mensajeDistrito = input.readUTF();
            InetAddress ip = paquete_recibido.getAddress();
            int port = paquete_recibido.getPort();
            //elijo la respuesta a partir del mensaje recibido
            DatagramPacket respuesta = Eleccion(mensajeOpcion, mensajeDistrito, ip, port);
            socket.send(respuesta);
        }
    }

    private void  RecibirParametros() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        mensajes[0] = "Ingresar IP Multicast";
        mensajes[1] = "Ingresar Puerto";
        String texto_usuario;
        int i;
        for ( i = 0; i< mensajes.length; i++){
            System.out.println(prefijo+mensajes[i]);
            System.out.print(comando);
            texto_usuario = br.readLine();
            resultados[i] = texto_usuario;
        }
    }

	private DatagramPacket Eleccion(String opcion, String distrito, InetAddress ip, int port) throws IOException {
        //elijo la accion a ejecutar en base al mensaje del cliente
		DatagramPacket respuesta;
        ByteArrayOutputStream stream_output = new ByteArrayOutputStream(largo);
        DataOutput output = new DataOutputStream(stream_output);

        if (opcion.equals('1')) {
            ListarTitanes(output, distrito, ip);
        } else if (opcion.equals('2')) {
            CambiarDistrito(output, distrito, ip);
        } else if (opcion.equals('3')){
            CapturarTitan(output, distrito, ip);
        } else if (opcion.equals('4')){
			AsesinarTitan(output, distrito, ip);
		} else if (opcion.equals('5')){
			ListarTitanesCapturados(output, distrito, ip);
		} else if (opcion.equals('6')){
			ListarTitanesAsesinados(output, distrito, ip);
		} else {
			System.out.print("Accion incorrecta, se considera rechazado");
            Rechazar(output);
		}
        byte[] mensaje = stream_output.toByteArray();
        respuesta = new DatagramPacket(mensaje, mensaje.length, ip, port);
        return respuesta;
    }

    private void CrearDistrito(){
        //crear distrito y mandarselo al servidor central
    }

    private void ListarTitanes(DataOutput output, String distrito, InetAddress ip) throws IOException {
		String ips = ip.toString().substring(1);
        String[] parametros_distrito; 
        parametros_distrito = lista_servidor_cliente.AñadirCliente(ips, distrito);
        int i, j;
        for (i = 0; i < distritos.length; i++){
			Distrito aux = distritos.get(i);
			if (aux.GetNombre.equals(distrito)){
				for (j = 0, j < aux.GetListaTitanes().lenght; j++){
					Titan aux2 = aux.GetListaTitanes().get(j);
					output.writeUTF(aux2.GetId() + " " + aux2.GetNombre() + " " + aux2.GetDistrito() + " " + aux2.GetTipo() + ",");		
				}
			}
        }
    }
	
	private void CambiarDistrito(DataOutput output, String distrito, InetAddress ip) throws IOException {

	}

    private void CapturarTitan(DataOutput output, String distrito, InetAddress ip) throws IOException {
    	String ips = ip.toString().substring(1);
        String[] parametros_distrito; 
        parametros_distrito = lista_servidor_cliente.AñadirCliente(ips, distrito);
        int i, j;
        for (i = 0; i < distritos.length; i++){
			if (distritos.get(i).GetNombre().equals(distrito)) {
				for (j = 0; j < distritos.get(i).GetListaTitanes().lenght; j++){
					if (distritos.get(i).GetListaTitanes().get(j).GetID().equals(idTitan)){
						titanesCapturados.add(distritos.get(i).GetListaTitanes().get(j));
						distritos.get(i).GetListaTitanes().remove(j);
						output.writeUTF("Titan " + idTitan + " Asesinado");
					}
				}
			}
            
        }
    }
	
	private void AsesinarTitan(DataOutput output, String idTitan, InetAddress ip) throws IOException {
		String ips = ip.toString().substring(1);
        String[] parametros_distrito; 
        parametros_distrito = lista_servidor_cliente.AñadirCliente(ips, distrito);
        int i, j;
        for (i = 0; i < distritos.length; i++){
			if (distritos.get(i).GetNombre().equals(distrito)) {
				for (j = 0; j < distritos.get(i).GetListaTitanes().lenght; j++){
					if (distritos.get(i).GetListaTitanes().get(j).GetID().equals(idTitan)){
						titanesAsesinados.add(distritos.get(i).GetListaTitanes().get(j));
						distritos.get(i).GetListaTitanes().remove(j);
						output.writeUTF("Titan " + idTitan + " Asesinado");
					}
				}
			}
            
        }
	}

	private void ListarTitanesCapturados(DataOutput output, String distrito, InetAddress ip) throws IOException {
		String ips = ip.toString().substring(1);
        String[] parametros_distrito; 
        parametros_distrito = lista_servidor_cliente.AñadirCliente(ips, distrito);
        int i;
        for (i = 0; i < titanesCapturados.length; i++){
			Titan aux = titanesCapturados.get(i);
            output.writeUTF(aux.GetId() + " " + aux.GetNombre() + " " + aux.GetDistrito() + " " + aux.GetTipo() + ",");
        }
	}

	private void ListarTitanesAsesinados(DataOutput output, String distrito, InetAddress ip) throws IOException {
		String ips = ip.toString().substring(1);
        String[] parametros_distrito; 
        parametros_distrito = lista_servidor_cliente.AñadirCliente(ips, distrito);
        int i;
        for (i = 0; i < titanesAsesinados.length; i++){
			Titan aux = titanesAsesinados.get(i);
            output.writeUTF(aux.GetId() + " " + aux.GetNombre() + " " + aux.GetDistrito() + " " + aux.GetTipo() + ",");
        }
	}

	private void InstanciarTitanEnDistrito() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] mensajesIT = new String[3];
        String[] respuestasIT = new String[3];
        mensajesIT[0] = "Ingresar Nombre Del Distrito";
        mensajesIT[1] = "Ingresar Nombre Del Titan";
		mensajesIT[2] = "Ingresar Tipo Del Titan";
        String texto_usuario;
        int i;
        for ( i = 0; i< mensajes.length; i++){
            System.out.println(prefijo+mensajes[i]);
            System.out.print(comando);
            texto_usuario = br.readLine();
            respuestasIT[i] = texto_usuario;
        }

		if(distritos.size() == 0){
            System.out.println("Lista De Distritos Vacia");
        } else {

        	for (i = 0; i < distritos.size() ; i++){
            	Distrito aux = distritos.get(i);
            	if(respuestasIT[0].equals(aux.GetNombre())){
            	    distritos.get(i).InstanciarTitan(idUltimoTitan, respuestasIT[1], respuestasIT[2]);
					idUltimoTitan ++;
            	}
        	}
        System.out.println("No Existe El Distrito");
		}
	}
}
