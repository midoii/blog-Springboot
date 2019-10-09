package com.blog.upload.service;


import com.blog.common.back.ImgTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.upload.config.UploadProperties;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private UploadProperties prop;

    public ImgTemplate uploadImage(MultipartFile file) {
        String url = null;
        try {
            //校验文件
            String contentType = file.getContentType();
            if(!prop.getAllowTypes().contains(contentType)){
                return new ImgTemplate("文件类型不匹配");
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                return new ImgTemplate("文件内容不能为空");
            }

            //上传到FastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //返回路径
            url = prop.getBaseUrl() + storePath.getFullPath();
        }catch (IOException e){

            return new ImgTemplate("文件上传失败");
        }
        return new ImgTemplate(url);
    }
}
