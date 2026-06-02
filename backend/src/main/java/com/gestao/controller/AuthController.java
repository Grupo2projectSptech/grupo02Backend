package com.gestao.controller;

import com.gestao.dto.AuthResponseDTO;
import com.gestao.dto.LoginRequestDTO;
import com.gestao.dto.RegisterRequestDTO;
import com.gestao.model.Usuario;
import com.gestao.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:3000", "http://localhost:3001",
    "http://localhost:3003", "http://localhost:5173"
})
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro de usuários")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // Chave mínima de 32 chars para HS256
    private final String secretKey = "my-super-secret-key-1234567890AB";

    // ── LOGIN ────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    @Operation(summary = "Realiza login", description = "Autentica o usuário e retorna token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Credenciais incorretas")
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmail());

        if (usuario == null || !usuarioService.validatePassword(dto.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body(buildError("Credenciais inválidas"));
        }

        String token = buildToken(usuario);

        AuthResponseDTO response = new AuthResponseDTO(
                token,
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),   // frontend usa campo "username"
                usuario.getRole()
        );

        return ResponseEntity.ok(response);
    }

    // ── REGISTER ─────────────────────────────────────────────────────────────

    @PostMapping("/register")
    @Operation(summary = "Registra novo usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado")
    })
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO dto) {
        Usuario saved = usuarioService.register(dto);

        // Retorna apenas dados públicos (sem senha)
        AuthResponseDTO response = new AuthResponseDTO(
                null,
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole()
        );

        return ResponseEntity.status(201).body(response);
    }

    // ── LOGOUT ───────────────────────────────────────────────────────────────

    @PostMapping("/logout")
    @Operation(summary = "Realiza logout")
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(java.util.Map.of("message", "Logout realizado"));
    }

    // ── PROFILE ──────────────────────────────────────────────────────────────

    @GetMapping("/profile")
    @Operation(summary = "Perfil do usuário autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil obtido com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String authHeader) {
        // O token já foi validado pelo filtro de segurança (ou está público conforme SecurityConfig)
        return ResponseEntity.ok(java.util.Map.of("message", "Token válido"));
    }

    // ── REFRESH TOKEN ─────────────────────────────────────────────────────────

    @PostMapping("/refresh")
    @Operation(summary = "Renova o token JWT")
    @ApiResponse(responseCode = "200", description = "Token renovado")
    public ResponseEntity<?> refresh(@RequestBody java.util.Map<String, String> body) {
        // Implementação simples: front envia refreshToken (ignoramos, geramos novo a partir do e-mail)
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(buildError("refreshToken ausente"));
        }
        // Para esta implementação stateless retornamos erro solicitando novo login
        return ResponseEntity.status(401).body(buildError("Sessão expirada, faça login novamente"));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String buildToken(Usuario u) {
        return Jwts.builder()
                .setSubject(u.getEmail())
                .claim("id",   u.getId())
                .claim("name", u.getName())
                .claim("role", u.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000L)) // 10 dias
                .signWith(
                    Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
                    SignatureAlgorithm.HS256
                )
                .compact();
    }

    private java.util.Map<String, String> buildError(String msg) {
        return java.util.Map.of("message", msg);
    }
}
