package com.lucifer.electronics.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadImageFile(MultipartFile imageFile, String filePath) throws IOException;

    InputStream getImageFile(String filePath, String fileName) throws FileNotFoundException;
}
