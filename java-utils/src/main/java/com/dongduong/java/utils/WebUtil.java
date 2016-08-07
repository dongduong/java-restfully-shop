/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dongduong
 */
public class WebUtil {

    static public final int LIMIT_USER = 50; //50
    protected static final String EMPTY = "";
    static protected final Logger logger = Logger.getLogger(WebUtil.class.getName());
    static public final String APPLICATION_PROPERTIES = "WEB-INF/application.properties";
    // error message
    private String hiddenMessageError;
    private Map<String, String> mapErrorFilter = new HashMap<>();
    private String errorKey = "a11111a";
    private String controlIdError = "";

    /**
     * getApplicationConfigValue
     *
     * @param key
     * @return
     */
    public static String getApplicationConfigValue(String key) {
        Properties properties = getProperties(getRealPath() + APPLICATION_PROPERTIES);
        return properties.getProperty(key);
    }

    /**
     * convertDisplayError
     *
     * @param error
     * @return
     */
    public String convertDisplayError(String error) {
        String keyId = "";
        String value = "";
        // check key has controlID 
        if (error.contains(errorKey)) {
            String[] arr = error.split(errorKey);
            keyId = arr[0].trim();
            value = arr[1].trim();

        } else {
            value = error.trim();
        }
        // add key to controlIdError
        if (!"".equals(keyId) && !controlIdError.contains(keyId + ",")) {
            controlIdError += keyId + ",";
        }
        if (mapErrorFilter.containsKey(value)) {
            return "";
        } else {
            mapErrorFilter.put(value, value);
        }

        return "";
    }

    /**
     * @return the hiddenMessageError
     */
    public String getHiddenMessageError() {
        // reset map error
        mapErrorFilter = new HashMap<String, String>();
        controlIdError = "";
        return hiddenMessageError;
    }

    /**
     * initLookupCustomResourceEJB
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Properties initLookupCustomResourceEJB(String path) throws IOException {
        Properties _properties = null;
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            _properties = (Properties) ctx.lookup(path);
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, "initLookupCustomResourceEJB {0}", ex);
        }
        return _properties;
    }

    /**
     * Method random password
     *
     * @param length
     * @return
     */
    public static String randomChar(int length) {
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toUpperCase();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

    public String loadMessages(String messages, String iso3) {
        String defaultPath = "com.pushinnovation.fwa.messages.";
        if (WebUtil.nullOrEmpty(iso3)) {
            return defaultPath + messages;
        }
        String addressFile = "com/pushinnovation/fwa/messages/" + messages + "_" + iso3 + ".properties";
        InputStream inputStream = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        inputStream = loader.getResourceAsStream(addressFile);
        if (inputStream != null) {
            return defaultPath + messages + "_" + iso3;
        } else {
            return defaultPath + messages;
        }
    }

    /**
     * writterLogInfo
     *
     * @param error
     */
    protected void writterLogInfo(String error) {
        logger.log(Level.INFO, error);
    }

    /**
     * writterLogServer
     *
     * @param error
     */
    protected void writterLogServer(String error) {
        logger.log(Level.SEVERE, error);
    }

    /**
     * getValue
     *
     * @param object
     * @return
     */
    public static String getValue(Object object) {
        return (object != null && !EMPTY.equals(object.toString())) ? object.toString() : EMPTY;
    }

    /**
     * lookupEJB
     *
     * @param jndi
     * @return
     */
    public static Object lookupEJB(String jndi) {
        try {
            InitialContext ctx = new InitialContext();
            return ctx.lookup(jndi);
        } catch (NamingException e) {
            throw new RuntimeException("couldn't lookup Dao", e);
        }
    }

    /**
     * Read the remote IP of a servlet request. This may be in one of two
     * places. If we are behind an Apache server with mod_proxy_http, we get the
     * remote IP address from the request header x-forwarded-for. In that case
     * the remote ip of the requestor is always that of the Apache server, since
     * that is the last proxy, as per spec.
     * <p>
     * If this header is missing, we're probably running locally for testing. In
     * that case we can just use the remote IP from the request Object itself.
     *
     * @param request The request to get the remote IP from.
     * @return The remote IP address.
     * @throws UnknownHostException When the remote IP could not be resolved.
     */
    public static InetAddress remoteIp(final HttpServletRequest request)
            throws UnknownHostException {
        if (request.getHeader("x-forwarded-for") != null) {
            return InetAddress.getByName(request.getHeader("x-forwarded-for"));
        }
        return InetAddress.getByName(request.getRemoteAddr());
    }

    /**
     * Read the remote IP of a servlet request. This may be in one of two
     * places. If we are behind an Apache server with mod_proxy_http, we get the
     * remote IP address from the request header x-forwarded-for. In that case
     * the remote ip of the requestor is always that of the Apache server, since
     * that is the last proxy, as per spec.
     * <p>
     * If this header is missing, we're probably running locally for testing. In
     * that case we can just use the remote IP from the request Object itself.
     *
     * @param request The request to get the remote IP from.
     * @return The remote IP address.
     * @throws UnknownHostException When the remote IP could not be resolved.
     */
    public String remoteIpClient(final HttpServletRequest request)
            throws UnknownHostException {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.log(Level.INFO, "IP client {0}", ip);
        return ip;
    }

    public static String loadFile(String filename) {
        String key = null;
        String line;
        StringBuilder base64Content = new StringBuilder();
        try {
            File f = new File(filename);
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                while ((line = br.readLine()) != null) {
                    base64Content.append(line);
                    //base64Content = base64Content + line;
                }
            } else {
                return key;
            }
            if (base64Content.length() > 0) {
                key = base64Content.toString();
            }
        } catch (IOException ex) {
        }
        return key;
    }

