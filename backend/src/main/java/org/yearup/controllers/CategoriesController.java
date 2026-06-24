package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
// add annotation to allow cross site origin requests

@RestController
//handles API/web requests and returns data like Json
@CrossOrigin
//wedsite uses to connect frontend
@RequestMapping("/categories")
//any requests with /categories belongs to this controller
public class CategoriesController {
    private final CategoryService categoryService;
    private final ProductService productService;


    @Autowired
    public CategoriesController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
        //spring when you create CategoriesController, please hand me CategoryService and ProductService
        // then I will save them inside my controller
    }


    // create an Autowired constructor to inject the categoryService and productService

    // add the appropriate annotation for a get action
    @GetMapping
    public List<Category> getAll() {
        // find and return all categories
        return categoryService.getAllCategories();
        //return list as Json
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category getById(@PathVariable int id) {
        // get the category by id
        return categoryService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        // get a list of product by categoryId

        return productService.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        // insert the category and return it with status 201 Created
        Category saved = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(category));
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category) {
        // update the category by id and return the updated category (200 OK)
        // check if the category exists before updating it
        if (categoryService.getById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            //Return 404 if the category id can not be found
        }
        //update category and return the updated object
        return categoryService.update(id, category);
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        // delete the category by id and return status 204 No Content
        //Verify if the category exists before attempting to delete it
        if(categoryService.getById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            //return 404 Not Found if the category id does not exist
        }
        categoryService.delete(id);
        // return 204 No Content to indicate the deletion was successful 
        return ResponseEntity.noContent().build();
    }
}
