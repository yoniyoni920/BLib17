package services;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
/*
 * Utility class for working with Maps.
 * This class provides a method to create a map from a variable number of arguments.
 */
public class MapUtils {
	 /**
     * Creates a map from a variable number of arguments.
     * The arguments should be in pairs, where each odd-indexed argument is a key and each even-indexed argument is the corresponding value.
     * 
     * @param args variable number of arguments representing key-value pairs
     * @return a Map containing the provided key-value pairs
     * @throws InvalidParameterException if the number of arguments is odd (invalid input)
     */
    public static Map<String, Object> mapOf(Object ...args) {
        Map<String, Object> map = new HashMap<>();
        // Check if the number of arguments is even (each key needs a corresponding value)
        if (args.length % 2 != 0) {
            throw new InvalidParameterException("Invalid number of arguments. You must give an even number of arguments.");
        }
        // Iterate through the arguments and populate the map with key-value pairs
        for (int i = 0; i < args.length; i += 2) {
            map.put((String)args[i], args[i+1]);// The odd-indexed arguments are the keys, and the even-indexed are the values
        }

        return map;// Return the populated map
    }
}
