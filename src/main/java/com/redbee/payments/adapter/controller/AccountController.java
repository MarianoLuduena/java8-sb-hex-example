package com.redbee.payments.adapter.controller;

import com.redbee.payments.adapter.controller.model.TransferMoneyControllerRequest;
import com.redbee.payments.adapter.controller.model.TransferMoneyControllerResponse;
import com.redbee.payments.application.port.in.TransferMoneyOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/accounts")
@Slf4j
@Validated
public class AccountController {

    private final TransferMoneyOperation transferMoneyOperation;

    public AccountController(final TransferMoneyOperation transferMoneyOperation) {
        this.transferMoneyOperation = transferMoneyOperation;
    }

    @PostMapping(path = "/{id}/transfers")
    TransferMoneyControllerResponse transfer(
            @PathVariable("id") @NotNull @Positive final Long id,
            @Validated @RequestBody final TransferMoneyControllerRequest request
    ) {
        log.info("Call to POST /accounts/{}/transfers with request {}", id, request);
        transferMoneyOperation.execute(request.toDomain(id));

        final TransferMoneyControllerResponse response =
                new TransferMoneyControllerResponse(id, request.getTargetAccount(), request.getAmount());

        log.info("Response {}", response);
        return response;
    }

}
