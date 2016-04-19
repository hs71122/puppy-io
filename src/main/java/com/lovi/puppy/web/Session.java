package com.lovi.puppy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovi.puppy.exceptions.message.ErrorMessage;

public class Session{

	private static final Logger logger = LoggerFactory.getLogger(Session.class);
	
	private io.vertx.ext.web.Session session;
	
	public Session(io.vertx.ext.web.Session session) {
		this.session = session;
	}
	
	public <T> void put(String key,T object){
		ObjectMapper objectMapper = new ObjectMapper();
		Object o = null;
		try {
			o = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error(ErrorMessage.SESSION_OBJECT_UNABLE_TO_PARSE.getMessage() + key);
		}
		session.put(key, o);
	}
	
	public <T> T get(String key,Class<T> cls){
		ObjectMapper objectMapper = new ObjectMapper();
		T t = null;
		try{
			t = objectMapper.readValue(session.get(key).toString(), cls);
		}catch(Exception e){
			logger.error(ErrorMessage.SESSION_OBJECT_UNABLE_TO_PARSE.getMessage() + key);
		}
		
		return t;
	}
	
}