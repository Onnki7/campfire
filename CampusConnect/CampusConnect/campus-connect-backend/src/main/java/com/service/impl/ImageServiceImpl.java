package com.campusconnect.service.impl;

import com.campusconnect.entity.Image;
import com.campusconnect.exception.CustomException;
import com.campusconnect.mapper.ImageMapper;
import com.campusconnect.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public String uploadImage(MultipartFile file, Long uploaderId) {
        if (file.isEmpty()) {
            throw new CustomException("文件不能为空");
        }

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = UUID.randomUUID().toString() + suffix;

            Path path = Paths.get(uploadDir + newFilename);
            Files.write(path, file.getBytes());

            String accessUrl = "/api/image/view/" + newFilename;

            Image img = Image.builder()
                    .url(accessUrl)
                    .path(uploadDir + newFilename)
                    .uploaderId(uploaderId)
                    .build();

            imageMapper.insert(img);
            return accessUrl;
        } catch (IOException e) {
            log.error("Failed to upload image: ", e);
            throw new CustomException("上传图片异常");
        }
    }
}