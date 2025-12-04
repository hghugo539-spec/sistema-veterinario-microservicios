package com.example.vet.controller;

import com.example.vet.config.ModelMapperConfig;
import com.example.vet.dto.AddressRequestDTO;
import com.example.vet.model.Address;
import com.example.vet.service.AddressService;
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

@WebMvcTest(controllers = AddressController.class)
@Import(ModelMapperConfig.class)
class AddressControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @MockBean private AddressService addressService;

    private static final String BASE_URL = "/api/v1/addresses";

    // Helper para crear una entidad Address
    private Address createAddress(Integer id, String street, String city) {
        Address a = new Address();
        a.setIdAddress(id);
        a.setStreet(street);
        a.setCity(city);
        return a;
    }

    // Helper para crear un DTO de petición válido
    private AddressRequestDTO createValidRequest() {
        AddressRequestDTO req = new AddressRequestDTO();
        req.setStreet("Calle Falsa 123");
        req.setCity("Puebla");
        return req;
    }

    @Nested
    @DisplayName("GET /api/v1/addresses")
    class GetAll {
        @Test @DisplayName("1. Debe devolver 200 y una lista de direcciones")
        void returns200_withList() throws Exception {
            when(addressService.findAllAddresses()).thenReturn(List.of(createAddress(1, "A", "Puebla"), createAddress(2, "B", "Cholula")));
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
        }

        @Test @DisplayName("2. Debe devolver 200 y una lista vacía")
        void returns200_withEmptyList() throws Exception {
            when(addressService.findAllAddresses()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/addresses/{id}")
    class GetById {
        @Test @DisplayName("3. Debe devolver 200 si el ID existe")
        void returns200_ifIdExists() throws Exception {
            when(addressService.findAddressById(1)).thenReturn(Optional.of(createAddress(1, "A", "Puebla")));
            mvc.perform(get(BASE_URL + "/{id}", 1)).andExpect(status().isOk()).andExpect(jsonPath("$.idAddress").value(1));
        }

        @Test @DisplayName("4. Debe devolver 404 si el ID no existe")
        void returns404_ifIdDoesNotExist() throws Exception {
            when(addressService.findAddressById(99)).thenReturn(Optional.empty());
            mvc.perform(get(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }

        @Test @DisplayName("5. Debe devolver 400 si el ID no es numérico")
        void returns400_ifIdIsNotNumeric() throws Exception {
            mvc.perform(get(BASE_URL + "/abc")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/addresses")
    class Create {
        @Test @DisplayName("6. Debe devolver 201 si el body es válido")
        void returns201_ifBodyIsValid() throws Exception {
            when(addressService.saveAddress(any(Address.class))).thenReturn(createAddress(1, "A", "Puebla"));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.idAddress").value(1));
        }

        @Test @DisplayName("7. Debe devolver 400 si la calle es nula")
        void returns400_ifStreetIsNull() throws Exception {
            AddressRequestDTO req = createValidRequest();
            req.setStreet(null);
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
        
        @Test @DisplayName("8. Debe devolver 400 si la calle está en blanco")
        void returns400_ifStreetIsBlank() throws Exception {
            AddressRequestDTO req = createValidRequest();
            req.setStreet("   ");
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("9. Debe devolver 400 si la ciudad es nula")
        void returns400_ifCityIsNull() throws Exception {
            AddressRequestDTO req = createValidRequest();
            req.setCity(null);
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
        
        @Test @DisplayName("10. Debe devolver 400 si la ciudad está en blanco")
        void returns400_ifCityIsBlank() throws Exception {
            AddressRequestDTO req = createValidRequest();
            req.setCity("");
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("PUT /api/v1/addresses/{id}")
    class Update {
        @Test @DisplayName("11. Debe devolver 200 si la actualización es exitosa")
        void returns200_ifUpdateIsSuccessful() throws Exception {
            when(addressService.updateAddress(eq(1), any(Address.class))).thenReturn(Optional.of(createAddress(1, "Updated", "Puebla")));
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.street").value("Updated"));
        }

        @Test @DisplayName("12. Debe devolver 404 si el ID a actualizar no existe")
        void returns404_ifIdToUpdateDoesNotExist() throws Exception {
            when(addressService.updateAddress(eq(99), any(Address.class))).thenReturn(Optional.empty());
            mvc.perform(put(BASE_URL + "/{id}", 99).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isNotFound());
        }

        @Test @DisplayName("13. Debe devolver 400 si el body es inválido")
        void returns400_ifBodyIsInvalid() throws Exception {
            AddressRequestDTO req = createValidRequest();
            req.setCity(null);
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        }
        
        @Test @DisplayName("14. Debe devolver 400 si el ID no es numérico")
        void returns400_ifIdIsNotNumeric() throws Exception {
            mvc.perform(put(BASE_URL + "/abc").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("DELETE /api/v1/addresses/{id}")
    class Delete {
        @Test @DisplayName("15. Debe devolver 204 si el borrado es exitoso")
        void returns204_ifDeleteIsSuccessful() throws Exception {
            when(addressService.deleteAddressById(1)).thenReturn(true);
            mvc.perform(delete(BASE_URL + "/{id}", 1)).andExpect(status().isNoContent());
        }

        @Test @DisplayName("16. Debe devolver 404 si el ID a borrar no existe")
        void returns404_ifIdToDeleteDoesNotExist() throws Exception {
            when(addressService.deleteAddressById(99)).thenReturn(false);
            mvc.perform(delete(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }
        
        @Test @DisplayName("17. Debe devolver 400 si el ID no es numérico")
        void returns400_ifIdIsNotNumericOnDelete() throws Exception {
            mvc.perform(delete(BASE_URL + "/abc")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Protocol & Header Tests")
    class ProtocolTests {
        @Test @DisplayName("18. POST debe devolver cabecera Location")
        void post_returnsLocationHeader() throws Exception {
            when(addressService.saveAddress(any(Address.class))).thenReturn(createAddress(123, "Test", "Test"));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(header().string("Location", containsString("/api/v1/addresses/123")));
        }
        
        @Test @DisplayName("19. GET debe incluir cabecera CORS")
        void get_includesCorsHeader() throws Exception {
            when(addressService.findAllAddresses()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL).header("Origin", "http://example.com"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
        }

        @Test @DisplayName("20. Respuesta debe ser Content-Type application/json")
        void response_isJson() throws Exception {
            when(addressService.findAllAddresses()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }

        @Test @DisplayName("21. POST con Content-Type incorrecto debe dar 415")
        void post_withWrongContentType_returns415() throws Exception {
            mvc.perform(post(BASE_URL).contentType(MediaType.TEXT_PLAIN).content("test"))
                .andExpect(status().isUnsupportedMediaType());
        }

        @Test @DisplayName("22. POST con JSON mal formado debe dar 400")
        void post_withMalformedJson_returns400() throws Exception {
             mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{ \"street\": \"Test\" "))
                .andExpect(status().isBadRequest());
        }
    }
}