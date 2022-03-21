package com.zcy.blog.service;

import com.zcy.blog.pojo.Tag;
import com.zcy.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag type);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTags();

    List<Tag> listTags(String ids);

    Tag updateTag(Long id,Tag type);

    void deleteTag(Long id);

    List<Tag> listTagsTop(Integer size);

    Page<Tag> listTags(Pageable pageable, Tag tag);
}
