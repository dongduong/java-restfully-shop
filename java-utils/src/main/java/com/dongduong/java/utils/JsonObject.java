/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TimeZone;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author dongduong
 *
 */
public class JsonObject implements Serializable {
    
    public static void main(String[] args) {

        // create json
        ArrayList<JsonObject> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JsonObject data = JsonObject.Object(
                    "data" + i, JsonObject.String("data" + i),
                    "integer" + i, JsonObject.Integer(i)
            );
            arrayList.add(data);
        }
        
        JsonObject json = JsonObject.Object(
                "stringkey", JsonObject.String("data"),
                "integerkey", JsonObject.Integer(100),
                "doublekey", JsonObject.Double(new BigDecimal("10.55")),
                "longkey", JsonObject.Integer(new BigInteger("10000000")),
                "booleankey", JsonObject.Boolean(true),
                "arraykey", JsonObject.Array(arrayList)
        );
        System.out.println("=====================create json=====================");
        System.out.println(json.toString());

        // parse json
        String jsonString = json.toString();
        JsonObject jsonObject = JsonObject.ObjectFromString(jsonString);
        String stringkey = jsonObject.objectForPath("stringkey").stringValue();
        int integerkey = jsonObject.objectForPath("integerkey").integerValue().intValue();
        double doublekey = jsonObject.objectForPath("doublekey").doubleValue().doubleValue();
        long longkey = jsonObject.objectForPath("longkey").integerValue().longValue();
        boolean booleankey = jsonObject.objectForPath("booleankey").booleanValue();
        JsonObject arrayListkey = jsonObject.objectForPath("arraykey");
        
