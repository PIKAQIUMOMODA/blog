package com.zcy.blog.dao;

import com.zcy.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {


    @Modifying
    @Transactional
    @Query("update Blog b set b.recommend=:#{#bl.recommend} where b.id=:#{#bl.id}")
    int updateRecommend(@Param("bl") Blog blog);

    @Query("select b from Blog b where b.recommend=true ")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog  b where b.published=true")
    Page<Blog> findPublistedAll(Pageable pageable);

    @Query("select b from Blog  b where b.title like ?1 or b.content like ?1")
    Page<Blog> findBlog(String query,Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(Long blogid);

    @Transactional
    @Modifying
    @Query("UPDATE Blog b set b.givelike=b.givelike+1 where b.id=?1")
    int updateGivelike(Long blogid);


    @Transactional
    @Modifying
    @Query("UPDATE Blog b set b.dislike=b.dislike+1 where b.id=?1")
    int updateDislike(Long blogid);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y')  order by year desc")
    List<String> findGroupYear();

    @Query("select function('date_format',b.updateTime,'%Y-%m')  as month from Blog b where function('date_format',b.updateTime,'%Y') =?1  group by function('date_format',b.updateTime,'%Y-%m')   order by month desc")
    List<String> findGroupYearMonth(String year);

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y-%m') =?1 ")
    List<Blog> findByMonth(String month);


}
