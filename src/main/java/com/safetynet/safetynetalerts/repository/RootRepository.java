package com.safetynet.safetynetalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

@Repository
public class RootRepository {
    private final String filePath = "/Users/senepascal/Desktop/Alert/data.json";
    private static final Logger logger = LoggerFactory.getLogger(RootRepository.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Root getRoot() throws IOException {
        return readRoot();
    }

    private Root readRoot() throws IOException {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            return objectMapper.readValue(file, Root.class);
        } else {
            logger.error("Le fichier JSON n'existe pas à l'emplacement spécifié");
            return new Root();
        }
    }

    public void write(Root root) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, root);
    }
}
