package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
        // get all categories
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(int categoryId)
    {
        // get category by id
        //retrieve the category with the specified id from the database
        return categoryRepository.findById(categoryId);
    }

    public Category create(Category category)
    {
        // create a new category
        //reset the category id so the database generates a new primary key
       category.setCategoryId(0);
       //Save the new category and return the created category object
       return categoryRepository.save(category);
    }

    public Category update(int id, Category category)
    {
        //retrieve the existing category from the database using its id
        //This prevents overwriting data or accidents
        Category existing = categoryRepository.findById(id).orElseThrow();
        //Update the category description with the new value from the request
        existing.setDescription(category.getDescription());
        //Update the category name with the new value from the request
        existing.setName(category.getName());
        //Save the updated category and return the modified object
        return categoryRepository.save(existing);
        /* this prevents overwriting fields,
         * lose data, insert new row if the id isn't set correctly
         * updating the existing entity is safer
         */

    }

    public void delete(int categoryId)
    {
        //Delete a category from the database
        //using the category's ID
        categoryRepository.deleteById(categoryId);
    }
}
