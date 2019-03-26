package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping("/")
    public String indexPage() {

        return "redirect:" + "/product";
    }

    @RequestMapping("/product")
    public String product(Model model) {

        model.addAttribute("h2Data", productRepository.findAll());
        return "h2List";
    }

    //CRUD operations
    @RequestMapping("api/products")
    @ResponseBody
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @RequestMapping("api/product/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable(value = "id") Long id) {
        Optional<Product> prod = productRepository.findById(id);
        if (prod == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(prod);
    }

    @PostMapping("api/product")
    @ResponseBody
    public ResponseEntity<String> saveProducts(@RequestBody Product product) {
        productRepository.save(product);
        return ResponseEntity.ok().body("Resource successFully created!!!");
    }

    @DeleteMapping("api/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable(value = "id") long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().body("Deleted successFully");
    }
}
