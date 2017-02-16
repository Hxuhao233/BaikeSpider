package com.spider.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class JsonUtils {
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }
	
	private JsonUtils(){
		
	}
	
	public static String encode(Object object){
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T decode(String jsonContent,Class<T> valueType) {
		if(jsonContent != null){
			try {
				return objectMapper.readValue(jsonContent, valueType);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		return null;
	}
	
	
	// use it for your self-defined class or set class like HashMap
	@SuppressWarnings("unchecked")
	public static <T> T decode(String jsonContent,TypeReference<T> referenceType){
		try {
			return (T) objectMapper.readValue(jsonContent, referenceType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
}
