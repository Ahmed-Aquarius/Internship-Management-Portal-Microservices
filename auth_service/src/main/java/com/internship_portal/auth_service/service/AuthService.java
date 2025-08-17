package com.internship_portal.auth_service.service;

import com.internship_portal.auth_service.dto.LoginDTO;

public interface AuthService {

    String authenticate(LoginDTO credentials);

}
