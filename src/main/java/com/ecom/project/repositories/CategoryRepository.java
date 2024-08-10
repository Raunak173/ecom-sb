package com.ecom.project.repositories;

import com.ecom.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

//Create a Interface repository that extends JpaRepository which will have type of
//entity and the type of the Primary key

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByCategoryName(String categoryName);
    //Magic of JPA

}
