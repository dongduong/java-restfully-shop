/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils;

import java.io.IOException;

/**
 *
 * @author dongduong
 */
public class ApplicationConfig extends ConfigAbstract{

    private static ApplicationConfig instance;
    
    @Override
    public String getPropertiesPath() {
        return "/application.properties";
    }
    
    public static synchronized ApplicationConfig getInstance() throws IOException {
        //if (instance == null) {
            instance = new ApplicationConfig();
            instance.init();
        //}
        return instance;
    }
    
}
