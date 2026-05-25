package com.gestao.controller;

import com.fasterxml.jackson.databind.introspect.AnnotatedAndMetadata;
import com.gestao.model.Usuario;
import com.gestao.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3003"})
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro de usuários")
public class AuthController<UsuarioTokenDto> {

    @Autowired
    private UsuarioService usuarioService;

    private final String secretKey = "my-super-secret-key-1234567890ABCDEF";

    @PostMapping("/login")
    @Operation(summary = "Realiza login do usuário", description = "Autentica o usuário e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou incompletas"),
            @ApiResponse(responseCode = "401", description = "Email ou senha incorretos")
    })
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("email e password são obrigatórios");
        }

        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario == null || !usuarioService.validatePassword(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        String token = Jwts.builder()
                .setSubject(email)
                .claim("id", usuario.getId())
                .claim("name", usuario.getName())
                .claim("role", usuario.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("id", usuario.getId());
        response.put("name", usuario.getName());
        response.put("username", usuario.getEmail());
        response.put("role", usuario.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        Usuario saved = usuarioService.save(usuario);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/logout")
    @Operation(summary = "Realiza logout do usuário", description = "Finaliza a sessão do usuário")
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout realizado");
    }

    @GetMapping("/profile")
    @Operation(summary = "Obtém o perfil do usuário", description = "Retorna as informações do perfil do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("Perfil do usuário");
    }
}

