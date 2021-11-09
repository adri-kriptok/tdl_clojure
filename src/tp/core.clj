(ns tp.core
  (:gen-class))

(declare driver-loop)
(declare string-a-tokens)
(declare evaluar-linea)
(declare cargar-archivo)

(defn -main []
  (driver-loop)
)
  
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; driver-loop: el REPL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn driver-loop     
   ([]
    (println "**********************************************************")
    (println "*                                                        *")
    (println "*             Contador de Palabras en Clojure            *")
    (println "*                                                        *")
    (println "**********************************************************")
    (println "*   Trabajo Practico de 75.24                            *")
    (println "*   Teoria de Lenguajes de Programacion - 2021           *")
    (println "*                                                        *")
    (println "*    - Cesar Castellanos   81404                         *")
    (println "*    - Adrian Cavaiuolo    91186                         *")
    (println "*    - Analia Acevedo      94425                         *")
    (println "*    - Edgardo Lopez       86140                         *")
    (println "*                                                        *")
    (println "**********************************************************")
    (flush)
    (driver-loop [{}]))
   ([amb]
      ; Imprimo el caracter de entrada y paso a "esperar el comando del usuario".
      (print "> ") (flush)
      (try (let [linea (string-a-tokens (read-line)), cabeza (first linea)]
             ; Instruccion para salir del sistema.   
             (cond  (or (= cabeza 'SALIR) (= cabeza 'EXIT))
                    (println "ADIOS!!!")
                    (empty? linea) (driver-loop amb)
                    :else (driver-loop (second (evaluar-linea linea amb)))))
           (catch Exception e (println (str "?ERROR " (get (Throwable->map e) :cause))) (driver-loop amb))))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; string-a-tokens: parsea la instruccion cargada en el REPL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn string-a-tokens [s]
  (let [mayu (clojure.string/upper-case s)]        
          (->> mayu
                    (re-seq #"EXIT|SALIR|CLEAR|LIMPIAR|LOAD|CARGAR|COUNT|CONTAR|\"[^\"]*\"|\d+")                    
                    (map #(symbol %)))
  )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; evaluar-linea: recibe una lista de sentencias y las evalua
; mientras sea posible hacerlo
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn evaluar-linea
  ([sentencia amb]
   (println sentencia)
   (println amb)
   
   (case (first sentencia)
     (CLEAR, LIMPIAR) (println "1")
     (LOAD, CARGAR) (cargar-archivo (apply str (next sentencia)))
     (COUNT, CONTAR) (println "3")
    )
 )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn get-palabras [linea]
  (println linea)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn cargar-archivo [nombre]
  (println "archivo: " nombre)
  (if (.exists (clojure.java.io/file nombre))
    (remove empty? (with-open [rdr (clojure.java.io/reader nombre)]
                     (doall (map get-palabras (line-seq rdr)))
                     )
            )
    (println "No se encontro el archivo.")
  ) ; File not found
)
