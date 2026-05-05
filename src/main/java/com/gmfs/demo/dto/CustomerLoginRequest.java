package com.gmfs.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerLoginRequest {

    @NotBlank
    private String ghanacardNumber;

    @NotBlank
    private String pin;
}
