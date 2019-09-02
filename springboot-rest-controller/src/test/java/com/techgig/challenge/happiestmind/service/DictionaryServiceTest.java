package com.techgig.challenge.happiestmind.service;

import com.techgig.challenge.happiestmind.base.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class DictionaryServiceTest extends BaseTest {
    @Autowired
    DictionaryService dictionaryService;

    @Before
    public void before() throws IOException {
        // clear all data
        redisConnectionFactory.getConnection().flushAll();

        long startTime = System.currentTimeMillis();
        MockMultipartFile mockMultipartFile =  new MockMultipartFile("files", "filename.txt",
                "text/plain", "suffix to source class names".getBytes(StandardCharsets.UTF_8));
        dictionaryService.parseAndAddInDictionary(mockMultipartFile);

        long elapsed = System.currentTimeMillis() - startTime;
        Long totalCount = redisConnectionFactory.getConnection().dbSize(); // get saved key size
        logger.info("Add 20k words on redis. elapsed={}ms, totalCount={}", elapsed, totalCount);
    }
    @Test
    public void test_totalCount(){
        assertEquals(new Long(4), redisConnectionFactory.getConnection().dbSize());
    }

   @Test
    public void searchDictionary() {
        assertTrue(dictionaryService.searchDictionary("suffix"));
    }

    @Test
    public void getAllWordsInDictionary() {
        assertThat(dictionaryService.getAllWordsInDictionary(), containsInAnyOrder("suffix","to","source","class","names"));
    }
}