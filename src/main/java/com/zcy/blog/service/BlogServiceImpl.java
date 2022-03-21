package com.zcy.blog.service;

import com.zcy.blog.dao.BlogRepository;
import com.zcy.blog.exception.NotFoundException;
import com.zcy.blog.pojo.Blog;
import com.zcy.blog.pojo.Type;
import com.zcy.blog.utils.MarkdownUtils;
import com.zcy.blog.utils.MyBeanUtils;
import com.zcy.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements  BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
     return    blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
               List<Predicate> predicateList=new ArrayList<>();
               if(!"".equals(blog.getTitle())&&blog.getTitle()!=null)
               {
                   predicateList.add(criteriaBuilder.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
               }
               if(blog.getTypeId()!=null)
               {
                   predicateList.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
               }
               if(blog.isRecommend())
               {
                   predicateList.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
               }

               query.where(predicateList.toArray(new Predicate[predicateList.size()]));


                return null;
            }
        },pageable);


    }

    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null)
        {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);

        }
        else
        {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);

    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1=blogRepository.getOne(id);
        if(blog1==null)
            throw new NotFoundException("不存在该博客");
        BeanUtils.copyProperties(blog,blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public boolean updateRecommend(Blog blog) {
        blogRepository.updateRecommend(blog);
        return false;
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog=blogRepository.getOne(id);
        if(blog==null)
        {
            throw new NotFoundException("该博客不存在");
        }
        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);
        String content=b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findPublistedAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join join=  root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {

        Sort sort=Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable= PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findBlog(query,pageable);
    }

    @Override
    public int updateViews(Long blogid) {
        return blogRepository.updateViews(blogid);
    }

    @Override
    public int updateGivelike(Long blogid) {
        return blogRepository.updateGivelike(blogid);
    }

    @Override
    public int updateDislike(Long blogid) {
        return blogRepository.updateDislike(blogid);
    }

    @Override
    public  Map<String,Map<String,List<Blog>>> archiveBlog() {
        List<String> years=blogRepository.findGroupYear();
        Map<String,Map<String,List<Blog>>> yearMap=new HashMap<>();
        for (String year:years) {
            List<String> months=blogRepository.findGroupYearMonth(year);
            Map<String,List<Blog>> blogsMap=new HashMap<>();
            for (String month:months) {
                List<Blog> blogs=blogRepository.findByMonth(month);
                blogsMap.put(month,blogs);
            }
             yearMap.put(year,blogsMap);
        }
        return yearMap;
    }

    public List<String> findGroupYear()
    {
        return blogRepository.findGroupYear();
    }

    public List<String> findGroupYearMonth(String year)
    {
        return blogRepository.findGroupYearMonth(year);
    }
    public List<Blog> findByMonth(String month)
    {
        return blogRepository.findByMonth(month);
    }

}
