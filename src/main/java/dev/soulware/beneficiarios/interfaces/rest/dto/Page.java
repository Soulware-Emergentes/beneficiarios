package dev.soulware.beneficiarios.interfaces.rest.dto;

import java.util.List;

public record Page<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int actualPage,
        int pageSize
) {}