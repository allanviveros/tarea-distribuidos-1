import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ListaServidorCliente {
    private ArrayList<String[]> clientes = new ArrayList<String[]>();
    private ArrayList<String[]> distritos = new ArrayList<String[]>();
    private final int parametros_distritos = 5;
    private final int parametros_clientes = 2;

    public ListaServidorCliente() {

    }

    //solo para testeo
    public void ValoresIniciales(){
        String[] dis_1 = new String[parametros_distritos];
        String[] dis_2 = new String[parametros_distritos];
        dis_1[0] = "Trost";
        dis_1[1] = "224.0.0.7";
        dis_1[2] = "5400";
        dis_1[3] = "10.10.2.135";
        dis_1[4] = "5700";
        dis_2[0] = "rost";
        dis_2[1] = "224.0.0.8";
        dis_2[2] = "5401";
        dis_2[3] = "10.10.2.135";
        dis_2[4] = "5700";
        distritos.add(dis_1);
        distritos.add(dis_2);
    }

    public String[] AñadirCliente(String ip, String distrito){
        String[] parametros = new String[parametros_clientes];
        parametros[0] = ip;
        parametros[1] = distrito;
        clientes.add(parametros);
        //devuelvo informacion para el cliente
        return InformacionDistrito(distrito);
    }

    public void AñadirDistritos(String distrito, String ip_multicast, String puerto_multicast,
                                String ip_unicast, String puerto_unicast) {
        String[] parametros = new String[parametros_distritos];
        parametros[0] = distrito;
        parametros[1] = ip_multicast;
        parametros[2] = puerto_multicast;
        parametros[3] = ip_unicast;
        parametros[4] = puerto_unicast;
        distritos.add(parametros);
    }

    public String[] InformacionDistrito(String distrito){
        //string de error
        String[] error = new  String[parametros_distritos];
        error[0] = "Error";
        error[1] = "224.0.0.10";
        error[2] = "5400";
        error[3] = "10.10.2.135";
        error[4] = "5555";
        //------------
        if(distrito.length() == 0){
            System.out.println("Lista de distritos vacia");
            return error;
        }
        int i;
        for (i = 0; i < distrito.length() ; i++){
            String[] aux = distritos.get(i);
            if(aux[0].equals(distrito)){
                //obtengo el distrito al cual el cliente quiere conectarse
                return aux;
            }
        }
        System.out.println("No existe el distrito al cual quiere conectarse");
        return error;
    }

    public void DesplegarInformacion(){
        int i;
        String[] aux;
        System.out.println("");
        System.out.println("Distritos:");
        System.out.println("");
        for (i=0; i< distritos.size(); i++){
            aux = distritos.get(i);
            System.out.println(aux[0]);
        }
        System.out.println("");
        System.out.println("Clientes:");
        System.out.println("");
        for (i=0; i< clientes.size(); i++){
            aux = clientes.get(i);
            System.out.println(aux[0]);
        }
        System.out.println("");
    }
}
