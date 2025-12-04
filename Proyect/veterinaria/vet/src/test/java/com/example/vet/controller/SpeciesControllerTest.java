package com.example.vet.controller;

import com.example.vet.config.ModelMapperConfig;
import com.example.vet.dto.SpeciesRequestDTO;
import com.example.vet.model.Species;
import com.example.vet.service.SpeciesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SpeciesController.class)
@Import(ModelMapperConfig.class)
class SpeciesControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @MockBean private SpeciesService speciesService;

    private static final String BASE_URL = "/api/v1/species";

    // Helper to create a Species entity
    private Species createSpecies(Integer id, String name) {
        Species species = new Species();
        species.setIdSpecies(id);
        species.setSpeciesName(name);
        return species;
    }

    // Helper to create a valid request DTO
    private SpeciesRequestDTO createValidRequest() {
        SpeciesRequestDTO request = new SpeciesRequestDTO();
        request.setSpeciesName("Dog");
        return request;
    }

    @Nested
    @DisplayName("GET /api/v1/species")
    class GetAll {
        @Test @DisplayName("1. Should return 200 and a list of species")
        void returns200_withListOfSpecies() throws Exception {
            when(speciesService.findAllSpecies()).thenReturn(List.of(createSpecies(1, "Dog"), createSpecies(2, "Cat")));
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
        }

        @Test @DisplayName("2. Should return 200 and an empty list if no species exist")
        void returns200_withEmptyList() throws Exception {
            when(speciesService.findAllSpecies()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/species/{id}")
    class GetById {
        @Test @DisplayName("3. Should return 200 and the species if ID exists")
        void returns200_whenIdExists() throws Exception {
            when(speciesService.findSpeciesById(1)).thenReturn(Optional.of(createSpecies(1, "Dog")));
            mvc.perform(get(BASE_URL + "/{id}", 1)).andExpect(status().isOk()).andExpect(jsonPath("$.idSpecies").value(1));
        }

        @Test @DisplayName("4. Should return 404 if ID does not exist")
        void returns404_whenIdDoesNotExist() throws Exception {
            when(speciesService.findSpeciesById(99)).thenReturn(Optional.empty());
            mvc.perform(get(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }

        @Test @DisplayName("5. Should return 400 if ID is not a number")
        void returns400_whenIdIsNotNumeric() throws Exception {
            mvc.perform(get(BASE_URL + "/abc")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/species")
    class Create {
        @Test @DisplayName("6. Should return 201 if body is valid")
        void returns201_ifBodyIsValid() throws Exception {
            when(speciesService.saveSpecies(any(Species.class))).thenReturn(createSpecies(1, "Dog"));
            
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.idSpecies").value(1));
        }

        @Test @DisplayName("7. Should return 400 if species name is null")
        void returns400_ifSpeciesNameIsNull() throws Exception {
            SpeciesRequestDTO request = new SpeciesRequestDTO();
            request.setSpeciesName(null);
            
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }
        
        @Test @DisplayName("8. Should return 400 if species name is blank")
        void returns400_ifSpeciesNameIsBlank() throws Exception {
            SpeciesRequestDTO request = new SpeciesRequestDTO();
            request.setSpeciesName("   ");
            
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("PUT /api/v1/species/{id}")
    class Update {
        @Test @DisplayName("9. Should return 200 if update is successful")
        void returns200_ifUpdateIsSuccessful() throws Exception {
            when(speciesService.updateSpecies(eq(1), any(Species.class))).thenReturn(Optional.of(createSpecies(1, "Canine")));

            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.speciesName").value("Canine"));
        }

        @Test @DisplayName("10. Should return 404 if ID to update does not exist")
        void returns404_ifIdToUpdateDoesNotExist() throws Exception {
            when(speciesService.updateSpecies(eq(99), any(Species.class))).thenReturn(Optional.empty());
            
            mvc.perform(put(BASE_URL + "/{id}", 99).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isNotFound());
        }

        @Test @DisplayName("11. Should return 400 if body is invalid on update")
        void returns400_ifBodyIsInvalidOnUpdate() throws Exception {
            SpeciesRequestDTO request = new SpeciesRequestDTO();
            request.setSpeciesName(""); // Empty name is invalid
            
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("DELETE /api/v1/species/{id}")
    class Delete {
        @Test @DisplayName("12. Should return 204 if delete is successful")
        void returns204_ifDeleteIsSuccessful() throws Exception {
            when(speciesService.deleteSpeciesById(1)).thenReturn(true);
            mvc.perform(delete(BASE_URL + "/{id}", 1)).andExpect(status().isNoContent());
        }

        @Test @DisplayName("13. Should return 404 if ID to delete does not exist")
        void returns404_ifIdToDeleteDoesNotExist() throws Exception {
            when(speciesService.deleteSpeciesById(99)).thenReturn(false);
            mvc.perform(delete(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }
        
        @Test @DisplayName("14. Should return 400 if ID is not a number")
        void returns400_ifIdIsNotNumericOnDelete() throws Exception {
            mvc.perform(delete(BASE_URL + "/abc")).andExpect(status().isBadRequest());
        }
    }
    
    // NOTE: Species does not have a search-by-name endpoint, so those tests are omitted.
    
    @Nested
    @DisplayName("Protocol & Header Tests")
    class ProtocolTests {
        @Test @DisplayName("15. POST should return Location header")
        void post_returnsLocationHeader() throws Exception {
            when(speciesService.saveSpecies(any(Species.class))).thenReturn(createSpecies(55, "Test"));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(header().string("Location", containsString("/api/v1/species/55")));
        }

        @Test @DisplayName("16. GET should include CORS header")
        void get_includesCorsHeader() throws Exception {
            when(speciesService.findAllSpecies()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL).header("Origin", "http://example.com"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
        }

        @Test @DisplayName("17. Response should be Content-Type application/json")
        void response_isJson() throws Exception {
            when(speciesService.findAllSpecies()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }

        @Test @DisplayName("18. POST with wrong Content-Type should return 415")
        void post_withWrongContentType_returns415() throws Exception {
            mvc.perform(post(BASE_URL).contentType(MediaType.TEXT_PLAIN).content("test"))
                .andExpect(status().isUnsupportedMediaType());
        }

        @Test @DisplayName("19. POST with malformed JSON should return 400")
        void post_withMalformedJson_returns400() throws Exception {
             mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{\"speciesName\": \"Test\"")) // Missing closing brace
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("20. PUT with malformed JSON should return 400")
        void put_withMalformedJson_returns400() throws Exception {
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content("{\"speciesName\":, }")) // Invalid JSON
               .andExpect(status().isBadRequest());
       }

       @Test @DisplayName("21. DELETE should return 204 even if service returns a body (which it shouldn't)")
       void delete_ignoresBody() throws Exception {
           when(speciesService.deleteSpeciesById(1)).thenReturn(true);
           mvc.perform(delete(BASE_URL + "/{id}", 1))
               .andExpect(status().isNoContent())
               .andExpect(content().string(""));
       }
    }
}