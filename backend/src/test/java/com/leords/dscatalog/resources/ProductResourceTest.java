package com.leords.dscatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leords.dscatalog.dto.ProductDTO;
import com.leords.dscatalog.services.ProductService;
import com.leords.dscatalog.services.exceptions.DatabaseException;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import com.leords.dscatalog.tests.ProductFactory;
import com.leords.dscatalog.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductResourceTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TokenUtil tokenUtil;
    
    @MockBean
    private ProductService service;
    
    private PageImpl<ProductDTO> page;
    
    private ProductDTO productDto;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private String username;
    private String password;
    
    @BeforeEach()
    void setup() {
        username = "maria@gmail.com";
        password = "123456";
        
        existingId = 1L;
        nonExistingId = 0L;
        dependentId = 3L;
        productDto = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(productDto));
        
        when(service.findAllProducts(any(), any(), any())).thenReturn(page);
        
        when(service.findProductById(existingId)).thenReturn(productDto);
        when(service.findProductById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        
        when(service.createProduct(any())).thenReturn(productDto);
        
        when(service.updateProduct(eq(existingId), any())).thenReturn(productDto);
        when(service.updateProduct(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        
        doNothing().when(service).deleteProduct(existingId);
        doThrow(ResourceNotFoundException.class).when(service).deleteProduct(nonExistingId);
        doThrow(DatabaseException.class).when(service).deleteProduct(dependentId);
    }
    
    @Test
    void findAllProductsShouldReturnPage() throws Exception {
        var resultActions = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isOk());
    }
    
    @Test
    void findProductByIdShouldReturnProductDtoWhenIdExist() throws Exception {
        var resultActions = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
        resultActions.andExpect(jsonPath("$.price").exists());
        resultActions.andExpect(jsonPath("$.imgUrl").exists());
        resultActions.andExpect(jsonPath("$.date").exists());
    }
    
    @Test
    void findProductByIdShouldReturnHttpStatusNotFoundWhenIdDoesNotExist() throws Exception {
        var resultActions = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isNotFound());
    }
    
    @Test
    void createProductShouldReturnProductDtoCreated() throws Exception {
        var jsonBody = objectMapper.writeValueAsString(productDto);
        
        var resultActions = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
        resultActions.andExpect(jsonPath("$.price").exists());
        resultActions.andExpect(jsonPath("$.imgUrl").exists());
        resultActions.andExpect(jsonPath("$.date").exists());
    }
    
    @Test
    void updateProductShouldReturnProductDtoWhenIdExist() throws Exception {
        var accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        var jsonBody = objectMapper.writeValueAsString(productDto);
        
        var resultActions = mockMvc.perform(put("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
        resultActions.andExpect(jsonPath("$.price").exists());
        resultActions.andExpect(jsonPath("$.imgUrl").exists());
        resultActions.andExpect(jsonPath("$.date").exists());
    }
    
    @Test
    void deleteProductShouldDoesNothingWhenIdExist() throws Exception {
        var accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        var resultActions = mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken));
        
        resultActions.andExpect(status().isNoContent());
        verify(service, times(1)).deleteProduct(existingId);
    }
    
    @Test
    void deleteProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        var accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        var resultActions = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken));
        
        resultActions.andExpect(status().isNotFound());
        verify(service, times(1)).deleteProduct(nonExistingId);
    }
    
    @Test
    void deleteProductShouldThrowDatabaseExceptionWhenDependentId() throws Exception {
        var accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        var resultActions = mockMvc.perform(delete("/products/{id}", dependentId)
                .header("Authorization", "Bearer " + accessToken));
        
        resultActions.andExpect(status().isBadRequest());
        verify(service, times(1)).deleteProduct(dependentId);
    }
    
}