package com.redbee.payments;

import com.redbee.payments.adapter.controller.model.TransferMoneyControllerRequest;
import com.redbee.payments.adapter.controller.model.TransferMoneyControllerResponse;
import com.redbee.payments.application.port.out.LoadAccountPort;
import com.redbee.payments.domain.Account;
import com.redbee.payments.domain.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferMoneyIntegrationTest {

    private static final Long SOURCE_ACCOUNT_ID = 1L;
    private static final Long TARGET_ACCOUNT_ID = 2L;
    private static final Long TRANSFERRED_AMOUNT = 500L;
    private static final String TRANSFERS_ENDPOINT = "/accounts/{sourceAccountId}/transfers";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoadAccountPort loadAccountPort;

    @Test
    @Sql("classpath:sql/TransferMoney.sql")
    void transferMoney() {
        // Given
        final Money initialSourceAccountMoney = sourceAccount().calculateBalance();
        final Money initialTargetAccountMoney = targetAccount().calculateBalance();

        final TransferMoneyControllerRequest requestBody =
                new TransferMoneyControllerRequest(TARGET_ACCOUNT_ID, TRANSFERRED_AMOUNT);

        final HttpEntity<TransferMoneyControllerRequest> request = new HttpEntity<>(requestBody, HttpHeaders.EMPTY);

        // When
        final ResponseEntity<TransferMoneyControllerResponse> response =
                restTemplate.exchange(
                        TRANSFERS_ENDPOINT,
                        HttpMethod.POST,
                        request,
                        TransferMoneyControllerResponse.class,
                        SOURCE_ACCOUNT_ID
                );

        // Then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(response.getBody())
                .isEqualTo(new TransferMoneyControllerResponse(SOURCE_ACCOUNT_ID, TARGET_ACCOUNT_ID, TRANSFERRED_AMOUNT));

        Assertions.assertThat(sourceAccount().calculateBalance())
                .isEqualTo(initialSourceAccountMoney.minus(Money.of(TRANSFERRED_AMOUNT)));

        Assertions.assertThat(targetAccount().calculateBalance())
                .isEqualTo(initialTargetAccountMoney.plus(Money.of(TRANSFERRED_AMOUNT)));
    }

    private Account sourceAccount() {
        return loadAccount(SOURCE_ACCOUNT_ID);
    }

    private Account targetAccount() {
        return loadAccount(TARGET_ACCOUNT_ID);
    }

    private Account loadAccount(final Long accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now());
    }

}
