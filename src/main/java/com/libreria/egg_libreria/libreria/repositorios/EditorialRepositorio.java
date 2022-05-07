package com.libreria.egg_libreria.libreria.repositorios;

import com.libreria.egg_libreria.libreria.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {

    @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public Editorial buscarEditorialPorNombre(@Param("nombre") String nombre);

//    @Modifying
//    @Query("UPDATE Editorial e SET e.alta = true WHERE e.id = :id")
//    void habilitar(@Param("id") Integer id);
//
//    boolean existsByNombre(String nombre);
}
