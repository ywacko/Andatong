package test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.unidt.helper.impl.DBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheTest {

    public static void testpush() {

        ICache cache = DBFactory.createRedis();

        System.out.println(cache.get("dev01_hr_token"));
    }


    public static void main(String[] args) throws ExecutionException {
        Logger log = LoggerFactory.getLogger(CacheTest.class);
        LoadingCache<String,String> cacheBuilder = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(10, TimeUnit.MINUTES).build(
                new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        log.info("load");
                        return s + ": cache";
                    }
                }
        );


        cacheBuilder.apply("hello");
        log.info("apply ok");
        System.out.println(cacheBuilder.get("hello"));
        System.out.println(cacheBuilder.get("hello"));
        System.out.println(cacheBuilder.get("hello"));
        System.out.println(cacheBuilder.get("hello"));

    }
}
