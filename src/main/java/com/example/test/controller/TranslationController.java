package com.example.test.controller;

import com.example.test.model.Translation;
import com.example.test.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping
    public ResponseEntity<Translation> createTranslation(@RequestBody Translation translation) {
        Translation createdTranslation = translationService.createTranslation(translation);
        return new ResponseEntity<Translation>(createdTranslation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Translation> updateTranslation(@PathVariable Integer id, @RequestBody Translation translation) {
        Translation updatedTranslation = translationService.updateTranslation(id, translation);
        return new ResponseEntity<Translation>(updatedTranslation, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Translation> getTranslation(@PathVariable Integer id) {
        Translation translation = translationService.getTranslation(id);
        return new ResponseEntity<Translation>(translation, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Translation>> searchTranslations(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String locale,
            @RequestParam(required = false) String tag) {
        List<Translation> translations = translationService.searchTranslations(key, locale, tag);
        return new ResponseEntity<List<Translation>>(translations, HttpStatus.OK);
    }

    @GetMapping("/export")
    public ResponseEntity<Map<String, String>> exportTranslations(@RequestParam String locale) {
        Map<String, String> translations = translationService.exportTranslations(locale);
        return new ResponseEntity<Map<String, String>>(translations, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}