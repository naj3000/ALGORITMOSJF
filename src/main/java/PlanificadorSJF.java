/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author USUARIO
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PlanificadorSJF {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número de procesos: ");
        int numProcesos = scanner.nextInt();

        ArrayList<Proceso> procesos = new ArrayList<>();

        for (int i = 0; i < numProcesos; i++) {
            System.out.println("Ingrese los datos para el proceso " + (i + 1));
            System.out.print("Nombre del proceso: ");
            String nombre = scanner.next();
            System.out.print("Ráfaga de CPU: ");
            int rafagaCPU = scanner.nextInt();
            System.out.print("Tiempo de llegada: ");
            int tiempoLlegada = scanner.nextInt();

            procesos.add(new Proceso(nombre, rafagaCPU, tiempoLlegada));
        }

        // Ordena los procesos por tiempo de llegada
        Collections.sort(procesos, (p1, p2) -> Integer.compare(p1.getTiempoLlegada(), p2.getTiempoLlegada()));

        int tiempoActual = 0;
        ArrayList<Proceso> colaListos = new ArrayList<>();
        ArrayList<Proceso> colaTerminados = new ArrayList<>();
        StringBuilder ordenEjecucion = new StringBuilder();

        System.out.println("\nTabla de procesos ingresados:");
        System.out.println("+---------+--------------+------------------+");
        System.out.println("| Proceso | Ráfaga de CPU | Tiempo de Llegada |");
        System.out.println("+---------+--------------+------------------+");

        for (Proceso proceso : procesos) {
            System.out.printf("|    %-3s  |       %-3d      |         %-3d        |\n", proceso.getNombre(), proceso.getRafagaCPU(), proceso.getTiempoLlegada());
        }

        System.out.println("+---------+--------------+------------------+\n");

        System.out.println("Orden en que los procesos hacen uso del CPU:");

        while (!procesos.isEmpty() || !colaListos.isEmpty()) {
            // Mover procesos que llegan al tiempo actual a la cola de listos
            while (!procesos.isEmpty() && procesos.get(0).getTiempoLlegada() <= tiempoActual) {
                colaListos.add(procesos.remove(0));
            }

            if (!colaListos.isEmpty()) {
                // Ordena la cola de listos por ráfaga de CPU más corta
                Collections.sort(colaListos, (p1, p2) -> Integer.compare(p1.getRafagaCPU(), p2.getRafagaCPU()));

                Proceso procesoEjecucion = colaListos.remove(0);
                int horaInicio = tiempoActual;
                int tiempoEjecucion = procesoEjecucion.getRafagaCPU();
                tiempoActual += tiempoEjecucion;
                int horaFin = tiempoActual;

                procesoEjecucion.setTiempoEspera(horaInicio - procesoEjecucion.getTiempoLlegada());
                procesoEjecucion.setTiempoRetorno(horaFin);

                colaTerminados.add(procesoEjecucion);
                ordenEjecucion.append(procesoEjecucion.getNombre()); // Mostrar solo la letra del proceso
                
                System.out.println("Proceso " + procesoEjecucion.getNombre() +
                        " - INICIO: " + horaInicio +
                        ", FIN: " + horaFin);
            } else {
                tiempoActual++; // No hay procesos listos, el CPU está inactivo.
            }
        }

        // Calcular tiempo promedio de retorno y mostrar resultados
        System.out.println("\nCálculos de tiempo medio de retorno (TMR):");
        double tiempoTotalRetorno = 0;

        for (Proceso proceso : colaTerminados) {
            int tiempoRetorno = proceso.getTiempoRetorno();
            System.out.println(proceso.getNombre() + " = " + tiempoRetorno);
            tiempoTotalRetorno += tiempoRetorno;
        }

        double tiempoPromedioRetorno = tiempoTotalRetorno / numProcesos;
        System.out.println("\nTiempo Medio de Retorno (TMR): " + tiempoPromedioRetorno);

        // Calcular tiempo promedio de espera y mostrar resultados
        System.out.println("\nCálculos de tiempo medio de espera (TME):");
        double tiempoTotalEspera = 0;

        for (Proceso proceso : colaTerminados) {
            int tiempoEspera = proceso.getTiempoEspera();
            System.out.println(proceso.getNombre() + " = (" + proceso.getTiempoRetorno() + " - " + proceso.getRafagaCPU() + ") " + tiempoEspera);
            tiempoTotalEspera += tiempoEspera;
        }

        double tiempoPromedioEspera = tiempoTotalEspera / numProcesos;
        System.out.println("\nTiempo Medio de Espera (TME): " + tiempoPromedioEspera);

        scanner.close();
    }
}

class Proceso {
    private String nombre;
    private int rafagaCPU;
    private int tiempoLlegada;
    private int tiempoEspera;
    private int tiempoRetorno;

    public Proceso(String nombre, int rafagaCPU, int tiempoLlegada) {
        this.nombre = nombre;
        this.rafagaCPU = rafagaCPU;
        this.tiempoLlegada = tiempoLlegada;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRafagaCPU() {
        return rafagaCPU;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public int getTiempoRetorno() {
        return tiempoRetorno;
    }

    public void setTiempoRetorno(int tiempoRetorno) {
        this.tiempoRetorno = tiempoRetorno;
    }
}
