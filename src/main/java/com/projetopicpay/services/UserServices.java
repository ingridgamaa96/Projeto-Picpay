package com.projetopicpay.services;

import com.projetopicpay.domain.user.User;
import com.projetopicpay.domain.user.UserType;
import com.projetopicpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServices {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal  amount) throws Exception {
        if (sender.getUserType()== UserType.MERCHANT){
            throw  new Exception( "Usuario do tipo Lojista não esta autorizado a fazer transação");

        }
        if (sender.getBalance().compareTo(amount)< 0){
            throw  new Exception( "Usuario não tem saldo suficiente para essa transação");
        }
    }

    public  User findUserById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("usuario não entrado"));
    }

    public void saveUser(User user) {
        this.repository.save(user);
    }
}
