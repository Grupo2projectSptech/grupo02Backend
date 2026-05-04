package com.gestao.controller;

import com.gestao.model.Usuario;
import com.gestao.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    private final String secretKey = "my-super-secret-key-1234567890ABCDEF";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username e password são obrigatórios");
        }

        Usuario usuario = usuarioService.findByUsername(username);
        if (usuario == null || !usuarioService.validatePassword(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        String token = Jwts.builder()
                .setSubject(username)
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
        response.put("username", usuario.getUsername());
        response.put("role", usuario.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        Usuario saved = usuarioService.save(usuario);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout realizado");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("Perfil do usuário");
    }
}

