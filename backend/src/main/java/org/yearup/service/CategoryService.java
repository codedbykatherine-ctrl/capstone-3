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
        // update category and return the updated category
        //retrieve the existing category from teh database using its id
        Category existing = categoryRepository.findById(id).orElseThrow();
        existing.setDescription(category.getDescription());
        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    public void delete(int categoryId)
    {
        // delete category
        categoryRepository.deleteById(categoryId);
    }
}
