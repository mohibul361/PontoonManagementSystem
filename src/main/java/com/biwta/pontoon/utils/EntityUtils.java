package com.biwta.pontoon.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public class EntityUtils {
    /*public static String getUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authTokenHeader = request.getHeader("Authorization");
        String jwtToken = authTokenHeader.replaceAll("Bearer ", "");
        String[] parts = jwtToken.split("\\.");
        String payload = StringUtils.newStringUtf8(Base64.decodeBase64(parts[20]));
        JSONObject jsonObject = new JSONObject(payload);
        String userName = jsonObject.getString("sub");
        return userName;
    }*/
    public static String getUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authTokenHeader = request.getHeader("Authorization");

        if (authTokenHeader != null && authTokenHeader.startsWith("Bearer ")) {
            String jwtToken = authTokenHeader.substring(7); // Remove "Bearer " prefix
            String[] parts = jwtToken.split("\\.");

            if (parts.length >= 2) {
                String payload = StringUtils.newStringUtf8(Base64.decodeBase64(parts[1]));
                JSONObject jsonObject = new JSONObject(payload);
                return jsonObject.getString("sub");
            } else {
                throw new RuntimeException("Invalid JWT token format");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or has an invalid format");
        }
    }

    public static <T> void setAdd(T entity, HttpServletRequest request) {
        Field[] fields = entity.getClass().getDeclaredFields();
        List<String> addFields = Arrays.asList("addUser", "addDate", "addTerm", "addIp");

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (addFields.contains(fieldName)) {
                    switch (fieldName) {
                        case "addUser":
                            field.set(entity, getUserName());
                            break;
                        case "addDate":
                            field.set(entity, LocalDateTime.now());
                            break;
                        case "addTerm":
                            String addTerm = getComputerName();
                            if (addTerm != null) {
                                field.set(entity, addTerm);
                            }
                            break;
                        case "addIp":
                            String addIp = getRealClientIP(request);
                            field.set(entity, addIp);
                            break;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> void setModify(T entity, HttpServletRequest request) {
        Field[] fields = entity.getClass().getDeclaredFields();
        List<String> modifyFields = Arrays.asList("modUser", "modDate", "modTerm", "modIp");

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (modifyFields.contains(fieldName)) {
                    switch (fieldName) {
                        case "modUser":
                            field.set(entity, getUserName());
                            break;
                        case "modDate":
                            field.set(entity, LocalDateTime.now());
                            break;
                        case "modTerm":
                            String modifiedTerm =getComputerName();
                            if (modifiedTerm != null) {
                                field.set(entity, modifiedTerm);
                            }
                            break;
                        case "modIp":
                            field.set(entity, getRealClientIP(request));
                            break;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static String getRealClientIP(HttpServletRequest request) {
        String[] headersToCheck = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR"};
        for (String header : headersToCheck) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    public static String getComputerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
