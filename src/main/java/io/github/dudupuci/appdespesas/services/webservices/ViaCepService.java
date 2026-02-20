package io.github.dudupuci.appdespesas.services.webservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.cep.ViaCepResponseDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ViaCepService {

    private static final String URL_BASE = "https://viacep.com.br/ws/";

    public ViaCepResponseDto buscarEndereco(String cep) {
        String urlString = URL_BASE + cep + "/json/";
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result.toString(), ViaCepResponseDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar endereço para cep: " + cep, e);
        }
    }

}
