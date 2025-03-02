package com.example.test;

import com.example.test.controller.TranslationController;
import com.example.test.model.Translation;
import com.example.test.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TranslationController.class)
public class TranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @Test
    @WithMockUser
    void testCreateTranslation_Authorized() throws Exception {
        Translation translation = new Translation();
        translation.setKey("greeting");
        translation.setLocale("en");
        translation.setContent("Hello");

        when(translationService.createTranslation(any(Translation.class))).thenReturn(translation);

        mockMvc.perform(post("/api/translations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\": \"greeting\", \"locale\": \"en\", \"content\": \"Hello\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.key").value("greeting"))
                .andExpect(jsonPath("$.locale").value("en"))
                .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    void testCreateTranslation_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/translations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\": \"greeting\", \"locale\": \"en\", \"content\": \"Hello\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testUpdateTranslation_Authorized() throws Exception {
        Translation translation = new Translation();
        translation.setId(1);
        translation.setKey("greeting");
        translation.setLocale("en");
        translation.setContent("Hi");

        when(translationService.updateTranslation(eq(1), any(Translation.class))).thenReturn(translation);

        mockMvc.perform(put("/api/translations/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\": \"greeting\", \"locale\": \"en\", \"content\": \"Hi\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("greeting"))
                .andExpect(jsonPath("$.locale").value("en"))
                .andExpect(jsonPath("$.content").value("Hi"));
    }

    @Test
    void testUpdateTranslation_Unauthorized() throws Exception {
        mockMvc.perform(put("/api/translations/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\": \"greeting\", \"locale\": \"en\", \"content\": \"Hi\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetTranslation_Authorized() throws Exception {
        Translation translation = new Translation();
        translation.setId(1);
        translation.setKey("greeting");
        translation.setLocale("en");
        translation.setContent("Hello");

        when(translationService.getTranslation(1)).thenReturn(translation);

        mockMvc.perform(get("/api/translations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("greeting"))
                .andExpect(jsonPath("$.locale").value("en"))
                .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    void testGetTranslation_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/translations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testSearchTranslations_Authorized() throws Exception {
        Translation translation1 = new Translation();
        translation1.setKey("greeting");
        translation1.setLocale("en");
        translation1.setContent("Hello");

        Translation translation2 = new Translation();
        translation2.setKey("farewell");
        translation2.setLocale("en");
        translation2.setContent("Goodbye");

        List<Translation> translations = Arrays.asList(translation1, translation2);

        when(translationService.searchTranslations(null, "en", null)).thenReturn(translations);

        mockMvc.perform(get("/api/translations/search")
                        .param("locale", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key").value("greeting"))
                .andExpect(jsonPath("$[1].key").value("farewell"));
    }

    @Test
    void testSearchTranslations_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/translations/search")
                        .param("locale", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testExportTranslations_Authorized() throws Exception {
        Map<String, String> translations = new HashMap<>();
        translations.put("greeting", "Hello");
        translations.put("farewell", "Goodbye");

        when(translationService.exportTranslations("en")).thenReturn(translations);

        mockMvc.perform(get("/api/translations/export")
                        .param("locale", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greeting").value("Hello"))
                .andExpect(jsonPath("$.farewell").value("Goodbye"));
    }

    @Test
    void testExportTranslations_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/translations/export")
                        .param("locale", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testHandleException() throws Exception {
        when(translationService.getTranslation(1)).thenThrow(new RuntimeException("Translation not found"));

        mockMvc.perform(get("/api/translations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Translation not found"));
    }
}
