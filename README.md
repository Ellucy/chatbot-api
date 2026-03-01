# AI Chatbot API

Backend teenus AI-põhise vestlusroboti jaoks, mis vastab juhtide klassikalistele küsimustele.

## Tehniline Stack

- **Backend**: Java 21 + Spring Boot 4.0.3
- **Andmebaas**: PostgreSQL (+ pgvector RAG-i jaoks tulevikus)
- **AI**: OpenAI API (GPT-4o-mini, text-embedding-3-small)
- **Chat Platform**: Discord (Discord4J)
- **Vastuse strateegia**: 3-tasandiline (Reeglid → RAG → LLM)

## Arhitektuur

```
Discord / REST API
        ↓
  ChatService (Orchestrator)
        ↓
   ┌────┼────┐
   ↓    ↓    ↓
Rule  RAG  LLM
Based      (OpenAI)
```

### Vastuse voog:

1. **Reeglipõhine vastus** (kõige kiirem, tasuta) - kontrollitakse, kas küsimus vastab eeldefineeritud reeglile
2. **RAG vastus** (keskmise kiirus, odav) - otsitakse vastust dokumentidest
3. **LLM vastus** (aeglasem, kulukam) - OpenAI genereerib vastuse

## Eeltingimused

- Java 21+
- Maven 3.8+
- PostgreSQL 15+
- Discord Bot Token (valikuline)
- OpenAI API võti (valikuline testimiseks)

## Kiire Start

Kui andmebaas on juba olemas:

```bash
export OPENAI_API_KEY="your-api-key-here"
./mvnw spring-boot:run
```

Koos Discord bot tokeniga:

```bash
export OPENAI_API_KEY="your-api-key-here"
export DISCORD_BOT_TOKEN="your-discord-token-here"
export DISCORD_BOT_ENABLED=true
./mvnw spring-boot:run

```

Rakendus käivitub aadressil: `http://localhost:8080`

## Paigaldamine

### 1. Klooni repositoorium

```bash
git clone <repo-url>
cd chatbot-api
```

### 2. Seadista andmebaas

Loo PostgreSQL andmebaas:

```sql
CREATE DATABASE chatbot_db;
```

### 3. Seadista keskkonnamuutujad

Loo fail `.env` või seadista keskkonnamuutujad:

```bash
# Andmebaas
export DB_USERNAME=postgres
export DB_PASSWORD=your_password

# Discord Bot
export DISCORD_BOT_TOKEN=your_discord_bot_token
export DISCORD_BOT_ENABLED=true

# OpenAI
export OPENAI_API_KEY=your_openai_api_key
```

### 4. Käivita rakendus

```bash
# Seadista OpenAI võti ja käivita
export OPENAI_API_KEY="your-api-key-here"
./mvnw spring-boot:run
```

Rakendus käivitub aadressil: `http://localhost:8080`

**Märkus**: Esimesel käivitusel kompileeritakse projekt automaatselt. `./mvnw clean install` käivitamine pole vajalik.

## Kasutamine

### REST API

#### Saada sõnum chatbotile:

```bash
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "question": "Mis on Spring Boot?"
  }'
```

#### Lisa uus reegel:

```bash
curl -X POST http://localhost:8080/api/rules \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Kust ma leian Amadeus API dokumentatsiooni?",
    "answer": "Amadeus API dokumentatsioon asub aadressil https://developers.amadeus.com/",
    "category": "API",
    "priority": 1
  }'
```

#### Vaata kõiki reegleid:

```bash
curl http://localhost:8080/api/rules
```

### Discord Bot

Kui Discord bot on seadistatud ja lubatud:

1. Lisa bot oma Discord serverisse
2. Kirjuta botile sõnum
3. Bot vastab automaatselt kasutades 3-tasandilist strateegiat

## Konfiguratsioon

Põhilised konfiguratsioonid asuvad failis `src/main/resources/application.yaml`:

- `discord.bot.enabled` - luba/keela Discord bot
- `openai.api.max-tokens` - maksimaalne tokenite arv vastuses (kulukontroll)
- `chatbot.rules.enabled` - luba/keela reeglipõhised vastused
- `chatbot.rag.enabled` - luba/keela RAG (tulevikus)
- `chatbot.llm.fallback-enabled` - luba/keela LLM fallback

## Projektstruktuur

```
src/main/java/com/team12/aichatbot/chatbot_api/
├── config/          # Konfiguratsioonid (OpenAI, Discord, Security)
├── controller/      # REST kontrollerid
├── dto/            # Andmete ülekande objektid
├── entity/         # JPA entiteedid
├── repository/     # Andmebaasi repositooriumid
└── service/        # Äriloogika teenused
```

## Testimine

```bash
# Käivita testid
./mvnw test
```

## Iteratsioonid

### ✅ Iteratsioon 1 (MVP)

- Discord bot integratsioon
- OpenAI LLM integratsioon
- Reeglipõhised vastused
- REST API
- PostgreSQL andmebaas

### 🔄 Iteratsioon 2

- Sanitization ja filtreerimine
- Täiustatud unit testid
- Funktsionaalsed testid

### 📅 Iteratsioon 3

- RAG implementatsioon
- pgvector integratsioon
- Embeddings genereerimine
- Semantiline otsing

### 📅 Iteratsioon 4

- Liquibase migratsioonid
- React frontend
