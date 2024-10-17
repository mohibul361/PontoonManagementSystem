package com.biwta.pontoon.utils;

import org.apache.commons.io.FilenameUtils;

import java.util.Arrays;
import java.util.List;
/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */

public class FileExtensionUtil {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png","pdf");
    private static final List<String> ALLOWED_DOCUMENT_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png","pdf");

    public static boolean isImageExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        return ALLOWED_IMAGE_EXTENSIONS.contains(extension);
    }

    public static boolean isDocumentExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        return ALLOWED_DOCUMENT_EXTENSIONS.contains(extension);
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return ""; // No extension found or the dot is at the end of the filename
        }
        return fileName.substring(lastDotIndex + 1);
    }
}