        System.out.println("=====================parse data=====================");
        System.out.println("stringkey = " + stringkey);
        System.out.println("integerkey = " + integerkey);
        System.out.println("doublekey = " + doublekey);
        System.out.println("longkey = " + longkey);
        System.out.println("booleankey = " + booleankey);
        System.out.println("arrayListkey = " + arrayListkey);
        // parse array
        for (int i = 0; i < arrayListkey.objectCount(); i++) {
            JsonObject jsondata = arrayListkey.objectAtIndex(i);
            String datakey = "data"+i;
            String integer = "integer"+i;
            System.out.println(datakey + " = " + jsondata.objectForPath(datakey).stringValue());
            System.out.println(integer + " = " + jsondata.objectForPath(integer).integerValue().intValue());
        }
    }
    static private final long serialVersionUID = 1L;
    
    static private final byte TAG_JSON_NULL = 0x00;
    static private final byte TAG_JSON_BOOLEAN = 0x01;
    static private final byte TAG_JSON_DOUBLE = 0x02;
    static private final byte TAG_JSON_INTEGER = 0x03;
    static private final byte TAG_JSON_STRING = 0x04;
    // Structure types
    static private final byte TAG_JSON_ARRAY = 0x05;
    static private final byte TAG_JSON_OBJECT = 0x06;

    // Class's static methods
    static public JsonObject Null() {
        return new JsonObject(TAG_JSON_NULL);
    }
    
    static public JsonObject Boolean() {
        return JsonObject.Boolean(false);
    }
    
    static public JsonObject Boolean(boolean value) {
        JsonObject o = new JsonObject(TAG_JSON_BOOLEAN);
        o.setBoolean(value);
        return o;
    }
    
    static public JsonObject Boolean(Boolean value) {
        JsonObject o = new JsonObject(TAG_JSON_BOOLEAN);
        if (value != null) {
            o.setBoolean(value);
        } else {
            o.setBoolean(Boolean.FALSE);
        }
        return o;
    }
    
    static public JsonObject Double() {
        JsonObject o = new JsonObject(TAG_JSON_DOUBLE);
        o.setDouble(new BigDecimal(0));
        return o;
    }
    
    static public JsonObject Double(float value) {
        JsonObject o = new JsonObject(TAG_JSON_DOUBLE);
        o.setDouble(new BigDecimal(value));
        return o;
    }
    
    static public JsonObject Double(double value) {
        JsonObject o = new JsonObject(TAG_JSON_DOUBLE);
        o.setDouble(new BigDecimal(value));
        return o;
    }
    
    static public JsonObject Double(BigDecimal value) {
        JsonObject o = new JsonObject(TAG_JSON_DOUBLE);
        if (value != null) {
            o.setDouble(value);
        } else {
            o.setDouble(new BigDecimal(0));
        }
        return o;
    }
    
    static public JsonObject Integer() {
        JsonObject o = new JsonObject(TAG_JSON_INTEGER);
        o.setInteger(new BigInteger("0"));
        return o;
    }
    
    static public JsonObject Integer(short value) {
        JsonObject o = new JsonObject(TAG_JSON_INTEGER);
        o.setInteger(new BigInteger("" + value));
        return o;
    }
    
    static public JsonObject Integer(int value) {
        JsonObject o = new JsonObject(TAG_JSON_INTEGER);
        o.setInteger(new BigInteger("" + value));
        return o;
    }
    
    static public JsonObject Integer(long value) {
        JsonObject o = new JsonObject(TAG_JSON_INTEGER);
        o.setInteger(new BigInteger("" + value));
        return o;
    }
    
    static public JsonObject Integer(BigInteger value) {
        JsonObject o = new JsonObject(TAG_JSON_INTEGER);
        if (value != null) {
            o.setInteger(value);
        } else {
            o.setInteger(new BigInteger("0"));
        }
        return o;
    }
    
    static public JsonObject String() {
        JsonObject o = new JsonObject(TAG_JSON_STRING);
        o.setString("");
        return o;
    }
    
    static public JsonObject String(String input) {
        JsonObject o = new JsonObject(TAG_JSON_STRING);
        if ((input != null) && (input.length() > 0)) {
            o.setString(input);
        } else {
            o.setString("");
        }
        return o;
    }
    
    static public JsonObject StringEncoded(String input) {
        JsonObject o = new JsonObject(TAG_JSON_STRING);
        if ((null != input) && (0 < input.length())) {
            input = JsonData.dataFromString(input).base64EncodedString();
            o.setString(input);
        } else {
            o.setString("");
        }
        return o;
    }
    
    static public String StringDecoded(String input) {
        if ((null != input) && (0 < input.length())) {
            input = JsonData.dataFromString(input).base64DecodedString();
            return input;
        }
        return "";
    }
    
    static public JsonObject Array() {
        JsonObject o = new JsonObject(TAG_JSON_ARRAY);
        return o;
    }
    
    static public JsonObject Array(JsonObject... objects) {
        /* Condition validation */
        if ((objects == null) || (objects.length == 0)) {
            return JsonObject.Array();
        }
        
        ArrayList<JsonObject> a = new ArrayList<>();
        for (JsonObject json : objects) {
            if (json == null) {
                continue;
            }
            a.add(json);
        }
        return JsonObject.Array(a);
    }
    
    static public JsonObject Array(ArrayList<JsonObject> objects) {
        JsonObject o = new JsonObject(TAG_JSON_ARRAY);
        
        if ((objects != null) && (objects.size() > 0)) {
            for (JsonObject json : objects) {
                if ((json == null) || (json._tag == TAG_JSON_NULL)) {
                    continue;
                }
                o._array.add(json);
            }
        }
        return o;
    }
    
    static public JsonObject Object() {
        JsonObject o = new JsonObject(TAG_JSON_OBJECT);
        return o;
    }
    
    static public JsonObject Object(Object... objects) {
        JsonObject o = new JsonObject(TAG_JSON_OBJECT);
        if ((objects != null) && (objects.length > 0)) {
            o.addKeysAndObjects(objects);
        }
        return o;
    }
    
    static public JsonObject Object(String key, JsonObject value) {
        if ((key == null) || (key.length() == 0)) {
            return JsonObject.Object();
        }
        
        JsonObject o = JsonObject.Object();
        o.addKeysAndObjects(key, value);
        return o;
    }
    
    static public JsonObject Object(LinkedHashMap<String, JsonObject> objects) {
        JsonObject o = new JsonObject(TAG_JSON_OBJECT);
        if (objects != null) {
            o._objects = objects;
        }
        return o;
    }
    
    static public JsonObject ObjectFromData(JsonData jsonData) {
        return JsonObject.ObjectFromString(jsonData.stringValue());
    }
    
    static public JsonObject ObjectFromString(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.enable(Feature.USE_BIG_DECIMAL_FOR_FLOATS, Feature.USE_BIG_INTEGER_FOR_INTS);
            mapper.enableDefaultTyping();
            
            
            Object temp = mapper.readValue(jsonString, Object.class);
            JsonObject j = JsonObject._toObject(temp);
            return j;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return JsonObject.Null();
    }
    
    static private JsonObject _toObject(Object object) {
        /* Condition validation */
        if (object == null) {
            return JsonObject.Null();
        }
        
        if (object instanceof JsonObject) {
            return (JsonObject) object;
        } else if (object instanceof Boolean) {
            return JsonObject.Boolean((Boolean) object);
        } else if (object instanceof Double) {
            return JsonObject.Double(new BigDecimal(((Double) object).toString()));
        } else if (object instanceof BigDecimal) {
            return JsonObject.Double((BigDecimal) object);
        } else if (object instanceof BigInteger) {
            return JsonObject.Integer((BigInteger) object);
        } else if (object instanceof String) {
            return JsonObject.String((String) object);
        } else if (object instanceof URL) {
            return JsonObject.String(((URL) object).toString());
        } else if (object instanceof JsonData) {
            return JsonObject.String(((JsonData) object).base64EncodedString());
        } else if (object instanceof Date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return JsonObject.String(dateFormat.format((Date) object));
        } else if (object instanceof ArrayList) {
            ArrayList<Object> src = (ArrayList<Object>) object;
            
            ArrayList<JsonObject> a = new ArrayList<>();
            for (java.lang.Object src1 : src) {
                a.add(JsonObject._toObject(src1));
            }
            JsonObject j = JsonObject.Array();
            j._array = a;
            return j;
        } else if (object instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> src = (LinkedHashMap<String, Object>) object;
            
            LinkedHashMap<String, JsonObject> d = new LinkedHashMap<>();
            Set<String> keys = src.keySet();
            Iterator<String> i = keys.iterator();
            while (i.hasNext()) {
                String key = i.next();
                Object value = src.get(key);
                d.put(key, JsonObject._toObject(value));
            }
            JsonObject j = JsonObject.Object();
            j._objects = d;
            return j;
        } else {
            return JsonObject.Null();
        }
    }
    
    static private Object _toJSON(JsonObject object) {
        /* Condition validation */
        if (object == null) {
            return null;
        }
        
        switch (object._tag) {
            case TAG_JSON_BOOLEAN:
                return object.booleanValue();
            case TAG_JSON_DOUBLE:
                return object.doubleValue();
            case TAG_JSON_INTEGER:
                return object.integerValue();
            case TAG_JSON_STRING:
                return object.stringValue();
            case TAG_JSON_ARRAY: {
                ArrayList<Object> a = new ArrayList<>();
                for (JsonObject _array1 : object._array) {
                    Object o = JsonObject._toJSON(_array1);
                    if (o == null) {
                        continue;
                    }
                    a.add(o);
                }
                return a;
            }
            case TAG_JSON_OBJECT: {
                LinkedHashMap<String, Object> d = new LinkedHashMap<>();
                Set<String> keys = object._objects.keySet();
                Iterator<String> i = keys.iterator();
                while (i.hasNext()) {
                    String key = i.next();
                    JsonObject value = object._objects.get(key);
                    d.put(key, JsonObject._toJSON(value));
                }
                return d;
            }
            
            case TAG_JSON_NULL:
            default:
                return null;
        }
    }

    // Global variables
    private LinkedHashMap<String, JsonObject> _objects = null;
    private ArrayList<JsonObject> _array = null;
    private byte _tag = TAG_JSON_NULL;
    
    private Boolean _bValue = null;
    private BigDecimal _dValue = null;
    private BigInteger _iValue = null;
    private String _sValue = null;

    // Class's constructors
    public JsonObject(byte tag) {
        this._tag = tag;
        
        if (this._tag == TAG_JSON_ARRAY) {
            this._array = new ArrayList<>();
        } else if (this._tag == TAG_JSON_OBJECT) {
            this._objects = new LinkedHashMap<>();
        }
    }

    // Class's public methods
    public Boolean booleanValue() {
        if (this._bValue == null) {
            return Boolean.FALSE;
        } else {
            return this._bValue;
        }
    }
    
    public BigDecimal doubleValue() {
        if (this._dValue == null) {
            return new BigDecimal(0);
        } else {
            return this._dValue;
        }
    }
    
    public BigInteger integerValue() {
        if (this._iValue == null) {
            return new BigInteger("0");
        } else {
            return this._iValue;
        }
    }
    
    public String stringValue() {
        if (this._sValue == null) {
            return "";
        } else {
            return this._sValue;
        }
    }
    
    public void setBoolean(Boolean value) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_BOOLEAN) || (value == null)) {
            return;
        }
        this._bValue = value;
    }
    
    public void setDouble(BigDecimal value) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_DOUBLE) || (value == null)) {
            return;
        }
        this._dValue = value;
    }
    
    public void setInteger(BigInteger value) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_INTEGER) || (value == null)) {
            return;
        }
        this._iValue = value;
    }
    
    public void setString(String value) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_STRING) || (value == null)) {
            return;
        }
        this._sValue = value;
    }
    
    public void setObjects(Object... objects) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_ARRAY) || (objects == null) || (objects.length == 0)) {
            return;
        }
        
        this._objects.clear();
        this.addObjects(objects);
    }
    
    public void addObjects(Object... objects) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_ARRAY) || (objects == null) || (objects.length == 0)) {
            return;
        }
        
        for (java.lang.Object object : objects) {
            JsonObject json = JsonObject._toObject(object);
            if ((json == null) || (json._tag == TAG_JSON_NULL)) {
                continue;
            }
            this._array.add(json);
        }
    }
    
    public void removeObjects(JsonObject... objects) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_ARRAY) || (objects == null) || (objects.length == 0)) {
            return;
        }
        
        for (JsonObject object : objects) {
            if (this._array.contains(object)) {
                this._array.remove(object);
            }
        }
    }
    
    public void setKeysAndObjects(Object... objects) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_OBJECT) || (objects == null) || (objects.length == 0)) {
            return;
        }
        
        this._objects.clear();
        this.addKeysAndObjects(objects);
    }
    
    public void addKeysAndObjects(Object... objects) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_OBJECT) || (objects == null) || (objects.length == 0)) {
            return;
        }
        
        String k = null;
        boolean b = true;
        for (java.lang.Object object : objects) {
            // b = true -> get key for dictionary
            if (b) {
                if (object instanceof String) {
                    k = (String) object;
                } else {
                    k = object.toString();
                }
                b = false;
            } // b = false -> get value for dictionary
            else {
                this._objects.put(k, JsonObject._toObject(object));
                k = null;
                b = true;
            }
        }
        if (k != null) {
            this._objects.put(k, JsonObject.Null());
        }
    }
    
    public void removeObjectsForKeys(String... keys) {
        /* Condition validation */
        if ((this._tag != TAG_JSON_OBJECT) || (keys == null) || (keys.length == 0)) {
            return;
        }
        
        for (java.lang.String key : keys) {
            if ((key == null) || (key.length() == 0)) {
                continue;
            }
            if (this._objects.containsKey(key)) {
                this._objects.remove(key);
            }
        }
    }
    
    public boolean isLike(JsonObject object) {
        /* Condition validation */
        if (object == null) {
            return false;
        }
        
        boolean like = true;
        if (this._tag != object._tag) {
            like = false;
        } else {
            if (object.objectCount() > 0) {
//                if (this.objectCount() != object.objectCount()) {
//                    like = false;
//                } else {
                switch (object._tag) {
                    case TAG_JSON_ARRAY: {
                        for (int i = 0; i < this.objectCount(); i++) {
                            like &= object.objectAtIndex(i).isLike(this.objectAtIndex(i));
                            if (!like) {
                                break;
                            }
                        }
                        break;
                    }
                    
                    case TAG_JSON_OBJECT: {
                        LinkedHashMap<String, JsonObject> d = object._objects;
                        if (d == null) {
                            like = false;
                        } else {
                            Set<String> keys = d.keySet();
                            Iterator<String> i = keys.iterator();
                            while (i.hasNext()) {
                                String key = i.next();
                                JsonObject obj = d.get(key);
                                JsonObject o = this._objects.get(key);
                                
                                if ((o != null) && (obj != null)) {
                                    like &= o.isLike(obj);
                                } else {
                                    like &= ((o == null) && (obj == null));
                                }
                                
                                if (!like) {
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    
                    default:
                        break;
                }
//                }
            }
        }
        return like;
    }
    
    public JsonData encoded() {
        String json = this.jsonString();
        
        if (json == null) {
            return null;
        } else {
            return JsonData.dataFromString(json);
        }
    }
    
    public String jsonString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object o = JsonObject._toJSON(this);
            return mapper.writeValueAsString(o);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }
    
    public String base64Encoded() {
        JsonData data = this.encoded();
        
        if ((data != null) && (data.length() > 0)) {
            return data.base64EncodedString();
        } else {
            return "";
        }
    }
    
    public int objectCount() {
        switch (this._tag) {
            case TAG_JSON_ARRAY:
                return this._array.size();
            case TAG_JSON_OBJECT:
                return this._objects.size();
            default:
                return 0;
        }
    }
    
    public JsonObject objectAtIndex(int index) {
        /* Condition validation */
        if (index < 0) {
            return null;
        }
        
        switch (this._tag) {
            case TAG_JSON_ARRAY: {
                if (index < this._array.size()) {
                    return this._array.get(index);
                } else {
                    return null;
                }
            }
            
            case TAG_JSON_OBJECT: {
                if (index < this._objects.size()) {
                    Set<String> keys = this._objects.keySet();
                    String key = keys.toArray(new String[this._objects.size()])[index];
                    return this._objects.get(key);
                } else {
                    return null;
                }
            }
            
            default:
                return null;
        }
    }
    
    public JsonObject objectForPath(String path) {
        /* Condition validation */
        if ((this._objects == null) || (this._objects.size() == 0) || (path == null) || path.length() == 0) {
            return null;
        }
        
        String[] tokens = path.trim().split("\\/");
        JsonObject o = this;
        for (java.lang.String token : tokens) {
            if (o._tag == TAG_JSON_ARRAY) {
                if (token.trim().matches("^\\d+$")) {
                    int index = Integer.parseInt(token);
                    if ((0 <= index) && (index < o._array.size())) {
                        o = o._array.get(index);
                    } else {
                        o = null;
                    }
                } else {
                    o = null;
                }
            } else if (o._tag == TAG_JSON_OBJECT) {
                o = o._objects.get(token);
            } else {
                o = null;
            }
            if (o == null) {
                o = JsonObject.Null();
                break;
            }
        }
        return o;
    }
    
    @Override
    public String toString() {
        switch (this._tag) {
            case TAG_JSON_BOOLEAN:
                return this._bValue.toString();
            case TAG_JSON_DOUBLE:
                return this._dValue.toString();
            case TAG_JSON_INTEGER:
                return this._iValue.toString();
            case TAG_JSON_STRING:
                return this._sValue;
            case TAG_JSON_ARRAY:
            case TAG_JSON_OBJECT: {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Object o = JsonObject._toJSON(this);
                    return mapper.writeValueAsString(o);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                return "";
            }
            case TAG_JSON_NULL:
            default:
                return "null";
        }
    }
}
