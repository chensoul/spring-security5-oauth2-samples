package com.chensoul.oauth.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JacksonUtils {
	public static final ObjectMapper OBJECT_MAPPER = getDefaultObjectMapper();

	public static final ObjectMapper PRETTY_SORTED_JSON_MAPPER = getDefaultObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

	public static ObjectMapper ALLOW_UNQUOTED_FIELD_NAMES_MAPPER = getDefaultObjectMapper().configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), false).configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

	private static ObjectMapper getDefaultObjectMapper() {
		return new ObjectMapper().registerModule(new CustomJavaTimeModule()).configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(JsonInclude.Include.NON_NULL); // 忽略 null 值
	}

	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		try {
			return fromValue != null ? OBJECT_MAPPER.convertValue(fromValue, toValueType) : null;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("The given object value cannot be converted to " + toValueType + ": " + fromValue, e);
		}
	}

	public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
		try {
			return fromValue != null ? OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef) : null;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("The given object value cannot be converted to " + toValueTypeRef + ": " + fromValue, e);
		}
	}

	public static <T> T fromString(String string, Class<T> clazz) {
		try {
			return string != null ? OBJECT_MAPPER.readValue(string, clazz) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + string, e);
		}
	}

	public static <T> T fromString(String string, TypeReference<T> valueTypeRef) {
		try {
			return string != null ? OBJECT_MAPPER.readValue(string, valueTypeRef) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + string, e);
		}
	}

	public static <T> T fromString(String string, JavaType javaType) {
		try {
			return string != null ? OBJECT_MAPPER.readValue(string, javaType) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given String value cannot be transformed to Json object: " + string, e);
		}
	}

	public static <T> T fromString(String string, Class<T> clazz, boolean ignoreUnknownFields) {
		try {
			return string != null ? OBJECT_MAPPER.readValue(string, clazz) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + string, e);
		}
	}

	public static <T> T fromBytes(byte[] bytes, Class<T> clazz) {
		try {
			return bytes != null ? OBJECT_MAPPER.readValue(bytes, clazz) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + Arrays.toString(bytes), e);
		}
	}

	public static <T> T fromBytes(byte[] bytes, TypeReference<T> valueTypeRef) {
		try {
			return bytes != null ? OBJECT_MAPPER.readValue(bytes, valueTypeRef) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + Arrays.toString(bytes), e);
		}
	}

	public static JsonNode fromBytes(byte[] bytes) {
		try {
			return OBJECT_MAPPER.readTree(bytes);
		} catch (IOException e) {
			throw new IllegalArgumentException("The given byte[] value cannot be transformed to Json object: " + Arrays.toString(bytes), e);
		}
	}

	public static String toJson(Object value) {
		return toString(value);
	}

	public static String toString(Object value) {
		try {
			return value != null ? OBJECT_MAPPER.writeValueAsString(value) : null;
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("The given Json object value cannot be transformed to a String: " + value, e);
		}
	}

	public static byte[] toBytes(Object value) {
		try {
			return value != null ? OBJECT_MAPPER.writeValueAsBytes(value) : null;
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("The given Json object value cannot be transformed to a String: " + value, e);
		}
	}

	public static String toPrettyString(Object value) {
		if (value == null) {
			return null;
		}
		try {
			return PRETTY_SORTED_JSON_MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T treeToValue(JsonNode node, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.treeToValue(node, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException("Can't convert value: " + node.toString(), e);
		}
	}

	public static JsonNode toJsonNode(Object value) {
		return toJsonNode(toString(value), OBJECT_MAPPER);
	}

	public static JsonNode toJsonNode(String value) {
		return toJsonNode(value, OBJECT_MAPPER);
	}

	public static JsonNode toJsonNode(String value, ObjectMapper mapper) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			return mapper.readTree(value);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static JsonNode toJsonNode(File value) {
		try {
			return value != null ? OBJECT_MAPPER.readTree(value) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given File object value: " + value + " cannot be transformed to a JsonNode", e);
		}
	}

	public static ObjectNode newObjectNode() {
		return newObjectNode(OBJECT_MAPPER);
	}

	public static ObjectNode newObjectNode(ObjectMapper mapper) {
		return mapper.createObjectNode();
	}

	public static ArrayNode newArrayNode() {
		return newArrayNode(OBJECT_MAPPER);
	}

	public static ArrayNode newArrayNode(ObjectMapper mapper) {
		return mapper.createArrayNode();
	}

	public static <T> T clone(T value) {
		if (value == null) {
			return null;
		}
		@SuppressWarnings("unchecked") Class<T> valueClass = (Class<T>) value.getClass();
		return fromString(toString(value), valueClass);
	}

	public static <T> JsonNode valueToTree(T value) {
		if (value == null) {
			return null;
		}
		return OBJECT_MAPPER.valueToTree(value);
	}

	public static JsonNode getJsonField(String jsonAsString, String field) {
		JsonNode jsonNode = readTree(jsonAsString);
		if (null == jsonNode) return null;
		return jsonNode.get(field);
	}


	public static JsonNode readTree(Object value) {
		return readTree(toString(value), OBJECT_MAPPER);
	}

	public static JsonNode readTree(String value) {
		return readTree(value, OBJECT_MAPPER);
	}

	public static JsonNode readTree(Path file) {
		try {
			return OBJECT_MAPPER.readTree(Files.readAllBytes(file));
		} catch (IOException e) {
			throw new IllegalArgumentException("Can't read file: " + file, e);
		}
	}

	public static JsonNode readTree(File value) {
		try {
			return value != null ? OBJECT_MAPPER.readTree(value) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("The given File object value: " + value + " cannot be transformed to a JsonNode", e);
		}
	}

	public static JsonNode readTree(String value, ObjectMapper mapper) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			return mapper.readTree(value);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static <T> byte[] writeValueAsBytes(T value) {
		if (value == null) {
			return null;
		}
		try {
			return OBJECT_MAPPER.writeValueAsBytes(value);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("The given Json object value cannot be transformed to a String: " + value, e);
		}
	}

	public static <T> void writeValue(Writer writer, T value) {
		try {
			OBJECT_MAPPER.writeValue(writer, value);
		} catch (IOException e) {
			throw new IllegalArgumentException("The given writer value: " + writer + "cannot be wrote", e);
		}
	}

	public static <T> void writeValue(OutputStream out, T value) {
		try {
			OBJECT_MAPPER.writeValue(out, value);
		} catch (IOException e) {
			throw new IllegalArgumentException("The given writer value: " + out + "cannot be wrote", e);
		}
	}

	public static JsonNode getSafely(JsonNode node, String... path) {
		if (node == null) {
			return null;
		}
		for (String p : path) {
			if (!node.has(p)) {
				return null;
			} else {
				node = node.get(p);
			}
		}
		return node;
	}

	public static Map<String, String> toFlatMap(JsonNode node) {
		HashMap<String, String> map = new HashMap<>();
		toFlatMap(node, "", map);
		return map;
	}

	public static <T> T fromReader(Reader reader, Class<T> clazz) {
		try {
			return reader != null ? OBJECT_MAPPER.readValue(reader, clazz) : null;
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid request payload", e);
		}
	}

	public static JavaType constructCollectionType(Class collectionClass, Class<?> elementClass) {
		return OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}

	private static void toFlatMap(JsonNode node, String currentPath, Map<String, String> map) {
		if (node.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
			currentPath = currentPath.isEmpty() ? "" : currentPath + ".";
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> entry = fields.next();
				toFlatMap(entry.getValue(), currentPath + entry.getKey(), map);
			}
		} else if (node.isValueNode()) {
			map.put(currentPath, node.asText());
		}
	}
}
