package com.techgig.challenge.happiestmind.controller;
import com.techgig.challenge.happiestmind.model.DictionaryData;
import com.techgig.challenge.happiestmind.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value= "/dictionary")
public class RestAPIController {

    @Autowired
    DictionaryService dictionaryService;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            boolean status = dictionaryService.parseAndAddInDictionary(file);
            message = "You successfully uploaded " + file.getOriginalFilename() + " in dictionary.";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Boolean> getWord(@RequestParam(value = "search") String word) {
        return ResponseEntity.ok() .body(dictionaryService.searchDictionary(word));
    }

    @GetMapping("/words")
    @ResponseBody
    public ResponseEntity<List<String>> getAllWords() {
        return ResponseEntity.ok().body(dictionaryService.getAllWordsInDictionary());
    }
}
