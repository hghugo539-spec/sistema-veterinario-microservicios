package com.example.vet.controller;

import com.example.vet.config.ModelMapperConfig;
import com.example.vet.dto.PetRequestDTO;
import com.example.vet.model.Pet;
import com.example.vet.service.PetService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class)
@Import(ModelMapperConfig.class)
class PetControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @MockBean private PetService petService;

    private static final String BASE_URL = "/api/v1/pets";

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        reset(petService);
    }

    // Helpers
    private Pet createPet(Integer id, String name) {
        Pet pet = new Pet();
        pet.setIdPet(id);
        pet.setName(name);
        return pet;
    }

    private PetRequestDTO createValidRequest() {
        PetRequestDTO req = new PetRequestDTO();
        req.setName("Fido");
        req.setBirthDate(LocalDate.of(2022, 1, 1));
        req.setIdClient(1);
        req.setIdSpecies(1);
        return req;
    }

    @Nested
    @DisplayName("GET /pets")
    class GetAll {
        @Test @DisplayName("1. Debe devolver 200 con una lista de mascotas")
        void returns200_withList() throws Exception {
            when(petService.findAllPets()).thenReturn(List.of(createPet(1, "Fido"), createPet(2, "Milo")));
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
        }

        @Test @DisplayName("2. Debe devolver 200 con una lista vacía")
        void returns200_withEmptyList() throws Exception {
            when(petService.findAllPets()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /pets/{id}")
    class GetById {
        @Test @DisplayName("3. Debe devolver 200 si el ID existe")
        void returns200_ifIdExists() throws Exception {
            when(petService.findPetById(1)).thenReturn(Optional.of(createPet(1, "Fido")));
            mvc.perform(get(BASE_URL + "/{id}", 1)).andExpect(status().isOk()).andExpect(jsonPath("$.idPet").value(1));
        }

        @Test @DisplayName("4. Debe devolver 404 si el ID no existe")
        void returns404_ifIdDoesNotExist() throws Exception {
            when(petService.findPetById(99)).thenReturn(Optional.empty());
            mvc.perform(get(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /pets/client/{clientId}")
    class GetByClientId {
        @Test @DisplayName("5. Debe devolver 200 y mascotas para un cliente")
        void returns200_withPetsForClient() throws Exception {
            when(petService.findPetsByClientId(1)).thenReturn(List.of(createPet(1, "Fido")));
            mvc.perform(get(BASE_URL + "/client/{clientId}", 1)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        }

        @Test @DisplayName("6. Debe devolver 200 y una lista vacía para un cliente sin mascotas")
        void returns200_forClientWithNoPets() throws Exception {
            when(petService.findPetsByClientId(2)).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL + "/client/{clientId}", 2)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("POST /pets")
    class Create {
        @Test @DisplayName("7. Debe devolver 201 con un body válido")
        void returns201_withValidBody() throws Exception {
            when(petService.savePet(any(PetRequestDTO.class))).thenReturn(createPet(1, "Fido"));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.idPet").value(1));
        }

        @Test @DisplayName("8. Debe devolver 400 si el nombre está en blanco")
        void returns400_ifNameIsBlank() throws Exception {
            PetRequestDTO req = createValidRequest();
            req.setName("");
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("9. Debe devolver 400 si la fecha de nacimiento es en el futuro")
        void returns400_ifBirthDateIsInFuture() throws Exception {
            PetRequestDTO req = createValidRequest();
            req.setBirthDate(LocalDate.now().plusDays(1));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("10. Debe devolver 400 si idClient es nulo")
        void returns400_ifIdClientIsNull() throws Exception {
            PetRequestDTO req = createValidRequest();
            req.setIdClient(null);
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("11. Debe devolver 400 si idSpecies es nulo")
        void returns400_ifIdSpeciesIsNull() throws Exception {
            PetRequestDTO req = createValidRequest();
            req.setIdSpecies(null);
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
        
        // --- ¡¡¡CORRECCIÓN IMPORTANTE AQUÍ!!! ---
        // Esta prueba debe esperar un 500, no fallar con una excepción
        @Test @DisplayName("12. Debe devolver 500 si el ID de cliente no existe")
        void returns500_ifClientIdIsNotFound() throws Exception {
            when(petService.savePet(any(PetRequestDTO.class))).thenThrow(new RuntimeException("Cliente no encontrado"));
            
            mvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isInternalServerError()); // <-- Se espera un 500
        }
    }
    
    // --- CORRECCIÓN DE ESTRUCTURA: Esta clase debe estar afuera de "Create" ---
    @Nested
    @DisplayName("PUT /pets/{id}")
    class Update {
        @Test @DisplayName("13. Debe devolver 200 si la actualización es exitosa")
        void returns200_ifUpdateSuccessful() throws Exception {
            when(petService.updatePet(anyInt(), any(PetRequestDTO.class))).thenReturn(Optional.of(createPet(1, "Fido Jr.")));
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name", is("Fido Jr.")));
        }

        @Test @DisplayName("14. Debe devolver 404 si el ID de mascota a actualizar no se encuentra")
        void returns404_ifIdToUpdateNotFound() throws Exception {
            when(petService.updatePet(anyInt(), any(PetRequestDTO.class))).thenReturn(Optional.empty());
            mvc.perform(put(BASE_URL + "/{id}", 99).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isNotFound());
        }

        @Test @DisplayName("15. Debe devolver 400 si el body es inválido en la actualización")
        void returns400_ifBodyIsInvalidOnUpdate() throws Exception {
            PetRequestDTO req = createValidRequest();
            req.setName("");
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
    }
    
    // --- CORRECCIÓN DE ESTRUCTURA: Esta clase debe estar afuera de "Create" ---
    @Nested
    @DisplayName("DELETE /pets/{id}")
    class Delete {
        @Test @DisplayName("16. Debe devolver 204 si la eliminación es exitosa")
        void returns204_ifDeleteSuccessful() throws Exception {
            when(petService.deletePetById(1)).thenReturn(true);
            mvc.perform(delete(BASE_URL + "/{id}", 1)).andExpect(status().isNoContent());
        }

        @Test @DisplayName("17. Debe devolver 404 si el ID de mascota a eliminar no se encuentra")
        void returns404_ifIdToDeleteIsNotFound() throws Exception {
            when(petService.deletePetById(99)).thenReturn(false);
            mvc.perform(delete(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }
    }
    
    // --- CORRECCIÓN DE ESTRUCTURA: Esta clase debe estar afuera de "Create" ---
    @Nested
    @DisplayName("Protocol & Header Tests")
    class ProtocolTests {
        @Test @DisplayName("18. POST debe devolver la cabecera Location")
        void post_returnsLocationHeader() throws Exception {
            when(petService.savePet(any(PetRequestDTO.class))).thenReturn(createPet(123, "Test"));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(header().string("Location", containsString("/api/v1/pets/123")));
        }
        
        @Test @DisplayName("19. GET debe incluir la cabecera CORS")
        void get_includesCorsHeader() throws Exception {
            when(petService.findAllPets()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL).header("Origin", "http://example.com"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
        }

        @Test @DisplayName("20. La respuesta debe ser Content-Type application/json")
        void response_isJson() throws Exception {
            when(petService.findAllPets()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }

        @Test @DisplayName("21. POST con JSON malformado debe devolver 400")
        void post_withMalformedJson_returns400() throws Exception {
             mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{ \"name\": \"Test\" "))
                .andExpect(status().isBadRequest());
        }
    }
}