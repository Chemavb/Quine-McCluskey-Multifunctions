package Quine2;
//Paquete Quine2

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//Importamos las clases que vamos a usar del paquete java.io.

public class Principal {
    
    public static void main(String[] args) throws IOException {
    //Programa principal, tira excepción si no encuentra el archivo.
    Tabla formula1 = Tabla.read(new BufferedReader(new FileReader(args[0])));
    List<List<Minitermino>> vector;
    vector = formula1.ordenarTabla();
    List<List<Minitermino>> vector2;
    
    while(formula1.combinacion_no_finalizada) {
        formula1.combinacion_no_finalizada = false;
        vector2 = formula1.combinarGruposAdyacentes(vector);
        vector = vector2;
 
    }
    
    
    //List<List<Minitermino>> vector_prueba = formula1.combinarGruposAdyacentes(vector1);
    //Lista que contiene Listas de minitérminos (es decir, posición 0 de vector tiene una lista de minitérminos. Posición 1 tiene otra lista de minitérminos.
    //Minitermino minitermino1 = formula1.listaTerminos.get(0);
    //System.out.println(minitermino1.toString());
    //Minitermino miniterminoprueba = vector1.get(0).get(0);
    //System.out.println(vector1.size());
    //System.out.println(miniterminoprueba);
    //System.out.println(vector_prueba.get(0).get(0));
    //List<List<Minitermino>> vector_prueba2 = formula1.combinarGruposAdyacentes(vector_prueba);
    //Minitermino minitermino_prueba = vector_prueba2.get(2).get(0);
    //System.out.println(minitermino_prueba);

    }
    
    
}
