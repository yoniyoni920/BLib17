package services;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static Map<String, Object> mapOf(Object ...args) {
        Map<String, Object> map = new HashMap<>();

        if (args.length % 2 != 0) {
            throw new InvalidParameterException("Invalid number of arguments. You must give an even number of arguments.");
        }

        for (int i = 0; i < args.length; i += 2) {
            map.put((String)args[i], args[i+1]);
        }

        return map;
    }
}
