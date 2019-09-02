package com.techgig.challenge.happiestmind.repository;

import com.techgig.challenge.happiestmind.model.DictionaryData;

import java.util.List;

public interface DictionaryRepository {
    String DEFAULT_DELIMITER = "§";
    List<DictionaryData> search(final String word);

    List<String> retrieveAll();

    List<DictionaryData> retrieve(final String word);

    List<DictionaryData> retrieve(final String word, final double min, final double max, final int offset);

    void add(final String word);

    void clear(final String key);
}
