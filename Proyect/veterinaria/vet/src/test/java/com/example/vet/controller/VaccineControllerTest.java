package com.example.vet.controller;

import com.example.vet.config.ModelMapperConfig;
import com.example.vet.dto.VaccineRequestDTO;
import com.example.vet.model.Pet;
import com.example.vet.model.Vaccine;
import com.example.vet.service.VaccineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VaccineController.class)
@Import(ModelMapperConfig.class)
class VaccineControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @MockBean private VaccineService vaccineService;

    private static final String BASE_URL = "/api/v1/vaccines";

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        reset(vaccineService);
    }

    // Helpers
    private Pet createPet(Integer id) {
        Pet pet = new Pet();
        pet.setIdPet(id);
        pet.setName("Fido");
        return pet;
    }

    private Vaccine createVaccine(Integer id, String name, Pet pet) {
        Vaccine vaccine = new Vaccine();
        vaccine.setIdVaccine(id);
        vaccine.setVaccineName(name);
        vaccine.setApplicationDate(LocalDate.now());
        vaccine.setPet(pet);
        return vaccine;
    }

    private VaccineRequestDTO createValidRequest() {
        VaccineRequestDTO req = new VaccineRequestDTO();
        req.setVaccineName("Rabies");
        req.setApplicationDate(LocalDate.of(2024, 10, 15));
        req.setIdPet(1);
        return req;
    }

    @Nested
    @DisplayName("GET /vaccines")
    class GetAll {
        @Test @DisplayName("1. Should return 200 and a list of vaccines")
        void returns200_withList() throws Exception {
            when(vaccineService.findAllVaccines()).thenReturn(List.of(createVaccine(1, "Rabies", createPet(1)), createVaccine(2, "Parvo", createPet(1))));
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
        }

        @Test @DisplayName("2. Should return 200 and an empty list")
        void returns200_withEmptyList() throws Exception {
            when(vaccineService.findAllVaccines()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /vaccines/{id}")
    class GetById {
        @Test @DisplayName("3. Should return 200 if ID exists")
        void returns200_ifIdExists() throws Exception {
            when(vaccineService.findVaccineById(1)).thenReturn(Optional.of(createVaccine(1, "Rabies", createPet(1))));
            mvc.perform(get(BASE_URL + "/{id}", 1)).andExpect(status().isOk()).andExpect(jsonPath("$.idVaccine").value(1));
        }

        @Test @DisplayName("4. Should return 404 if ID does not exist")
        void returns404_ifIdDoesNotExist() throws Exception {
            when(vaccineService.findVaccineById(99)).thenReturn(Optional.empty());
            mvc.perform(get(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /vaccines/pet/{petId}")
    class GetByPetId {
        @Test @DisplayName("5. Should return 200 and vaccines for a pet")
        void returns200_withVaccinesForPet() throws Exception {
            when(vaccineService.findVaccinesByPetId(1)).thenReturn(List.of(createVaccine(1, "Rabies", createPet(1))));
            mvc.perform(get(BASE_URL + "/pet/{petId}", 1)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        }

        @Test @DisplayName("6. Should return 200 and an empty list for a pet with no vaccines")
        void returns200_forPetWithNoVaccines() throws Exception {
            when(vaccineService.findVaccinesByPetId(2)).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL + "/pet/{petId}", 2)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }
        
        @Test @DisplayName("7. Should return 400 if pet ID is not a number")
        void returns400_ifPetIdIsInvalid() throws Exception {
            mvc.perform(get(BASE_URL + "/pet/abc")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /vaccines")
    class Create {
        @Test @DisplayName("8. Should return 201 with a valid body")
        void returns201_withValidBody() throws Exception {
            when(vaccineService.saveVaccine(any(VaccineRequestDTO.class))).thenReturn(createVaccine(1, "Rabies", createPet(1)));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.idVaccine").value(1));
        }

        @Test @DisplayName("9. Should return 400 if vaccineName is blank")
        void returns400_ifVaccineNameIsBlank() throws Exception {
            VaccineRequestDTO req = createValidRequest();
            req.setVaccineName(" ");
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
        
        @Test @DisplayName("10. Should return 400 if applicationDate is null")
        void returns400_ifApplicationDateIsNull() throws Exception {
            VaccineRequestDTO req = createValidRequest();
            req.setApplicationDate(null);
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("11. Should return 400 if idPet is null")
        void returns400_ifIdPetIsNull() throws Exception {
            VaccineRequestDTO req = createValidRequest();
            req.setIdPet(null);
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
       
    }

    @Nested
    @DisplayName("DELETE /vaccines/{id}")
    class Delete {
        @Test @DisplayName("13. Should return 204 if delete is successful")
        void returns204_ifDeleteIsSuccessful() throws Exception {
            when(vaccineService.deleteVaccineById(1)).thenReturn(true);
            mvc.perform(delete(BASE_URL + "/{id}", 1)).andExpect(status().isNoContent());
        }

        @Test @DisplayName("14. Should return 404 if ID to delete does not exist")
        void returns404_ifIdToDeleteDoesNotExist() throws Exception {
            when(vaccineService.deleteVaccineById(99)).thenReturn(false);
            mvc.perform(delete(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Protocol & Header Tests")
    class ProtocolTests {
        @Test @DisplayName("15. POST should return Location header")
        void post_returnsLocationHeader() throws Exception {
            when(vaccineService.saveVaccine(any(VaccineRequestDTO.class))).thenReturn(createVaccine(123, "Test", createPet(1)));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(header().string("Location", containsString("/api/v1/vaccines/123")));
        }
        
        @Test @DisplayName("16. GET should include CORS header")
        void get_includesCorsHeader() throws Exception {
            when(vaccineService.findAllVaccines()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL).header("Origin", "http://example.com"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
        }

        @Test @DisplayName("17. Response should be Content-Type application/json")
        void response_isJson() throws Exception {
            when(vaccineService.findAllVaccines()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }

        @Test @DisplayName("18. POST with malformed JSON should return 400")
        void post_withMalformedJson_returns400() throws Exception {
             mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{ \"vaccineName\": \"Test\""))
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("19. POST with wrong Content-Type should return 415")
        void post_withWrongContentType_returns415() throws Exception {
            mvc.perform(post(BASE_URL).contentType(MediaType.TEXT_PLAIN).content("test"))
                .andExpect(status().isUnsupportedMediaType());
        }

        @Test @DisplayName("20. DELETE with non-numeric ID should return 400")
        void delete_withNonNumericId_returns400() throws Exception {
            mvc.perform(delete(BASE_URL + "/abc")).andExpect(status().isBadRequest());
        }
    }
}
}