Auth-Common-Library
This library provides a centralized, "Zero-Config" security solution for a Spring Boot microservices ecosystem. It allows you to share security logic, JWT validation, and user principal extraction across multiple services without duplicating code or connecting every microservice to the Auth database.

🚀 The Core Idea
In a standard microservice architecture, every service needs to validate incoming JWTs. Instead of writing the same JwtFilter and SecurityConfig in every service (Order, Product, etc.), this library acts as a Spring Boot Starter.

When a microservice includes this JAR, it automatically:

Intercepts every request.

Validates the JWT signature using a shared secret.

Extracts User ID, Username, and Roles.

Populates the Spring Security Context for use with @PreAuthorize.

🧠 Logic & Architecture
The library is designed to be Database-Independent for non-auth services.

Token Validation: Uses io.jsonwebtoken (JJWT) to verify the token signature locally within the microservice.

Principal Extraction: Instead of a DB-backed UserDetailsService, the filter parses the JWT claims directly to create a UserPrincipal.

Auto-Configuration: Uses the Spring Boot 3 AutoConfiguration.imports mechanism to register security beans automatically upon application startup.

Fallback Prevention: Includes a dummy UserDetailsService bean to prevent Spring from generating a default security password in the logs.

🛠️ Usage
1. Install the Library
   In the auth-common-library directory, run:

Bash

mvn clean install
2. Add Dependency
   Add the following to your Microservice pom.xml:

XML

<dependency>
    <groupId>com.example</groupId>
    <artifactId>auth-common-library</artifactId>
    <version>1.0.0</version>
</dependency>
3. Provide the Secret Key
Every microservice must have the secret key used by the AuthService in its application.properties:

Properties

jwt.secret=your_shared_secret_key_here
4. Zero Local Config
   You do not need to write a SecurityConfig class in your microservice. The library handles:

Stateless Session Management.

Whitelisting /public/** and /actuator/**.

Adding the SharedJwtFilter to the filter chain.

🔐 Security Configuration Handling
By design, all security configurations for the microservices are managed within this library.

Global Security: Changes made to SharedSecurityConfig in this library (like adding a new global whitelist) will propagate to all microservices upon their next build.

Method Security: You can use @PreAuthorize("hasRole('ADMIN')") in any microservice controller immediately.

Customization: If a specific service needs a unique security rule, it can still define a LocalSecurityConfig with a @Order(1) annotation to take precedence over the library.

📁 Project Structure
com.example.common.config: Contains the SharedSecurityConfig (Auto-Configuration).

com.example.common.security: Contains AuthUtil and SharedJwtFilter.

META-INF/spring/: Contains the .imports file that enables the "Magic" auto-loading.