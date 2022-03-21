package com.zcy.blog.service;

import com.zcy.blog.dao.TypeRepository;
import com.zcy.blog.exception.NotFoundException;
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
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        if(typeRepository.findTypesByName(type.getName()).isEmpty())
        {
            type.setCreateTime(new Date());
            return typeRepository.save(type);
        }
        else
            return null;

    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t=typeRepository.getOne(id);
        if(t==null)
        {
            throw new NotFoundException("不存在该类型");
        }
        type.setUpdateTime(new Date());
        BeanUtils.copyProperties(type,t,"id","createTime");
        return  typeRepository.save(t);

    }

    @Transactional
    @Override
    public void deleteType(Long id) {
      typeRepository.deleteById(id);
    }

    @Override
    public Page<Type> getTypeByName(Pageable pageable, String name) {

        return null;

    }

    @Override
    public List<Type> listTypes() {
        return typeRepository.findAll();
    }

    @Override
    public Page<Type> listTypes(Pageable pageable, Type type) {

        return     typeRepository.findAll(new Specification<Type>() {
            @Override
            public Predicate toPredicate(Root<Type> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList=new ArrayList<>();
                if(!"".equals(type.getName())&&type.getName()!=null)
                {
                    predicateList.add(criteriaBuilder.like(root.<String>get("name"),"%"+type.getName()+"%"));
                }

                query.where(predicateList.toArray(new Predicate[predicateList.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public List<Type> listTypesTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable=PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }


}
