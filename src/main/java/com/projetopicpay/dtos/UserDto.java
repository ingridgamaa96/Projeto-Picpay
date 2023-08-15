package com.projetopicpay.dtos;

import com.projetopicpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserDto(String lastName , String firstName, String Document, BigDecimal balance , String email, String password , UserType usertype){

}
