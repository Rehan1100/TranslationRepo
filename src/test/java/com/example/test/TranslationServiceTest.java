package com.example.test;

import com.example.test.model.Translation;
import com.example.test.repo.TranslationRepository;
import com.example.test.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;

    @InjectMocks
    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTranslation() {
        Translation translation = new Translation();
        translation.setKey("greeting");
        translation.setLocale("en");
        translation.setContent("Hello");

        when(translationRepository.save(any(Translation.class))).thenReturn(translation);

        Translation createdTranslation = translationService.createTranslation(translation);
        assertNotNull(createdTranslation);
        assertEquals("greeting", createdTranslation.getKey());
        verify(translationRepository, times(1)).save(translation);
    }

    @Test
    void testUpdateTranslation() {
        Translation existingTranslation = new Translation();
        existingTranslation.setId(1);
        existingTranslation.setKey("greeting");
        existingTranslation.setLocale("en");
        existingTranslation.setContent("Hello");

        Translation updatedTranslation = new Translation();
        updatedTranslation.setContent("Hi");

        when(translationRepository.findById(1)).thenReturn(Optional.of(existingTranslation));
        when(translationRepository.save(any(Translation.class))).thenReturn(existingTranslation);

        Translation result = translationService.updateTranslation(1, updatedTranslation);
        assertNotNull(result);
        assertEquals("Hi", result.getContent());
        verify(translationRepository, times(1)).findById(1);
        verify(translationRepository, times(1)).save(existingTranslation);
    }

    @Test
    void testGetTranslation() {
        Translation translation = new Translation();
        translation.setId(1);
        translation.setKey("greeting");
        translation.setLocale("en");
        translation.setContent("Hello");

        when(translationRepository.findById(1)).thenReturn(Optional.of(translation));

        Translation result = translationService.getTranslation(1);
        assertNotNull(result);
        assertEquals("greeting", result.getKey());
        verify(translationRepository, times(1)).findById(1);
    }

    @Test
    void testSearchTranslations() {
        Translation translation1 = new Translation();
        translation1.setKey("greeting");
        translation1.setLocale("en");
        translation1.setContent("Hello");

        Translation translation2 = new Translation();
        translation2.setKey("farewell");
        translation2.setLocale("en");
        translation2.setContent("Goodbye");

        when(translationRepository.findByKeyAndLocale("greeting", "en")).thenReturn(Arrays.asList(translation1));
        when(translationRepository.findByTagsContaining("web")).thenReturn(Arrays.asList(translation1, translation2));
        when(translationRepository.findByLocale("en")).thenReturn(Arrays.asList(translation1, translation2));
        when(translationRepository.findAll()).thenReturn(Arrays.asList(translation1, translation2));

        List<Translation> result1 = translationService.searchTranslations("greeting", "en", null);
        assertEquals(1, result1.size());

        List<Translation> result2 = translationService.searchTranslations(null, null, "web");
        assertEquals(2, result2.size());

        List<Translation> result3 = translationService.searchTranslations(null, "en", null);
        assertEquals(2, result3.size());

        List<Translation> result4 = translationService.searchTranslations(null, null, null);
        assertEquals(2, result4.size());
    }
}
