/**
 *
 * @author kevin
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PuntoA{
    /*
     * Nombres de los archivos de lectura y escritura, modifique como considere.
     */
    static String ARCHIVO_LECTURA = "inA";
    static String ARCHIVO_ESCRITURA = "outA";
    /*
     * Método para realizar la lectura del problema, no modificar.
     */
    public static Procedimiento[] input(){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        Procedimiento[] p = null;
        try {
            archivo = new File ("NoDinamicos/"+ ARCHIVO_LECTURA +".txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            int n = Integer.parseInt(br.readLine());
            p = new Procedimiento[n];
            String linea;
            for(int i = 0 ; i < n ; ++i){
                linea=br.readLine();
                String[]data = linea.split(" ");
                String[]horas = data[1].split("-");
                int horaI, minI, horaF, minF;
                String[]tiempo = horas[0].split(":");
                horaI = Integer.valueOf(tiempo[0]);
                minI = Integer.valueOf(tiempo[1]);
                tiempo = horas[1].split(":");
                horaF = Integer.valueOf(tiempo[0]);
                minF = Integer.valueOf(tiempo[1]);
                p[i] = new Procedimiento(data[0], new Hora(horaI, minI), new Hora(horaF, minF));
            }   
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{                    
            if( null != fr ){   
                fr.close();     
            }                  
        }catch (Exception e2){ 
            e2.printStackTrace();
        }
        return p;
    }
    /*
     * Método para realizar la escritura de la respuesta del problema, no modificar.
     */
    public static void output(Respuesta output){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("NoDinamicos/"+ ARCHIVO_ESCRITURA+".txt");
            pw = new PrintWriter(fichero);

            pw.println(output.n);
            pw.println(output.tiempoTotal);
            for(int i = 0 ; i < output.n ; ++i){
                pw.println(output.nombreProcedimientos[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
           if (null != fichero)
              fichero.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        
    }

    /*
     * Implementar el algoritmo y devolver un objeto de tipo Respuesta, el cual servirá
     * para imprimir la solución al problema como se requiere en el enunciado.
     */
    
    
    
    static Respuesta solve(int n, Procedimiento[] procedimientos){
       
            
        //Organiza los arrays por hora de inicio
        
        Arrays.sort(procedimientos);
        ArrayList<ArrayList> arrayDeArrays = new ArrayList<>(); 
        ArrayList<Hora> horas = new ArrayList<>();
       
        
        for (int j = 0; j < procedimientos.length; j++) {
            
        Procedimiento pivote = procedimientos[j]; 
        
        ArrayList<Procedimiento> procedimiento1 = new ArrayList<>();
       
         
          for (int i = j; i < procedimientos.length; i++) {   
            //Caso donde la hora inicial y final son iguales pero los minutos de la hora inicial
            // del siguiente procedimiento es mayor
            
            if(i!= procedimientos.length-1){ 
            if(pivote.horaFin.hora  == procedimientos[i+1].horaInicio.hora   &&  
                    pivote.horaFin.minutos  > procedimientos[i+1].horaInicio.minutos ){ 
            }
            //La hora final de procedimiento es mayor al inicio del siguiente procedimiento
            else if(pivote.horaFin.hora  > procedimientos[i+1].horaInicio.hora){  
            }
            else{
                
                if(!procedimiento1.contains(pivote)){
                procedimiento1.add(pivote);
                procedimiento1.add(procedimientos[i+1]); 
                arrayDeArrays.add(procedimiento1);
                }
                else{   
                procedimiento1.add(procedimientos[i+1]); 
                }
                
                pivote = procedimientos[i+1];     
             }
                
           }else{ 
                
          }
        }
    }
       
        
        for (int i = 0; i < arrayDeArrays.size(); i++) {
           horas.add(sumaProcs(arrayDeArrays.get(i)));           
        }
        
       
        Hora[] hor;
        hor = new Hora[horas.size()];
        for (int i = 0; i < horas.size(); i++) {
            hor[i] = horas.get(i);
        }
        Arrays.sort(hor);
        System.out.println("El uso maximo de la sala de operaciones es: "+ hor[0].toString());
        
        return new Respuesta(hor[0].n, new Hora(hor[0].hora,hor[0].minutos), hor[0].operaciones);
    }
       
    public static Hora sumaProcs(ArrayList<Procedimiento> procs) {
        int horas = 0;
        int minutos = 0;
        int n = procs.size();
        String[] procedimientos = new String[procs.size()];
        
        
        for (int i = 0; i < procs.size(); i++) {
            horas += procs.get(i).horaFin.hora - procs.get(i).horaInicio.hora;
            minutos += procs.get(i).horaFin.minutos - procs.get(i).horaInicio.minutos;
            procedimientos[i] = procs.get(i).nombre;
            
        }
        
        horas += minutos/60;
        minutos = minutos % 60;
        Hora hora = new Hora(horas, minutos, procedimientos, n);
        return hora;
    }
    
    
    
    
    public static void main(String[]args){
        Procedimiento[]input = input();
        Respuesta r = solve(input.length, input);
        output(r);
    }

    static class Respuesta{
        int n;
        Hora tiempoTotal;
        String []nombreProcedimientos;
        public Respuesta(int n, Hora tiempoTotal, String[] nombreProcedimientos) {
            this.n = n;
            this.tiempoTotal = tiempoTotal;
            this.nombreProcedimientos = nombreProcedimientos;
        }   
    }
    /*
     * Clase base para interpretar los objetos tratados en el problema.
     */
    static class Procedimiento implements Comparable<Procedimiento>{
        String nombre;
        Hora horaInicio, horaFin;

        public Procedimiento(String nombre, Hora horaInicio, Hora horaFin) {
            this.nombre = nombre;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
        }

        @Override
        public String toString() {
            return "Procedimiento [horaFin=" + horaFin + ", horaInicio=" + horaInicio + ", nombre=" + nombre + "]";
        }

        @Override
        public int compareTo(Procedimiento t) {
            if (this.horaInicio.hora != t.horaInicio.hora) {
            }else if (this.horaInicio.hora == t.horaInicio.hora) {
              return this.horaInicio.minutos - t.horaInicio.minutos;
            }
            return 1;
        }

        public int getHoraInicio() {
            return horaInicio.hora;
        }
         
        
    }
    
    
    static class Hora implements Comparable<Hora>{
        int hora, minutos,n;
        String[] operaciones;

        public Hora(int hora, int minutos) {
            this.hora = hora;
            this.minutos = minutos;
        }
        
        public Hora(int hora, int minutos, String[] operaciones, int n) {
            this.hora = hora;
            this.minutos = minutos;
            this.operaciones = operaciones;
            this.n = n;
        }
        @Override
        public String toString(){
            String res = "";
            if (hora < 10)
                res+="0"+hora;
            else
                res+=hora; 
            res+=":";
            if (minutos < 10)
                res+="0"+minutos;
            else
                res+=minutos; 
            
            return res;
        }
        

        @Override
        public int compareTo(Hora t) {
            if (this.hora != t.hora) {
            }else if (this.hora == t.hora) {
              return this.minutos - t.minutos;
            }
            return 1; 
        }
    }
}
    

