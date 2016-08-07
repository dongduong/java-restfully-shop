/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * ref http://www.rgagnon.com/javadetails/java-0452.html
 * @author dongduong
 */
public class SMTP {

    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$";

    public SMTP() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate hex with regular expression
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validate(final String hex) {
        if (pattern == null) {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }
        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    private static int hear(BufferedReader in) throws IOException {
        String line = null;
        int res = 0;

        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, 3);
            try {
                res = Integer.parseInt(pfx);
            } catch (Exception ex) {
                res = -1;
            }
            if (line.charAt(3) != '-') {
                break;
            }
        }

        return res;
    }

    private static void say(BufferedWriter wr, String text)
            throws IOException {
        wr.write(text + "\r\n");
        wr.flush();

        return;
    }

    private static ArrayList getMX(String hostName)
            throws NamingException {
        // Perform a DNS lookup for MX records in the domain
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext(env);
        Attributes attrs = ictx.getAttributes(hostName, new String[]{"MX"});
        Attribute attr = attrs.get("MX");

        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            attrs = ictx.getAttributes(hostName, new String[]{"A"});
            attr = attrs.get("A");
            if (attr == null) {
                throw new NamingException("No match for name '" + hostName + "'");
            }
        }

        // Huzzah! we have machines to try. Return them as an array list
        // NOTE: We SHOULD take the preference into account to be absolutely
        //   correct. This is left as an exercise for anyone who cares.
        ArrayList res = new ArrayList();
        NamingEnumeration en = attr.getAll();

        while (en.hasMore()) {
            String x = (String) en.next();
            String f[] = x.split(" ");
            if (f[1].endsWith(".")) {
                f[1] = f[1].substring(0, (f[1].length() - 1));
            }
            res.add(f[1]);
        }
        return res;
    }

    public static boolean isAddressValid(String address) {
        //Validate hex with regular expression
        if (!validate(address)) {
            return false;
        } 
        return true;

//        // Find the separator for the domain name
//        int pos = address.indexOf('@');
//
//        // If the address does not contain an '@', it's not valid
//        if (pos == -1) {
//            return false;
//        }
//
//        // Isolate the domain/machine name and get a list of mail exchangers
//        String domain = address.substring(++pos);
//        ArrayList mxList = null;
//        try {
//            mxList = getMX(domain);
//        } catch (NamingException ex) {
//            return false;
//        }
//
//        // Just because we can send mail to the domain, doesn't mean that the
//        // address is valid, but if we can't, it's a sure sign that it isn't
//        if (mxList.isEmpty()) {
//            return false;
//        }
//
//        // Now, do the SMTP validation, try each mail exchanger until we get
//        // a positive acceptance. It *MAY* be possible for one MX to allow
//        // a message [store and forwarder for example] and another [like
//        // the actual mail server] to reject it. This is why we REALLY ought
//        // to take the preference into account.
//        for (int mx = 0; mx < mxList.size(); mx++) {
//            boolean valid = false;
//            try {
//                int res;
//                Socket skt = new Socket((String) mxList.get(mx), 25);
//                BufferedReader rdr = new BufferedReader(new InputStreamReader(skt.getInputStream()));
//                BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));
//
////                res = hear(rdr);
////                if (res != 220) {
////                    throw new Exception("Invalid header");
////                }
//                say(wtr, "EHLO gmail.com");
//
//                res = hear(rdr);
//                if (res != 250) {
//                    throw new Exception("Not ESMTP");
//                }
//
//                // validate the sender address  
//                say(wtr, "MAIL FROM: <sonthepham@gmail.com>");
//                res = hear(rdr);
//                if (res != 250) {
//                    throw new Exception("Sender rejected");
//                }
//
//                say(wtr, "RCPT TO: <" + address + ">");
//                res = hear(rdr);
//
//                // be polite
//                say(wtr, "RSET");
//                hear(rdr);
//                say(wtr, "QUIT");
//                hear(rdr);
//                if (res != 250) {
//                    throw new Exception("Address is not valid!");
//                }
//
//                valid = true;
//                rdr.close();
//                wtr.close();
//                skt.close();
//            } catch (Exception ex) {
//                // Do nothing but try next host
//            } finally {
//                if (valid) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    public static void main(String args[]) {
        String testData[] = {
            //          "synovavietnam@yahoo.com", // Valid address
            //          "fail.me@nowhere.spam", // Invalid domain name
            //          "arkham@bigmeanogre.net", // Invalid address
            //          "laanhbang@gmail.com", // Failure of this method
            "alex@on.n.et"
        };
        for (int ctr = 0; ctr < testData.length; ctr++) {
            System.out.println(testData[ ctr] + " is valid? "
                    + isAddressValid(testData[ ctr]));
        }
        return;
    }
}
