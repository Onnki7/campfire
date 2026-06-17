package com.campusconnect.service;

import com.campusconnect.entity.Tag;
import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    Tag createTag(String name);
    void deleteTag(Long id);
}