Gerar token JWT local:
  set JWT_SECRET=uma_chave_segura
  set JWT_SUBJECT=admin
  python -m venv .venv
  .venv\Scripts\activate
  pip install -r requirements.txt
  python generate_token.py

Voce pode sobrescrever:
  JWT_ISSUER, JWT_EXP_MINUTES




