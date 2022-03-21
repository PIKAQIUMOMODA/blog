package com.zcy.blog.service;

import com.zcy.blog.pojo.Blog;
import com.zcy.blog.pojo.Type;
import com.zcy.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    Type updateType(Long id,Type type);

    void deleteType(Long id);

    Page<Type> getTypeByName(Pageable pageable,String name);

    List<Type> listTypes();

    Page<Type> listTypes(Pageable pageable, Type type);

    List<Type> listTypesTop(Integer size);

}
