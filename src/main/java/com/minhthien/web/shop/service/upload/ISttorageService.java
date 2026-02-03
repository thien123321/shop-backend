package com.minhthien.web.shop.service.upload;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface ISttorageService {

    public String storeFile(MultipartFile file);
    public Stream<Path> loadFiles();
    public byte[] readFile(String filename);
    public void deleteFile(String filename);
}
