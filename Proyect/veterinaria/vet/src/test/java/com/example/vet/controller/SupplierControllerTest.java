package com.example.vet.controller;

import com.example.vet.config.ModelMapperConfig;
import com.example.vet.dto.SupplierRequestDTO;
import com.example.vet.model.Supplier;
import com.example.vet.service.SupplierService;
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

@WebMvcTest(controllers = SupplierController.class)
@Import(ModelMapperConfig.class)
class SupplierControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @MockBean private SupplierService supplierService;

    private static final String BASE_URL = "/api/v1/suppliers";

    // Helper para crear una entidad Supplier
    private Supplier createSupplier(Integer id, String name) {
        Supplier supplier = new Supplier();
        supplier.setIdSupplier(id);
        supplier.setName(name);
        return supplier;
    }

    // Helper para crear un DTO de petición válido
    private SupplierRequestDTO createValidRequest() {
        SupplierRequestDTO request = new SupplierRequestDTO();
        request.setName("Proveedor S.A. de C.V.");
        request.setContactPerson("Juan Perez");
        return request;
    }

    @Nested
    @DisplayName("GET /api/v1/suppliers")
    class GetAll {
        @Test @DisplayName("1. Debe devolver 200 y una lista de proveedores")
        void returns200_withList() throws Exception {
            when(supplierService.findAllSuppliers()).thenReturn(List.of(createSupplier(1, "Proveedor A"), createSupplier(2, "Proveedor B")));
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
        }

        @Test @DisplayName("2. Debe devolver 200 y una lista vacía")
        void returns200_withEmptyList() throws Exception {
            when(supplierService.findAllSuppliers()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/suppliers/{id}")
    class GetById {
        @Test @DisplayName("3. Debe devolver 200 si el ID existe")
        void returns200_ifIdExists() throws Exception {
            when(supplierService.findSupplierById(1)).thenReturn(Optional.of(createSupplier(1, "Proveedor A")));
            mvc.perform(get(BASE_URL + "/{id}", 1)).andExpect(status().isOk()).andExpect(jsonPath("$.idSupplier").value(1));
        }

        @Test @DisplayName("4. Debe devolver 404 si el ID no existe")
        void returns404_ifIdDoesNotExist() throws Exception {
            when(supplierService.findSupplierById(99)).thenReturn(Optional.empty());
            mvc.perform(get(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }

        @Test @DisplayName("5. Debe devolver 400 si el ID no es numérico")
        void returns400_ifIdIsNotNumeric() throws Exception {
            mvc.perform(get(BASE_URL + "/abc")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/suppliers")
    class Create {
        @Test @DisplayName("6. Debe devolver 201 si el body es válido")
        void returns201_ifBodyIsValid() throws Exception {
            when(supplierService.saveSupplier(any(SupplierRequestDTO.class))).thenReturn(createSupplier(1, "Proveedor A"));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.idSupplier").value(1));
        }

        @Test @DisplayName("7. Debe devolver 400 si el nombre es nulo")
        void returns400_ifNameIsNull() throws Exception {
            SupplierRequestDTO request = createValidRequest();
            request.setName(null);
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }
        
        @Test @DisplayName("8. Debe devolver 400 si el nombre está en blanco")
        void returns400_ifNameIsBlank() throws Exception {
            SupplierRequestDTO request = createValidRequest();
            request.setName("  ");
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("PUT /api/v1/suppliers/{id}")
    class Update {
        @Test @DisplayName("9. Debe devolver 200 si la actualización es exitosa")
        void returns200_ifUpdateIsSuccessful() throws Exception {
            when(supplierService.updateSupplier(eq(1), any(SupplierRequestDTO.class))).thenReturn(Optional.of(createSupplier(1, "Proveedor Actualizado")));
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Proveedor Actualizado"));
        }

        @Test @DisplayName("10. Debe devolver 404 si el ID a actualizar no existe")
        void returns404_ifIdToUpdateDoesNotExist() throws Exception {
            when(supplierService.updateSupplier(eq(99), any(SupplierRequestDTO.class))).thenReturn(Optional.empty());
            mvc.perform(put(BASE_URL + "/{id}", 99).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isNotFound());
        }
        
        @Test @DisplayName("11. Debe devolver 400 si el body es inválido")
        void returns400_ifBodyIsInvalid() throws Exception {
            SupplierRequestDTO request = createValidRequest();
            request.setName(""); // Nombre vacío es inválido
            mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("DELETE /api/v1/suppliers/{id}")
    class Delete {
        @Test @DisplayName("12. Debe devolver 204 si el borrado es exitoso")
        void returns204_ifDeleteIsSuccessful() throws Exception {
            when(supplierService.deleteSupplierById(1)).thenReturn(true);
            mvc.perform(delete(BASE_URL + "/{id}", 1)).andExpect(status().isNoContent());
        }

        @Test @DisplayName("13. Debe devolver 404 si el ID a borrar no existe")
        void returns404_ifIdToDeleteDoesNotExist() throws Exception {
            when(supplierService.deleteSupplierById(99)).thenReturn(false);
            mvc.perform(delete(BASE_URL + "/{id}", 99)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Protocol & Header Tests")
    class ProtocolTests {
        @Test @DisplayName("14. POST debe devolver cabecera Location")
        void post_returnsLocationHeader() throws Exception {
            when(supplierService.saveSupplier(any(SupplierRequestDTO.class))).thenReturn(createSupplier(123, "Test"));
            mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createValidRequest())))
                .andExpect(header().string("Location", containsString("/api/v1/suppliers/123")));
        }
        
        @Test @DisplayName("15. GET debe incluir cabecera CORS")
        void get_includesCorsHeader() throws Exception {
            when(supplierService.findAllSuppliers()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL).header("Origin", "http://example.com"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
        }

        @Test @DisplayName("16. Respuesta debe ser Content-Type application/json")
        void response_isJson() throws Exception {
            when(supplierService.findAllSuppliers()).thenReturn(Collections.emptyList());
            mvc.perform(get(BASE_URL)).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }

        @Test @DisplayName("17. POST con Content-Type incorrecto debe dar 415")
        void post_withWrongContentType_returns415() throws Exception {
            mvc.perform(post(BASE_URL).contentType(MediaType.TEXT_PLAIN).content("test"))
                .andExpect(status().isUnsupportedMediaType());
        }

        @Test @DisplayName("18. POST con JSON mal formado debe dar 400")
        void post_withMalformedJson_returns400() throws Exception {
             mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{ \"name\": \"Test\" "))
                .andExpect(status().isBadRequest());
        }
        
        @Test @DisplayName("19. PUT con JSON mal formado debe dar 400")
        void put_withMalformedJson_returns400() throws Exception {
             mvc.perform(put(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content("{\"name\":, }"))
                .andExpect(status().isBadRequest());
        }

        @Test @DisplayName("20. DELETE con ID no numérico debe dar 400")
        void delete_withNonNumericId_returns400() throws Exception {
            mvc.perform(delete(BASE_URL + "/abc")).andExpect(status().isBadRequest());
        }
    }
}