    public static void saveFile(String filename, String base64String) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(filename));
            pw.print(base64String);
        } catch (IOException ex) {
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static Object getSessionClass(String nameClass) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getSession().getAttribute(nameClass);
    }

    public static boolean nullOrEmpty(String s) {
        if (s == null || s.trim().equals(EMPTY)) {
            return true;
        }
        if (s.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    public static void handleFacesMessages(String msg, FacesMessage.Severity severity, String id) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(msg);
        facesMessage.setSeverity(severity);
        facesContext.addMessage(id, facesMessage);
    }

    /**
     * path : <code>folder/page.xhtml</code>
     * <p>
     * Ex : userinfomanagement/updateprofile.xhtml
     *
     * @param path
     */
    public static void redirect(String path) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "redirect {0}", ex);
        }
    }

    public static void dispatch(String path) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().dispatch(path);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "redirect {0}", ex);
        }
    }

    /**
     * path : <code>folder/page.xhtml</code>
     *
     * @param url
     */
    public void redirectPage(String url) {
        String path = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            path = facesContext.getExternalContext().getRequestContextPath();
        }
        redirect(path + url);
    }

    /**
     *
     * @return locale
     */
    public static java.util.Locale getCurrentLocale() {
        return getFacesContext().getViewRoot().getLocale();
    }

    /**
     *
     * @return facesContext
     */
    public static javax.faces.context.FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
        return httpServletRequest;
    }

    public static boolean validateDate(byte day, byte month, int year) {
        // check null
        // check illegal day in a month
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int actualMaximumDateInMonth = calendar.getActualMaximum(Calendar.DATE);

        if (day > actualMaximumDateInMonth) {
            return false;
        }
        return true;
    }

    public static void redirectWithContexPath(String path) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
            fc.getExternalContext().redirect(request.getContextPath() + "/" + path);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "{0}", ex);
        }
    }

    public static boolean isDateValid(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            formatter.format(date);
        } catch (Exception ex) {
            //logger.log(Level.SEVERE, "{0}", ex);
            return false;
        }
        return true;
    }

    public static boolean isDateValidMonthYear(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            formatter.parse(date);
        } catch (ParseException ex) {
            //logger.log(Level.SEVERE, "{0}", ex);
            return false;
        }
        return true;
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isBigInteger(String value) {
        try {
            BigInteger a = new BigInteger(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String value) {
        try {
            Integer a = new Integer(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * getBigDecimal
     *
     * @param value
     * @return
     */
    public static BigDecimal getBigDecimal(String value) {
        try {
            return new BigDecimal(value);

        } catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    public static BigInteger getBigInteger(String value) {
        BigInteger result = null;
        try {
            if (value == null) {
                return new BigInteger("0");
            }
            String expression = "^[0-9]{1,22}$";
            CharSequence inputStr = value;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                result = new BigInteger(value);
            } else {
                return new BigInteger("0");
            }
        } catch (Exception e) {
            return new BigInteger("0");
        }
        return result;
    }

    public static Integer getInteger(String value) {
        Integer result = null;
        try {
            if (value == null) {
                return new Integer("0");
            }
            String expression = "^[0-9]{1,10}$";
            CharSequence inputStr = value;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                result = new Integer(value);
            } else {
                return new Integer("0");
            }
        } catch (NumberFormatException e) {
            return new Integer("0");
        }
        return result;
    }

    public static UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }
        Iterator<UIComponent> kids = c.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public static void handleFacesMessages(String msg, FacesMessage.Severity severity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(msg);
        facesMessage.setSeverity(severity);
        facesContext.addMessage(null, facesMessage);
    }

    public static Properties getPropertiesForSendMail(String fileName) {
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            File f = new File(fileName);
            if (f.exists()) {
                in = new FileInputStream(f);
                properties.load(in);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "{0}", ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "{0}", e);
            }
        }
        return properties;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            File f = new File(fileName);
            if (f.exists()) {
                in = new FileInputStream(f);
                properties.load(in);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "{0}", ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "{0}", e);
            }
        }
        return properties;
    }

    public static String getRealPath() {
        FacesContext fcontext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) fcontext.getExternalContext().getContext();
        String path = scontext.getRealPath("/");
        return path;
    }

    public static String convertTime2Text(long time, String strPattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strPattern);
        //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String str = simpleDateFormat.format(date);
        return str;
    }
    
    public static String convertTime2Text(long time, String strPattern, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strPattern);
        simpleDateFormat.setTimeZone(timeZone);
        String str = simpleDateFormat.format(date);
        return str;
    }

