package com.techgig.challenge.happiestmind.services.impl;

import com.techgig.challenge.happiestmind.model.DictionaryData;
import com.techgig.challenge.happiestmind.repository.DictionaryKeyRepository;
import com.techgig.challenge.happiestmind.repository.DictionaryRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DictionaryRepositoryServiceImpl implements DictionaryRepository {
    private final double min = 0;
    private final double max = 0;
    private final int offset = 30;
    private StringRedisTemplate stringRedisTemplate;
    private DictionaryKeyRepository keyRepository;

    public DictionaryRepositoryServiceImpl(StringRedisTemplate stringRedisTemplate, DictionaryKeyRepository keyRepository) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.keyRepository = keyRepository;
    }

    @Override
    public List<DictionaryData> search(String word) {
        return retrieve(word, 1,1,word.length()+1);
    }

    @Override
    public List<String> retrieveAll() {
        Set<String> keys = stringRedisTemplate.keys("*");
        List<String> dictionaryDataList = new ArrayList<String>();
        keys.stream().forEach(key -> {
            Set<String> rangeResultsWithScore = stringRedisTemplate.opsForZSet().reverseRangeByScore(key,1,1);
            rangeResultsWithScore.stream().forEach(s -> {
                if (s.endsWith(DEFAULT_DELIMITER)) {
                    dictionaryDataList.add(s.replace(DEFAULT_DELIMITER, ""));
                }
            });
        });
        return dictionaryDataList;
    }

    @Override
    public List<DictionaryData> retrieve(String word) {
        return retrieve(word, min, max, offset);
    }

    @Override
    public List<DictionaryData> retrieve(String word, double min, double max, int offset) {
        Assert.hasLength(word, "Word cannot be empty or null");

        String trimedWord = word.trim();
        int trimedWordLength = trimedWord.length();

        String key = keyRepository.getKey(trimedWord);

        List<DictionaryData> dictionaryDataList = new ArrayList<>();
        for (int i = trimedWordLength; i < offset; i++) {
            if (dictionaryDataList.size() == offset) break;

            Set<ZSetOperations.TypedTuple<String>> rangeResultsWithScore = stringRedisTemplate
                    .opsForZSet()
                    .reverseRangeByScoreWithScores(key + i, min, max, 0, offset);
            if (rangeResultsWithScore.isEmpty()) continue;

            for (ZSetOperations.TypedTuple<String> typedTuple : rangeResultsWithScore) {
                if (dictionaryDataList.size() == offset) break;

                String value = typedTuple.getValue();
                int minLength = Math.min(value.length(), trimedWordLength);
                if (!value.endsWith(DEFAULT_DELIMITER) || !value.startsWith(trimedWord.substring(0, minLength))) continue;
                dictionaryDataList.add(new DictionaryData(value.replace(DEFAULT_DELIMITER, ""), typedTuple.getScore().intValue()));
            }
        }
        Collections.sort(dictionaryDataList);
        return dictionaryDataList;
    }

    @Override
    public void add(String word) {
        keyRepository.create(word, DEFAULT_DELIMITER);
    }

    @Override
    public void clear(String key) {

    }
}
