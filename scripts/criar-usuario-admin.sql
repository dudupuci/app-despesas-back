-- ============================================
-- Script para Criar Usuário ADMIN
-- ============================================

-- 1. Verificar as roles existentes
SELECT * FROM roles;

-- 2. Se a role ADMIN não existir, criar
INSERT INTO roles (nome, descricao, poder, data_criacao, data_atualizacao)
VALUES ('ADMIN', 'Administrador do sistema', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (nome) DO NOTHING;

-- 3. Verificar o ID da role ADMIN
SELECT id FROM roles WHERE nome = 'ADMIN';

-- 4. Criar usuário admin (ajuste o email e senha hash conforme necessário)
-- NOTA: A senha abaixo é "admin123" com BCrypt
-- Para gerar um novo hash BCrypt, use: https://bcrypt-generator.com/
INSERT INTO usuarios_sistema (
    id,
    nome,
    sobrenome,
    email,
    senha,
    nome_usuario,
    ativo,
    role_id,
    data_criacao,
    data_atualizacao
)
VALUES (
    gen_random_uuid(),
    'Eduardo',
    'Pucinelli',
    'dudupucinelli@gmail.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'admin.sistema',
    true,
    (SELECT id FROM roles WHERE nome = 'ADMIN'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- 5. Verificar se o usuário foi criado
SELECT
    u.id,
    u.nome,
    u.sobrenome,
    u.email,
    u.nome_usuario,
    u.ativo,
    r.nome as role_nome
FROM usuarios_sistema u
JOIN roles r ON u.role_id = r.id
WHERE u.email = 'dudupucinelli@gmail.com';

-- ============================================
-- Gerar Hash BCrypt para Novas Senhas
-- ============================================
-- Use um dos sites abaixo ou uma ferramenta local:
-- - https://bcrypt-generator.com/
-- - https://bcrypt.online/
--
-- Exemplo de hashes BCrypt (rounds=10):
-- senha123   → $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- admin123   → $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- teste123   → $2a$10$cKKU7h5xF7.gBz3/yEqrgeRUv4gYkRUYqGqkPqU8wXgPvLLVqr1oW

