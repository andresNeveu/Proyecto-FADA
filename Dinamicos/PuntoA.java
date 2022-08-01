import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class PuntoA {
    /*
     * Nombres de los archivos de lectura y escritura, modifique como considere.
     */
    static String ARCHIVO_LECTURA = "inA";
    static String ARCHIVO_ESCRITURA = "outA";
    static double suma = 0.0;
    static int iteration = 0;

    /*
     * Método para realizar la lectura del problema, no modificar.
     */
    public static Procedimiento[] input() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        Procedimiento[] p = null;
        try {
            archivo = new File(ARCHIVO_LECTURA + ".txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            int n = Integer.parseInt(br.readLine());
            p = new Procedimiento[n];
            String linea;
            for (int i = 0; i < n; ++i) {
                linea = br.readLine();
                String[] data = linea.split(" ");
                String[] horas = data[1].split("-");
                int horaI, minI, horaF, minF;
                String[] tiempo = horas[0].split(":");
                horaI = Integer.valueOf(tiempo[0]);
                minI = Integer.valueOf(tiempo[1]);
                tiempo = horas[1].split(":");
                horaF = Integer.valueOf(tiempo[0]);
                minF = Integer.valueOf(tiempo[1]);
                p[i] = new Procedimiento(data[0], new Hora(horaI, minI), new Hora(horaF, minF));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (null != fr) {
                fr.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return p;
    }

    /*
     * Método para realizar la escritura de la respuesta del problema, no modificar.
     */
    public static void output(Respuesta output) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ARCHIVO_ESCRITURA + ".txt");
            pw = new PrintWriter(fichero);

            pw.println(output.n);
            pw.println(output.tiempoTotal);
            for (int i = 0; i < output.n; ++i) {
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
    static Respuesta solve(int n, Procedimiento[] procedimientos) {

        // complejidad heapSort 0(n log(n))
        Arrays.sort(procedimientos);
        int totalHoras = 24;
        int[][] tmax = new int[n + 1][totalHoras + 1];
        ArrayList[][] matrizProcedimientos = new ArrayList[n + 1][totalHoras + 1];


        // inicializar la matriz O(m)
        for (int i = 0; i <= totalHoras; i++) {
            tmax[0][i] = 0;
            matrizProcedimientos[0][i] = new ArrayList<>();
        }

        // Complejidad n x totalHoras
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= totalHoras; j++) {

                int horasProc = hourProc(procedimientos[i - 1]);
                double minutosProc = minutesProc(procedimientos[i - 1]);

                // no tomar el item, restamos 1 a la cantidad total de items
                tmax[i][j] = tmax[i - 1][j];
                // No tomar el procedimiento
                ArrayList<Procedimiento> procNoTomar = (ArrayList<Procedimiento>) matrizProcedimientos[i - 1][j].clone();
                matrizProcedimientos[i][j] = procNoTomar;

                if ((j >= horasProc) && (tmax[i - 1][j - horasProc] + (horasProc + minutosProc) > tmax[i][j])) {

                    ArrayList<Procedimiento> procTomarlo = (ArrayList<Procedimiento>) matrizProcedimientos[i - 1][j - horasProc].clone();

                    if (!(checkCrossing(procTomarlo, procedimientos[i - 1]))) {
                        if ((j >= (suma + horasProc + minutosProc))) {
                            tmax[i][j] = tmax[i - 1][j - horasProc] + horasProc;
                            procTomarlo.add(procedimientos[i - 1]);
                            matrizProcedimientos[i][j] = (ArrayList<Procedimiento>) procTomarlo.clone();
                        }
                    }
                    else {
                        if(tmax[i][j - 1] > tmax[i - 1][j - horasProc]) {
                            tmax[i][j] = tmax[i][j - 1];
                            matrizProcedimientos[i][j] = (ArrayList<Procedimiento>) matrizProcedimientos[i][j - 1].clone();
                        }
                     }
                }
                iteration++;
                System.out.print(tmax[i][j] + "\t");
            }
            System.out.print("\n");
        }

        ArrayList<Procedimiento> salida = matrizProcedimientos[n][totalHoras];
        int num = salida.size();
        int horas = 0;
        int minutos = 0;
        String[] names = new String[num];
        int i = 0;
        for (Procedimiento proc : salida) {
            horas += hourProc(proc);
            minutos += proc.horaFin.minutos - proc.horaInicio.minutos;
            names[i] = proc.nombre;
            i++;
        }
        horas += minutos / 60;
        minutos = minutos % 60;
        return new Respuesta(num, new Hora(horas, minutos), names);
    }

    public static boolean checkCrossing(ArrayList<Procedimiento> procs, Procedimiento currentProc) {
        suma = 0;
        for (Procedimiento proc : procs) {
            if ((currentProc.horaInicio.equals(proc.horaInicio.hora))) {
                return true;
            } else if ((currentProc.horaInicio.hora == proc.horaFin.hora)
                    && (proc.horaFin.minutos > currentProc.horaInicio.minutos)) {
                return true;
            } else if ((proc.horaFin.hora > currentProc.horaInicio.hora)) {
                return true;
            }
            iteration++;
            suma += (hourProc(proc) + minutesProc(proc));
        }
        return false;
    }

    public static int hourProc(Procedimiento procedimiento) {
        return procedimiento.horaFin.hora - procedimiento.horaInicio.hora;
    }

    public static double minutesProc(Procedimiento procedimiento) {
        int minutos = procedimiento.horaFin.minutos - procedimiento.horaInicio.minutos;
        return minutos / 60.0;
    }

    public static void main(String[] args) {
        Procedimiento[] input = input();
        Instant start = Instant.now();
        Respuesta r = solve(input.length, input);
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Time " + timeElapsed + " Milisegundos");
        output(r);
    }

    static class Respuesta {
        int n;
        Hora tiempoTotal;
        String[] nombreProcedimientos;

        public Respuesta(int n, Hora tiempoTotal, String[] nombreProcedimientos) {
            this.n = n;
            this.tiempoTotal = tiempoTotal;
            this.nombreProcedimientos = nombreProcedimientos;
        }

    }

    /*
     * Clase base para interpretar los objetos tratados en el problema.
     */
    static class Procedimiento implements Comparable<Procedimiento> {
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
        public int compareTo(Procedimiento o) {
            if (this.horaInicio.hora != o.horaInicio.hora) {
                return this.horaInicio.hora - o.horaInicio.hora;
            } else if (this.horaInicio.hora == o.horaInicio.hora) {
                return this.horaInicio.minutos - o.horaInicio.minutos;
            }
            return 1;
        }
    }

    static class Hora {
        int hora, minutos;

        public Hora(int hora, int minutos) {
            this.hora = hora;
            this.minutos = minutos;
        }

        @Override
        public String toString() {
            String res = "";
            if (hora < 10)
                res += "0" + hora;
            else
                res += hora;
            res += ":";
            if (minutos < 10)
                res += "0" + minutos;
            else
                res += minutos;

            return res;
        }
    }
}