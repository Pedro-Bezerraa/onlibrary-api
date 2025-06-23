package com.onlibrary.onlibrary_api.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    private final String bucket = "onlibrarybucket";

    @Value("${supabase.storage.url}")
    private String storageUrl;

    @Value("${supabase.storage.apikey}")
    private String apiKey;

    private final WebClient webClient;

    public SupabaseStorageService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(storageUrl).build();
    }

    public String uploadImagem(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null) originalName = "file";
            String safeName = originalName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            String nomeArquivo = UUID.randomUUID() + "-" + safeName;

            String contentType = file.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream";
            }

            byte[] bytes = file.getBytes();
            String path = "/storage/v1/object/" + bucket + "/image/book/" + nomeArquivo;

            var response = webClient.put()
                    .uri(path)
                    .header("Content-Type", contentType)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("x-upsert", "true")
                    .bodyValue(bytes)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            if (response == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Falha no upload, status: " + (response != null ? response.getStatusCode() : "null"));
            }

            return storageUrl + "/storage/v1/object/public/" + bucket + "/image/book/" + nomeArquivo;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload da imagem", e);
        }
    }
}