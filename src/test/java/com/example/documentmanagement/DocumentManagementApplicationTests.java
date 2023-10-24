package com.example.documentmanagement;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(WebsiteController.class)
class DocumentManagementApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homeLoads() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("date"));
    }
    @Test
    public void homePageLinkReturnsIndex() throws Exception {
        this.mockMvc
                .perform(get("/")).andDo(print())
                .andExpect(view().name("index"));
    }
}
