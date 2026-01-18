package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.entity.Token;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.repository.TokenRepository;
import com.example.predict_numbers.service.TokenService;
import com.example.predict_numbers.util.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    @Override
    public long saveToken(Token token) {
        Optional<Token> tokenDB = this.tokenRepository.findByUsername(token.getUsername());
        if(tokenDB.isEmpty()){
            Token saved = tokenRepository.save(token);
            return saved.getId();
        }else{
            Token currentToken = tokenDB.get();
            currentToken.setAccessToken(token.getAccessToken());
            currentToken.setRefreshToken(token.getRefreshToken());
            Token saved = tokenRepository.save(currentToken);
            return saved.getId();
        }
    }

    @Override
    public void deleteToken(Token token) {
        this.tokenRepository.delete(token);
    }

    @Override
    public Token getByUsername(String username) {
        return this.tokenRepository.findByUsername(username).orElseThrow(()-> new AppException(ErrorCode.INVALID_TOKEN));
    }
}
