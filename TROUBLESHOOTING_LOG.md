# Microservices Authentication Troubleshooting Log

## Session Overview
This document tracks all issues encountered and solutions applied during the microservices authentication implementation.

---

## Issues Faced & Solutions

### 1. **401 Unauthorized Error on Login Endpoint**
**Issue**: Auth Service login endpoint (`POST /api/auth/login`) returning 401 unauthorized.

**Root Cause**: SecurityFilterChain was commented out in Auth Service, causing Spring Security to block all requests by default.

**Solution**: 
- Uncommented SecurityFilterChain in `auth_service/config/SecurityConfig.java`
- Configured to allow public access to `/api/auth/**` endpoints
- Added JWT authentication filter and stateless session management

**Files Modified**: `auth_service/src/main/java/com/internship_portal/auth_service/config/SecurityConfig.java`

---

### 2. **Service Discovery Issues - 503 Service Unavailable**
**Issue**: Auth Service couldn't reach User Service through Eureka discovery.

**Root Cause**: Service name mismatch between registration and calls:
- User Service registered as: `user_service` (underscore)
- Auth Service called: `user-service` (hyphen)

**Solution**: 
- Fixed Auth Service to use correct service name: `user_service`
- Updated AuthController URLs to match Eureka registration

**Files Modified**: `auth_service/src/main/java/com/internship_portal/auth_service/controller/AuthController.java`

---

### 3. **Load Balancer Configuration Issues**
**Issue**: WebClient with `@LoadBalanced` annotation causing connection timeouts.

**Root Cause**: Load balancer trying to find multiple instances of services when only single instances were running.

**Solution**: 
- Removed `@LoadBalanced` annotation from WebClient configuration
- Used direct HTTP URLs for service communication
- Kept hard-coded URLs for development simplicity

**Files Modified**: `auth_service/src/main/java/com/internship_portal/auth_service/config/AppConfig.java`

---

### 4. **User Service Security Blocking Authentication Calls**
**Issue**: User Service returning 401 for `/username/{username}` endpoint needed by Auth Service.

**Root Cause**: 
- Security configuration rule ordering problem
- JWT filter running for all requests, including public endpoints

**Solutions Applied**:
1. **Security Rule Reordering**: Moved specific patterns before general patterns in SecurityFilterChain
2. **JWT Filter Bypass**: Added `isPublicEndpoint()` method to skip JWT validation for authentication endpoints

**Files Modified**: 
- `user_service/src/main/java/com/internship_portal/user_service/config/SecurityConfig.java`
- `user_service/src/main/java/com/internship_portal/user_service/jwt_auth/JwtAuthFilter.java`

---

### 5. **JSON Deserialization Error - Model Mismatch**
**Issue**: Auth Service couldn't deserialize User Service response due to complex Role objects.

**Root Cause**: 
- User Service returned complex Role entities with database relationships
- Auth Service expected simple Role enums

**Solution**: 
- Created `UserDto.java` with record-based DTOs matching User Service structure
- Added helper method `getRoleNames()` to extract simple role strings
- Updated AuthController and AuthService to use DTOs instead of domain models

**Files Modified**:
- Created: `auth_service/src/main/java/com/internship_portal/auth_service/dto/UserDto.java`
- Modified: `auth_service/src/main/java/com/internship_portal/auth_service/controller/AuthController.java`
- Modified: `auth_service/src/main/java/com/internship_portal/auth_service/service/AuthService.java`
- Modified: `auth_service/src/main/java/com/internship_portal/auth_service/service/AuthServiceImpl.java`
- Modified: `auth_service/src/main/java/com/internship_portal/auth_service/service/JwtService.java`

---

### 6. **Password Encoding Mismatch**
**Issue**: "Invalid password" error even with correct credentials. BCryptPasswordEncoder warning: "Encoded password does not look like BCrypt".

**Root Cause**: 
- User Service used `DelegatingPasswordEncoder` (supports multiple formats)
- Auth Service initially used `BCryptPasswordEncoder` (only BCrypt format)
- Password format mismatch between services

**Solution**: 
- Changed Auth Service to use `DelegatingPasswordEncoder` to match User Service
- Both services now use same password encoding strategy

**Files Modified**: `auth_service/src/main/java/com/internship_portal/auth_service/config/SecurityConfig.java`

