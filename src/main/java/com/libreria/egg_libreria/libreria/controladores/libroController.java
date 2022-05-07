package com.libreria.egg_libreria.libreria.controladores;

import com.libreria.egg_libreria.libreria.entidades.Autor;
import com.libreria.egg_libreria.libreria.entidades.Editorial;
import com.libreria.egg_libreria.libreria.entidades.Libro;
import com.libreria.egg_libreria.libreria.errores.ErrorServicio;
import com.libreria.egg_libreria.libreria.servicios.AutorServicio;
import com.libreria.egg_libreria.libreria.servicios.EditorialServicio;
import com.libreria.egg_libreria.libreria.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Controller
@RequestMapping("/libro")
public class libroController {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/formularioLibro")
    public String formulario(ModelMap vista) {
        vista.put("tipo", null);
        List<Autor> listadoAutores = autorServicio.buscarTodos();
        List<Editorial> listadoEditoriales = editorialServicio.buscarTodos();
        vista.addAttribute("autores", listadoAutores);
        vista.addAttribute("editoriales", listadoEditoriales);
        return "formularioLibro";
    }


    @PostMapping("/registrarLibro")
    public String registrarLibro(ModelMap modelo, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam String autor, @RequestParam String editorial) throws ErrorServicio {
        Autor objetoAutor = autorServicio.buscarPorNombre(autor);
        Editorial objetoEditorial = editorialServicio.buscarPorNombre(editorial);
        try {
            libroServicio.crearLibro(isbn, titulo, anio, ejemplares, objetoAutor, objetoEditorial);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("anio", anio);
            modelo.addAttribute("autores", autorServicio.buscarTodos());
            modelo.addAttribute("eitoriales", editorialServicio.buscarTodos());
            return "formularioLibro";
        }
        modelo.put("mensaje", "Libro cargado satisfactoriamente.");
        return "index";
    }
    
     @GetMapping("listadoLibros")
    public String listadoLibros(ModelMap modelo) {
        modelo.addAttribute("lista", libroServicio.mostrarTodosLosLibros());
        
        return "listadoLibros";
    }
    
      @GetMapping("/editarLibro/{id}")
    public String editarLibro(@PathVariable("id") String id,RedirectAttributes redirectAttributes, ModelMap modelo) {
        Libro libro = libroServicio.buscarLibrosxId(id).get();
      //  Libro libro = libroServicio.buscarPorId(id);
        
        modelo.put("tipo", libro);
//        modelo.put("isbn", libro.getIsbn());
//        modelo.put("titulo", libro.getTitulo());
//        modelo.put("anio", libro.getAnio());
        modelo.put("autores", autorServicio.buscarTodos());
        modelo.put("editoriales", editorialServicio.buscarTodos());
     
        return "formularioLibro";
    }
    
     @PostMapping("/edicion-libro")
    public String editamosLibro(RedirectAttributes redirectAttributes, @RequestParam String id, ModelMap modelo, @RequestParam(required = false) Long isbn, @RequestParam(required = false) String titulo, @RequestParam(required = false) Integer anio, @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) Autor autor, @RequestParam(required = false) Editorial editorial) {
        Libro libro = libroServicio.buscarPorId(id);
//        Autor objetoAutor = autorServicio.buscarPorNombre(autor);
//        Editorial objetoEditorial = editorialServicio.buscarPorNombre(editorial);
        try {
            libroServicio.modificar(id, isbn, titulo, anio, ejemplares, autor, editorial, ejemplares);
        } catch (ErrorServicio ex) {
            ex.printStackTrace();
            modelo.put("tipo", libro);
            redirectAttributes.addAttribute("id", id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/libro/{id}";
        }
        modelo.put("mensaje", "Libro modificado con éxito");
        return "index";
    }
    
     @GetMapping("/baja/{id}")
    public String bajaLibro(@PathVariable("id") String id, ModelMap modelo) throws ErrorServicio {
        try {
            libroServicio.deshabilitarLibro(id);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "listadoLibros";
        }
        return "redirect:/libro/listadoLibros";
    }
    
        @GetMapping("/alta/{id}")
    public String altaLibro(@PathVariable("id") String id, ModelMap modelo) throws ErrorServicio {
        try {
            libroServicio.habilitarLibro(id);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "listadoLibros";
        }

        return "redirect:/libro/listadoLibros";
    }
    
    @GetMapping("/borrar")
    public String borrar(ModelMap modelo, @RequestParam String id) {
        try {
            libroServicio.borrarPorId(id);
            modelo.addAttribute("mensaje", "Libro eliminado correctamente.");
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage()); //            modelo.put("error", ex.getMessage());
            return "redirect:/libro/listadoLibros";
        }
        return "redirect:/libro/listadoLibros";
    }
}