//    public static String convertTime2Text(long time, String strPattern) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time);
//        Date date = calendar.getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strPattern);
//        String str = simpleDateFormat.format(date);
//        return str;
//    }
    public static long parseDateToLong(String text) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(WebContant.DATE_FORMAT_FULL);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            return dateFormat.parse(text).getTime();
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, "parseDateToLong {0}", ex);
        }
        return 0;
    }

    /**
     * @author bangla
     * @param <T>
     * @param managedBeanName
     * @param beanClass
     * @return Managed Bean Use Application#evaluateExpressionGet() that it will
     * create bean when not done yet.
     */
    public static <T> T findBean(String managedBeanName, Class<T> beanClass) {
        FacesContext context = FacesContext.getCurrentInstance();
        return beanClass.cast(context.getApplication().evaluateExpressionGet(context, "#{" + managedBeanName + "}", beanClass));
    }

    /**
     * 
     * @param year
     * @param month
     * @param day
     * @return 
     */
    public synchronized static java.util.Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        java.util.Date date = cal.getTime();

        return date;
    }

    /**
     *
     * @param aStart
     * @param aEnd
     * @param aRandom
     * @return
     */
    private synchronized static int randomInteger(int aStart, int aEnd, java.util.Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }

    /**
     * randomSynID 12 number
     *
     * @return
     */
    public synchronized static String randomActivationCode() {
        Random random = new Random();
        String s = "" + randomInteger(1000, 9999, random);
        return s;
    }

    public synchronized static boolean IsUrl(String url) {
//        Pattern pattern = Pattern.compile(
//                "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
//                + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov"
//                + "|mil|biz|info|mobi|name|aero|jobs|museum"
//                + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
//                + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?"
//                + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
//                + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)"
//                + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
//                + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
//                + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        Pattern pattern = Pattern.compile(
                "([a-zA-Z0-9.]|%[0-9A-Za-z]|/|:[0-9]?)*");
        Matcher matcher = pattern.matcher(url);

        return matcher.find();
    }

    public static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
                // e.printStackTrace();
            }
        }
    }

    public static String getUrlConcentSlash(String url, String slash) {
        if (nullOrEmpty(url)) {
            return "";
        }
        if (url.substring(url.length() - 1, url.length()).contains("/")) {
            return url + slash;
        }
        return url + "/" + slash;
    }

    public synchronized static String convertStringUtf8(String data) {
        if (WebUtil.nullOrEmpty(data)) {
            return "";
        }
        byte[] utf8 = new byte[0];
        try {
            utf8 = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, "convertStringUtf8 UnsupportedEncodingException", ex);
        }
        data = new String(utf8);
        return data;
    }

    public static boolean checkUsernameValidity(String username) {
        Pattern p = Pattern.compile("^(\\w|[.]|[-]){6,32}$");      // the expression
        Matcher m = p.matcher(username);       // the source
        return m.matches();
    }

    /**
     * @param hiddenMessageError the hiddenMessageError to set
     */
    public void setHiddenMessageError(String hiddenMessageError) {
        this.hiddenMessageError = hiddenMessageError;
    }

    /**
     * @return the mapErrorFilter
     */
    public Map<String, String> getMapErrorFilter() {
        return mapErrorFilter;
    }

    /**
     * @param mapErrorFilter the mapErrorFilter to set
     */
    public void setMapErrorFilter(Map<String, String> mapErrorFilter) {
        this.mapErrorFilter = mapErrorFilter;
    }

    /**
     * @return the errorKey
     */
    public String getErrorKey() {
        return errorKey;
    }

    /**
     * @param errorKey the errorKey to set
     */
    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    /**
     * @return the controlIdError
     */
    public String getControlIdError() {
        return controlIdError;
    }

    /**
     * @param controlIdError the controlIdError to set
     */
    public void setControlIdError(String controlIdError) {
        this.controlIdError = controlIdError;
    }

    public String getStringDateFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public Date getDateFormat(String v1, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(v1);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, "getDateFormat {0}", ex);
        }
        return null;
    }

    public Date getDateFormat(Date date, String format) {
        String v1 = getStringDateFormat(date, format);
        SimpleDateFormat formatter = null;
        Date date1 = null;
        try {
            formatter = new SimpleDateFormat(format);
            date = formatter.parse(v1);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, "getDateFormat {0}", ex);
        }
        return date1;
    }

    public long getLongValueFromStringDate(String dateConvert, String patern, boolean isDateFrom) {
        long returnValue = 0;
        Date date = new Date();
        if (!nullOrEmpty(dateConvert)) {
            date = getDateFormat(dateConvert, patern);
            if (isDateFrom) {
                date = getDateFromValue(date);
            } else {
                date = getDateToValue(date);
            }
            if (date != null) {
                returnValue = date.getTime();
            }
        }

        return returnValue;
    }

    public Date getDateFromValue(Date dateFrom) {
        if (dateFrom == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0); // for the new day
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date getDateToValue(Date dateTo) {
        if (dateTo == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTo);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTime();
    }

    /**
     * readBufferImageFile
     *
     * @param fileimage
     * @return
     */
    public static byte[] readBufferImageFile(File fileimage) {
        byte[] imageInByte = null;
        FileInputStream fileInputStream = null;
        try {

            if (fileimage.exists()) {
                imageInByte = new byte[(int) fileimage.length()];
                fileInputStream = new FileInputStream(fileimage);
                fileInputStream.read(imageInByte);
                fileInputStream.close();
            }

        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception {0}", ex);
        } finally {
            close(fileInputStream);
        }
        return imageInByte;
    }

    public static boolean isValidDate(String inDate, String pattern) {

        if (inDate == null) {
            return false;
        }

        //set the format to use as a constructor argument
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        if (inDate.trim().length() != dateFormat.toPattern().length()) {
            return false;
        }

        dateFormat.setLenient(false);

        try {
            //parse the inDate parameter
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * checkIds
     *
     * @param ids
     * @return
     */
    public static boolean checkIds(String ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return true;
            }
            String[] arrayIds = ids.split(",");
            if (arrayIds.length > 0) {
                for (String id : arrayIds) {
                    if (!isInteger(id)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static String getIds(String ids) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String[] arrayIds = ids.split(",");
            if (arrayIds.length > 0) {
                int index = 0;
                for (String id : arrayIds) {
                    if (isInteger(id)) {
                        stringBuilder.append(id);
                    }
                    index++;
                    if (index <= arrayIds.length - 1) {
                        stringBuilder.append(",");
                    }
                }
            } else {

            }
            return stringBuilder.toString();
        } catch (Exception ex) {
        }
        return null;
    }
    
    public String getRequestParam(String key){
           return  FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
        }
    
    public static void main(String[] args) {
//        String shipPostCode = "12345-1234";
//        if (shipPostCode.length() > 5) {
//            String zipCode = shipPostCode.substring(0, 5);
//            String zipCodeAddOn = shipPostCode.substring(6, shipPostCode.length());
//            System.out.println("zipCode " + zipCode);
//            System.out.println("zipCodeAddOn " + zipCodeAddOn);
//        } else {
//            System.out.println("zipCode " + shipPostCode);
//        }

        String[] shipPostCodeArray = {
            "12345-1234", "12345/1234",
            "12345,1234", "12345.1234",
            "12345 1234", "12345_1234",
            "1234-1234" , "12345",
            ""          , "123"
        };
        for (String shipPostCode : shipPostCodeArray) {

            String[] array = shipPostCode.split("\\W");
            if (array.length > 1) {
                String zipCode = array[0];
                String zipCodeAddOn = array[1];
                System.out.println("shipPostCode " + shipPostCode);
                System.out.println("zipCode " + zipCode);
                System.out.println("zipCodeAddOn " + zipCodeAddOn);
                System.out.println("===========================");
            } else {
                String zipCode = array[0];
                System.out.println("shipPostCode " + shipPostCode);
                System.out.println("zipCode " + zipCode);
                System.out.println("===========================");
            }

//            if (shipPostCode.length() > 5) {
//                String[] array = shipPostCode.split("\\W");
//                if (array.length > 1) {
//                    String zipCode = array[0];
//                    String zipCodeAddOn = array[1];
//                    System.out.println("shipPostCode " + shipPostCode);
//                    System.out.println("zipCode " + zipCode);
//                    System.out.println("zipCodeAddOn " + zipCodeAddOn);
//                    System.out.println("===========================");
//                } else {
//
//                }
//            } else {
//                System.out.println("zipCode " + shipPostCode);
//            }
        }

    }
}
