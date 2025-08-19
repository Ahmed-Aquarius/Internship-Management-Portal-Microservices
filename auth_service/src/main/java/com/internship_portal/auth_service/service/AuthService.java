package com.internship_portal.auth_service.service;

import com.internship_portal.auth_service.dto.UserCredentialsDTO;
import com.internship_portal.auth_service.dto.UserDto;

public interface AuthService {

    String authenticate(UserCredentialsDTO inputCredentials, UserDto.Response userResponse);

}
