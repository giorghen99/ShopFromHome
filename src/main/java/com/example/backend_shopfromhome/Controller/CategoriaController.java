package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Categoria;
import com.example.backend_shopfromhome.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Categoria createCategory(@RequestBody Categoria categoria) {
        return categoriaService.createCategory(categoria);
    }

    @PutMapping("/{id}")
    public Categoria updateCategory(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.updateCategory(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoriaService.deleteCategory(id);
    }
}
