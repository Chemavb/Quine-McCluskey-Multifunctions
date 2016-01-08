package pruebasets;
//Pertenece al paquete Quine2
import java.util.*;
import java.io.*;
import com.google.common.collect.*;
//Importamos las clases que vamos a usar de los paquetes java.util y java.io

class Tablav3 {
    
    public boolean proceso_terminado;
    public boolean combinacion_no_finalizada = true;
    public List<Miniterminov3> listaTerminos; //Lista que almacena los minitérminos en primera instancia (se va actualizando y termina con los implicantes primos).
    public List<Miniterminov3> listaTerminosImporta = new ArrayList<Miniterminov3>();
    public List<Miniterminov3> listaImplicantesPrimos  = new ArrayList<Miniterminov3>();
    public List<Miniterminov3> listaImplicantesPrimosEsenciales  = new ArrayList<Miniterminov3>();
    public List<Miniterminov3> listaMiniterminosPendientesACubrir = new ArrayList<Miniterminov3>();
    public List<Miniterminov3> listaColumna = new ArrayList<Miniterminov3>();
    public List<Miniterminov3> listaFila = new ArrayList<Miniterminov3>();
    List<List<Miniterminov3>> vectorSoluciones = new ArrayList<List<Miniterminov3>>();
    List<List<Miniterminov3>> vectorMejoresSoluciones = new ArrayList<List<Miniterminov3>>();
    List<List<Miniterminov3>> vectorSolucionesMenorCoste = new ArrayList<List<Miniterminov3>>();
    private List<Miniterminov3> listaOriginal; //Lista que almacena los minitérminos originales.
    
    public Tablav3(List<Miniterminov3> listaTerminos) {
        //Método público CONSTRUCTOR de la tabla de minitérminos (listaTerminos).
        this.listaTerminos = listaTerminos;
    }
    
    public void generarListaTerminosImporta() {
        //Recorrer la listaTerminos (lista que contiene todos los minitérminos incluidos a simplificar -tanto importa como no importa- 
        //y me voy a quedar únicamente con los importa y los almacenaré en listaTerminosImporta.
        for(Miniterminov3 miniterm: this.listaTerminos) {
            if (miniterm.noImporta1 == false ) {
                this.listaTerminosImporta.add(miniterm);
            }
        }
    }
    
    public List<List<Miniterminov3>> ordenarTabla() {
        List<List<Miniterminov3>> vector = new ArrayList<List<Miniterminov3>>();
        int n = listaTerminos.get(0).cuentaVars();
        for (int i = 0; i<=n;i++) {
            vector.add(new ArrayList<Miniterminov3>());
        }
        
        int n_unos;
        for (int i = 0; i<this.listaTerminos.size(); i++) { //Recorro la lista de minitérminos que entran
            Miniterminov3 minterm = this.listaTerminos.get(i);
            n_unos=minterm.cuentaValores((byte)1);
        
            List <Miniterminov3> lista = vector.get(n_unos);
            lista.add(minterm);
        
        }
        return vector;
    }
    
