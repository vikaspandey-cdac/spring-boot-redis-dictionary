package com.techgig.challenge.happiestmind.key;

import com.techgig.challenge.happiestmind.repository.DictionaryKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

public class DictionaryCustomKeyImpl implements DictionaryKeyRepository {
    protected static final String DELIMITER = ":";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String getPrefix(final String word){
        return word.substring(0, 1);
    }
    private String generateKey(final String firstLetter, int length){
        return generateKeyWithoutLength(firstLetter) + length;
    }

    private String generateKeyWithoutLength(final String firstLetter){
        return firstLetter + DELIMITER;
    }

    @Override
    public String create(String word, String identifier) {
        Assert.hasLength(word, "Word cannot be empty or null");

        String trimedWord =  word.trim();
        String firstLetter = getPrefix(trimedWord);
        String generatedKey = generateKey(firstLetter, trimedWord.length());

        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
                stringRedisConn.zAdd(generatedKey, 1, trimedWord + identifier);
                stringRedisConn.zAdd(generatedKey, 1, firstLetter);
                for (int index = 1; index < trimedWord.length(); index++) {
                    String sliceWord = trimedWord.substring(0, index - 1);
                    stringRedisConn.zAdd(generatedKey, 0, sliceWord);
                }
                return null;
            }
        });
        return generatedKey;
    }

    @Override
    public double incr(String word, String identifier) {
        return 0;
    }

    @Override
    public String getKey(String word) {
        String firstLetter = getPrefix(word);
        return generateKeyWithoutLength(firstLetter);
    }


}
