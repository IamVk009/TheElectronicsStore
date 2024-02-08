package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.exceptions.BadApiRequestException;
import com.lucifer.electronics.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadImageFile(MultipartFile imageFile, String filePath) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();
        logger.info("Original File Name : {}", originalFilename);

//      Multiple files with same name can be uploaded, So we have to assign random and unique file name to each file.
        String fileName = UUID.randomUUID().toString() + "-" + originalFilename;
        logger.info("FileName : {}", fileName);
        String fullFilePathWithFileName = filePath + fileName;
        logger.info("Full Filepath with Filename : {}", fullFilePathWithFileName);
        String fileExtension = null;
        if (originalFilename != null) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        if (fileExtension != null && fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg")) {
            File folder = new File(filePath);
            logger.info("Folder : {}", folder);

            if (!folder.exists()) {
//              Create folders with all subfolders
                folder.mkdirs();
            }

//          Upload/save file in desired location
            Files.copy(imageFile.getInputStream(), Paths.get(fullFilePathWithFileName));
            return fileName;
        } else {
            throw new BadApiRequestException("File with extension '" + fileExtension + "' is not allowed.");
        }
    }

    @Override
    public InputStream getImageFile(String filePath, String fileName) throws FileNotFoundException {
        String fileDestination = filePath + fileName;
        InputStream inputStream = new FileInputStream(fileDestination);
        return inputStream;
    }
}