   public List<List<Miniterminov3>> combinarGruposAdyacentes(List<List<Miniterminov3>> vector1) {
       //Función que admite una estructura "lista de listas de miniterminos" y devuelve una estructura de la misma forma con una lista de minitérminos menos.
       //Llamamos al método combinar para generar los nuevo minitérminos combinados.
       
        int num_sublistas = vector1.size(); //Número de sublistas que tenemos en nuestra estructura vector1
        List<List<Miniterminov3>> vector2 = new ArrayList<List<Miniterminov3>>();//Generamos el nuevo vector con un elemento menos.
                
        for (int i = 0; i < num_sublistas-1 ; i++) {
            vector2.add(new ArrayList<Miniterminov3>());
        }
        
        for (int k = 0; k <num_sublistas-1; k++) { //Recorremos todas las sublistas y comparamos grupos adyacentes.
            List<Miniterminov3> listaActual = vector1.get(k);
            List<Miniterminov3> listaSiguiente = vector1.get(k+1);
            
            for (int i=0; i < listaActual.size(); i++) { //Recorro todos los elementos de la actual sublista
                for (int j=0; j < listaSiguiente.size(); j++) { //Recorro todos los elementos de la siguiente sublista
                    
                    Miniterminov3 miniterminoCombinado = listaActual.get(i).combinar(listaSiguiente.get(j));
                    if (miniterminoCombinado != null) {
                        listaActual.get(i).tachado = true;
                        listaSiguiente.get(j).tachado = true;
                        vector2.get(k).add(miniterminoCombinado);
                    }
                }
            }
        }
        //Leer vector2 (si todos los elementos son nulos, entonces variable Combinacion_Finalizada = True) y desde el main (u otro método) se corta el while y no invocamos a este método de combinar grupos adyacentes.
        for(int k = 0; k<num_sublistas-1; k++) {
            List<Miniterminov3> listaActual = vector2.get(k);
            if (listaActual.size() != 0) { //Con que una lista de la nueva estructura generada (vector2) tenga al menos un minitérmino, entonces es que se ha producido una simplificación.
               combinacion_no_finalizada = true; //Actualizamos la variable.
            }
        }
        //Leer vector1. Todos los elementos que no hayan sido combinados (Minitermino.tachado=False). Todos esos elementos se añaden a la lista<miniterminos> llamada listaImplicantesPrimos.
        for(int k = 0; k<num_sublistas; k++) {
            List<Miniterminov3> listaActual = vector1.get(k);
            for(int i = 0; i<listaActual.size(); i++ ) {
                Miniterminov3 minterm = listaActual.get(i);
                if (minterm.tachado == false) {
                    if(! listaImplicantesPrimos.contains(minterm)) { //Si el minitérmino combinado no existe en la listaImplicantesPrimos, se añade, en caso contrario no se añade.
                    listaImplicantesPrimos.add(minterm);
                    }
                }   
        }
        
        
        }
        return vector2;
   }
   
   public List<List<Miniterminov3>> combinarGruposAdyacentesMulti(List<List<Miniterminov3>> vector1) {
       //Función que admite una estructura "lista de listas de miniterminos" y devuelve una estructura de la misma forma con una lista de minitérminos menos.
       //Llamamos al método combinar para generar los nuevo minitérminos combinados.
       
        int num_sublistas = vector1.size(); //Número de sublistas que tenemos en nuestra estructura vector1
        List<List<Miniterminov3>> vector2 = new ArrayList<List<Miniterminov3>>();//Generamos el nuevo vector con un elemento menos.
                
        for (int i = 0; i < num_sublistas-1 ; i++) {
            vector2.add(new ArrayList<Miniterminov3>());
        }
        
        for (int k = 0; k <num_sublistas-1; k++) { //Recorremos todas las sublistas y comparamos grupos adyacentes.
            List<Miniterminov3> listaActual = vector1.get(k);
            List<Miniterminov3> listaSiguiente = vector1.get(k+1);
            
            for (int i=0; i < listaActual.size(); i++) { //Recorro todos los elementos de la actual sublista
                for (int j=0; j < listaSiguiente.size(); j++) { //Recorro todos los elementos de la siguiente sublista
                    
                    Miniterminov3 miniterminoCombinado = listaActual.get(i).combinarMulti(listaSiguiente.get(j));
                    if (miniterminoCombinado != null) {
                        if(miniterminoCombinado.etiquetaCubre(listaActual.get(i))) { //Lógica de tachado para el primer minitérmino.
                        listaActual.get(i).tachado = true;
                        }
                        if(miniterminoCombinado.etiquetaCubre(listaSiguiente.get(j))) { //Lógica de tachado para el segundo minitérmino.
                        listaSiguiente.get(j).tachado = true;
                        }
                        vector2.get(k).add(miniterminoCombinado);
                    }
                }
            }
        }
        //Leer vector2 (si todos los elementos son nulos, entonces variable Combinacion_Finalizada = True) y desde el main (u otro método) se corta el while y no invocamos a este método de combinar grupos adyacentes.
        for(int k = 0; k<num_sublistas-1; k++) {
            List<Miniterminov3> listaActual = vector2.get(k);
            if (listaActual.size() != 0) { //Con que una lista de la nueva estructura generada (vector2) tenga al menos un minitérmino, entonces es que se ha producido una simplificación.
               combinacion_no_finalizada = true; //Actualizamos la variable.
            }
        }
        //Leer vector1. Todos los elementos que no hayan sido combinados (Minitermino.tachado=False). Todos esos elementos se añaden a la lista<miniterminos> llamada listaImplicantesPrimos.
        for(int k = 0; k<num_sublistas; k++) {
            List<Miniterminov3> listaActual = vector1.get(k);
            for(int i = 0; i<listaActual.size(); i++ ) {
                Miniterminov3 minterm = listaActual.get(i);
                if (minterm.tachado == false) {
                    if(! listaImplicantesPrimos.contains(minterm)) { //Si el minitérmino combinado no existe en la listaImplicantesPrimos, se añade, en caso contrario no se añade.
                    listaImplicantesPrimos.add(minterm);
                    }
                }   
        }
        
        
        }
        return vector2;
   }
   
