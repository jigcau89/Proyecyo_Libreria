package com.libreria.egg_libreria.libreria.repositorios;

import com.libreria.egg_libreria.libreria.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    public Autor buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.id = :id")
    public Autor burcarPorId(@Param("id") String id);

}

//  @Modifying
//@Query("UPDATE Autor a SET a.alta = true WHERE a.id = :id")
//void habilitar(@Param("id") Integer id);
//boolean existsByNombreAndApellido(String nombre, String apellido);

