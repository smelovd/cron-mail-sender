package org.smelovd.mailsender.controllers;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.log.LogResponse;
import org.smelovd.mailsender.services.LogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("logs")
public class LogController {

    private final LogService logService;

    @GetMapping
    public PaginationResponse<LogResponse> getAllUsersPaginate(@RequestParam @Min(1) final int page, @RequestParam @Min(1) final int count) {
        return logService.findAllPaginate(page, count);
    }
}
