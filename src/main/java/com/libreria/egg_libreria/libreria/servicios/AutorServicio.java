package com.libreria.egg_libreria.libreria.servicios;

import com.libreria.egg_libreria.libreria.entidades.Autor;
import com.libreria.egg_libreria.libreria.errores.ErrorServicio;
import com.libreria.egg_libreria.libreria.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Service
public class AutorServicio {

    //creamos el atributo autorRepositorio
    //atributo Autowired:la variable la inicializa el servidor de aplicaciones
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional//(propagation = Propagation.NESTED)
    public void crearAutor(String nombre) throws ErrorServicio {

        validad(nombre);
        //si alguno de los datos que el usuario ingresó no es válido
        //se dispara el error de servicio y no se crea la entidad,
        //no se setean los atributos y no se persisten en la base de datos
        //hay que transformar los datos recibidos del formulario en una entidad de nuestro sistema
        Autor autor = new Autor();        //creamos la entidad Autor
        //seteamos los atributos de todos los datos que el usuario completa en el formulario
        autor.setNombre(nombre);
        autor.setAlta(Boolean.TRUE);

        //llamamos a la clase autorRepositorio
        //llamamos al metodo save
        //y le pasamos como parametro autor
        autorRepositorio.save(autor);

        //recibimos los datos
        //se transforman en entidad de tipo autor
        //el repositorio almacena el objeto
        //tipo autor en la base de datos
        //el repositorio es el encargado de transformar el objeto en una o más tablas de la base de datos
        //antes de persistir tenemos que validar que los atributos estén llegando sean válidos
    }

    @Transactional//(propagation = Propagation.NESTED)
    public void modificarAutor(String id, String nombre) throws ErrorServicio {

        validad(nombre);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            //Autor autor = autorRepositorio.findById(id).get(); //el metodo get nos devuelve el autor si lo encontro de findById
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            // autor.setAlta(alta);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado. ");
        }
        //mediante la clase Optional puedo ver si como respuesta al id me devuelve
        //un autor entonces lo busco y modifico, sino devuelve una excepción
        // como es un metodo de modificación a través del id, tenemos que buscar
        //cuál es el autor  vinculado a ese id, para modificar los datos a ese autor.

//el metodo FindById devuelve una clase Optional a esa clase le consultamos 
//si el id está presente
    }

    @Transactional
    public void deshabilitarAutor(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(Boolean.FALSE);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado. ");
        }
    }

    @Transactional
    public void habilitarAutor(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(Boolean.TRUE);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado. ");
        }
    }

    public void eliminar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autorRepositorio.delete(autor);
        } else {
            throw new ErrorServicio("No se encontró un autor con ese id.");
        }
    }

    public void validad(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo. ");
        }
        //usamos un metodo para no repetir la logica
    }

    @Transactional(readOnly = true)
    public List<Autor> buscarTodos() {
        return (List<Autor>) autorRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Autor buscarPorNombre(String nombre) {
        return autorRepositorio.buscarAutorPorNombre(nombre);
    }

//    @Transactional(readOnly = true)
//    public Autor buscarPorId(String id) throws ErrorServicio {
//        Optional<Autor> respuesta = autorRepositorio.findById(id);
//        if(respuesta.isPresent()){
//            return respuesta.get();
//        }else {
//            throw new ErrorServicio("El autor solicitado no existe.");
//        }
//        
//    }
    public Optional<Autor> buscarPorId(String id) {
        return autorRepositorio.findById(id);
    }

}
