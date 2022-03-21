package com.zcy.blog.utils;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class MyBeanUtils {

    public static String[] getNullPropertyNames(Object source)
    {
        BeanWrapper beanWrapper=new BeanWrapperImpl(source);
        PropertyDescriptor[] pds=beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames=new ArrayList<String>();
        for(PropertyDescriptor pd:pds)
        {
            String propertName=pd.getName();
            if(beanWrapper.getPropertyValue(propertName)==null)
            {
                nullPropertyNames.add(propertName);
            }

        }
        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);

    }
}
