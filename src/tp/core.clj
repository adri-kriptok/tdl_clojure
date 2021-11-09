(ns tp.core
  (:gen-class))

(declare driver-loop)
(declare string-a-tokens)
(declare evaluar-linea)

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
             (cond  (or (= cabeza 'SALIR) (= cabeza 'EXIT)) 'GOODBYE
                    (empty? linea) (driver-loop amb)
                    :else (driver-loop (second (evaluar-linea linea (assoc amb 1 [:ejecucion-inmediata (count linea)]))))))
           (catch Exception e (println (str "?ERROR " (get (Throwable->map e) :cause))) (driver-loop amb))))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; string-a-tokens: parsea la instruccion cargada en el REPL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn string-a-tokens [s]
  (let [mayu (clojure.string/upper-case s)]        
          (->> mayu
                    (re-seq #"EXIT|SALIR|CLEAR|LIMPIAR|LOAD|CARGAR|COUNT|CONTAR|\"[^\"]*\"|\d+")                        
                    (flatten)
                    (map #(symbol %)))
  )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; evaluar-linea: recibe una lista de sentencias y las evalua
; mientras sea posible hacerlo
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn evaluar-linea
  ([sentencias amb]
   
   (println sentencias)
   (println amb)
  )
 )

