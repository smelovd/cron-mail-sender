package org.smelovd.mailsender.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public final class PaginationResponse<T> {

    private final List<T> content;
    private final int totalPages;
    private final int currentPage;
    private final int elementsPerPage;
    private final long totalElements;
}
