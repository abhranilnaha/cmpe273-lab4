package edu.sjsu.cmpe.cache.client;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Distributed cache service
 * 
 */
public class DistributedCacheService implements CacheServiceInterface {
    private final String cacheServerUrl;
    private String value;
    public static AtomicInteger successCount = new AtomicInteger();

    public DistributedCacheService(String serverUrl) {
        this.cacheServerUrl = serverUrl;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */
    @Override
    public Future<HttpResponse<JsonNode>> get(long key) {
    	Future<HttpResponse<JsonNode>> future = Unirest
    			.get(this.cacheServerUrl + "/cache/{key}")
                .header("accept", "application/json")
                .header("Accept-Content-Encoding", "gzip")
                .routeParam("key", Long.toString(key))
                .asJsonAsync(new Callback<JsonNode>() {	
    	            public void failed(UnirestException e) {
    	                System.out.println("The fetch request has failed for " + getServerName());
    	            }
    	
    	            public void completed(HttpResponse<JsonNode> response) {
    	            	value = response.getBody().getObject().getString("value");
    	            	System.out.println("The fetch request has completed for " + getServerName());
    	            }
    	
    	            public void cancelled() {
    	                System.out.println("The fetch request has been cancelled for " + getServerName());
    	            }	
    	        });

        return future;
    }
    
    public String getValue() {
    	return this.value;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#put(long, java.lang.String)
     */
    @Override
    public Future<HttpResponse<JsonNode>> put(long key, String value) {
    	Future<HttpResponse<JsonNode>> future = Unirest
	        .put(this.cacheServerUrl + "/cache/{key}/{value}")
	        .header("accept", "application/json")
	        .routeParam("key", Long.toString(key))
	        .routeParam("value", value)
	        .asJsonAsync(new Callback<JsonNode>() {	
	            public void failed(UnirestException e) {	            	
	                System.out.println("The save request has failed for " + getServerName());
	            }
	
	            public void completed(HttpResponse<JsonNode> response) {
	            	successCount.incrementAndGet();
	            	System.out.println("The save request has completed for " + getServerName());
	            }
	
	            public void cancelled() {
	                System.out.println("The save request has been cancelled for " + getServerName());
	            }
	        });
    	
    	return future;   	
    }
    
    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#delete(long)
     */
    @Override
    public Future<HttpResponse<JsonNode>> delete(long key) {
    	Future<HttpResponse<JsonNode>> future = Unirest
    		.delete(this.cacheServerUrl + "/cache/{key}")
	        .header("accept", "application/json")
	        .routeParam("key", Long.toString(key))
	        .asJsonAsync(new Callback<JsonNode>() {	        	
	            public void failed(UnirestException e) {
	                System.out.println("The delete request has failed for " + getServerName());
	            }
	
	            public void completed(HttpResponse<JsonNode> response) {
	            	System.out.println("The delete request has completed for " + getServerName());
	            }
	
	            public void cancelled() {
	                System.out.println("The delete request has been cancelled for " + getServerName());
	            }	
	        });
        return future;
    }
    
    public String getServerName() {
    	if (this.cacheServerUrl.contains("3000")) {
    		return "Server_A";
    	} else if (this.cacheServerUrl.contains("3001")) {
    		return "Server_B";
    	} else if (this.cacheServerUrl.contains("3002")) {
    		return "Server_C";
    	}	
    	return null;
    }
}