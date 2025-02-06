package kr.hhplus.be.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String KEY="keyword";


    @Test
    void redis연결_테스트() {

        redisTemplate.opsForValue().set("testKey", "Hello Redis");

        String value = redisTemplate.opsForValue().get("testKey");

        assertNotNull(value);
        assertEquals("Hello Redis", value);
    }
}