---

## Architecture Decisions Made

### **Service Communication**
- **Approach**: Direct HTTP calls with hard-coded URLs
- **Rationale**: Simpler for development, single-instance deployment
- **Alternative**: Service discovery with load balancing (for production)

### **DTO Strategy**
- **Approach**: Service-specific DTOs for data transfer
- **Rationale**: Clean separation, matches internship service pattern
- **Trade-off**: Some duplication vs. tight coupling

### **Security Configuration**
- **Public Endpoints**: `/api/auth/**`, `/api/users/username/*`, user listing endpoints
- **Rationale**: Required for authentication flow, avoid circular dependencies
- **Security Note**: Should be reviewed for production deployment

---

## Current System Status
✅ **Working Components**:
- Auth Service login endpoint (`POST /api/auth/login`)
- User Service authentication data endpoint
- JWT token generation and validation
- Service-to-service communication
- Password validation with proper encoding

🔄 **Pending Items**:
- Registration endpoint testing
- Production security review
- Service discovery optimization (if needed)
- Error handling improvements

---

## Key Learnings

1. **Service Discovery**: Exact service name matching is critical
2. **Security Configuration**: Rule ordering matters in Spring Security
3. **Password Encoding**: Both services must use compatible encoders
4. **Model Mapping**: DTOs prevent tight coupling between services
5. **Load Balancing**: Only needed for multi-instance deployments

---

### 7. **Service Discovery Issues in Internship Service (Recurring)**
**Issue**: Internship Service getting 403 errors when trying to create internships. Load balancer warnings: "No servers available for service: user-service".

**Root Cause**: Same service name mismatch issue as #2, but in Internship Service:
- Internship Service Feign client: `@FeignClient(name = "user-service")` (hyphen)
- User Service registers as: `user_service` (underscore)

**Solution**: 
- Fixed Feign client annotation to use correct service name: `user_service`
- This is the **third occurrence** of this naming issue across services

**Files Modified**: `internship_service/src/main/java/com/example/internship_service/client/UserServiceClient.java`

**Pattern Identified**: All services must use consistent naming convention - `user_service` (underscore) to match Eureka registration.

---

### 8. **Feign Client Hostname Validation Error**
**Issue**: Internship Service failing to start with error: "Service id not legal hostname (user_service)".

**Root Cause**: Feign Client validates service names as hostnames and doesn't accept underscores. Even though Eureka accepts `user_service`, Feign considers it invalid.

**Solution**: 
- Used direct URL approach: `@FeignClient(name = "user-service", url = "http://localhost:8082")`
- This bypasses service discovery but ensures compatibility
- Keeps the name as `user-service` (valid hostname) while using direct URL

**Files Modified**: `internship_service/src/main/java/com/example/internship_service/client/UserServiceClient.java`

**Trade-off**: Lost service discovery benefits but gained application startup stability.

---

### 9. **Service-to-Service Authentication Issue (Resolved with JWT Forwarding)**
**Issue**: Internship Service getting "Authorization header missing or invalid" when calling User Service endpoints via Feign client.

**Root Cause**: 
- User Service requires ADMIN authority for `/api/users/{id}` endpoints
- Feign client calls didn't include JWT tokens for authentication
- Service-to-service calls were being blocked by security configuration

**Solution**: 
- **Created FeignConfig with RequestInterceptor** to automatically forward JWT tokens
- **Interceptor extracts Authorization header** from incoming requests and adds it to Feign calls
- **Updated UserServiceClient** to use the FeignConfig configuration
- **Reverted public endpoint changes** to maintain proper security

**Files Modified**: 
- Created: `internship_service/src/main/java/com/example/internship_service/config/FeignConfig.java`
- Modified: `internship_service/src/main/java/com/example/internship_service/client/UserServiceClient.java`
- Reverted: `user_service/src/main/java/com/internship_portal/user_service/jwt_auth/JwtAuthFilter.java`

**Benefits**: 
- ✅ **Proper Security**: Maintains authentication requirements
- ✅ **Token Forwarding**: User's JWT token is passed through service calls
- ✅ **Authorization Context**: User Service can validate the actual user's permissions

---

*Last Updated: 2025-08-18 - Service-to-service authentication issue resolved*
