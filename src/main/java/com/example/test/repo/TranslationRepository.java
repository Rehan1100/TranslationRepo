package com.example.test.repo;

import com.example.test.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation, Integer> {
    List<Translation> findByKeyAndLocale(String key, String locale);
    List<Translation> findByTagsContaining(String tag);
    List<Translation> findByLocale(String locale);
}