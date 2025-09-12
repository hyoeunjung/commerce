package com.example.commerce.controller;

import com.example.commerce.dto.ProductCreateRequest;
import com.example.commerce.dto.ProductResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private final LocalDateTime now = LocalDateTime.now();

    private Product setBaseTimeEntityFields(Product product, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        try {

            Field createdDateField = product.getClass().getSuperclass().getDeclaredField("createdDate");
            createdDateField.setAccessible(true);
            createdDateField.set(product, createdDate);


            Field lastModifiedDateField = product.getClass().getSuperclass().getDeclaredField("lastModifiedDate");
            lastModifiedDateField.setAccessible(true);
            lastModifiedDateField.set(product, lastModifiedDate);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set BaseTimeEntity fields via reflection", e);
        }
        return product;
    }

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


        Product product = Product.builder()
                .id(1L)
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .description(request.getDescription())
                .isDeleted(false)
                .build();
        setBaseTimeEntityFields(product, now, now);

        given(productService.createProduct(any(ProductCreateRequest.class))).willReturn(product);

        //when then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("사과"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.deleted").value(false));
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
        Long productId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("수정된 상품");
        request.setPrice(30000);
        request.setStock(10);
        request.setDescription("수정된 상품 설명");


        Product updatedProduct = Product.builder()
                .id(productId)
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .description(request.getDescription())
                .isDeleted(false)
                .build();
        setBaseTimeEntityFields(updatedProduct, now.minusDays(1), now);

        given(productService.updateProduct(eq(productId), any(ProductUpdateRequest.class))).willReturn(updatedProduct);

        // when & then
        mockMvc.perform(put("/api/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("수정된 상품"))
                .andExpect(jsonPath("$.price").value(30000))
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.deleted").value(false));
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
        mockMvc.perform(put("/api/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 권한으로 상품 삭제 성공 (200 OK, Product 반환)")
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct_Success() throws Exception {
        // given
        Long productId = 1L;

        Product deletedProduct = Product.builder()
                .id(productId)
                .name("일반사과")
                .price(10000)
                .stock(100)
                .description("맛있는 사과")
                .isDeleted(true)
                .build();
        setBaseTimeEntityFields(deletedProduct, now.minusDays(2), now);

        given(productService.deleteProduct(eq(productId))).willReturn(deletedProduct);

        // when & then
        mockMvc.perform(delete("/api/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true))
                .andExpect(jsonPath("$.name").value("일반사과"));
    }

    @Test
    @DisplayName("일반 사용자 권한으로 상품 삭제 실패 (403 Forbidden)")
    @WithMockUser(roles = "USER")
    void testDeleteProduct_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("상품 검색 성공 - 로그인하지 않아도 가능")
    void testSearchProducts_Success() throws Exception {
        // given
        Product productApple = Product.builder()
                .id(1L)
                .name("사과")
                .price(1000)
                .stock(50)
                .description("싱싱한 사과")
                .isDeleted(false)
                .build();
        setBaseTimeEntityFields(productApple, now.minusHours(2), now.minusHours(2));

        Product productPear = Product.builder()
                .id(2L)
                .name("배")
                .price(2000)
                .stock(30)
                .description("달콤한 배")
                .isDeleted(false)
                .build();
        setBaseTimeEntityFields(productPear, now.minusHours(1), now.minusHours(1));


        List<ProductResponse> productResponses = Arrays.asList(
                new ProductResponse(productApple),
                new ProductResponse(productPear)
        );

        Page<ProductResponse> mockPage = new PageImpl<>(productResponses);

        given(productService.searchProducts(any(String.class), any(Pageable.class)))
                .willReturn(mockPage);

        // when & then
        mockMvc.perform(get("/api/products/search")
                        .param("keyword", "과일")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "price,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("사과"))
                .andExpect(jsonPath("$.content[1].name").value("배"))
                .andExpect(jsonPath("$.content[0].deleted").value(false))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("키워드 없이 상품 검색 성공")
    void testSearchProducts_NoKeyword_Success() throws Exception {
        // given
        Product productOrange = Product.builder()
                .id(3L)
                .name("오렌지")
                .price(3000)
                .stock(40)
                .description("새콤달콤 오렌지")
                .isDeleted(false)
                .build();
        setBaseTimeEntityFields(productOrange, now.minusHours(3), now.minusHours(3));

        List<ProductResponse> productResponses = List.of(new ProductResponse(productOrange));
        Page<ProductResponse> mockPage = new PageImpl<>(productResponses);

        given(productService.searchProducts(eq(null), any(Pageable.class)))
                .willReturn(mockPage);

        // when & then
        mockMvc.perform(get("/api/products/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdDate,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("오렌지"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("검색 결과가 없을 경우 빈 목록 반환")
    void testSearchProducts_NoResult_Success() throws Exception {
        // given
        Page<ProductResponse> emptyPage = new PageImpl<>(List.of());

        given(productService.searchProducts(eq("없는 키워드"), any(Pageable.class)))
                .willReturn(emptyPage);

        // when & then
        mockMvc.perform(get("/api/products/search")
                        .param("keyword", "없는 키워드")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}