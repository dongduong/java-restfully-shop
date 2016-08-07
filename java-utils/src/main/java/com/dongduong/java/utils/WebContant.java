/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author dongduong
 */
@Startup
@Singleton
public class WebContant extends HashMap<String, String> {

    public static final String LL_START_TIME_FORMAT = "HH:mm:ss";
    public static final String LL_START_DATE_FORMAT = "MM/dd/yyyy";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

//    public static final String DATE_FORMAT = "dd/MM/yyyy";
//    public static final String DATE_FORMAT_FULL = "dd/MM/yyyy HH:mm:ss";
    public static final int PASSWORD_RANDOM_LIMIT = 10;

    public static final String SETTING_EMAIL_PROPERTIES = "fwaSetting";

    @PostConstruct
    public void init() {
        put("DATE_FORMAT", DATE_FORMAT);
    }
}
