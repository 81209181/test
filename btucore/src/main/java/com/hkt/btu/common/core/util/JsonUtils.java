package com.hkt.btu.common.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JsonUtils {

	private static final Logger LOG = LogManager.getLogger(JsonUtils.class);

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static Gson gson;

	public static ObjectMapper getMapperFormatLocalDateTime2String() {
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
		objectMapper.registerModule(javaTimeModule);
		return objectMapper;
	}

	/**
	 * Object convert to json string
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> String obj2String(T obj){
		try {
			// using gson here with purpose for easier debugging (better exception for stack trace)
			if (gson == null) {
				gson = getGson();
			}

			return obj instanceof String ? (String) obj : gson.toJson(obj);
		} catch (ProcessingException | WebApplicationException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * String convert to object
	 * @param str json string
	 * @param type Conversion object reference type
	 * @param <T>
	 * @return
	 */
	public static <T> T string2Obj(String str, Type type){
		try {
			// using gson here with purpose for easier debugging (better exception for stack trace)
			if (gson == null) {
				gson = getGson();
			}

			return gson.fromJson(str, type);
		} catch (ProcessingException | WebApplicationException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	private static Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, jsonDeserializationContext) ->
				json == null ? null : new Date(json.getAsLong()));
		builder.registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
				ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime());
		builder.registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
				ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDate());
		return builder.create();
	}
}
