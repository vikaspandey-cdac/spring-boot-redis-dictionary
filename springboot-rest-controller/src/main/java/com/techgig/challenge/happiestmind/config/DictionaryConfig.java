package com.techgig.challenge.happiestmind.config;

import com.techgig.challenge.happiestmind.key.DictionaryCustomKeyImpl;
import com.techgig.challenge.happiestmind.repository.DictionaryKeyRepository;
import com.techgig.challenge.happiestmind.repository.DictionaryRepository;
import com.techgig.challenge.happiestmind.services.impl.DictionaryRepositoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class DictionaryConfig {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean(name = {"dictionaryKeyRepository", "keyRepository"})
    public DictionaryKeyRepository keyRepository() {
        // auto-complete custom key implements for test
        DictionaryKeyRepository keyRepository = new DictionaryCustomKeyImpl();
        return keyRepository;
    }

    @Bean(name = {"DictionaryRepository"})
    public DictionaryRepository DictionaryRepository(DictionaryKeyRepository DictionaryKeyRepository) {
        DictionaryRepository dictionaryRepository = new DictionaryRepositoryServiceImpl(stringRedisTemplate, DictionaryKeyRepository);
        return dictionaryRepository;
    }
}
