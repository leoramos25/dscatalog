package com.leords.dscatalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leords.dscatalog.tests.ProductFactory;
import com.leords.dscatalog.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ProductResourceIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TokenUtil tokenUtil;
    
    private Long existingId;
    private Long nonExistId;
    private Long countTotalProducts;
    private String username;
    private String password;
    
    @BeforeEach
    void setup() throws Exception {
        username = "maria@gmail.com";
        password = "123456";
        
        existingId = 1L;
        nonExistId = 0L;
        countTotalProducts = 25L;
    }
    
    @Test
    void findAllProductsShouldReturnSortedPageWhenSortByName() throws Exception {
        var resultActions = mockMvc
                .perform(get("/products?page=0&size=12&direction=ASC&sort=name,ASC")
                        .accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        resultActions.andExpect(jsonPath("$.content").exists());
        resultActions.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        resultActions.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        resultActions.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }
    
    @Test
    void updateProductShouldReturnProductDTOWhenIdExist() throws Exception {
        var productDto = ProductFactory.createProductDTO();
        var jsonBody = objectMapper.writeValueAsString(productDto);
        var expectedProductDtoId = productDto.getId();
        var accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        
        var resultActions = mockMvc
                .perform(put("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(expectedProductDtoId));
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
        resultActions.andExpect(jsonPath("$.price").exists());
        resultActions.andExpect(jsonPath("$.imgUrl").exists());
        resultActions.andExpect(jsonPath("$.date").exists());
    }
    
    @Test
    void updateProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        var accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        var productDto = ProductFactory.createProductDTO();
        var jsonBody = objectMapper.writeValueAsString(productDto);
        
        var resultActions = mockMvc.perform(put("/products/{id}", nonExistId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        
        resultActions.andExpect(status().isNotFound());
    }
    
}