package com.libreria.egg_libreria.libreria.controladores;

import com.libreria.egg_libreria.libreria.entidades.Editorial;
import com.libreria.egg_libreria.libreria.errores.ErrorServicio;
import com.libreria.egg_libreria.libreria.servicios.EditorialServicio;
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
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Controller
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/formularioEditorial")
    public String formulario() {
        return "formularioEditorial";
    }

    @PostMapping("/registrarEditorial")
    public String registrarEditorial(ModelMap modelo, @RequestParam(required = false) String nombre) {
        System.out.println("nombre: " + nombre);
        try {
            editorialServicio.crearEditorial(nombre);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "formularioEditorial";
        }

        modelo.put("titulo", "Libreria Cielo ");
        modelo.put("mensaje", "Editorial cargado satisfactoriamente");
        return "exito";
    }

    @GetMapping("listadoEditorial")
    public String listadodeEditorial(ModelMap modelo) {
        modelo.addAttribute("lista", editorialServicio.buscarTodos());
        modelo.addAttribute("nombreReferencia", "Listado de Editoriales");
        return "listadoEditoriales";
    }

    @GetMapping("/editarEditorial/{id}")
    public String editarEditorial(@PathVariable("id") String id, ModelMap modelo) {

        Editorial editorial = null;
        try {
            editorial = editorialServicio.buscarPorId(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(EditorialController.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.put("tipo", editorial);
        modelo.put("nombre", editorial.getNombre());
        return "formularioEditorial";
    }

    @PostMapping("/edicion-editorial")
    public String editamosEditorial(RedirectAttributes redirectAttributes, ModelMap modelo, String id, @RequestParam(required = false) String nombre) throws ErrorServicio {
        Editorial editorial = editorial = editorialServicio.buscarPorId(id);
        try {

            modelo.put("nombre", editorial.getNombre());
            editorialServicio.modificarEditorial(id, nombre);
        } catch (ErrorServicio ex) {

            modelo.put("tipo", editorial);
            modelo.put("error", ex.getMessage());
            redirectAttributes.addAttribute("id", id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            Logger.getLogger(EditorialController.class.getName()).log(Level.SEVERE, null, ex);
            return "redirect:/editorial/{id}";
        }

        modelo.put("mensaje", "Editorial modificada con éxito");
        return "index";

    }

    @GetMapping("/baja/{id}")
    public String bajaEditorial(@PathVariable("id") String id, ModelMap modelo) throws ErrorServicio {
        try {
            editorialServicio.deshabilitarEditorial(id);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "listadoEditoriales";
        }
        return "redirect:/editorial/listadoEditorial";
    }

    @GetMapping("/alta/{id}")
    public String altaEditorial(@PathVariable("id") String id, ModelMap modelo) throws ErrorServicio {
        try {
            editorialServicio.habilitarHabilitar(id);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "listadoEditoriales";
        }
        return "redirect:/editorial/listadoEditorial";
    }

    @GetMapping("/borrar")
    public String borrar(ModelMap modelo, @RequestParam String id) {
        try {
            editorialServicio.eliminar(id);//.deshabilitarAutor(id);
        } catch (ErrorServicio ex) {
//            modelo.put("error", ex.getMessage());
            modelo.addAttribute("error", ex.getMessage());
            return "redirect:/editorial/listadoEditorial";
        }
        return "redirect:/editorial/listadoEditorial";
    }
}
