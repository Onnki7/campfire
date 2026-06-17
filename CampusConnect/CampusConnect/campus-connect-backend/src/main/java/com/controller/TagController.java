package com.campusconnect.controller;

import com.campusconnect.entity.Tag;
import com.campusconnect.service.TagService;
import com.campusconnect.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public Result<List<Tag>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Tag> createTag(@RequestParam String name) {
        return Result.success(tagService.createTag(name));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success("删除成功");
    }
}