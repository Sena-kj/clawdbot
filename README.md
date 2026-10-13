# ClawdBot

Backend de pesquisa que cruza notícias de cripto com candles da Binance para medir se o "peso" das notícias de uma semana explicaria o movimento do BTC e do ETH, gerando um backtest com decisão de compra, venda ou espera.

## Stack

- Java 17, Spring Boot 3.2 (Web, Data JPA, Security), Maven
- PostgreSQL (Supabase) com migrations Flyway (`V1` a `V4`)
- JWT (jjwt 0.11) e API key para autenticação
- Microserviço de sentimento em Python: FastAPI + Uvicorn (`python/nlp`)
- APIs externas: Binance (klines OHLC), CryptoCompare e NewsAPI

## Destaques técnicos

- **Pipeline de backtest** (`WeeklyBacktestService`): baixa candles diários da Binance, coleta notícias do período, filtra ruído, pontua cada notícia e decide `BUY` / `SELL` / `HOLD` por limiar (±0,4), gerando uma narrativa com a notícia de maior impacto.
- **Weight vector** (`WeightVectorScorer`): sentimento base ajustado por bônus de fonte de alta autoridade, penalidade para termos de hype, bônus de importância e um fator de volume (notícias / volume negociado), sempre limitado a [-1, 1]. Tem teste unitário.
- **Sentimento plugável**: interface `SentimentAnalyzer` com implementação HTTP (chama o serviço FastAPI, `@Primary`) e implementação por regras; provedores de notícia também atrás de uma interface (`NewsProvider`, CryptoCompare como primário).
- **Segurança em camadas** no Spring Security: filtro de rate limit por IP com janela e whitelist, filtro de API key e filtro JWT; CORS por perfil (`dev` / `prod`) e validação de configuração no startup (`StartupValidationRunner`).
- **Persistência opcional**: com `PERSISTENCE_ENABLED=true` grava candles, notícias, resultados, sinais e métricas; views SQL (`v_backtest_summary`, `v_symbol_kpi`, `v_news_impact`...) alimentam a API de consulta paginada em `/api/backtests`.
- **Nenhum segredo no código**: todas as chaves vêm de variáveis de ambiente (`application.yml` só tem placeholders).

## Como rodar

Requisitos: JDK 17, Maven e Python 3.10+.

1. Configure as variáveis de ambiente a partir de [`.env.example`](.env.example) (chaves das APIs, `JWT_SECRET`, `API_KEY` e, se for persistir, a conexão Postgres).

2. Serviço de sentimento (porta 8000):

```bash
cd python/nlp
python -m venv .venv
.venv\Scripts\activate          # Linux/macOS: source .venv/bin/activate
pip install -r requirements.txt
uvicorn app:app --host 0.0.0.0 --port 8000
```

3. Aplicação Spring (roda o backtest de BTCUSDT e ETHUSDT no startup e sobe a API):

```bash
mvn spring-boot:run
mvn test
```

4. Token JWT para testar a API localmente: `scripts/generate_token.py` (instruções em `scripts/README.txt`).

## Estrutura

```
src/main/java/com/clawdbot/
  backtest/      serviço e runner do backtest semanal
  weight/        cálculo do weight vector
  news/          filtro de informação (hype, autoridade)
  nlp/           analisadores de sentimento (HTTP e regras)
  infra/         clientes Binance, CryptoCompare e NewsAPI
  persistence/   entidades, repositórios e serviço JPA
  security/      JWT, API key, rate limit, CORS
  api/           endpoints de auth e consulta de backtests
src/main/resources/db/migration/   migrations Flyway
python/nlp/      microserviço FastAPI de sentimento
scripts/         geração de token JWT
docs/            schema e views SQL de referência
```

## Status

Projeto pessoal de estudo. O serviço de sentimento em Python ainda é um classificador por palavras-chave, pensado para ser trocado por um modelo de NLP sem mudar o lado Java.

## English

ClawdBot is a Java 17 / Spring Boot research backend that backtests whether a week of crypto news explains BTC and ETH price moves.
It scores news with a weighted vector (sentiment, source authority, hype penalty, volume), pulls OHLC from Binance and stores results in Postgres via Flyway.
Sentiment runs in a pluggable FastAPI microservice; the API is protected by JWT, API key and rate limiting.
