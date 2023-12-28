package com.example.basicsecuritydemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountRepository repository;

    @Test
    @WithMockUser()
    void testHi() throws Exception {
       mvc.perform(get("/hi"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content",is("Hello user")));
    }

    @Test
    @WithMockUser
    void testAccounts()  throws Exception {
        when(repository.findAll()).thenReturn(List.of(new Account("user", "password", true)));
        mvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",not(empty())))
                .andExpect(jsonPath("$.[0].userName", is("user")))
                .andExpect(jsonPath("$.[0].password", is("password")))
                .andExpect(jsonPath("$.[0].active", is(true)));
    }



}
