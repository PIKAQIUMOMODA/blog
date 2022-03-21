package com.zcy.blog.dao;

import com.zcy.blog.pojo.Blog;
import com.zcy.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long>, JpaSpecificationExecutor<Type> {
    List<Type> findTypesByName(String name);

    @Query("SELECT t from Type t")
    List<Type> findTop(Pageable pageable);
}
