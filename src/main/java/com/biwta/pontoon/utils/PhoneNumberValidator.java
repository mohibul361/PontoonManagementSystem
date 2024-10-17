package com.biwta.pontoon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nasimkabir
 * ১৪/১/২৪
 */
public class PhoneNumberValidator {
    public static boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Regular expression for Bangladeshi phone numbers
        String regex = "(\\+8801|01)[0-9]{9}$";

        // Creating a pattern object
        Pattern pattern = Pattern.compile(regex);

        // Creating a Matcher object
        Matcher matcher = pattern.matcher(phoneNumber);

        // Verifying the match
        return matcher.matches();
    }
}
