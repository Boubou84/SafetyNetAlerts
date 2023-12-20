package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.Residence;
import com.safetynet.safetynetalerts.interfaces.IResidenceService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 class ResidenceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IResidenceService residenceService;

    @InjectMocks
    private ResidenceController residenceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
    }

    @Test
    @DisplayName("Doit retourner des résidences pour des stations données")
    void getResidencesByStations_Success() throws Exception {
        List<Integer> stations = Arrays.asList(1, 2);
        List<Residence> residences = Arrays.asList(new Residence(), new Residence());

        when(residenceService.getResidencesByStations(stations)).thenReturn(residences);

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Doit retourner un statut 404 si aucune résidence n'est trouvée")
    void getResidencesByStations_NotFound() throws Exception {
        List<Integer> stations = Arrays.asList(99);

        when(residenceService.getResidencesByStations(stations)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
