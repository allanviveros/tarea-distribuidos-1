import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorDistrito {
    //necesito una lista de distritos
    ArrayList<Distrito> distritos = new ArrayList<Distrito>();
    private int idUltimoTitan = 0;

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

    private void AccionCliente(int eleccion) throws IOException {
        switch (eleccion){
            case 1:
                CrearDistrito();
                //ConexionDistrito();
                break;
            case 2:
                ListarDistritos();
                //ConexionDistrito();
                break;
            case 3:
                CapturarTitan();
                //ConexionDistrito();
                break;
            default:
                System.out.println("ERROR, ELEGIR ACCION 1, 2 O 3");
                //ConexionDistrito();
                break;
        }
    }

    private void CrearDistrito(){
        //crear distrito y mandarselo al servidor central
    }

    private void ListarDistritos(){

    }

    private void CapturarTitan(){
    
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
