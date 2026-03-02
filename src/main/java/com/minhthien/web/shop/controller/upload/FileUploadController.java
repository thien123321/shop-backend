package com.minhthien.web.shop.controller.upload;

import com.minhthien.web.shop.dto.upload.ResponseObject;
import com.minhthien.web.shop.service.upload.ISttorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
@Transactional

public class FileUploadController {

    private final ISttorageService iSttorageService;
    @PostMapping(value ="/uploadfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file) {
        try{

            String generaratedFileName = iSttorageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","Uploaded Successfully",generaratedFileName)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("ok", e.getMessage(), "")
            );
        }

    }
    // get img url
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> readDetailFile(@PathVariable String fileName) {
        try {
            Path file = Paths.get("upload").resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            String contentType = Files.probeContentType(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Load all upload file

    @GetMapping("/loadall")
    public ResponseEntity<ResponseObject> getUploadedFiles() {
        try {
            List<String> url = iSttorageService.loadFiles()
                    .map(path -> {
                        //convert fileName to url(send request "readDetailFile")
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "readDetailFile", path.getFileName().toString()).build().toUri().toString();
                        return urlPath;
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(
                    new ResponseObject("ok","List files uploaded successfully",url)
            );
        }catch (Exception e) {
            return ResponseEntity.ok(
                    new ResponseObject("failed", "List files uploaded failed", new String[]{})
            );
        }

    }

}
