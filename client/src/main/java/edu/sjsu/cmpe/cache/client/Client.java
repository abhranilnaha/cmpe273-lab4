package edu.sjsu.cmpe.cache.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Client {

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
        
        Future<HttpResponse<JsonNode>> future1 = cache1.put(key, value);        
        Future<HttpResponse<JsonNode>> future2 = cache2.put(key, value);
        Future<HttpResponse<JsonNode>> future3 = cache3.put(key, value);
        
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        
        try {
        	future1.get();
        } catch (Exception e) {
        	//e.printStackTrace();
        } finally {
        	countDownLatch.countDown();
        }
        
        try {
        	future2.get();
        } catch (Exception e) {
        	//e.printStackTrace();
        } finally {
        	countDownLatch.countDown();
        }
        
        try {
        	future3.get();
        } catch (Exception e) {
        	//e.printStackTrace();
        } finally {
        	countDownLatch.countDown();
        }

        countDownLatch.await();
        
        if (DistributedCacheService.successCount.intValue() < 2) {	        	
        	cache1.delete(key);
        	cache2.delete(key);
        	cache3.delete(key);
        } else {
        	cache1.get(key);
        	cache2.get(key);
        	cache3.get(key);
        	Thread.sleep(1000);
        	System.out.println("Result from Server A is: " + cache1.getValue());
    	    System.out.println("Result from Server B is: " + cache2.getValue());
    	    System.out.println("Result from Server C is: " + cache3.getValue());
        }
        DistributedCacheService.successCount = new AtomicInteger();
    }
}
