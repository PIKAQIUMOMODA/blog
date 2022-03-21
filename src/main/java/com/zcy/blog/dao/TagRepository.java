package com.zcy.blog.dao;

import com.zcy.blog.pojo.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long>, JpaSpecificationExecutor<Tag> {

   Tag findByName(String name);

   List<Tag> findTagsByName(String name);

   @Query("SELECT t from Tag t")
   List<Tag> findTop(Pageable pageable);
}
