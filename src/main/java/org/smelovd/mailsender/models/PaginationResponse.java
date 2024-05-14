package org.smelovd.mailsender.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResponse<T> {

    private List<T> content;
    private int totalPages;
    private int currentPage;
    private int elementsPerPage;
    private long totalElements;
}
