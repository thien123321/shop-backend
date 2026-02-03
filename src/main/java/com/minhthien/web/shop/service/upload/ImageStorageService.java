package com.minhthien.web.shop.service.upload;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service

public class ImageStorageService implements ISttorageService {

    private final Path storageFolder = Paths.get("upload");

    public ImageStorageService() {
        try{
            Files.createDirectories(storageFolder);
        }catch (IOException ioException){
            throw new RuntimeException("Unable to create directory" ,ioException);
        }
    }

    private boolean isImageFile(MultipartFile file){
        String fileExtention = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"jpg", "jpeg", "png", "gif", "webp"}).contains(fileExtention.trim().toLowerCase());

    }


    @Override
    public String storeFile(MultipartFile file) {
        try{
            if (file.isEmpty()){
                throw new RuntimeException("File is empty");
            }

            if (!isImageFile(file)){
                throw new RuntimeException("File is not an image");
            }


            long fileSize = file.getSize();
            if (fileSize > 5*1024*1024){
                throw new RuntimeException("File is too large");
            }

            String fileExtention = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());
            String generatedFileName = UUID.randomUUID().toString().replace("-","");
            generatedFileName = generatedFileName + "." + fileExtention;

            Path destinationFilePath = this.storageFolder.resolve(generatedFileName)
                    .toAbsolutePath().normalize();


            Path storageFolderAbs = storageFolder.toAbsolutePath().normalize();

            if (!destinationFilePath.startsWith(storageFolderAbs)) {
                throw new RuntimeException("Cannot store file outside upload folder");
            }


            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
                return generatedFileName;
            }


        }catch (IOException exception){
            throw new RuntimeException("Failed to store file" , exception);
        }
    }

    @Override
    public Stream<Path> loadFiles() {
        try{
            return Files.walk(this.storageFolder,1)
                    .filter(path -> !path.equals(this.storageFolder))
                    .map(this.storageFolder::relativize);
        }catch (IOException ioException){
            throw new RuntimeException("Failed to load files", ioException);
        }
    }

    @Override
    public byte[] readFile(String filename) {
        try{
            Path file = storageFolder.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException("Could not read file`" + filename);
            }
        }catch (IOException exception){
            throw new RuntimeException("Failed to read file" + filename, exception);
        }
    }

    @Override
    public void deleteFile(String filename) {

    }
}