   public void generarListaImplicantesPrimos(List<List<Miniterminov3>> vector) { //Función que haciendo uso de las otras funciones, me crea listaImplicantesPrimos.
       
        List<List<Miniterminov3>> vector2;
        while(this.combinacion_no_finalizada) {
        this.combinacion_no_finalizada = false;
        vector2 = this.combinarGruposAdyacentes(vector);
        vector = vector2;
 
        }
   }
   
      public void generarListaImplicantesPrimosMulti(List<List<Miniterminov3>> vector) { //Función que haciendo uso de las otras funciones, me crea listaImplicantesPrimos.
       
        List<List<Miniterminov3>> vector2;
        while(this.combinacion_no_finalizada) {
        this.combinacion_no_finalizada = false;
        vector2 = this.combinarGruposAdyacentesMulti(vector);
        //Mostrar estructuras creadas:
        
        for(List<Miniterminov3> lista : vector2) {
            System.out.println(lista);
        }
        
        //
        vector = vector2;
 
        }
   }
   
   public void generarListaImplicantesPrimosEsenciales() {
       //Añade elementos a dos listas: Si un minitermino de listaImplicantesPrimos es esencial, lo añade a listaImplicantesPrimosEsenciales
       //Si no es esencial, añadimos el minitermino original IMPORTA a listaMiniterminosPendientesACubrir
       this.generarListaTerminosImporta(); //Introducimos solo los elementos importa en listaTerminosImporta.
       
        for (int i = 0; i < this.listaTerminosImporta.size(); i++) {
        Miniterminov3 miniterm1 = null;
        Miniterminov3 miniterm2 = null;
        
        int n_veces = 0;
        int j = 0;

        
        while (n_veces < 2 && j < this.listaImplicantesPrimos.size()) {
            
            if(this.listaImplicantesPrimos.get(j).implica(this.listaTerminosImporta.get(i))) {
                n_veces = n_veces+1;
                miniterm1 = this.listaImplicantesPrimos.get(j);
            }
            j=j+1;
        }
        
        if (n_veces < 2) { //Si es implicante primo esencial (el termino original solo es implicado por un minitermino implicante primo)
            if(!this.listaImplicantesPrimosEsenciales.contains(miniterm1)) {
                miniterm1.esencial = true;
                this.listaImplicantesPrimosEsenciales.add(miniterm1);
            }
        }
        else { //Si no es implicante primo esencial (el termino original es implicado más de una vez
            miniterm2 = this.listaTerminosImporta.get(i);
            this.listaMiniterminosPendientesACubrir.add(miniterm2);
            
        }
        
    }
   }
   
    public void generarListaFila() {
        //Recorremos listaImplicantesPrimos y quito los esenciales
        //Si esencial = False -> añadir a la lista ListaFila (que representa la fila de la Tabla de Cobertura).
        for(Miniterminov3 minitermino1 : this.listaImplicantesPrimos) {
            if (minitermino1.esencial == false) {
                this.listaFila.add(minitermino1);
            }
        }
    }
    
