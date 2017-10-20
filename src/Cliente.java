import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cliente {

    private final String prefijo = "[CLIENTE] ";
    private final String comando = "> ";

    private String servidor_central_ip;
    private String servidor_central_puerto;
    private String distrito;

    public Cliente() { }

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente();
        cliente.Inicio();
        cliente.ConexionDistrito(); //solo para testeo
    }

    private void Inicio() throws IOException {
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
        servidor_central_ip = resultados[0];
        servidor_central_puerto = resultados[1];
        distrito = resultados[2];
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
