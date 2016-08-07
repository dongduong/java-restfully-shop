/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author dongduong
 */
@SuppressWarnings("serial")
public class JsonData implements Serializable, Comparable<JsonData> {

	
	static private final byte[] _Base64EncodingTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();
	static private final byte[] _Base64DecodingTable = {
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x13, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb1, (byte) 0x27, (byte) 0x05, (byte) 0x00,
		(byte) 0x24, (byte) 0xb8, (byte) 0x05, (byte) 0x00, (byte) 0xc7, (byte) 0xab, (byte) 0x00, (byte) 0x00,
		(byte) 0xcc, (byte) 0xb7, (byte) 0x05, (byte) 0x00, (byte) 0xd8, (byte) 0x0f, (byte) 0x05, (byte) 0x00,
		(byte) 0x6a, (byte) 0xb9, (byte) 0x00, (byte) 0x00, (byte) 0x36, (byte) 0xdb, (byte) 0xce, (byte) 0x01,
		(byte) 0xd8, (byte) 0x0f, (byte) 0x05, (byte) 0x3e, (byte) 0x22, (byte) 0xb3, (byte) 0x00, (byte) 0x3f,
		(byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x3a, (byte) 0x3b,
		(byte) 0x3c, (byte) 0x3d, (byte) 0x00, (byte) 0x00, (byte) 0x0e, (byte) 0xd0, (byte) 0xb6, (byte) 0x04,
		(byte) 0xd8, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06,
		(byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e,
		(byte) 0x0f, (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16,
		(byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x00, (byte) 0xa9, (byte) 0xca, (byte) 0x00, (byte) 0x00,
		(byte) 0x1c, (byte) 0x1a, (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e, (byte) 0x1f, (byte) 0x20,
		(byte) 0x21, (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27, (byte) 0x28,
		(byte) 0x29, (byte) 0x2a, (byte) 0x2b, (byte) 0x2c, (byte) 0x2d, (byte) 0x2e, (byte) 0x2f, (byte) 0x30,
		(byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x00, (byte) 0xd8, (byte) 0x0f, (byte) 0x05, (byte) 0x00};

	
	// Class's static constructors
	static public JsonData parseBase64String(String base64String) {
		/* Condition validation */
		if (base64String == null || base64String.length() == 0) return null;
		
		int    counter   = 0;
		String subString = base64String.trim();
		for (int i = 0; i < subString.length(); i++) {
			
			char c = subString.charAt(i);
			if (c == '\n' || c == '\r' || c == '\t' || c == ' ') {
				counter++;
			}
		}
		
		// Validate base64 length
		int length = subString.length() - counter;
		if ((length % 4) != 0) return null;
		
		// Parse base64String
		return (new JsonData(subString, length)).base64DecodedData();
	}
	static public JsonData dataFromString(String input) {
		try {
			byte[] bytes = input.getBytes("UTF-8");
			return new JsonData(bytes);
		}
		catch (UnsupportedEncodingException ex) {
		}
		return new JsonData();
	}
	
	// Global variables
	protected byte[] _bytes = null;
	protected boolean _isBase64 = false;
	
	
	// Class's constructors
	public JsonData() {
		_bytes = null;
		_isBase64 = false;
	}
	public JsonData(byte[] bytes) {
		if (bytes != null && bytes.length > 0) this._bytes = bytes;
	}
	public JsonData(int capacity) {
		if (capacity > 0) {
			this._bytes = new byte[capacity];
			for (int i = 0; i < capacity; i++) this._bytes[i] = (byte)0x00;
		}
	}
	public JsonData(JsonData data) {
		if (data != null && data.bytes() != null && data.bytes().length > 0) {
			this._isBase64 = data._isBase64;
			if (data.length() > 0) this._bytes = new byte[data.bytes().length];
			if (data.bytes() != null) System.arraycopy(data.bytes(), 0, this._bytes, 0, data._bytes.length);
		}
		else _bytes = null;
	}
	private JsonData(String base64String, int length) {
		_isBase64 = true;
		_bytes = new byte[length];
		
		int index = 0;
		for (int i = 0; i < base64String.length(); i++) {
			char c = base64String.charAt(i);
			
			if (c == '\n' || c == '\r' || c == '\t' || c == ' ') continue;
			_bytes[index] = (byte)c;
			index++;
		}
	}
	
	
	// Class's accessors
	public byte[] bytes() {
		return this._bytes;
	}
	public int length() {
		if (this._bytes != null) return this._bytes.length;
		else return 0;
	}
	
	
	// Class's public methods
	public JsonData base64DecodedData() {
		/* Condition validation */
		if (_bytes == null || _bytes.length <= 0) return null;
		
		if (_isBase64) {
			int iLength = this.length();
			byte[] input = this.bytes();
			if (iLength > 0) {
				if ((iLength % 4) == 0) {
					while (iLength > 0 && input[iLength - 1] == '=') {
						iLength--;
					}
					int oLength = iLength * 3 / 4;
					byte[] output = new byte[oLength];
					
					int iPtr = 0;
					int oPtr = 0;
					int i0, i1, i2, i3;
					while (iPtr < iLength) {
						i0 = input[iPtr++];
						i1 = input[iPtr++];
						i2 = (iPtr < iLength) ? input[iPtr++] : 'A';
						i3 = (iPtr < iLength) ? input[iPtr++] : 'A';
						output[oPtr++] = (byte)((_Base64DecodingTable[i0] << 2) | (_Base64DecodingTable[i1] >> 4));
						if (oPtr < oLength) output[oPtr++] = (byte)(((_Base64DecodingTable[i1] & 0xf) << 4) | (_Base64DecodingTable[i2] >> 2));
						if (oPtr < oLength) output[oPtr++] = (byte)(((_Base64DecodingTable[i2] & 0x3) << 6) | _Base64DecodingTable[i3]);
					}					
					return new JsonData(output);
				}
				else {
					// Invalid data length: must be multuple of 4!
					return null;
				}
			}
			else {
				// Nothing to encode...
				return null;
			}
		}
		else {
			return this;
		}
	}
	public JsonData base64EncodedData() {
		/* Condition validation */
		if (_bytes == null || _bytes.length <= 0) return null;
		
		if (!_isBase64) {
			int    iLength = _bytes.length;
			int    oLength = ((iLength + 2) / 3) * 4;
			byte[] output  = new byte[oLength];
			
			int i, j, n, idx;
			for (i = 0; i < iLength; i += 3) {
				n = 0;
				for (j = i; j < i + 3; j++) {
					n <<= 8;
					if (j < iLength) n |= (0xff & _bytes[j]);
				}
				
				idx = (i / 3) * 4;
				output[idx] = JsonData._Base64EncodingTable[(n >> 18) & 0x3f];
				output[idx + 1] = JsonData._Base64EncodingTable[(n >> 12) & 0x3f];
				output[idx + 2] = ((i + 1) < iLength ? _Base64EncodingTable[(n >> 6) & 0x3f] : (byte) '=');
				output[idx + 3] = ((i + 2) < iLength ? _Base64EncodingTable[n & 0x3f] : (byte) '=');
			}
			JsonData d = new JsonData(output);
			d._isBase64 = true;
			return d;
		}
		else {
			return this;
		}
	}
	public String base64DecodedString() {
		JsonData data = this.base64DecodedData();
		
		if (data == null) return "";
		else return data.stringValue();
	}
	public String base64EncodedString() {
		JsonData data = this.base64EncodedData();
		
		if (data == null) return "";
		else return data.stringValue();
	}
	public String stringValue() {
		/* Condition validation */
		if (_bytes == null || _bytes.length <= 0) return "";
		try { return new String(_bytes, "UTF-8"); } catch (UnsupportedEncodingException ex) { return ""; }
	}
	
	public void clean() {
		/* Condition validation */
		if (_bytes == null || _bytes.length == 0) return;
		for (int i = 0; i < _bytes.length; i++) _bytes[i] = (byte)0x00;
	}
	
	
	// Comparable's members
	public boolean equals(JsonData d) {
		return Arrays.equals(this._bytes, d.bytes());
	}
        @Override
	public int compareTo(JsonData d) {
		BigInteger a = new BigInteger(this._bytes);
		BigInteger b = new BigInteger(d.bytes());
		return a.compareTo(b);
	}

	
	@Override
	public String toString() {
		/* Condition validation */
		if (this._bytes == null || this._bytes.length == 0) return "";
		
		StringBuilder s = new StringBuilder("<");
		for (int i = 0; i < this._bytes.length; i++) {
			String text = Integer.toString((this._bytes[i] & 0xff), 16);
			s.append(String.format("%s%s", (text.length() == 1 ? "0" : ""), text));
			if (i > 0 && i < (this._bytes.length - 1) && ((i + 1) % 4) == 0) s.append(" ");
		}
		s.append(">");
		return s.toString();
	}
	
	/**
	 * PhuongTNM 
	 * Parser and Decoded String
	 * @param input String
	 * @return String 
	 */
	static public String parseBase64Decoded(String input){
		if(null == input || 0 == input.length()){ return ""; }
		try{
			return parseBase64String(input).base64DecodedString();
		}catch(Exception e){
			
		}
		return "";
	}
	
	/**
	 * PhuongTNM 
	 * Parser and Encode String
	 * @param input String
	 * @return String 
	 */
	static public String parseBase64Encoded(String input) {
		if(null == input || 0 == input.length()){ return ""; }
		try{
			return dataFromString(input).base64EncodedString();
		}catch(Exception e){
			
		}
		return "";
	}
}
