package com.example.test.service;

import com.example.test.model.Translation;
import com.example.test.repo.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TranslationService {
    @Autowired
    private TranslationRepository translationRepository;

    public Translation createTranslation(Translation translation) {
        return translationRepository.save(translation);
    }

    public Translation updateTranslation(Integer id, Translation updatedTranslation) {
        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translation not found"));
        translation.setContent(updatedTranslation.getContent());
        translation.setTags(updatedTranslation.getTags());
        return translationRepository.save(translation);
    }

    public Translation getTranslation(Integer id) {
        return translationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translation not found"));
    }

    public List<Translation> searchTranslations(String key, String locale, String tag) {
        if (key != null && locale != null) {
            return translationRepository.findByKeyAndLocale(key, locale);
        } else if (tag != null) {
            return translationRepository.findByTagsContaining(tag);
        } else if (locale != null) {
            return translationRepository.findByLocale(locale);
        }
        return translationRepository.findAll();
    }

    public Map<String, String> exportTranslations(String locale) {
        List<Translation> translations = translationRepository.findByLocale(locale);
        return translations.stream()
                .collect(Collectors.toMap(Translation::getKey, Translation::getContent));
    }
}