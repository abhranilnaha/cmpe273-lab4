package edu.sjsu.cmpe.cache.client;

import java.util.concurrent.Future;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

/**
 * Cache Service Interface
 * 
 */
public interface CacheServiceInterface {
    public Future<HttpResponse<JsonNode>> get(long key);
    
    public Future<HttpResponse<JsonNode>> delete(long key);

    public Future<HttpResponse<JsonNode>> put(long key, String value);
    
    public String getValue();
}
