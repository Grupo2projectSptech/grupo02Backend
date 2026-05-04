# Swagger/OpenAPI - Documentação da API

Este projeto inclui integração completa com Swagger (OpenAPI 3.0) para documentação interativa da API.

## Acessando a Documentação

Após iniciar a aplicação, você pode acessar a documentação do Swagger em:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)
- **OpenAPI YAML**: [http://localhost:8080/api-docs.yaml](http://localhost:8080/api-docs.yaml)

## Recursos

### Definido na Configuração

- **Título**: API de Gestão
- **Versão**: 1.0.0
- **Descrição**: API para gestão de Empresas, Fornecedores e Produtos
- **Autenticação**: JWT (Bearer Token)

### Endpoints Documentados

A API possui os seguintes grupos de endpoints:

#### 1. **Autenticação** (`/api/auth`)
- `POST /login` - Realiza login do usuário
- `POST /register` - Registra um novo usuário
- `POST /logout` - Realiza logout
- `GET /profile` - Obtém o perfil do usuário autenticado

#### 2. **Empresas** (`/api/empresas`)
- `GET /` - Lista todas as empresas
- `GET /{id}` - Obtém uma empresa específica
- `POST /` - Cria uma nova empresa
- `PUT /{id}` - Atualiza uma empresa
- `DELETE /{id}` - Deleta uma empresa

#### 3. **Fornecedores** (`/api/fornecedores`)
- `GET /` - Lista todos os fornecedores
- `GET /ativos` - Lista fornecedores ativos
- `GET /{id}` - Obtém um fornecedor específico
- `POST /` - Cria um novo fornecedor
- `PUT /{id}` - Atualiza um fornecedor
- `DELETE /{id}` - Deleta um fornecedor

#### 4. **Produtos** (`/api/produtos`)
- `GET /` - Lista todos os produtos
- `GET /{id}` - Obtém um produto específico
- `GET /fornecedor/{fornecedorId}` - Lista produtos por fornecedor
- `GET /empresa/{empresaId}` - Lista produtos por empresa
- `POST /` - Cria um novo produto
- `PUT /{id}` - Atualiza um produto
- `DELETE /{id}` - Deleta um produto

## Testando a API

1. Acesse [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
2. Cada endpoint possui um botão "Try it out"
3. Para endpoints protegidos, autorize a requisição usando o token JWT obtido do endpoint `/api/auth/login`

### Usando Autenticação

1. Execute o endpoint `POST /api/auth/login` com as credenciais:
   ```json
   {
     "username": "admin",
     "password": "password123"
   }
   ```

2. Copie o token JWT retornado

3. Clique no botão "Authorize" no canto superior direito

4. Cole o token no campo com o prefixo "Bearer" (ou deixe como "Bearer <seu_token>")

5. Agora você pode testar os endpoints protegidos

## Configuração

A configuração do Swagger está em:
- `SwaggerConfig.java` - Configuração customizada da API
- `application.properties` - Propriedades do Swagger

Você pode customizar:
- Título e descrição da API
- Versão
- Informações de contato
- Esquemas de autenticação
- Etc.

## Dependências

A integração do Swagger utiliza:
- `springdoc-openapi-starter-webmvc-ui:2.3.0` - Integração do OpenAPI com Spring Boot

## Documentação Adicional

- [Springdoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.0)
- [Swagger Documentation](https://swagger.io/docs/)
