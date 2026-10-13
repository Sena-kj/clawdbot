Scripts de NLP para o ClawdBot.
Entrada esperada: texto de noticia.
Saida esperada: score de sentimento entre -1 e 1.
Rodar o servico:
  python -m venv .venv
  .venv\Scripts\activate
  pip install -r requirements.txt
  uvicorn app:app --host 0.0.0.0 --port 8000

