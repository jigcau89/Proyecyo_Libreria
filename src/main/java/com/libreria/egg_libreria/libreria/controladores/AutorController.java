package com.libreria.egg_libreria.libreria.controladores;

import com.libreria.egg_libreria.libreria.entidades.Autor;
import com.libreria.egg_libreria.libreria.errores.ErrorServicio;
import com.libreria.egg_libreria.libreria.servicios.AutorServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 *
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Controller
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/formularioAutor")
    public String formulario() {
        return "formularioAutor";
    }

    @PostMapping("/registrarAutor")
    public String registrarAutor(ModelMap modelo, @RequestParam(required = false) String nombre) {
        System.out.println("nombre: " + nombre);
        try {
            autorServicio.crearAutor(nombre);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "formularioAutor";
        }
        modelo.put("titulo", "Libreria Cielo ");
        modelo.put("mensaje", "Autor cargado satisfactoriamente");
        return "exito";
    }

    @GetMapping("listadoAutor")
    public String listadodeAutores(ModelMap modelo) {
        modelo.addAttribute("lista", autorServicio.buscarTodos());
        modelo.addAttribute("nombreReferencia", "Listado de Editoriales");
        return "listadoAutores";
    }

    @GetMapping("/editarAutor/{id}")
    public String editarAutor(RedirectAttributes redirectAttributes, @PathVariable("id") String id, ModelMap modelo) throws ErrorServicio {
        Autor autor = autorServicio.buscarPorId(id).get();
        modelo.put("tipo", autor);
        modelo.put("nombre", autor.getNombre());
        return "formularioAutor";
    }

    @PostMapping("/edicion-autor")
    public String editamosAutor(RedirectAttributes redirectAttributes, ModelMap modelo, String id, @RequestParam String nombre) throws ErrorServicio {
        Autor autor = autorServicio.buscarPorId(id).get();
        try {
            modelo.put("nombre", autor.getNombre());
            autorServicio.modificarAutor(id, nombre);
            modelo.put("mensaje", "Autor modificado correctamente.");
        return "index";
        } catch (ErrorServicio ex) {
            modelo.put("tipo", autor);
            modelo.put("error", ex.getMessage());
            redirectAttributes.addAttribute("id", id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/autor/{id}";
        }
        
    }

    @GetMapping("/baja/{id}")
    public String bajaAutor(@PathVariable("id") String id, ModelMap modelo) throws ErrorServicio {
        try {
            autorServicio.deshabilitarAutor(id);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "listadoAutores";
        }
        return "redirect:/autor/listadoAutor";
    }

    @GetMapping("/alta/{id}")
    public String altaAutor(@PathVariable("id") String id, ModelMap modelo) throws ErrorServicio {
        try {
            autorServicio.habilitarAutor(id);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "listadoAutores";
        }
        return "redirect:/autor/listadoAutor";
    }

    @GetMapping("/borrar")
    public String borrar(ModelMap modelo, @RequestParam String id) {
        try {
            autorServicio.eliminar(id);//.deshabilitarAutor(id);
            modelo.addAttribute("mensaje", "Autor eliminado correctamente.");
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage()); //            modelo.put("error", ex.getMessage());
            return "redirect:/autor/listadoAutor";
        }
        return "redirect:/autor/listadoAutor";
    }
}
