package com.campusconnect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campusconnect.entity.Tag;
import com.campusconnect.exception.CustomException;
import com.campusconnect.mapper.TagMapper;
import com.campusconnect.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.selectList(new LambdaQueryWrapper<Tag>().orderByAsc(Tag::getId));
    }

    @Override
    public Tag createTag(String name) {
        Long count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
        if (count > 0) {
            throw new CustomException("标签名已存在");
        }
        Tag tag = Tag.builder().name(name).build();
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public void deleteTag(Long id) {
        tagMapper.deleteById(id);
    }
}