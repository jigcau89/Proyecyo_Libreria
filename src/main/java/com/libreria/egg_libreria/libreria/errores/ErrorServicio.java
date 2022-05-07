
package com.libreria.egg_libreria.libreria.errores;


public class ErrorServicio extends Exception{
    //constructor
    public ErrorServicio(String mensaje){
        super(mensaje);
    }
    
    
    //se crea la clase ErrorServicio para diferenciar
    //los errores de nuestra lógica de negocios
    //de los que ocurren en el sistema
}
