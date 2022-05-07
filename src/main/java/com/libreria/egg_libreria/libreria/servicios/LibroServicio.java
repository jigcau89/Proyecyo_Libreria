package com.libreria.egg_libreria.libreria.servicios;

import com.libreria.egg_libreria.libreria.entidades.Autor;
import com.libreria.egg_libreria.libreria.entidades.Editorial;
import com.libreria.egg_libreria.libreria.entidades.Libro;
import com.libreria.egg_libreria.libreria.errores.ErrorServicio;
import com.libreria.egg_libreria.libreria.repositorios.AutorRepositorio;
import com.libreria.egg_libreria.libreria.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional(propagation = Propagation.NESTED)
    public void crearLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Autor autor, Editorial editorial) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, autor, editorial);
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(0);
       // libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setAlta(Boolean.TRUE);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepositorio.save(libro);

    }

    @Transactional(propagation = Propagation.NESTED)
    public void modificar(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Autor autor, Editorial editorial, Integer ejemplaresPrestados) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, autor, editorial);

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(0);
            libro.setEjemplaresRestantes(ejemplares);
            //libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro solicitado. ");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void deshabilitarLibro(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.FALSE);

            libroRepositorio.save(libro);

        } else {
            throw new ErrorServicio("No se encontro el libro solicitado. ");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void habilitarLibro(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.TRUE);

            libroRepositorio.save(libro);

        } else {
            throw new ErrorServicio("No se encontro el libro solicitado. ");
        }
    }

    public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Autor autor, Editorial editorial) throws ErrorServicio {
        if (isbn == null) {
            throw new ErrorServicio("El numero de isbn no es válido");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ErrorServicio("El titulo no puede esta vacio");
        }
        if (anio == null || anio.toString().length() > 4) {
            throw new ErrorServicio("El año no puede sobrepasar los 4 digitos");
        }

        if (ejemplares == null || ejemplares <= 0) {
            throw new ErrorServicio("La cantidad de ejemplares no pueden ser menor a cero");
        }
        if (autor == null) {
            throw new ErrorServicio("no autor no debe estar vacio");
        }
        if (editorial == null) {
            throw new ErrorServicio("la editorial no debe estar vacio");
        }

    }

//    public void consulta() {
//        List<Libro> libro = new ArrayList<>();
//        libro.forEach(System.out::println);
//
//        //List<Libro> libro = new ArrayList<Libro>();
//        //Iterator<Libro> iter = libro.iterator();
//        //while(iter.hasNext())
//        //System.out.println(iter.next().toString());
//        //Transactional(readOnly = true)
//    }
    @Transactional(readOnly = true)
    public List<Libro> mostrarTodosLosLibros() {
        return (List<Libro>) libroRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Libro> buscarLibrosxId(String id) {
        return libroRepositorio.findById(id);
    }

    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {
        return libroRepositorio.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public Libro buscarLibrosPorTitulo(String titulo) {
        return libroRepositorio.buscarPorTitulo(titulo);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void borrarPorId(String id) throws ErrorServicio { //Integer id
        // libroRepositorio.findById(id);
        Optional<Libro> optional = libroRepositorio.findById(id);
        if (optional.isPresent()) {
            libroRepositorio.delete(optional.get());
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void borrarPorId2(String id) {

        libroRepositorio.delete(libroRepositorio.buscarPorId(id));
    }

    
}
