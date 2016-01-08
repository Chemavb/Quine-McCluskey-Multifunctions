package Quine2;
//Pertenece al paquete Quine2
import java.util.*;
import java.io.*;
//Importamos las clases que vamos a usar de los paquetes java.util y java.io

class Tabla {
   
    public boolean combinacion_no_finalizada = true;
    public List<Minitermino> listaTerminos; //Lista que almacena los minitérminos en primera instancia (se va actualizando y termina con los implicantes primos).
    public List<Minitermino> listaImplicantesPrimos;
    private List<Minitermino> listaOriginal; //Lista que almacena los minitérminos originales.
    
    public Tabla(List<Minitermino> listaTerminos) {
        //Método público CONSTRUCTOR de la tabla de minitérminos (listaTerminos).
        this.listaTerminos = listaTerminos;
    }
    
    public List<List<Minitermino>> ordenarTabla() {
        List<List<Minitermino>> vector = new ArrayList<List<Minitermino>>();
        int n = listaTerminos.get(0).cuentaVars();
        for (int i = 0; i<=n;i++) {
            vector.add(new ArrayList<Minitermino>());
        }
        
        int n_unos;
        for (int i = 0; i<this.listaTerminos.size(); i++) { //Recorro la lista de minitérminos que entran
            Minitermino minterm = this.listaTerminos.get(i);
            n_unos=minterm.cuentaValores((byte)1);
        
            List <Minitermino> lista = vector.get(n_unos);
            lista.add(minterm);
        
        }
        return vector;
    }
    
   public List<List<Minitermino>> combinarGruposAdyacentes(List<List<Minitermino>> vector1) {
       //Función que admite una estructura "lista de listas de miniterminos" y devuelve una estructura de la misma forma con una lista de minitérminos menos.
       //Llamamos al método combinar para generar los nuevo minitérminos combinados.
       
        int num_sublistas = vector1.size(); //Número de sublistas que tenemos en nuestra estructura vector1
        List<List<Minitermino>> vector2 = new ArrayList<List<Minitermino>>(); //Generamos el nuevo vector con un elemento menos.
        for (int i = 0; i < num_sublistas-1 ; i++) {
            vector2.add(new ArrayList<Minitermino>());
        }
        
        for (int k = 0; k <num_sublistas-1; k++) { //Recorremos todas las sublistas y comparamos grupos adyacentes.
            List<Minitermino> listaActual = vector1.get(k);
            List<Minitermino> listaSiguiente = vector1.get(k+1);
            
            for (int i=0; i < listaActual.size(); i++) {
                for (int j=0; j < listaSiguiente.size(); j++) {
                    
                    Minitermino miniterminoCombinado = listaActual.get(i).combinar(listaSiguiente.get(j));
                    if (miniterminoCombinado != null) {
                        vector2.get(k).add(miniterminoCombinado);
                    }
                }
            }
        }
        //Leer vector2 (si todos los elementos son nulos, entonces variable Combinacion_Finalizada = True) y desde el main (u otro método) se corta el while y no invocamos a este método de combinar grupos adyacentes.
        for(int k = 0; k<num_sublistas-1; k++) {
            List<Minitermino> listaActual = vector2.get(k);
            if (listaActual.size() != 0) { //Con que una lista de la nueva estructura generada (vector2) tenga al menos un minitérmino, entonces es que se ha producido una simplificación.
               combinacion_no_finalizada = true; //Actualizamos la variable.
            }
        }
        //Leer vector1. Todos los elementos que no hayan sido combinados (Minitermino.tachado=False). Todos esos elementos se añaden a la lista<miniterminos> llamada listaImplicantesPrimos.
        return vector2;
        
    }

