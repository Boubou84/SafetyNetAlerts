package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.interfaces.IPhoneAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PhoneAlertControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IPhoneAlertService phoneAlertService;

    @InjectMocks
    private PhoneAlertController phoneAlertController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneAlertController).build();
    }

    @Test
    @DisplayName("Doit retourner une liste de numéros de téléphone pour une station donnée")
    void getPhoneNumbers_Success() throws Exception {
        List<String> phoneNumbers = Arrays.asList("123-456-7890", "987-654-3210");
        when(phoneAlertService.getPhoneNumbersByStationNumber(eq(1))).thenReturn(phoneNumbers);

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("123-456-7890"))
                .andExpect(jsonPath("$[1]").value("987-654-3210"));
    }

    @Test
    @DisplayName("Doit retourner NOT FOUND si aucune correspondance n'est trouvée pour une station donnée")
    void getPhoneNumbers_NotFound() throws Exception {
        when(phoneAlertService.getPhoneNumbersByStationNumber(eq(99))).thenReturn(Arrays.asList());

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "99"))
                .andExpect(status().isNotFound());
    }
}
