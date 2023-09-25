package com.safetynet.SafetyNetAlerts.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNetAlerts.model.Root;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DataLoader {

    private Root root;

    public DataLoader() {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = getClass().getResourceAsStream("/data.json")) {
            this.root = mapper.readValue(is, Root.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Root getRoot() {
        return root;
    }
}
