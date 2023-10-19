package com.xw.controller;

import com.xw.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/7/21
 */
@ApiIgnore  // 忽略此 控制类
@RestController
@RequestMapping("/redis")
public class RedisController {

    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public String set(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
        redisOperator.set(key, value);
        return "OK!";
    }

    @GetMapping("/get")
    public String get(String key) {
//        return redisTemplate.opsForValue().get(key);
        return redisOperator.get(key);
    }

    /**
     * 大量key查询
     * @param keys
     * @return
     */
    @GetMapping("/getALot")
    public Object getALot(String... keys) {
        List<String> result = new ArrayList<>();
        for (String key : keys) {
            result.add(redisOperator.get(key));
        }
        return result;
    }

    /**
     * 批量查询 mget
     * @param keys
     * @return
     */
    @GetMapping("/mGet")
    public Object  mGet(String... keys) {
        List<String> keysList = Arrays.asList(keys);
        return  redisOperator.mGet(keysList);
    }

    /**
     * 批量查询 管道 pipeline
     * @param keys
     * @return
     */
    @GetMapping("/batchGet")
    public Object  batchGet(String... keys) {
        List<String> keysList = Arrays.asList(keys);
        return redisOperator.batchGet(keysList);
    }

    @GetMapping("/delete")
    public String delete(String key) {
//        redisTemplate.delete(key);
        redisOperator.del(key);
        return "OK!";
    }

    @RequestMapping("/redisLock")
    public String distributeLock() {
        System.out.println("进入方法...");
        /**
         * 1、对加锁释放锁进行封装
         */
//        String key = "redisKey";
//        String value = UUID.randomUUID().toString();

//        RedisCallback<Boolean> redisCallback1 = new RedisCallback() {
//            @Override
//            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                // 序列化 key
//                byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
//                // 序列化 value
//                byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
//                // 设置过期时间
//                Expiration expiration = Expiration.seconds(30);
//                // 设置 NX
//                RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
//                // 执行setNX
//                Boolean result = redisConnection.set(redisKey, redisValue, expiration, setOption);
//                return result;
//            }
//        };
//        Boolean lock = (Boolean) redisTemplate.execute(redisCallback1);
        RedisLock redisLock = new RedisLock(redisTemplate, "redisKey", 30);
        Boolean lock = redisLock.getLock();
        if (lock) {
            System.out.println("拿到锁🔒");
            try {
                Thread.sleep(15000);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                /**
                 * 1、对加锁释放锁进行封装
                 */
//                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
//                        "        return redis.call(\"del\",KEYS[1])\n" +
//                        "    else\n" +
//                        "        return 0\n" +
//                        "    end";
//                RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
//                List<String> keys = Collections.singletonList(key);
//                Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
                /**
                 * 2、JDK 1.7推出的新特性：自动释放锁 AutoCloseable
                 */
//                boolean result = redisLock.unLock();
//                System.out.println("释放锁的结果：" + result);
            }
        }
        System.out.println("释放锁的结果");
        return "释放锁的结果";
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendSms() {
        try (RedisLock redisLock = new RedisLock(redisTemplate, "redisKey", 30);) {
            if (redisLock.getLock()) {
                System.out.println("向178xxxxxxxx发啥短信！ ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装 redis 锁🔒
     * JDK 1.7推出的新特性：自动释放锁 AutoCloseable
     */
    public static class RedisLock implements AutoCloseable {
        private RedisTemplate redisTemplate;
        private String key;
        private String value;
        // 单位：秒
        private Integer expireTime;

        public RedisLock(RedisTemplate redisTemplate, String key, Integer expireTime) {
            this.redisTemplate = redisTemplate;
            this.key = key;
            this.expireTime = expireTime;
            this.value = UUID.randomUUID().toString();
        }

        /**
         * 获取 redis 分布式锁🔒
         * @return
         */
        public boolean getLock() {
            RedisCallback<Boolean> redisCallback1 = redisConnection -> {
                // 序列化 key
                byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
                // 序列化 value
                byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
                // 设置过期时间
                Expiration expiration = Expiration.seconds(expireTime);
                // 设置 NX
                RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
                // 执行setNX
                Boolean result = redisConnection.set(redisKey, redisValue, expiration, setOption);
                return result;
            };

            Boolean lock = (Boolean) redisTemplate.execute(redisCallback1);
            return lock;
        }

        public boolean unLock() {
            String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                    "        return redis.call(\"del\",KEYS[1])\n" +
                    "    else\n" +
                    "        return 0\n" +
                    "    end";
            RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
            List<String> keys = Collections.singletonList(key);
            Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
            System.out.println("释放锁的结果：" + result);
            return result;
        }

        @Override
        public void close() throws Exception {
            unLock();
        }
    }
}
