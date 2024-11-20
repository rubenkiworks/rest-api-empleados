package com.example.models;

import lombok.Builder;

@Builder
public record FileUploadResponse(String fileName, String downloadURI, long fileSize) {

}