    @SuppressWarnings("unchecked")
    public void reducirAImplicantesPrimos() {
        listaOriginal = new ArrayList<Minitermino>(listaTerminos); //Llamos al constructor que admite como parámetro de entrada una colección.
        int numVars = listaTerminos.get(0).cuentaVars();
        ArrayList<Minitermino>[][] tabla1 = new ArrayList[numVars + 1][numVars + 1]; //Creamos un ArrayList de Minitérminos de dos dimensiones y le llamamos tabla1.
        //Tiene siempre una columna y una más que el nº de variables de la función a simplificar.
        for(int dontKnows=0; dontKnows <= numVars; dontKnows++) { //Recorremos la tabla1 y cada elemento de esta tabla será un ArrayList.
            for(int ones=0; ones <= numVars; ones++) {
            tabla1[dontKnows][ones] = new ArrayList<Minitermino>(); //Con esta sentencia creamos el ArrayList en cada elemento.
        }
        }
        for(int i=0; i < listaTerminos.size();i++){ //Recorremos la lista de minitérminos del archivo.txt introducido por el usuario.
            int noImportas = listaTerminos.get(i).cuentaValores(Minitermino.NoImporta);
            int unos = listaTerminos.get(i).cuentaValores((byte) 1);
            //Almacenamos en la variable noImportas y unos el número de condiciones no importa y de unos respectivamente que tiene el minitérmino en cuestión.
            tabla1[noImportas][unos].add(listaTerminos.get(i));
            /*Añadimos el minitérmino en la posición (i,j) de la tabla, donde i indica el número de condiciones no importa del minitérmino y j indica
            el número de unos que tiene el minitérmino. (Ver gráfico de diapositivas, se entiende mejor).*/
        }
        
    for(int noImportas=0; noImportas<=numVars - 1; noImportas++) {
        for(int unos =0; unos <= numVars - 1; unos++) {
            //Recorremos la tabla llamada tabla1 de dos dimensiones (filas = nº condiciones NoImporta, columnas = nº de Unos del minitérmino).
            ArrayList<Minitermino> izquierda  = tabla1[noImportas][unos]; //Variable de referencia a un ArrayList que se actualiza (siempre a la izquierda del siguiente). 
            ArrayList<Minitermino> derecha = tabla1[noImportas][unos + 1]; //Variable de referencia a un ArrayList que se actualiza (siempre a la derecha del anterior).
            ArrayList<Minitermino> salida   = tabla1[noImportas+1][unos]; //Variable de referencia a un ArrayList que siempre está en la fila inferior a la varaible 'izquierda'.
            for(int izqIdx = 0; izqIdx < izquierda.size(); izqIdx++) {
                for(int derIdx = 0; derIdx < derecha.size(); derIdx++) { //Recorremos todos los minitérminos de la lista de cada posición de la tabla1.
                    Minitermino combinado = izquierda.get(izqIdx).combinar(derecha.get(derIdx)); //Llamamos al método combinar con cada elemento.
                    if (combinado != null) { //Si el Minitérmino combinado no es nulo (es decir, que se puede combinar).
                        if (!salida.contains(combinado)) { //Si la lista de salida no contiene a este Minitérmino.
                            salida.add(combinado); //Añadimos a la lista el Minitérmino que recientemente se ha combinado.
                        }
                        //Actualización de la lista de implicantes primos (variable listaTerminos).
                        listaTerminos.remove(izquierda.get(izqIdx)); //Eliminamos el 1er minitérmino que se ha combinado. (No será IP).
                        listaTerminos.remove(derecha.get(derIdx)); //Eliminamos el 2º minitérmino que se ha combinado. (No será IP).
                        if (!listaTerminos.contains(combinado)) { //Si la lista de IP no contiene al nuevo minitérmino generado, lo añadimos a la lista.
                            listaTerminos.add(combinado);
                        }
                    }
                }
            }
        }
    }
    
     //Todo esto se va repitiendo hasta terminar de recorrer la tabla1 (con un único scaneo de arriba a abajo completamos el proceso). 
    } 
    //Se cierra el método.
    
    
    
    
    public void reducirAImplicantesPrimosEsenciales() {
        /*Método público que obtiene de la variable listaTerminos los implicantes primos esenciales.*/
    
        int numImplicantesPrimos = listaTerminos.size(); //Almacenamos en la variable el tamaño de la lista actualizada de minitérminos.
        int numOriginalTerminos   = listaOriginal.size(); //Almacenamos en la variable el tamaño de la lista original de minitérminos.
        boolean[][] tabla = new boolean[numImplicantesPrimos][numOriginalTerminos]; //Creamos tabla booleana de dos dimensiones.
        for (int i=0; i < numImplicantesPrimos; i++) { //Recorremos la tabla
            for (int j=0; j < numOriginalTerminos; j++) {
               tabla[i][j] = listaTerminos.get(i).implica(listaOriginal.get(j)); /*Añadimos TRUE ó FALSE según si el minitérmino
               i implica al minitérmino j o no lo hace.*/
            }
        }
        
        ArrayList<Minitermino> nuevaListaMiniterminos = new ArrayList<Minitermino>(); //Creamos un nuevo ArrayList.
        boolean fin = false;
        int impl; //Definición de variables.
        while (!fin) { //Mientras no hayamos finalizado
            impl = extraeImplicantesEsenciales(tabla);
            if (impl != -1) { //Mientras no sea -1
                nuevaListaMiniterminos.add(listaTerminos.get(impl)); //Añadimos el minitérmino esencial a la lista.
            } else {
            impl = extraeMayoresImplicantes(tabla); //Llamamos al método que extrae los implicantes primos que a más minitérminos originales implican.
            if (impl != -1) {
            nuevaListaMiniterminos.add(listaTerminos.get(impl));
            } else {
            fin = true; //Finalizamos el bucle
        }
    }
}
listaTerminos = nuevaListaMiniterminos; //Se actualiza la listaTerminos de contener implicantes primos a contener implicantes primos esenciales.      
listaOriginal = null; //Damos referencia nula a la variable listaOriginal (no la vamos a utilizar más).
}
    
    

