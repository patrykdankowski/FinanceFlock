package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.dto.LoginDto;
import com.patrykdankowski.financeflock.dto.RegisterDto;

public interface AuthenticationService {

    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);

}
