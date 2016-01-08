package Quine2;
//Pertenece al paquete Quine2

import java.util.*;
import java.io.*;
//Importamos las clases que vamos a usar de los paquetes java.util y java.io

class Minitermino {
    public static final byte NoImporta = 2; //Variable NoImporta debe ser un Byte (-128 a 127) y distinto del 0 ó 1 (en este caso 99).
    private byte[] valorVariables; //Atributo más importante de los minitérminos (el valor de cada bit de un minitérmino). Se almacena como vector de bytes.
    
    public Minitermino(byte[] valorVariables) {
        //Método constructor de la clase Minitermino.
        this.valorVariables = valorVariables;
        /*Cuando llamamos al constructor, este admite como parámetro de entrada
        un vector de bytes (valores de los minitérminos: 0,1,NoImporta).*/
    }
    
    public int cuentaVars() {
        return valorVariables.length;
        /*Devuelve el número de variables de los minitérminos (este número es
        constante en cada ejecución del programa)*/
    }
    
    public String toString() {
        /*Metodo público que convierte a cadena de caracteres el valor de los
        minitérminos para poder visualizarlos al final de la ejecución.*/
        String resultado = "{ ";
        for(int i=0; i<valorVariables.length;i++) {
            if (valorVariables[i] == NoImporta)
                resultado = resultado + "_";
            else
                resultado = resultado + valorVariables[i];
            resultado = resultado + " ";
        }
        resultado = resultado + "}";
        return resultado;
        /* El minitérmino final viene dedado por la forma {10_1} donde 1 es la variable
        activa, 0 es la variable negada y '_' es condición no importa. */
    }
    
    public int cuentaValores(byte valor) {
        /*Método público que toma como parámetro de entrada un valor (0, 1 ó NoImporta)
        y devuelve el número de veces que existe dicho valor en el minitérmino.*/
        int resultado = 0;
        for(int i=0; i<valorVariables.length; i++) {
            if (valorVariables[i] == valor) {
                resultado++;
            }
        }
        return resultado;
    }
    
    public boolean equals(Object objeto) {
        /*Método público que sobreescribe al método de la superclase Object
        que indica cuándo dos objetos minitérminos son iguales.*/
        if (objeto == this) {
            return true;
        } else if (objeto == null || !getClass().equals(objeto.getClass())) {
            return false;
        } else {
            Minitermino minter1 = (Minitermino)objeto;
            return Arrays.equals(this.valorVariables, minter1.valorVariables);
         /*Aquí la clave está en este else, dos minitérminos van a ser iguales si coinciden
            todos los bits de estos minitérminos. Por ello se hace uso del método equals
            de la clase Arrays, que evalúa los elementos de los vectores y devuelve true
            si son exactamente iguales, false si no lo son.*/   
        }
    }
    
    public int hashCode() {
        /*Método público que sobreescribe al método de la superclase Object que indica
        el valor hash del minitérmino en cuestión.*/
        return valorVariables.hashCode();
    }
    
    public boolean implica(Minitermino miniterm) {
        /*Método público que devuelve true si un minitérmino implica a otro, devuelve false
        si el minitérmino no implica a otro.*/
         for(int i=0; i<valorVariables.length; i++) {
            if (this.valorVariables[i] != NoImporta &&
            this.valorVariables[i] != miniterm.valorVariables[i]) {
            return false;
        }
            //Simplemente hay que ver si las variables del minitérmino A que son 1 ó 0 también lo son en el minitérmino B.
    }
    return true;
    /*Por ejemplo el minitérmino {110_} implica a {1101} (devolverá TRUE),
    y el minitérmino {110_} no implica a {1000}, devolverá FALSE.*/
}
    
    public static Minitermino read(Reader reader) throws IOException {
     /*Método público que sirve para leer el archivo .txt desde donde el usuario
        introduce los minitérminos. Tira excepción si no encuentra el archivo.*/   
    int a = '\0';
    ArrayList<Byte> columna = new ArrayList<Byte>();
    while (a != '\n' && a != -1) {
        /*Mientras no haya salto de línea, vamos almacenando en el ArrayList columna
        los bits de cama minitérmino. */
        a = reader.read();
        if (a == '0') {
            columna.add((byte)0);
        } else if (a == '1') {
            columna.add((byte)1);
        }
    }
    if (columna.size() > 0) {
        /*Si este ArrayList tiene tamaño, creamos un vector llamado resultadoBytes
        que tiene en cada posición los bits correspondientes al minitérminos.*/
        byte[] resultadoBytes = new byte[columna.size()];
        for(int i=0; i<columna.size(); i++) {
            resultadoBytes[i] = (byte)columna.get(i);
        }
        return new Minitermino(resultadoBytes);
        //Con esta sentencia creamos el minitérmino correspondiente a cada línea del .txt
    } else {
        return null;
    }
    /*Básicamente este método consigue leer los minitérminos introducidos por el usuario
    en el .txt y verificar que están introducidos de la forma correcta, es decir, que solo
    tengan valor 1 ó 0 y que además todas las filas tengan el mismo número de bits. 
    En caso contrario dará error de compilación (se exceden los límites).*/
}
    
    public Minitermino combinar(Minitermino term) {
        /*Método público que devuelve referencia NULA si dos minitérminos no se pueden
        combinar(si son iguales ó si difieren en más de un bit), el método devuelve un 
        objeto Minitérmino nuevo si los dos minitérminos sí se pueden combinar.*/
        int posDifieren = -1; //La posición donde los minitérminos difieren.
        for(int i=0; i<valorVariables.length;i++) {
            //Recorremos los dos minitérminos que se van a combinar.
            if (this.valorVariables[i] != term.valorVariables[i]) {
                //Si el elemento i difiere.
                if(posDifieren == -1) { //Si no han diferido antes.     
                    posDifieren = i; //Se actualiza la posición donde difieren.
                } else { //Ya han diferido más de una vez (no son combinables).
                    return null; //Retornamos nulo.
                }
            }       
        }
        if (posDifieren == -1) {
            //Los dos minitérminos son iguales.
            return null; //Retornamos nulo.
        }
        //En cualquier otro caso, los dos minitérminos sí se pueden combinar.
        byte[] resultadoVars = valorVariables.clone(); //Clonamos el minitérmino.
        resultadoVars[posDifieren] = NoImporta; //Condición no importa en la posición donde difieren.
        return new Minitermino(resultadoVars); //Creamos el nuevo minitérmino.
    
    }

}

