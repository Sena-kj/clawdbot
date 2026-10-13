import os
import time

import jwt


def main() -> None:
    secret = os.getenv("JWT_SECRET")
    issuer = os.getenv("JWT_ISSUER", "clawdbot")
    subject = os.getenv("JWT_SUBJECT", "admin")
    exp_minutes = int(os.getenv("JWT_EXP_MINUTES", "120"))

    if not secret:
        raise RuntimeError("JWT_SECRET nao configurado.")

    now = int(time.time())
    payload = {
        "iss": issuer,
        "sub": subject,
        "iat": now,
        "exp": now + exp_minutes * 60,
    }
    token = jwt.encode(payload, secret, algorithm="HS256")
    print(token)


if __name__ == "__main__":
    main()




