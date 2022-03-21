package com.zcy.blog.service;

import com.zcy.blog.pojo.Blog;
import com.zcy.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    boolean updateRecommend(Blog blog);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Page<Blog> listBlog(String query,Pageable pageable);

    int updateViews(Long blogid);

    int updateGivelike(Long blogid);

    int updateDislike(Long blogid);

    Map<String,Map<String,List<Blog>>> archiveBlog();

     List<String> findGroupYear();

     List<String> findGroupYearMonth(String year);

    List<Blog> findByMonth(String month);



}
