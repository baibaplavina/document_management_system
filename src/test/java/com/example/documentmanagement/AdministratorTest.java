package com.example.documentmanagement;

import com.example.documentmanagement.administrator.Administrator;
import com.example.documentmanagement.administrator.AdministratorController;
import com.example.documentmanagement.administrator.AdministratorRepository;
import com.example.documentmanagement.administrator.AdministratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdministratorController.class)
public class AdministratorTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministratorRepository administratorRepository;
    @MockBean
    private AdministratorService administratorService;
    @MockBean
    private Administrator administrator;

    @Test
    public void shouldReturnCreateAdmin() throws Exception {
        this.mockMvc
                .perform(get("/create-administrator"))
                .andExpect(status().isOk())
                .andExpect(view().name("createAdministrator"))
                .andExpect(model().attributeExists("admin"))
                .andExpect(model().attributeExists("adminList"));
             //   .andExpect(model().attributeExists("message"))
             //   .andExpect(model().attributeExists("error"));
    }
}
