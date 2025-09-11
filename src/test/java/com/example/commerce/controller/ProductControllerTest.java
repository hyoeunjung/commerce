package com.example.commerce.controller;

import com.example.commerce.dto.ProductCreateRequest;
import com.example.commerce.dto.ProductUpdateRequest;
import com.example.commerce.entity.Product;
import com.example.commerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("관리자 권한으로 상품 등록 성공")
    @WithMockUser(roles = "ADMIN")
    void testCreateProductWithAdminRole() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("사과");
        request.setPrice(1000);
        request.setStock(50);
        request.setDescription("싱싱한 사과입니다.");

        // Mocking
        Product product = Product.builder()
                .id(1L)
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .description(request.getDescription())
                .build();
        given(productService.createProduct(any(ProductCreateRequest.class))).willReturn(product);

        //when then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("사과"))
                .andExpect(jsonPath("$.price").value(1000));
    }

    @Test
    @DisplayName("일반 사용자 권한으로 상품 등록 실패 (403 Forbidden)")
    @WithMockUser(roles = "USER")
    void testCreateProductWithUserRole_shouldFail() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("배");
        request.setPrice(2000);
        request.setStock(30);
        request.setDescription("달콤한 배입니다.");

        // when then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 권한으로 상품 수정 성공")
    @WithMockUser(roles = "ADMIN")
    void testUpdateProduct_Success() throws Exception {
        // given
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("수정된 상품");
        request.setPrice(30000);
        request.setStock(10);
        request.setDescription("수정된 상품 설명");

        Product updatedProduct = Product.builder()
                .id(1L)
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .description(request.getDescription())
                .build();

        // Mocking
        given(productService.updateProduct(any(Long.class), any(ProductUpdateRequest.class))).willReturn(updatedProduct);

        // when & then
        mockMvc.perform(put("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("수정된 상품"))
                .andExpect(jsonPath("$.price").value(30000));
    }

    @Test
    @DisplayName("일반 사용자 권한으로 상품 수정 실패 (403 Forbidden)")
    @WithMockUser(roles = "USER")
    void testUpdateProduct_Forbidden() throws Exception {
        // given
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("수정될 상품");
        request.setPrice(100);
        request.setStock(10);
        request.setDescription("설명");

        // when  then
        mockMvc.perform(put("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 권한으로 상품 삭제 성공")
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct_Success() throws Exception {
        // given
        Product deletedProduct = Product.builder()
                .id(1L)
                .name("일반사과")
                .price(10000)
                .stock(100)
                .description("맛있는 사과")
                .build();
        deletedProduct.softDelete();

        // Mocking
        given(productService.deleteProduct(any(Long.class))).willReturn(deletedProduct);

        // when & then
        mockMvc.perform(delete("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true))
                .andExpect(jsonPath("$.name").value("일반사과"));
    }

    @Test
    @DisplayName("일반 사용자 권한으로 상품 삭제 실패 (403 Forbidden)")
    @WithMockUser(roles = "USER")
    void testDeleteProduct_Forbidden() throws Exception {
        mockMvc.perform(delete("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}