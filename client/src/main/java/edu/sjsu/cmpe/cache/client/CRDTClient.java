package edu.sjsu.cmpe.cache.client;

import java.util.HashMap;
import java.util.Map;

public class CRDTClient {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        CacheServiceInterface cache1 = new DistributedCacheService(
                "http://localhost:3000");
        CacheServiceInterface cache2 = new DistributedCacheService(
                "http://localhost:3001");
        CacheServiceInterface cache3 = new DistributedCacheService(
                "http://localhost:3002");
        
        long key = 1;
        String value = "a";
        
        cache1.put(key, value);
        cache2.put(key, value);
        cache3.put(key, value);
        
        System.out.println("Setting value a step");
        Thread.sleep(30000);
        
        cache1.get(1);
	    cache2.get(1);
	    cache3.get(1);
	        
	    System.out.println("Getting value a step");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from Server A is: " + cache1.getValue());
	    System.out.println("Result from Server B is: " + cache2.getValue());
	    System.out.println("Result from Server C is: " + cache3.getValue());
        
        value = "b";
        cache1.put(key, value);
        cache2.put(key, value);
        cache3.put(key, value);
        
        System.out.println("Setting value b step");
        Thread.sleep(30000);
	        
	    cache1.get(1);
	    cache2.get(1);
	    cache3.get(1);
	        
	    System.out.println("Getting value b step");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from Server A is: " + cache1.getValue());
	    System.out.println("Result from Server B is: " + cache2.getValue());
	    System.out.println("Result from Server C is: " + cache3.getValue());
	        
	    String[] values = {cache1.getValue(), cache2.getValue(), cache3.getValue()};
	    
	    Map<String, Integer> map = new HashMap<String, Integer>();
	    String majority = null;
	    for (String val : values) {
	        Integer count = map.get(val);	        
	        map.put(val, count != null ? count+1 : 1);
	        if (map.get(val) > values.length / 2) {
	        	majority = val;
	        	break;
	        }	
	    }
	    
	    cache1.put(key, majority);
        cache2.put(key, majority);
        cache3.put(key, majority);
        
        System.out.println("Correcting value b step");
	    Thread.sleep(1000);
	    
	    cache1.get(key);
        cache2.get(key);
        cache3.get(key);
        
        System.out.println("Getting again value b step");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from Server A is: " + cache1.getValue());
	    System.out.println("Result from Server B is: " + cache2.getValue());
	    System.out.println("Result from Server C is: " + cache3.getValue());
    }
}
