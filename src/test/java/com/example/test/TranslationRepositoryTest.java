package com.example.test;

import com.example.test.model.Translation;
import com.example.test.repo.TranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TranslationRepositoryTest {

    @Autowired
    private TranslationRepository translationRepository;

    @BeforeEach
    void setUp() {
        translationRepository.deleteAll();
    }

    @Test
    void testFindByKeyAndLocale() {
        Translation translation1 = new Translation();
        translation1.setKey("greeting");
        translation1.setLocale("en");
        translation1.setContent("Hello");

        Translation translation2 = new Translation();
        translation2.setKey("greeting");
        translation2.setLocale("fr");
        translation2.setContent("Bonjour");

        translationRepository.saveAll(Arrays.asList(translation1, translation2));

        List<Translation> result = translationRepository.findByKeyAndLocale("greeting", "en");
        assertEquals(1, result.size());
        assertEquals("Hello", result.get(0).getContent());
    }

    @Test
    void testFindByTagsContaining() {
        Translation translation1 = new Translation();
        translation1.setKey("greeting");
        translation1.setLocale("en");
        translation1.setContent("Hello");
        translation1.setTags(Arrays.asList("web", "mobile"));

        Translation translation2 = new Translation();
        translation2.setKey("farewell");
        translation2.setLocale("en");
        translation2.setContent("Goodbye");
        translation2.setTags(Arrays.asList("web"));

        translationRepository.saveAll(Arrays.asList(translation1, translation2));

        List<Translation> result = translationRepository.findByTagsContaining("web");
        assertEquals(2, result.size());

        List<Translation> result2 = translationRepository.findByTagsContaining("mobile");
        assertEquals(1, result2.size());
        assertEquals("Hello", result2.get(0).getContent());
    }

    @Test
    void testFindByLocale() {
        Translation translation1 = new Translation();
        translation1.setKey("greeting");
        translation1.setLocale("en");
        translation1.setContent("Hello");

        Translation translation2 = new Translation();
        translation2.setKey("farewell");
        translation2.setLocale("en");
        translation2.setContent("Goodbye");

        Translation translation3 = new Translation();
        translation3.setKey("greeting");
        translation3.setLocale("fr");
        translation3.setContent("Bonjour");

        translationRepository.saveAll(Arrays.asList(translation1, translation2, translation3));

        List<Translation> result = translationRepository.findByLocale("en");
        assertEquals(2, result.size());

        List<Translation> result2 = translationRepository.findByLocale("fr");
        assertEquals(1, result2.size());
        assertEquals("Bonjour", result2.get(0).getContent());
    }
}