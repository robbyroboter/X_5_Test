# Система управления банковскими картами

Backend API для управления банковскими картами с JWT-аутентификацией, шифрованием номеров карт и ролевым доступом.

✨ Возможности
🔐 JWT аутентификация (admin/admin123, user/user123)

🛡️ RBAC (Role-Based Access Control): ADMIN/USER

💳 Шифрование номеров карт (AES/GCM)

🧾 Маскировка номеров (**** **** **** 1234)

💸 Переводы между картами

📊 CRUD операции с пагинацией и фильтрами

📄 OpenAPI 3 / Swagger UI

🗄️ Liquibase миграции

🐳 Docker Compose

🏗️ Структура проекта

src/main/java/com/example/bankcards/
├── config/           # SecurityConfig
├── controller/       # REST API
├── dto/             # Data Transfer Objects
├── entity/          # JPA Entities
├── exception/       # GlobalExceptionHandler
├── repository/      # Spring Data JPA
├── security/        # JWT + CustomUserDetails
├── service/         # Business Logic
└── util/            # CardNumberMasker, Encryptor

git clone <repo>
cd bankcards
mvn clean compile

# БД
docker-compose up -d db

# Приложение (Liquibase применит миграции)
mvn spring-boot:run
docker-compose up --build

Тестирование API
🔐 Аутентификация

# ADMIN
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"admin123"}'

# USER
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"user","password":"user123"}'

