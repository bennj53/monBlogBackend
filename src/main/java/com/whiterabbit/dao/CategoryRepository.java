package com.whiterabbit.dao;

import com.whiterabbit.entities.CategoryOld;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource
public interface CategoryRepository extends MongoRepository<CategoryOld, String> {
}
