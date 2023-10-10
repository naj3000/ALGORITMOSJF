/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sjf;

/**
 *
 * @author USUARIO
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SJF {
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

        System.out.println("\nOrden en que los procesos hacen uso del CPU:");

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
                procesoEjecucion.setTiempoRetorno(horaFin - procesoEjecucion.getTiempoLlegada());

                colaTerminados.add(procesoEjecucion);
                ordenEjecucion.append(procesoEjecucion.getNombre()); // Mostrar solo la letra del proceso
            } else {
                tiempoActual++; // No hay procesos listos, el CPU está inactivo.
            }
        }

        // Mostrar orden de ejecución
        System.out.println(ordenEjecucion.toString());

        // Calcular tiempos promedio
        double tiempoPromedioEspera = 0;
        double tiempoPromedioRetorno = 0;

        for (Proceso proceso : colaTerminados) {
            tiempoPromedioEspera += proceso.getTiempoEspera();
            tiempoPromedioRetorno += proceso.getTiempoRetorno();
        }

        tiempoPromedioEspera /= colaTerminados.size();
        tiempoPromedioRetorno /= colaTerminados.size();

        // Mostrar resultados detallados
        System.out.println("\nResultados:");
        for (Proceso proceso : colaTerminados) {
            System.out.println("Proceso " + proceso.getNombre() +
                    " - Tiempo de Espera: " + proceso.getTiempoEspera() +
                    " - Tiempo de Retorno: " + proceso.getTiempoRetorno() +
                    " (Hora de inicio: " + (proceso.getTiempoLlegada() + proceso.getTiempoEspera()) +
                    ", Hora de fin: " + (proceso.getTiempoLlegada() + proceso.getTiempoRetorno()) + ")");
        }

        System.out.println("\nTiempo Medio de Espera: " + tiempoPromedioEspera);
        System.out.println("Tiempo Medio de Retorno: " + tiempoPromedioRetorno);

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
