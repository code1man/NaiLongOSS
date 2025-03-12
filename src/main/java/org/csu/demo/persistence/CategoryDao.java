package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.csu.demo.domain.Category;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Mapper
public interface CategoryDao {
    List<Category> getCategoryList();

    Category getCategoryById(int id);
}
