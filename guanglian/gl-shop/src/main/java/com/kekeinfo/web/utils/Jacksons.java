package com.kekeinfo.web.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

public class Jacksons {
	//private static final Logger LOGGER = LoggerFactory.getLogger(Jacksons.class);
//	public static String serializeAllExcept(Object obj, String... filterFields) {
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.setSerializationConfig(
//					mapper.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL));
//
//			FilterProvider filters = new SimpleFilterProvider().addFilter(obj.getClass().getName(),
//					SimpleBeanPropertyFilter.serializeAllExcept(filterFields));
//			mapper.setFilters(filters);
//
//			mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
//				@Override
//				public Object findFilterId(AnnotatedClass ac) {
//					return ac.getName();
//				}
//			});
//
//			return mapper.writeValueAsString(obj);
//		} catch (Exception e) {
//			LOGGER.debug("format obj failure.obj=" + obj);
//			e.printStackTrace();
//			throw new RuntimeException("Json.format error:" + obj, e);
//		}
//	}
	
	private ObjectMapper objectMapper;  
	  
    public static Jacksons me() {  
        return new Jacksons();  
    }  
  
    private Jacksons() {  
        objectMapper = new ObjectMapper();  
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性  
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);  
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
//        objectMapper.setSerializationConfig(objectMapper.getSerializationConfig().withSerializationInclusion(
//                JsonSerialize.Inclusion.NON_NULL)); 

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));  
    }  
  
    public Jacksons filter(String filterName, String... properties) {  
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName,  
                SimpleBeanPropertyFilter.serializeAllExcept(properties));  
        objectMapper.setFilters(filterProvider);  
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() 
        { 
            @Override 
            public Object findFilterId(AnnotatedClass ac) 
            { 
                return ac.getName(); 
            } 
        }); 
        return this;  
    }  
  
    public Jacksons addMixInAnnotations(Class<?> target, Class<?> mixinSource) {  
        objectMapper.getSerializationConfig().addMixInAnnotations(target, mixinSource);  
        objectMapper.getDeserializationConfig().addMixInAnnotations(target, mixinSource);  
        return this;  
    }  
  
    public Jacksons setDateFormate(DateFormat dateFormat) {  
        objectMapper.setDateFormat(dateFormat);  
        return this;  
    }  
  
    public <T> T json2Obj(String json, Class<T> clazz) {  
        try {  
            return objectMapper.readValue(json, clazz);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析json错误");  
        }  
    }  
  
    public String readAsString(Object obj) {  
        try {  
            return objectMapper.writeValueAsString(obj);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析对象错误");  
        }  
    }  
  
    @SuppressWarnings("unchecked")  
    public List<Map<String, Object>> json2List(String json) {  
        try {  
            return objectMapper.readValue(json, List.class);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析json错误");  
        }  
    } 
}
