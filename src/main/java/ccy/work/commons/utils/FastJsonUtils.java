package ccy.work.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具转换
 * Created by chenchunyu on 2016/11/3.
 */
public class FastJsonUtils {

    private static SerializeConfig mapping = new SerializeConfig();

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * javaBean、list、map convert to json string
     */
    public static String obj2json(Object obj) {
        // return JSON.toJSONString(obj,SerializerFeature.UseSingleQuotes);//使用单引号
        // return JSON.toJSONString(obj,true);//格式化数据，方便阅读
        // return JSON.toJSONString(obj,mapping,SerializerFeature.PrettyFormat,
        // SerializerFeature.UseSingleQuotes,SerializerFeature.SortField,
        // SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteNullListAsEmpty,
        // SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteNullListAsEmpty,
        // SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNonStringKeyAsString);
        return JSON.toJSONString(obj, mapping);
    }

    /**
     * json string convert to javaBean、map
     */
    public static <T> T json2obj(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr, clazz);
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        return JSON.parseArray(jsonArrayStr, clazz);
    }

    /**
     * json string convert to map
     */
    public static Map<String, Object> json2map(String jsonStr) {
        return json2obj(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
        Map<String, T> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, T>>() {
        });
//        for (Entry<String, T> entry : map.entrySet()) {
//            JSONObject obj = (JSONObject) entry.getValue();
//            map.put(entry.getKey(), JSONObject.toJavaObject(obj, clazz));
//        }
        return map;
    }
}
