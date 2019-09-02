package com.techgig.challenge.happiestmind.service;

import com.techgig.challenge.happiestmind.model.DictionaryData;
import com.techgig.challenge.happiestmind.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DictionaryService {
    @Autowired
    protected DictionaryRepository dictionaryRepository;
    public boolean parseAndAddInDictionary(MultipartFile file) {
        Stream<String> words;
        try {
            words = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines()
                    .map(line -> line.split("[\\s]+"))
                    .flatMap(Arrays::stream)
                    .distinct();
            words.forEach(word -> dictionaryRepository.add(word.replaceAll("[-+.^:,]","")));
            words.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public boolean searchDictionary(String word){
        List<DictionaryData> dictionaryDatas = dictionaryRepository.search(word);
        String firstWord = "";
        if(!dictionaryDatas.isEmpty()) {
            firstWord = dictionaryDatas.get(0).getValue();
            return firstWord.equalsIgnoreCase(word);
        }
        return false;
    }

    public List<String> getAllWordsInDictionary(){
        List<String> dictionaryDataList = dictionaryRepository.retrieveAll();
        return dictionaryDataList;
    }
}
