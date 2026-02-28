# Kiire Alustamisjuhend / Quick Start Guide

## 1. Minimaalne seadistus testimiseks

### Variant A: Ilma Discord/OpenAI-ta (ainult REST API)

1. **Seadista keskkonnamuutujad:**

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export DISCORD_BOT_ENABLED=false
export OPENAI_API_KEY=dummy-key
```

2. **Loo andmebaas:**

```sql
CREATE DATABASE chatbot_db;
```

3. **Käivita rakendus:**

```bash
./mvnw spring-boot:run
```

4. **Testi REST API-d:**

```bash
# Lisa reegel
curl -X POST http://localhost:8080/api/rules \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Tere!",
    "answer": "Tere! Kuidas saan sind aidata?",
    "category": "greeting",
    "priority": 1
  }'

# Saada sõnum
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "test-user",
    "question": "Tere!"
  }'
```

### Variant B: Koos OpenAI-ga (ilma Discordita)

1. **Hangi OpenAI API võti:** https://platform.openai.com/api-keys

2. **Seadista:**

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export DISCORD_BOT_ENABLED=false
export OPENAI_API_KEY=sk-proj-...
```

3. **Käivita ja testi:** (sama mis Variant A)

### Variant C: Täielik seadistus (Discord + OpenAI)

1. **Loo Discord bot:** https://discord.com/developers/applications
   - New Application → Bot → Reset Token
   - Enable MESSAGE CONTENT INTENT
   - OAuth2 → URL Generator → bot + Send Messages
   - Kopeeri generated URL ja lisa bot oma serverisse

2. **Seadista:**

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export DISCORD_BOT_TOKEN=your-token-here
export DISCORD_BOT_ENABLED=true
export OPENAI_API_KEY=sk-proj-...
```

3. **Käivita rakendus**

4. **Kirjuta botile Discordis:** Bot peaks automaatselt vastama

## 2. Esmased testid

### Lisa reegleid:

```bash
# API dokumentatsioon
curl -X POST http://localhost:8080/api/rules \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Kust ma leian Amadeus API dokumentatsiooni?",
    "answer": "Amadeus API dokumentatsioon asub aadressil https://developers.amadeus.com/",
    "category": "API",
    "priority": 1
  }'

# ArcGIS
curl -X POST http://localhost:8080/api/rules \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Mis tüüpi koordinaate ArcGISis kasutada saab?",
    "answer": "ArcGIS toetab peamiselt WGS84, Web Mercator ja projekteeritud koordinaatsüsteeme.",
    "category": "GIS",
    "priority": 1
  }'
```

### Vaata reegleid:

```bash
curl http://localhost:8080/api/rules
```

### Testi chatbot:

```bash
# Reeglipõhine vastus (peaks vastama kohe)
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "test-user",
    "question": "Kust ma leian Amadeus API dokumentatsiooni?"
  }'

# LLM vastus (kasutab OpenAI-d, kui võti on seatud)
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "test-user",
    "question": "Mis on Spring Boot?"
  }'
```

## 3. Andmebaasi vaatamine

```sql
-- Vaata salvestatud sõnumeid
SELECT * FROM chat_messages ORDER BY created_at DESC;

-- Vaata reegleid
SELECT * FROM rules WHERE active = true;

-- Statistika
SELECT
    response_type,
    COUNT(*) as count
FROM chat_messages
GROUP BY response_type;
```

## 4. Probleemide lahendamine

### "Connection refused" - andmebaas

```bash
# Kontrolli, kas PostgreSQL töötab
brew services list  # macOS
sudo systemctl status postgresql  # Linux

# Käivita PostgreSQL
brew services start postgresql  # macOS
sudo systemctl start postgresql  # Linux
```

### Discord bot ei reageeri

- Kontrolli, kas MESSAGE CONTENT INTENT on lubatud
- Vaata logi: kas bot ühendus õnnestus?
- Kontrolli, kas token on õige

### OpenAI vead

- Kontrolli API key-d
- Vaata OpenAI konto krediiti: https://platform.openai.com/usage
- Kontrolli rate limits

## 5. Mida saab järgmisena teha?

- **Lisa reegleid** tegelike küsimuste jaoks
- **Testi Discordiga** (kui seadistatud)
- **Tutvu koodiga** - vaata kuidas 3-tasandiline süsteem töötab
- **Kirjuta teste** - RuleService, ChatService
- **Lisa valideerimist** - sanitization (iteratsioon 2)
- **Alusta RAG-iga** - pgvector, embeddings (iteratsioon 3)
