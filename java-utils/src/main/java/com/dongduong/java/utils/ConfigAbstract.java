/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author dongduong
 */
public abstract class ConfigAbstract {

    private String _configFile;
    public Properties _properties;
    private String DEFAULT_PROPERTIES = "/application.properties"; // defaul

    public abstract String getPropertiesPath();
    
    public String getProperty(String name) {
        if (name == null) {
            return null;
        }
        return _properties.getProperty(name);
    }

    protected void init() throws IOException {
        InputStream is = null;
        if (!WebUtil.nullOrEmpty(getPropertiesPath())) {
            DEFAULT_PROPERTIES = getPropertiesPath();
        }
        Properties defaults;
        // try to find a default configuration file in the classpath
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        
        is = loader.getResourceAsStream(DEFAULT_PROPERTIES);

        if (is != null) {
            defaults = new Properties();
        } else {
            throw new FileNotFoundException(DEFAULT_PROPERTIES);
        }
        defaults.load(is);

        if (_configFile == null) {
            // just use the defaults
            _properties = defaults;
        } else {
            // the properties in _configFile override the defaults
            is = new FileInputStream(_configFile);
            if (is != null) {
                _properties = new Properties(defaults);
                _properties.load(is);
            } else {
                _properties = defaults;
            }
        }
        is.close();
    }

    protected void initLookupCustomResourceEJB() throws IOException {
        _properties = WebUtil.initLookupCustomResourceEJB(getPropertiesPath());
    }
}
