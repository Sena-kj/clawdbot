from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()


class SentimentRequest(BaseModel):
    text: str


class SentimentResponse(BaseModel):
    score: float


@app.post("/analyze", response_model=SentimentResponse)
def analyze(request: SentimentRequest) -> SentimentResponse:
    text = request.text.lower()
    if "ban" in text or "hack" in text or "lawsuit" in text:
        return SentimentResponse(score=-0.6)
    if "approve" in text or "etf" in text or "adoption" in text:
        return SentimentResponse(score=0.6)
    return SentimentResponse(score=0.0)




