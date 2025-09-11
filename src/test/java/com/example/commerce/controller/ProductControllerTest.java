package com.example.commerce.controller;

import com.example.commerce.dto.ProductCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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


        //when then
        mockMvc.perform(post("/products/create")
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
        mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}