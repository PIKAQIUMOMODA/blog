package com.zcy.blog.service;

import com.zcy.blog.dao.TagRepository;
import com.zcy.blog.exception.NotFoundException;
import com.zcy.blog.pojo.Tag;
import com.zcy.blog.pojo.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        if(tagRepository.findTagsByName(tag.getName()).isEmpty())
        {
            tag.setCreateTime(new Date());
            return tagRepository.save(tag);
        }
        else
            return null;

    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return  tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTags(String ids) {

        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids)
    {
        List<Long> list=new ArrayList<>();
        if(!"".equals(ids)&&ids!=null)
        {
            String[] idarry=ids.split(",");
            for (int i=0;i<idarry.length;i++)
            {
                list.add(new Long(idarry[i]));
            }
        }
        return list;

    }


    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t=tagRepository.getOne(id);
        if(t==null)
        {
            throw new NotFoundException("不存在该标签");
        }
        t.setUpdateTime(new Date());
        BeanUtils.copyProperties(tag,t,"id","createTime","updateTime");
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
            tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> listTagsTop(Integer size) {

        Sort sort=Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable= PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    @Override
    public Page<Tag> listTags(Pageable pageable, Tag tag) {
         return  tagRepository.findAll(new Specification<Tag>() {
            @Override
            public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList=new ArrayList<>();
                if(!"".equals(tag.getName())&&tag.getName()!=null)
                {
                    predicateList.add(criteriaBuilder.like(root.<String>get("name"),"%"+tag.getName()+"%"));
                }

                query.where(predicateList.toArray(new Predicate[predicateList.size()]));
                return null;
            }
        },pageable);
    }




}