    public static Tabla read(Reader reader) throws IOException {
        /*Método público que lee si la tabla de minitérminos es correcta y si lo es, devuelve la lista de minitérminos creada.*/
        ArrayList<Minitermino> terminos = new ArrayList<Minitermino>();
        Minitermino termino;
        while ((termino = Minitermino.read(reader)) != null){ //Mientras el minitérmino no sea nulo, añadimos términos al ArrayList. 
            terminos.add(termino);
        }
        return new Tabla(terminos); //Creación de la tabla de minitérminos.
    }
    
    private int extraeImplicantesEsenciales(boolean[][] tabla) {
        /*Método público que extrae los implicantes primos esenciales de la variable listaTerminos (que contiene implicantes primos tras la combinación.)*/
    for (int termino=0; termino < tabla[0].length; termino++) { //Recorremos la tabla.
        int referencia = -1; //Variable de referencia inicializada a -1.
        for (int impl=0; impl < tabla.length; impl++) { //Recorremos las filas de cada columna
            if (tabla[impl][termino]){ //Si el elemento es true
                if (referencia == -1) { //Si la referencia es -1 (es decir, es la primera vez que encontramos la implicación).
                    referencia = impl; //Referencia se actualiza y toma el valor de la fila.
                } else {
                    // Esto ocurre cuando ha encontrado una implicación pero ya había otra (este minitérmino NO ES ESENCIAL).
                    referencia = -1; //Actualizamos la referencia.
                    break; //Nos salimos del bucle for, ahora recorremos la siguiente columna.
                }
            }
        }
        if (referencia != -1) { //Si al salir del for, referencia no es -1 (es decir, tenemos un implicante primo esencial).
            extraeImplicante(tabla, referencia); //Llamamos al método extraeImplicante.
            return referencia; //Retornamos la posición del minitérmino que es IMPLICANTE PRIMO ESENCIAL.
        }
    }
    return -1;
}
    
    private void extraeImplicante(boolean[][] table, int impl) {
        //Método que borra (pone a false) todos los elementos de una fila y columna de una tabla booleana.
    for (int term=0; term < table[0].length; term++) { //Recorremos la tabla y 'borramos' los elementos no deseados.
        if (table[impl][term]) { //Si true.
            for (int impl2=0; impl2 < table.length; impl2++) {
                table[impl2][term] = false; //Hacemos false los elementos.
            }
        }
    }
}
    
    private int extraeMayoresImplicantes(boolean[][] tabla) {
        //Método que extrae los implicantes primos de la variable 'listaTerminos' que a más número de minitérminos originales implican.
    int maxNumTerminos = 0; //Variable de referencia que contará el número de términos a los que implica.
    int maxNumTerminosImpl = -1; //Variable de referencia.
    for (int impl=0; impl < tabla.length; impl++) { //Recorremos la tabla boolena.
        int numTerminos = 0; //Variable numTerminos contadora.
        for (int term=0; term < tabla[0].length; term++) { //Recorremos la fila.
            if (tabla[impl][term]) { //Si true (si el minitérmino i implica al minitérmino original j).
                numTerminos++; //La variable numTerminos se incrementa en uno.
            }
        }
        if (numTerminos > maxNumTerminos) { //Si el número de terminos a los que implica es mayor que la referencia (que se va actualizando).
            maxNumTerminos = numTerminos; //Actualizamos la variable maxNumTerminos de referencia a dicho valor.
            maxNumTerminosImpl = impl; //Implicante que implica a la mayor cantidad de minitérminos es el correspondiente a la fila i de la tabla (índice impl).
        }
    }
    if (maxNumTerminosImpl != -1) { //Si existe algun minitérmino que implica a más de uno.
        extraeImplicante(tabla, maxNumTerminosImpl); //Llamamos al método extraeImplicante 
        return maxNumTerminosImpl; //Retorna la posición donde se encuentra el minitérmino que más minitérminos originales implica.
    }
    return -1; //Retornamos -1 para salir cuando llamemos a este método.
}

       
}

