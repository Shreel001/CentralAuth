package com.banking.accountService.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    @PostMapping("/withdrawal")
    public ResponseEntity<?> withDraw(){

        return ResponseEntity.ok("Sucess");
    }

}