    public void generaSolucionConIPEsenciales() {
        
        if (this.vectorMejoresSoluciones.isEmpty()) {
        this.vectorMejoresSoluciones.add(listaImplicantesPrimosEsenciales);
        }
    }
    
   
    public void generarListaColumna() {
        this.listaColumna.addAll(this.listaMiniterminosPendientesACubrir);

        for (Miniterminov3 miniterm1 : this.listaImplicantesPrimosEsenciales) {

            if (!this.listaColumna.isEmpty()) {
                for (Miniterminov3 miniterm2 : this.listaMiniterminosPendientesACubrir) {
                    if (miniterm1.implica(miniterm2)) {
                        this.listaColumna.remove(miniterm2);
                    }
                }
            }
            else {
                this.proceso_terminado = true;
                this.generaSolucionConIPEsenciales();
            }
        }
        //Si tras recorrer la lista de ImplicantesPrimosEsenciales, listaColumna está vacía, entonces es que hemos terminado también y la solución es listaImplicantesPrimosEsenciales.
        if(this.listaColumna.isEmpty()) {
            this.proceso_terminado = true;
            this.generaSolucionConIPEsenciales();
        }
    }
    
    public void generarListaColumnaMulti() {
        this.listaColumna.addAll(this.listaTerminos); //Primero hacemos que listaColumna contenga a todos los elementos de listaTerminos
        
        for (Miniterminov3 miniterm : this.listaTerminos) {
            if (Arrays.equals(miniterm.vnoImporta, miniterm.etiqueta)) { //Si el vector esImplicado tras la actualización coincide con la etiqueta, ese minitérmino ya ha sido implicado y no debe aparecer en listaColumna.
                this.listaColumna.remove(miniterm);
            }
            
        }
    }
   
   public void generarTablaCobertura() {
       this.generarListaFila();
       this.generarListaColumna();
   } 
   
   public void generarTablaCoberturaMulti() {
       //this.generarListaFilaMulti();
       this.generarListaColumnaMulti();
   }

   public static boolean listaAImplicaListaB(List<Miniterminov3> listaA, List<Miniterminov3> listaB) {
       
       List<Miniterminov3> listaBClon = new ArrayList<Miniterminov3>(listaB);
       
       for (Miniterminov3 miniterminoA : listaA) {
           
           for (Miniterminov3 miniterminoB : listaB) {
               if(miniterminoA.implica(miniterminoB)) {
                   listaBClon.remove(miniterminoB);
               }
           }
           
           if (listaBClon.isEmpty()) { //listaA implica a listaB. **Algoritmo más eficiente puesto que no se espera al final de recorrer listaA para retornar true si listaB esta vacía en alguna iteración***
               return true; 
           }
       
        }
       
       return false; //Si después de recorrer todos los elementos de listaA, no conseguimos listaB vacia, significa que listaA no implica a listaB
   }
   
      public static boolean listaAImplicaListaBMulti(List<Miniterminov3> listaA, List<Miniterminov3> listaB) {
       
          //Cada vez que llamo a esta función para saber si una listaA implica a otra listaB, establezo a los valores por defecto con setEsImplicadoDefault() a los valores por defecto.
       for(Miniterminov3 miniterm : listaB) { //Recorro la lista y todos los minitérminos de listaB se actualiza su vector esImplicado. (en caso contrario se guardaría cada vez que llamo a la función listaAimplicaListaBMulti y no funcionaría correctamente.
           miniterm.setEsImplicadoDefault();
       }
          
          
       List<Miniterminov3> listaBClon = new ArrayList<Miniterminov3>(listaB);
       
       for (Miniterminov3 miniterminoA : listaA) {
           
           for (Miniterminov3 miniterminoB : listaB) {
               if(miniterminoA.implica(miniterminoB)) {
                   
                   for (int i = 0; i < miniterminoB.esImplicado.length; i++) { //Actualizo vector miniterminoB.esImplicado
                       miniterminoB.esImplicado[i] = miniterminoA.etiqueta[i] || miniterminoB.esImplicado[i];
                   }
                   
                   //Compruebo si ya pudo eliminar dicho elemento de la lista clonada:
                   
                   if (Arrays.equals(miniterminoB.etiqueta, miniterminoB.esImplicado)) {
                       //Si son iguales el vector esImplicado de miniterminoB y el vector etiqueta de miniterminoB significa que miniterminoB ya ha sido implicado por todas las funciones que debe. 
                       //Por tanto lo puedo eliminar de la lista clon
                       listaBClon.remove(miniterminoB);
                   }
                   
               }
           }
           
           if (listaBClon.isEmpty()) { //listaA implica a listaB. **Algoritmo más eficiente puesto que no se espera al final de recorrer listaA para retornar true si listaB esta vacía en alguna iteración***
               return true; 
           }
       
        }
       
       return false; //Si después de recorrer todos los elementos de listaA, no conseguimos listaBClon vacia, significa que listaA no implica a listaB
   }
      
