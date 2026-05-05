package com.gmfs.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmfs.demo.dto.AccountBalanceResponse;
import com.gmfs.demo.dto.AmountRequest;
import com.gmfs.demo.dto.CustomerLoginRequest;
import com.gmfs.demo.dto.SignupRequest;
import com.gmfs.demo.dto.SignupResponse;
import com.gmfs.demo.dto.LoginResponse;
import com.gmfs.demo.dto.TransactionHistoryPageResponse;
import com.gmfs.demo.dto.TransactionMutationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerFlowTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signupLoginDepositWithdrawAndHistory() throws Exception {
        String ghanacardNumber = "GHA-" + UUID.randomUUID();

        SignupRequest signup = new SignupRequest();
        signup.setFirstName("Ada");
        signup.setLastName("Lovelace");
        signup.setDob(LocalDate.of(1990, 5, 5));
        signup.setGhanacardNumber(ghanacardNumber);
        signup.setPin("1234");

        MvcResult signupResult = mockMvc.perform(post("/api/v1/customers/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isCreated())
                .andReturn();

        SignupResponse created = objectMapper.readValue(signupResult.getResponse().getContentAsString(), SignupResponse.class);
        assertThat(created.getCustomerId()).isNotNull();
        assertThat(created.getPrimaryAccountId()).isNotNull();

        CustomerLoginRequest login = new CustomerLoginRequest();
        login.setGhanacardNumber(ghanacardNumber);
        login.setPin("1234");

        MvcResult loginResult = mockMvc.perform(post("/api/v1/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse tokenResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
        assertThat(tokenResponse.getToken()).isNotBlank();

        String token = tokenResponse.getToken();
        Long accountId = created.getPrimaryAccountId();

        AmountRequest deposit = new AmountRequest();
        deposit.setAmount(new BigDecimal("100.50"));

        MvcResult depositResult = mockMvc.perform(post("/api/v1/accounts/{accountId}/deposit", accountId)
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andReturn();

        TransactionMutationResponse afterDeposit = objectMapper.readValue(
                depositResult.getResponse().getContentAsString(), TransactionMutationResponse.class);
        assertThat(afterDeposit.getBalance()).isEqualByComparingTo(new BigDecimal("100.5000"));

        AmountRequest withdraw = new AmountRequest();
        withdraw.setAmount(new BigDecimal("40.25"));

        MvcResult withdrawResult = mockMvc.perform(post("/api/v1/accounts/{accountId}/withdraw", accountId)
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andReturn();

        TransactionMutationResponse afterWithdraw = objectMapper.readValue(
                withdrawResult.getResponse().getContentAsString(), TransactionMutationResponse.class);
        assertThat(afterWithdraw.getBalance()).isEqualByComparingTo(new BigDecimal("60.2500"));

        MvcResult balanceResult = mockMvc.perform(get("/api/v1/accounts/{accountId}/balance", accountId)
                        .header("X-Auth-Token", token))
                .andExpect(status().isOk())
                .andReturn();

        AccountBalanceResponse balance = objectMapper.readValue(
                balanceResult.getResponse().getContentAsString(), AccountBalanceResponse.class);
        assertThat(balance.getAccountId()).isEqualTo(accountId);
        assertThat(balance.getBalance()).isEqualByComparingTo(new BigDecimal("60.2500"));

        MvcResult historyResult = mockMvc.perform(get("/api/v1/accounts/{accountId}/transactions", accountId)
                        .header("X-Auth-Token", token)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andReturn();

        TransactionHistoryPageResponse history = objectMapper.readValue(
                historyResult.getResponse().getContentAsString(), TransactionHistoryPageResponse.class);
        assertThat(history.getContent()).hasSize(2);
    }
}
