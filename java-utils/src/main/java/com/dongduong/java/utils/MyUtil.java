/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dongduong.java.utils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author dongduong
 */
public abstract class MyUtil {
    
    static protected final Logger logger = Logger.getLogger(MyUtil.class.getName());
    static protected final String LOOKUP_NAME = "fulfillment";
    /**
     * getProperties
     * @param lookupName
     * @param key
     * @return 
     */
    protected static String getProperties(String lookupName, String key) {
        String output;
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            Properties props = (Properties) ctx.lookup(lookupName);
            output = props.getProperty(key);
        } catch (NamingException ex) {
            output = "key not found";
            logger.log(Level.SEVERE, "getProperties {0}", ex);
        }
        return output;
    }
}