    public static int calculaCosteMulti(List<Miniterminov3> solucion) {
        int costeTotal = 0;
        for (Miniterminov3 miniterm : solucion) { //recorrer los elementos (miniterminos) de la lista
            costeTotal = costeTotal + miniterm.calculaCoste1();
        }
        return costeTotal; //Retorna el coste total (costeMulti) de una lista de Miniterminos
    }

    public void generarVectorSoluciones() {

        List<List<Miniterminov3>> lista1 = new ArrayList<List<Miniterminov3>>(); //lista1 almacenará en cada elemento una List<Minitermino>. Y cada elemento tiene una combinación de todas las posibles combinaciones. Lista1 estará rellena de listas de miniterminos con todas las combinaciones posibles.
        Set<Miniterminov3> conjunto1 = new LinkedHashSet<Miniterminov3>(this.listaFila);
        Set<Set<Miniterminov3>> conjuntoCombinaciones = Sets.powerSet(conjunto1);

        for (Set<Miniterminov3> subconjuntoCombinado : conjuntoCombinaciones) {

            if (!subconjuntoCombinado.isEmpty()) {
                lista1.add(new ArrayList<Miniterminov3>(subconjuntoCombinado));

            }

        }

        for (List<Miniterminov3> sublista : lista1) {
            if (listaAImplicaListaB(sublista, this.listaColumna)) { //Esa combinación es solución.

                this.vectorSoluciones.add(sublista);
            }
        }

    }
    
        public void generarVectorSolucionesMulti() {

        List<List<Miniterminov3>> lista1 = new ArrayList<List<Miniterminov3>>(); //lista1 almacenará en cada elemento una List<Minitermino>. Y cada elemento tiene una combinación de todas las posibles combinaciones. Lista1 estará rellena de listas de miniterminos con todas las combinaciones posibles.
        Set<Miniterminov3> conjunto1 = new LinkedHashSet<Miniterminov3>(this.listaImplicantesPrimos); //Será this.listaFila en el futuro. Por ahora lo haremos con this.implicantesPrimos
        Set<Set<Miniterminov3>> conjuntoCombinaciones = Sets.powerSet(conjunto1);

        for (Set<Miniterminov3> subconjuntoCombinado : conjuntoCombinaciones) {

            if (!subconjuntoCombinado.isEmpty()) { //Si no es el subconjunto nulo
                lista1.add(new ArrayList<Miniterminov3>(subconjuntoCombinado));

            }

        }

        for (List<Miniterminov3> sublista : lista1) {
            //Para cada sublista vemos si implica (si es solución). Si esto se cumple, vemos si esta solución no es redundante (si no hay alguna mejor ya). Si esto también se cumple, se añade al vectorSoluciones.
            if (listaAImplicaListaBMulti(sublista, this.listaColumna)) { //this.listaColumna en el futuro. Por ahora será this.listaTerminos Esa combinación es solución.
                //sublista es solución.
                //Comprobar si sublista es redundante o no.
                int i = 0;
                boolean solucionRedundante = false;

                while (i < this.vectorSoluciones.size() && solucionRedundante == false) {

                    List<Miniterminov3> listaSolucion = this.vectorSoluciones.get(i);
                    if (sublista.containsAll(listaSolucion)) {
                        solucionRedundante = true;
                    } else if (listaSolucion.containsAll(sublista)) {
                        this.vectorSoluciones.remove(listaSolucion);
                    } else {
                        i = i+1;
                }

            } //termina el while
                
                if(solucionRedundante == false) { //Se ha salido del while sin modificar solucionRedundante, es decir, es una solución no redundante, la añadimos al vector de soluciones.
                    this.vectorSoluciones.add(sublista); //sublista es solución no redundante.
                }

        }
    }
        //Al final de este método, vectorSoluciones tendrá las soluciones  NO REDUNDANTES de la función original.
    }
    
