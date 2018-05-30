package com.muhammadelsayed.bybike.activity.utils;

public class Utils {

    // Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    // Fragments Tags
    public static final String LoginFragment = "LoginFragment";
    public static final String SignUpFragment = "SingUpFragment";
    public static final String ForgotPasswordFragment = "ForgotPasswordFragment";


    /**
     * This method split any name into 2 names, first and last name
     *
     * @param name is the name that will be split
     * @return String array holding first and last name;
     */
    public static String[] splitName(String name) {
        String[] names = name.split(" ", 2);
        return names;
    }

    /**
     * This method concatenates first and last name into full name
     *
     * @param firstname Last Name
     * @param lastname First Name
     * @return Full Name
     */
    public static String concatNames(String firstname, String lastname) {
        return firstname + " " + lastname;
    }

}
