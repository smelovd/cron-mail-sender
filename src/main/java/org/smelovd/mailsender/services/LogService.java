package org.smelovd.mailsender.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.Log;
import org.smelovd.mailsender.entities.User;
import org.smelovd.mailsender.entities.enums.SEND_TYPE;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.log.LogResponse;
import org.smelovd.mailsender.repositories.LogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public PaginationResponse<LogResponse> findAllPaginate(int page, int count) {
        Pageable pageable = PageRequest.of(page - 1, count);
        Page<LogResponse> paginatedResponse = logRepository.findAllLogResponsesOrderByCount(pageable);

        return PaginationResponse.<LogResponse>builder()
                .content(paginatedResponse.getContent())
                .totalPages(paginatedResponse.getTotalPages())
                .currentPage(page)
                .totalElements(paginatedResponse.getTotalElements())
                .elementsPerPage(count)
                .build();
    }

    public void save(User user, SEND_TYPE type) {
        logRepository.save(Log.builder()
                .type(type)
                .user(user)
                .build());
    }

    public void saveAll(List<User> users, SEND_TYPE type, Date createdOn) {
        List<Log> logs = users.stream()
                .map(user ->
                        Log.builder()
                                .type(type)
                                .user(user)
                                .createdOn(createdOn)
                                .build())
                .toList();

        logRepository.saveAll(logs);
    }
}
