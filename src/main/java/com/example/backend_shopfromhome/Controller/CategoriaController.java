package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Categoria;
import com.example.backend_shopfromhome.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/categorie")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> getAllCategories() {
        return categoriaService.getAllCategories();
    }

    @PreAuthorize("hasRole('MAGAZZINIERE')")
    @PostMapping
    public Categoria createCategory(@RequestBody Categoria categoria) {
        return categoriaService.createCategory(categoria);
    }

    @PreAuthorize("hasRole('MAGAZZINIERE')")
    @PutMapping("/{id}")
    public Categoria updateCategory(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.updateCategory(id, categoria);
    }

    @PreAuthorize("hasRole('MAGAZZINIERE')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoriaService.deleteCategory(id);
    }
}
