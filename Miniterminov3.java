package pruebasets;
//Pertenece al paquete Quine2

import java.util.*;
import java.io.*;
//Importamos las clases que vamos a usar de los paquetes java.util y java.io

class Miniterminov3 {
    public static byte n_funciones;
    public static byte n_variables = 1;
    public boolean tachado;
    public boolean esencial;
    public boolean noImporta1;
    public static final byte NoImporta = 2; //Variable NoImporta debe ser un Byte (-128 a 127) y distinto del 0 ó 1 (en este caso 99).
    private byte[] valorVariables;
    public boolean[] etiqueta;
    public boolean[] esImplicado;
    public boolean[] vnoImporta;
    public boolean[] solucion;
    //Atributo más importante de los minitérminos (el valor de cada bit de un minitérmino). Se almacena como vector de bytes.
    
    public Miniterminov3(byte[] valorVariables) {
        //Método constructor de la clase Minitermino.
        this.valorVariables = valorVariables;
        this.tachado = false;
        this.esencial = false;
        /*Cuando llamamos al constructor, este admite como parámetro de entrada
        un vector de bytes (valores de los minitérminos: 0,1,NoImporta).*/
    }
        public Miniterminov3(byte[] valorVariables, boolean noImporta1) {
        //Método constructor de la clase Minitermino.
        this.valorVariables = valorVariables;
        this.tachado = false;
        this.esencial = false;
        this.noImporta1 = noImporta1;
        /*Cuando llamamos al constructor, este admite como parámetro de entrada
        un vector de bytes (valores de los minitérminos: 0,1,NoImporta).*/
    }
        
    public Miniterminov3(byte[] valorVariables, boolean[] etiqueta) {
        this.valorVariables = valorVariables;
        this.etiqueta = etiqueta;
        this.solucion = Arrays.copyOf(etiqueta, etiqueta.length);

    }    
        
    public Miniterminov3(byte[] valorVariables, boolean[] etiqueta, boolean[] vnoImporta) {
        this.valorVariables = valorVariables;
        this.etiqueta = etiqueta;
        this.vnoImporta = vnoImporta;
        this.esImplicado = new boolean[n_funciones];
        this.solucion = Arrays.copyOf(etiqueta, etiqueta.length);

    }
    
    public Miniterminov3(int miniterminoInt, boolean noImporta1) {
        this.noImporta1 = noImporta1;
        String miniterminoBin = Integer.toBinaryString(miniterminoInt); //miniterminoBin contiene un String binario que representa al entero introducido al constructor
        byte[] resultadoBytes = new byte[n_variables]; //Vector de byte inicializado a 0 todas las posiciones de longitud  igual a n_variables
        int k = resultadoBytes.length - miniterminoBin.length(); //Índice del primer elemento de resultadoBytes que actualizaremos
        for (int i = 0; i < miniterminoBin.length(); i++ ) {
            if(miniterminoBin.charAt(i) == '1') { //Quiero poner 1 en el vector resultadoBytes en la posición correspondiente
                resultadoBytes[k] = (byte)1;
            }
            k = k+1;
        }
        //Tras el for ya tengo en resultadoBytes el vector de Bytes correspondiente a mi minitérmino.
        this.valorVariables = resultadoBytes;
    }
    
    public Miniterminov3(int miniterminoInt, boolean[] etiqueta, boolean[] vnoImporta) {
        this.etiqueta = etiqueta;
        this.vnoImporta = vnoImporta;
        this.esImplicado = new boolean[n_funciones];
        this.solucion = Arrays.copyOf(etiqueta, etiqueta.length);
        
        String miniterminoBin = Integer.toBinaryString(miniterminoInt); //miniterminoBin contiene un String binario que representa al entero introducido al constructor
        byte[] resultadoBytes = new byte[n_variables]; //Vector de byte inicializado a 0 todas las posiciones de longitud  igual a n_variables
        int k = resultadoBytes.length - miniterminoBin.length(); //Índice del primer elemento de resultadoBytes que actualizaremos
        for (int i = 0; i < miniterminoBin.length(); i++) {
            if (miniterminoBin.charAt(i) == '1') { //Quiero poner 1 en el vector resultadoBytes en la posición correspondiente
                resultadoBytes[k] = (byte) 1;
            }
            k = k + 1;
        }
        //Tras el for ya tengo en resultadoBytes el vector de Bytes correspondiente a mi minitérmino.
        this.valorVariables = resultadoBytes;
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
        resultado = resultado + Arrays.toString(solucion); //Esta ultima parte Arrays.toString(this.etiqueta) solo usada en minitérminos multiFuncion.
        return resultado;
        /* El minitérmino final viene dedado por la forma {10_1} donde 1 es la variable
        activa, 0 es la variable negada y '_' es condición no importa. */
    }
    