    public void generarVectorMejoresSoluciones() {
        
        int n_terminos;
        int referencia = 10000;
        
        for(List<Miniterminov3> solucion : this.vectorSoluciones) { //Tras este for, en la variable referencia tengo el tamaño de la menor solución (la solución que tiene el menor numero de terminos).
            
            n_terminos = solucion.size();
            if (n_terminos < referencia) {
                referencia = n_terminos;
            }
        }
        
        for(List<Miniterminov3> solucion : this.vectorSoluciones) {
            if(solucion.size() == referencia){ //Esa solucion es una MEJOR SOLUCIÓN (solucion con el minimo numero de terminos).
                this.vectorMejoresSoluciones.add(solucion);
            }
        }
        
        for(List<Miniterminov3> mejorSolucion : this.vectorMejoresSoluciones) {
            mejorSolucion.addAll(this.listaImplicantesPrimosEsenciales);
        }
        
        //Tras terminar este método, vectorMejoresSoluciones contiene Listas con las mejores soluciones (menor nº de terminos) y se le concatenan los IP'esenciales que debena aparecer en la solución.
        ////////****** IMPORTANTE: HACER FORMULA PARA COMPARAR COSTES.********//////////
        
    }
    
    public void generaSolucionesMenorCosteMulti() {
        int coste;
        int referencia = Integer.MAX_VALUE;
        
        for(List<Miniterminov3> solucion : this.vectorSoluciones) { //vectorSoluciones contiene soluciones no redundantes. Ahora queremos sacar las de menor coste.
        //Recorro la lista y después de recorrer todas las sublistas, tendré en la variable referencia el menor coste encontrado.
            coste = calculaCosteMulti(solucion);
            System.out.println(coste);

            if (coste < referencia) {
                referencia = coste;
            }
        }
        
        for(List<Miniterminov3> solucion : this.vectorSoluciones) {
            if(calculaCosteMulti(solucion) == referencia) { //es solución de menor coste
                this.vectorSolucionesMenorCoste.add(solucion);
            }
        }
        
    }
    
    public void actualizaSoluciones() {
        for(List<Miniterminov3> solucionMenorCoste : this.vectorSolucionesMenorCoste) {
            for(int i = 0; i < Miniterminov3.n_funciones; i++) { //i toma valores de 0 a n, siendo n el numero de funciones-1 (ej 3 funciones, i toma 0,1,2).
                
                List<Miniterminov3> listaFilai = new ArrayList<Miniterminov3>(); //listaFilai contendrá los minitérminos de la solución que impliquen a la función i
                List<Miniterminov3> listaColumnai = new ArrayList<Miniterminov3>();//listaColumnai contendrá los minitérminos de listaColumna IMPORTA de la función i
                
                //Recorro los elementos de la lista solucionMenorCoste y voy añadiendo en listaFilai y listaColumnai los elementos correspondientes
                for(Miniterminov3 miniterm : solucionMenorCoste) {
                    if(miniterm.etiqueta[i] == true) {
                        listaFilai.add(miniterm);
                    }
                }
                
                for (Miniterminov3 miniterm1 : this.listaColumna) { //Añadir a listaColumnai los elementos correspondientes.
                    if(miniterm1.etiqueta[i] == true && miniterm1.vnoImporta[i] == false) {
                        listaColumnai.add(miniterm1);
                    }
                }
                
                //Ya tengo listaFilai y listaColumnai completa.
                //Necesito sacar la solución mejor de todas las combinaciones de listaFilai
                
                List<List<Miniterminov3>> lista1 = new ArrayList<List<Miniterminov3>>(); //lista1 almacenará en cada elemento una List<Minitermino>. Y cada elemento tiene una combinación de todas las posibles combinaciones. Lista1 estará rellena de listas de miniterminos con todas las combinaciones posibles.
                Set<Miniterminov3> conjunto1 = new LinkedHashSet<Miniterminov3>(listaFilai);
                Set<Set<Miniterminov3>> conjuntoCombinaciones = Sets.powerSet(conjunto1);
                
                for (Set<Miniterminov3> subconjuntoCombinado : conjuntoCombinaciones) {

                    if (!subconjuntoCombinado.isEmpty()) { //Si no es el subconjunto nulo
                        lista1.add(new ArrayList<Miniterminov3>(subconjuntoCombinado));

                    }

                }
                
                List<Miniterminov3> mejorLista = new ArrayList<Miniterminov3>(); //apuntará a la mejor lista de todas las sublistas (la que menos elementos tenga, la que menos tamaño tenga)
                int referencia = Integer.MAX_VALUE; //referencia contendrá en todo momento el tamaño de la mejor lista posible (menor tamaño de una lista solución).

                for (List<Miniterminov3> sublista : lista1) {
                    if (listaAImplicaListaB(sublista, listaColumnai)) { //Esa combinación es solución.
                        
                        if(sublista.size() < referencia) {
                            mejorLista = sublista;
                            referencia = mejorLista.size();
                        }
                        
                    }
                }
                
                //En este punto tengo mejorLista que tendrá los elementos de listaFilai que sí deben pertenecer a la solución.
                //Recorro los elementos de ListaFilai y si mejorLista no contiene a un elemento, eseElemento.solucion[i] = false;
                
                for ( Miniterminov3 miniterm3 : listaFilai) {
                    if (! mejorLista.contains(miniterm3)) { //Si mejorLista no contiene al miniterm3, entonces miniterm3 no es necesario que aparezca en la solución.
                        miniterm3.solucion[i] = false;
                    }
                }
                
                
            } //Cierra el for grande. Se ha actualizado el vector solucion de cada minitermino. En la posicion i aparecerá true si la funcion i debe ese minitermino en su solución, false en caso contrario.
        }
    }
       

