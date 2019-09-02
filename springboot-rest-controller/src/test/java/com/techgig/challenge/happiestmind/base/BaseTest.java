package com.techgig.challenge.happiestmind.base;

import com.techgig.challenge.happiestmind.DictionaryApplication;
import com.techgig.challenge.happiestmind.model.DictionaryData;
import com.techgig.challenge.happiestmind.repository.DictionaryRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DictionaryApplication.class)
@WebAppConfiguration
public class BaseTest {

	protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

	@Autowired
	protected RedisConnectionFactory redisConnectionFactory;

	@Autowired
	protected StringRedisTemplate stringRedisTemplate;

	@Autowired
	protected DictionaryRepository dictionaryRepository;

	@Autowired
	protected ResourceLoader resourceLoader;

	/*protected long retrieve() {
		String word = RandomStringUtils.randomAlphabetic(1);
		long startTime = System.currentTimeMillis();
		List<DictionaryData> dictionaryDataList = dictionaryRepository.retrieve(word);
		String firstWord = "";
		if(!dictionaryDataList.isEmpty()) firstWord = dictionaryDataList.get(0).getValue();

		long elapsed = System.currentTimeMillis() - startTime;
		logger.info("prefix={}, elapsed={}ms, size={}, firstWord={}", word, elapsed, dictionaryDataList.size(), firstWord);
		return elapsed;
	}*/

}