    public String toString2() {
        /*Método público que convierte a cadena de caracteres en formato de literales
        el valor de los minitérminos para poder visualizarlos al final de la ejecución*/
        StringBuilder sb = new StringBuilder();
        String resultado;
        char k = 'a';
        String negado = "'";
        for(int i = 0; i< valorVariables.length; i++) {
            if (valorVariables[i] == 1) {
                sb.append(k);
            } 
            else if (valorVariables[i] == 0) {
                sb.append(k);
                sb.append(negado);
                
            }
            
        k++; //Sumamos 1 al caracter, es decir, obtenemos el inmediatamente siguiente según el abecedario.
        }
        resultado = sb.toString();
        return resultado;
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
            Miniterminov3 minter1 = (Miniterminov3)objeto;
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
    
    public boolean implica(Miniterminov3 miniterm) {
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
    
    public static Miniterminov3 read(Reader reader) throws IOException {
     /*Método público que sirve para leer el archivo .txt desde donde el usuario
        introduce los minitérminos. Tira excepción si no encuentra el archivo.*/   
    int a = '\0';
    boolean noImporta = false;
    ArrayList<Byte> columna = new ArrayList<Byte>();
    while (a != '\n' && a != -1) {
        /*Mientras no haya salto de línea, vamos almacenando en el ArrayList columna
        los bits de cama minitérmino. */
        a = reader.read();
        if (a == '0') {
            columna.add((byte)0);
        } else if (a == '1') {
            columna.add((byte)1);
        } else if (a=='s') {
            noImporta = false;
        } else if (a=='n') {
            noImporta = true;
        }
    }
    if (columna.size() > 0) {
        /*Si este ArrayList tiene tamaño, creamos un vector llamado resultadoBytes
        que tiene en cada posición los bits correspondientes al minitérminos.*/
        byte[] resultadoBytes = new byte[columna.size()];
        for(int i=0; i<columna.size(); i++) {
            resultadoBytes[i] = (byte)columna.get(i);
        }
        return new Miniterminov3(resultadoBytes,noImporta);
        //Con esta sentencia creamos el minitérmino correspondiente a cada línea del .txt
    } else {
        return null;
    }
    }
    /*Básicamente este método consigue leer los minitérminos introducidos por el usuario
    en el .txt y verificar que están introducidos de la forma correcta, es decir, que solo
    tengan valor 1 ó 0 y que además todas las filas tengan el mismo número de bits. 
    En caso contrario dará error de compilación (se exceden los límites).*/
    
    public static Miniterminov3 readMulti(Reader reader) throws IOException {
     /*Método público que sirve para leer el archivo .txt desde donde el usuario
        introduce los minitérminos. Tira excepción si no encuentra el archivo.*/   
    int a = '\0';
    boolean noImporta = false;
    boolean[] etiqueta = new boolean[n_funciones];
    boolean[] vnoImporta = new boolean[n_funciones];
    
    ArrayList<Byte> columna = new ArrayList<Byte>();
    while (a != '\n' && a != -1) {
        /*Mientras no haya salto de línea, vamos almacenando en el ArrayList columna
        los bits de cama minitérmino. */
        a = reader.read();
        if (a == '0') {
            columna.add((byte)0);
        } else if (a == '1') {
            columna.add((byte)1);
        } else if (a=='s') {
            noImporta = false;
        } else if (a=='n') {
            noImporta = true;
        } else if (a=='A') {
            etiqueta[0] = true;
        } else if (a=='a') {
            etiqueta[0] = true;
            vnoImporta[0] = true;     
        } else if (a=='B') {
            etiqueta[1] = true;     
        } else if (a=='b') {
            etiqueta[1] = true;
            vnoImporta[1] = true;
        } else if (a=='C') {
            etiqueta[2] = true;        
        } else if (a=='c') {
            etiqueta[2] = true;
            vnoImporta[2] = true;
        } else if (a=='D') {
            etiqueta[3] = true;
        } else if (a=='d') {
            etiqueta[3] = true;
            vnoImporta[3] = true;
        } else if (a=='E') {
            etiqueta[4] = true;
        } else if (a=='e') {
            etiqueta[4] = true;
            vnoImporta[4] = true;
        } else if (a=='F') {
            etiqueta[5] = true;
        } else if (a=='f') {
            etiqueta[5] = true;
            vnoImporta[5] = true;
        } else if (a=='G') {
            etiqueta[6] = true;
        } else if (a=='g') {
            etiqueta[6] = true;
            vnoImporta[6] = true;
        } else if (a=='H') {
            etiqueta[7] = true;
        } else if (a=='h') {
            etiqueta[7] = true;
            vnoImporta[7] = true;
        }
    }
    if (columna.size() > 0) {
        /*Si este ArrayList tiene tamaño, creamos un vector llamado resultadoBytes
        que tiene en cada posición los bits correspondientes al minitérminos.*/
        byte[] resultadoBytes = new byte[columna.size()];
        for(int i=0; i<columna.size(); i++) {
            resultadoBytes[i] = (byte)columna.get(i);
        }
        return new Miniterminov3(resultadoBytes,etiqueta,vnoImporta);
        //Con esta sentencia creamos el minitérmino correspondiente a cada línea del .txt
    } else {
        return null;
    }
    /*Básicamente este método consigue leer los minitérminos introducidos por el usuario
    en el .txt y verificar que están introducidos de la forma correcta, es decir, que solo
    tengan valor 1 ó 0 y que además todas las filas tengan el mismo número de bits. 
    En caso contrario dará error de compilación (se exceden los límites).*/
}
    
    public Miniterminov3 combinar(Miniterminov3 term) {
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
        return new Miniterminov3(resultadoVars); //Creamos el nuevo minitérmino.
    
    }
    
    public Miniterminov3 combinarMulti(Miniterminov3 term) {
        /*Método público que devuelve referencia NULA si dos minitérminos no se pueden
         combinarMulti(si son iguales ó si difieren en más de un bit O si no se pueden combinar por etiqueta), el método devuelve un 
         objeto Minitérmino nuevo si los dos minitérminos sí se pueden combinar (tanto por propiedad como por etiqueta).*/

        boolean resultadoEtiqueta = this.combinarEtiqueta(term); //Variable true si se pueden combinar por etiqueta, false en caso de que no.

        if (resultadoEtiqueta == false) { //Si por etiqueta no se pueden combinar, devolvemos nulo (que manejaremos en otra parte del programa).
            return null;
        } else { //Si por etiqueta si se pueden combinar, pasamos al proceso de intentar combinar por diferencia de 1 bit.

            int posDifieren = -1; //La posición donde los minitérminos difieren.
            for (int i = 0; i < valorVariables.length; i++) {
                //Recorremos los dos minitérminos que se van a combinar.
                if (this.valorVariables[i] != term.valorVariables[i]) {
                    //Si el elemento i difiere.
                    if (posDifieren == -1) { //Si no han diferido antes.     
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
            
            //Creación de etiqueta con el AND lógico de las etiquetas de los 2 minitérminos combinados:
            boolean[] etiquetaCombinado = this.etiqueta.clone();
            for(int i = 0; i<etiquetaCombinado.length; i++) {
                etiquetaCombinado[i] = this.etiqueta[i] && term.etiqueta[i];
            }
            
            byte[] resultadoVars = valorVariables.clone(); //Clonamos el minitérmino.
            resultadoVars[posDifieren] = NoImporta; //Condición no importa en la posición donde difieren.
            return new Miniterminov3(resultadoVars,etiquetaCombinado); //Creamos el nuevo minitérmino.

        }

    }
    
    public boolean combinarEtiqueta(Miniterminov3 term) { //Método que devuelve true si se pueden combinar por etiqueta (la etiqueta resultante no es nula)
        
        for(int i=0; i < this.etiqueta.length; i++) { //Recorrer todos los elementos del atributo etiqueta.
            if(this.etiqueta[i] && term.etiqueta[i]) { //Si el AND lógico entre dos elementos es TRUE (la etiqueta no será nula en su totalidad y devolvemos true).
                return true;
            }
        }
        
        return false; //Si todos los AND lógicos han dado false, la etiqueta es nula en su totalidad y retornamos false.
    }
    
    public boolean etiquetaCubre(Miniterminov3 term) {
        
        for(int i=0; i < this.etiqueta.length; i++) {
            if (this.etiqueta[i] == false && term.etiqueta[i] == true) {
                return false;
            }
        }
        return true;
    }
    
    public void setEsImplicadoDefault() {
        for(int i = 0; i < this.esImplicado.length; i++) { //Recorro esImplicado
            this.esImplicado[i] = this.vnoImporta[i]; //Por defecto significa que el vector esimplicado sea igual que el vector de no Importas.
        }
    }
      
    public int calculaCoste1() { //Calcula el coste de un minitérmino. Formula = longitud de variables - número de condiciones no importa (ese es el número de literales que tendrá la puerta and).
        int coste;
        coste = this.cuentaVars() - this.cuentaValores(NoImporta);
        return coste;
    }
    
    public int calculaCoste2() { //Calcula el coste de un minitérmino para multifunciones en función de las veces que aparece en diferentes funciones.
        //recorro la etiqueta
        int coste2 = 0;
        for(int i = 0; i < this.etiqueta.length ; i++) {
            if ( this.etiqueta[i] == true) {
                coste2 = coste2 + 1;
            }
        } //terminamos de recorrer etiqueta del minitérmino
        
        coste2 = coste2-1; //Restamos uno puesto que el coste asociado a calculaCoste2 es 1 por cada función a la que pertenezca el minitermino (0 coste a 1 funcion, 1 coste a 2 funciones).
        return coste2;
        
    }
    
    public int calculaCosteMulti() { //Devuelve el coste de un minitérmino. Se calcula sumando el coste por su número de literales (calculaCoste1) y el coste por aparecer en más de una función (calculaCoste2).
        int coste1, coste2, costeMulti;
        coste1 = this.calculaCoste1();
        coste2 = this.calculaCoste2();
        
        costeMulti = coste1 + coste2;
        return costeMulti;
        
        
        
    }

}

