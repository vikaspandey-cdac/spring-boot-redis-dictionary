package com.techgig.challenge.happiestmind.repository;

public interface DictionaryKeyRepository {

	String create(String word, String identifier);

	double incr(String word, String identifier);

	String getKey(String firstLetter);

}
