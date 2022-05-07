package com.libreria.egg_libreria.libreria.servicios;

import com.libreria.egg_libreria.libreria.entidades.Editorial;
import com.libreria.egg_libreria.libreria.errores.ErrorServicio;
import com.libreria.egg_libreria.libreria.repositorios.EditorialRepositorio;
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
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional//(propagation = Propagation.NESTED)
    public void crearEditorial(String nombre) throws ErrorServicio {

        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(Boolean.TRUE);

        editorialRepositorio.save(editorial);

    }

    @Transactional//(propagation = Propagation.NESTED)
    public void modificarEditorial(String id, String nombre) throws ErrorServicio {
        validar(nombre);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            //  Editorial editorial = editorialRepositorio.findById(id).get();
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró la editorial pedida.");
        }

    }

    @Transactional
    public void deshabilitarEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.FALSE);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial solicitada");
        }
    }

    @Transactional
    public void habilitarHabilitar(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.TRUE);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial solicitada");
        }
    }

    public void eliminar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorialRepositorio.delete(editorial);
        } else {
            throw new ErrorServicio("No se encontró una editorial con ese id.");
        }
    }

    private void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del Autor no puede ser nulo. ");
        }

    }

    @Transactional(readOnly = true)
    public List<Editorial> buscarTodos() {
        return editorialRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Editorial> listadoEditorial() {
        return (List<Editorial>) editorialRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Editorial buscarPorNombre(String nombre) {
        return editorialRepositorio.buscarEditorialPorNombre(nombre);
    }

//    @Transactional(readOnly = true)
//    public Optional<Editorial> buscarPorId(String id) {
//        return editorialRepositorio.findById(id);
//    }
    @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("La editorial solicitada no existe.");
        }

    }

}
