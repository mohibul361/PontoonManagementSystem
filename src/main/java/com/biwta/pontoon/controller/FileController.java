package com.biwta.pontoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author nasimkabir
 * ১৪/১/২৪
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final Environment ENVIRONMENT;

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<?> employeeImage(@PathVariable("filename") String filename, HttpServletRequest request) throws IOException {
        try {
            Path filePath = Paths.get(ENVIRONMENT.getProperty("fileStore.directory") + "/employeeImage/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            log.info("resource = " + resource);
            if (resource.exists()) {
                log.info("Image found and ready to download");
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(resource);
            } else {
                log.info("Image not found");
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException | IllegalArgumentException ex) {
            log.info("Image not found ---");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/images/pontoonImage/{filename:.+}")
    public ResponseEntity<?> pontoonImage(@PathVariable("filename") String filename, HttpServletRequest request) throws IOException {
        try {
            Path filePath = Paths.get(ENVIRONMENT.getProperty("fileStore.directory") + "/pontoonImage/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            log.info("resource = " + resource);
            if (resource.exists()) {
                log.info("Image found and ready to download");
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(resource);
            } else {
                log.info("Image not found");
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException | IllegalArgumentException ex) {
            log.info("Image not found ---");
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/profileImages/{filename:.+}")
    public ResponseEntity<?> userImage(@PathVariable("filename") String filename, HttpServletRequest request) throws IOException {
        try {
            Path filePath = Paths.get(ENVIRONMENT.getProperty("fileStore.directory") + "/userImage/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            log.info("resource = " + resource);
            if (resource.exists()) {
                log.info("Image found and ready to download");
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(resource);
            } else {
                log.info("Image not found");
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException | IllegalArgumentException ex) {
            log.info("Image not found ---");
            return ResponseEntity.notFound().build();
        }
    }

}
