;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Adri?n Cavaiuolo - 91186 (2do Cuatrimestre 2020)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(require '[clojure.test :refer [is deftest run-tests]])

(load-file "basic.clj")

(deftest test-palabra-reservada?
  (is (= true (palabra-reservada? 'REM)))
  (is (= false (palabra-reservada? 'SPACE)))
)

(deftest test-operador?
  (is (= true (operador? '+)))
  (is (= true (operador? (symbol "+"))))
  (is (= false (operador? (symbol "%"))))
  (is (= true (operador? (symbol "+"))))
  (is (= true (operador? (symbol "-"))))
  (is (= true (operador? (symbol "*"))))
  (is (= true (operador? (symbol "/"))))
  (is (= true (operador? (symbol "^"))))
  (is (= true (operador? '-u)))
  (is (= true (operador? (symbol"="))))
  (is (= true (operador? '<)))
  (is (= true (operador? '>)))
  (is (= true (operador? '<>)))
  (is (= true (operador? '<=)))
  (is (= true (operador? '>=)))
  (is (= true (operador? 'AND)))
  (is (= true (operador? 'OR)))
)

(deftest test-anular-invalidos
  (is (= 
    '(IF X nil * Y < 12 THEN LET nil X = 0) 
    (anular-invalidos '(IF X & * Y < 12 THEN LET ! X = 0)) 
  ))
)

(deftest test-cargar-linea
  (is (= 
    ['((10 (PRINT X))) [:ejecucion-inmediata 0] [] [] [] 0 {}]
    (cargar-linea '(10 (PRINT X)) [() [:ejecucion-inmediata 0] [] [] [] 0 {}]) 
  ))
  (is (= 
    ['((10 (PRINT X)) (20 (X = 100))) [:ejecucion-inmediata 0] [] [] [] 0 {}]
    (cargar-linea '(20 (X = 100)) ['((10 (PRINT X))) [:ejecucion-inmediata 0] [] [] [] 0 {}])
  ))
  (is (= 
    ['((10 (PRINT X)) (15 (X = X + 1)) (20 (X = 100))) [:ejecucion-inmediata 0] [] [] [] 0 {}]
    (cargar-linea '(15 (X = X + 1)) ['((10 (PRINT X)) (20 (X = 100))) [:ejecucion-inmediata 0] [] [] [] 0 {}])
  ))
  (is (= 
    ['((10 (PRINT X)) (15 (X = X - 1)) (20 (X = 100))) [:ejecucion-inmediata 0] [] [] [] 0 {}]
    (cargar-linea '(15 (X = X - 1)) ['((10 (PRINT X)) (15 (X = X + 1)) (20 (X = 100))) [:ejecucion-inmediata 0] [] [] [] 0 {}])
  ))
)

(def n (list '(PRINT 1) (list 'NEXT 'A (symbol ",") 'B)))

(deftest test-expandir-nexts
  (is (=
    (list '(PRINT 1) (list 'NEXT 'A (symbol ",") 'B))
    n
  ))
  (is (= 
    '((PRINT 1) (NEXT A) (NEXT B))
    (expandir-nexts n)
  ))
)

(deftest test-dar-error
  (is (= nil (dar-error 16 [:ejecucion-inmediata 4])))
  (is (= nil (dar-error "?ERROR DISK FULL" [:ejecucion-inmediata 4])))
  (is (= nil (dar-error 16 [100 3])))
  (is (= nil (dar-error "?ERROR DISK FULL" [100 3])))
)

(deftest test-variable-float
  (is (= (variable-float? 'X)  true))
  (is (= (variable-float? 'X%) false))
  (is (= (variable-float? 'X$) false))
)

(deftest test-variable-integer
  (is (= (variable-integer? 'X%)  true))
  (is (= (variable-integer? 'X) false))
  (is (= (variable-integer? 'X$) false))
)

(deftest test-variable-string
  (is (= (variable-string? 'X$) true))  
  (is (= (variable-string? 'X%) false))
  (is (= (variable-string? 'X) false))  
)

(deftest test-contar-sentencias
  (is (= 2 (contar-sentencias 10 
    [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [10 1] [] [] [] 0 {}])))
  (is (= 1 (contar-sentencias 15 
    [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [10 1] [] [] [] 0 {}])))
  (is (= 2 (contar-sentencias 20 
    [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [10 1] [] [] [] 0 {}])))
)

(deftest test-buscar-lineas-restantes
  (is (= nil (buscar-lineas-restantes [() [:ejecucion-inmediata 0] [] [] [] 0 {}])))
  (is (= nil (buscar-lineas-restantes ['((PRINT X) (PRINT Y)) [:ejecucion-inmediata 2] [] [] [] 0 {}])))
  (is (= (list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [10 2] [] [] [] 0 {}]))))
  (is (= (list '(10 (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J)))
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [10 1] [] [] [] 0 {}])))
   (is (= (list '(10) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J)))
     (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [10 0] [] [] [] 0 {}])))
  (is (=  (list '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J)))         
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [15 1] [] [] [] 0 {}])))
  (is (= (list '(15) (list 20 (list 'NEXT 'I (symbol ",") 'J)))
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [15 0] [] [] [] 0 {}])))
  (is (= '((20 (NEXT I) (NEXT J)))
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 3] [] [] [] 0 {}])))
  (is (= '((20 (NEXT I) 
    (NEXT J)))(buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 2] [] [] [] 0 {}])))
  (is (= '((20 (NEXT J)))
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 1] [] [] [] 0 {}])))
  (is (= '((20))
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 0] [] [] [] 0 {}])))
  (is (= '((20))
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 -1] [] [] [] 0 {}])))
  (is (= nil 
    (buscar-lineas-restantes [(list '(10 (PRINT X) (PRINT Y)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [25 0] [] [] [] 0 {}])))
)


(deftest test-continuar-linea
  (is (= [nil [(list '(10 (PRINT X)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 3] [] [] [] 0 {}]]  
    (continuar-linea [(list '(10 (PRINT X)) '(15 (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 3] [] [] [] 0 {}])
  ))
  (is (= [:omitir-restante [(list '(10 (PRINT X)) '(15 (GOSUB 100) (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [15 1] [] [] [] 0 {}]]
    (continuar-linea [(list '(10 (PRINT X)) '(15 (GOSUB 100) (X = X + 1)) (list 20 (list 'NEXT 'I (symbol ",") 'J))) [20 3] [[15 2]] [] [] 0 {}])
  ))
)

(deftest test-extraer-data
  (is (= '()  
    (extraer-data '(()))
  ))
  (is (= '("HOLA" "MUNDO" 10 20)   
    (extraer-data (list '(10 (PRINT X) (REM ESTE NO) (DATA 30)) '(20 (DATA HOLA)) (list 100 (list 'DATA 'MUNDO (symbol ",") 10 (symbol ",") 20))))
  ))
)

(deftest test-ejecutar-asignacion
  (is (= '[((10 (PRINT X))) [10 1] [] [] [] 0 {X 5}]
    (ejecutar-asignacion '(X = 5) ['((10 (PRINT X))) [10 1] [] [] [] 0 {}])
  ))
  (is (= '[((10 (PRINT X))) [10 1] [] [] [] 0 {X 5}]
    (ejecutar-asignacion '(X = 5) ['((10 (PRINT X))) [10 1] [] [] [] 0 '{X 2}])
  ))
  (is (= '[((10 (PRINT X))) [10 1] [] [] [] 0 {X 3}]
    (ejecutar-asignacion '(X = X + 1) ['((10 (PRINT X))) [10 1] [] [] [] 0 '{X 2}])
  ))
  (is (= '[((10 (PRINT X))) [10 1] [] [] [] 0 {X$ "HOLA MUNDO"}]
    (ejecutar-asignacion '(X$ = X$ + " MUNDO") ['((10 (PRINT X))) [10 1] [] [] [] 0 '{X$ "HOLA"}])
  ))  
)

(deftest test-preprocesar-expresion
  (is (= '("HOLA" + " MUNDO" + "")
    (preprocesar-expresion '(X$ + " MUNDO" + Z$) ['((10 (PRINT X))) [10 1] [] [] [] 0 '{X$ "HOLA"}])
  ))
  (is (= '(5 + 0 / 2 * 0)
    (preprocesar-expresion '(X + . / Y% * Z) ['((10 (PRINT X))) [10 1] [] [] [] 0 '{X 5 Y% 2}])
  ))
)

(deftest test-desambiguar
  (is (= (list '-u 2 '* (symbol "(") '-u 3 '+ 5 '- (symbol "(") '2 '/ 7 (symbol ")") (symbol ")"))
    (desambiguar (list '- 2 '* (symbol "(") '- 3 '+ 5 '- (symbol "(") '+ 2 '/ 7 (symbol ")") (symbol ")")))
  ))
  (is (= (list 'MID$ (symbol "(") 1 (symbol ",") 2 (symbol ")"))
    (desambiguar (list 'MID$ (symbol "(") 1 (symbol ",") 2 (symbol ")")))
  ))
  (is (= (list 'MID3$ (symbol "(") 1 (symbol ",") (symbol "(") 2 (symbol ")") (symbol ",") 3 (symbol ")"))
    (desambiguar (list 'MID$ (symbol "(") 1 (symbol ",") 2 (symbol ",") 3 (symbol ")")))
  ))
  (is (= (list 'MID3$ (symbol "(") 1 (symbol ",") (symbol "(") '-u 2 '+ 'K (symbol ")") (symbol ",") 3 (symbol ")"))
    (desambiguar (list 'MID$ (symbol "(") 1 (symbol ",") '- 2 '+ 'K (symbol ",") 3 (symbol ")")))
  ))
)

(deftest test-precedencia
  (is (= 1 (precedencia 'OR)))
  (is (= 2 (precedencia 'AND)))
  (is (= 6 (precedencia '*)))
  (is (= 7 (precedencia '-u)))
  (is (= 9 (precedencia 'MID$)))
)

(deftest test-aridad
  (is (= 0 (aridad 'THEN)))
  (is (= 1 (aridad 'SIN)))
  (is (= 2 (aridad '*)))
  (is (= 2 (aridad 'MID$)))
  (is (= 3 (aridad 'MID3$)))
)

(deftest test-eliminar-cero-decimal
  (is (= 1.5 (eliminar-cero-decimal 1.5)))
  (is (= 1.5 (eliminar-cero-decimal 1.50)))
  (is (= 1 (eliminar-cero-decimal 1.0)))
  (is (= 'A (eliminar-cero-decimal 'A)))
)

(deftest test-eliminar-cero-entero
  (is (= nil (eliminar-cero-entero nil)))
  (is (= "A" (eliminar-cero-entero 'A)))
  (is (= "0" (eliminar-cero-entero 0)))
  (is (= "1.5" (eliminar-cero-entero 1.5)))
  (is (= "1" (eliminar-cero-entero 1)))
  (is (= "-1" (eliminar-cero-entero -1)))
  (is (= "-1.5" (eliminar-cero-entero -1.5)))
  (is (= ".5" (eliminar-cero-entero 0.5)))
  (is (= "-.5" (eliminar-cero-entero -0.5)))
)

(run-tests)
 
 
