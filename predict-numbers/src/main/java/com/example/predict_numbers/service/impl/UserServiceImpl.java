package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.configuration.UserPrincipal;
import com.example.predict_numbers.dto.request.CreateUserRequest;
import com.example.predict_numbers.dto.request.PredictNumberRequest;
import com.example.predict_numbers.dto.response.PredictResponse;
import com.example.predict_numbers.dto.response.UserResponse;
import com.example.predict_numbers.entity.Role;
import com.example.predict_numbers.entity.User;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.mapper.UserMapper;
import com.example.predict_numbers.repository.RoleRepository;
import com.example.predict_numbers.repository.UserRepository;
import com.example.predict_numbers.service.RoleService;
import com.example.predict_numbers.service.UserService;
import com.example.predict_numbers.util.enums.ErrorCode;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final double WIN_RATE = 0.05;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 5;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> this.userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = this.userMapper.toUser(createUserRequest);
        if(StringUtils.isBlank(createUserRequest.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        Role role = this.roleService.getByName("USER");
        user.setRole(role);
        user.setScore(0);
        user.setTurns(0);
        return this.userMapper.toUserResponse(this.userRepository.save(user));
    }

    @Override
    public UserResponse getUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepository.findById(userPrincipal.getId()).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return this.userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> leaderboard() {
        Pageable userPageable = PageRequest.of(0, 10);
        return this.userRepository.findAllByOrderByScoreDescUpdatedAtAscIdAsc(userPageable)
                .stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse addTurn(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
        user.setTurns(user.getTurns() + 5);
        return this.userMapper.toUserResponse(this.userRepository.save(user));
    }


    @Override
    @Transactional
    public PredictResponse predict(PredictNumberRequest predictNumberRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int rowUpdate = this.userRepository.decreaseTurn(userPrincipal.getId());
        if(rowUpdate == 0) {
            throw new AppException(ErrorCode.PREDICT_NUMBER_NO_TURN);
        }
        boolean result = isWin(WIN_RATE);
        int resultNumber;
        String messageResult;
        if(result){
            resultNumber = predictNumberRequest.getGuessNumber();
            this.userRepository.increaseScore(userPrincipal.getId());
            messageResult="Congratulations, you guessed correctly!";
        }else{
            resultNumber = randomExcept(MIN_NUMBER, MAX_NUMBER, predictNumberRequest.getGuessNumber());
            messageResult="Predict number has been lost";
        }
        User user = this.userRepository.findById(userPrincipal.getId()).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return PredictResponse.builder()
                .result(messageResult)
                .secretNumber(resultNumber)
                .guessNumber(predictNumberRequest.getGuessNumber())
                .userId(user.getId())
                .username(user.getUsername())
                .score(user.getScore())
                .turns(user.getTurns())
                .build();
    }

    public static boolean isWin(double winRate) {
        double roll = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
        return roll < winRate;
    }
    public static int randomBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static int randomExcept(int min, int max, int except) {
        int value;
        do {
            value = randomBetween(min, max);
        } while (value == except);
        return value;
    }
}