    public static Tablav3 read(Reader reader) throws IOException {
        /*Método público que lee si la tabla de minitérminos es correcta y si lo es, devuelve la lista de minitérminos creada.*/
        ArrayList<Miniterminov3> terminos = new ArrayList<Miniterminov3>();
        Miniterminov3 termino;
        while ((termino = Miniterminov3.read(reader)) != null){ //Mientras el minitérmino no sea nulo, añadimos términos al ArrayList. 
            terminos.add(termino);
        }
        return new Tablav3(terminos); //Creación de la tabla de minitérminos.
    }
    
    public static Tablav3 readMulti(Reader reader) throws IOException {
        /*Método público que lee si la tabla de minitérminos es correcta y si lo es, devuelve la lista de minitérminos creada.*/
        ArrayList<Miniterminov3> terminos = new ArrayList<Miniterminov3>();
        Miniterminov3 termino;
        while ((termino = Miniterminov3.readMulti(reader)) != null) { //Mientras el minitérmino no sea nulo, añadimos términos al ArrayList. 
            terminos.add(termino);
        }
        return new Tablav3(terminos); //Creación de la tabla de minitérminos.
    }
    
    public static String convertirSolucionALiteral(List<Miniterminov3> listaSolucion) {
        StringBuilder sb = new StringBuilder();
        String suma = " + ";
        String resultado;
        for(Miniterminov3 minitermino : listaSolucion) {
            String miniterminoStr = minitermino.toString2();
            
            if(sb.length() != 0) { //No es el primer minitérmino a añadir
                sb.append(suma);
                sb.append(miniterminoStr);
            } else { //Si la longitud es 0, es el primer minitérmino a añadir
                sb.append(miniterminoStr);
            }
            
        }
        resultado = sb.toString();
        return resultado;
    }
    
    public static List<String> convertirSolucionALiteralMF(List<Miniterminov3> listaSolucion) {
        int n_funciones1 = Miniterminov3.n_funciones;
        String suma = " + ";
        String resultado;
        List<String> resultadoLista = new ArrayList<String>();

        for (int i = 0; i < n_funciones1; i++) { //Índice i con tantas iteraciones como número de funciones exista.
            StringBuilder sb = new StringBuilder();
            for (Miniterminov3 minitermino : listaSolucion) {
                if (minitermino.solucion[i] == true) {
                    String miniterminoStr = minitermino.toString2();

                    if (sb.length() != 0) { //No es el primer minitérmino a añadir
                        sb.append(suma);
                        sb.append(miniterminoStr);
                    } else { //Si la longitud es 0, es el primer minitérmino a añadir
                        sb.append(miniterminoStr);
                    }
                }
            }
            resultado = sb.toString();
            resultadoLista.add(resultado);
        }
        
        return resultadoLista;
    }
       
}

