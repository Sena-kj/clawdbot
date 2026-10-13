create or replace view v_backtest_summary as
select
  m.symbol,
  m.start_date,
  m.end_date,
  m.news_count,
  m.avg_sentiment,
  m.avg_importance,
  m.trade_volume,
  m.start_close,
  m.end_close,
  m.price_change_percent,
  m.weight_vector_score,
  m.decision,
  m.created_at
from backtest_metric m;

create or replace view v_latest_signals as
select distinct on (symbol)
  symbol,
  start_date,
  end_date,
  decision,
  weight_vector_score,
  created_at
from backtest_signal
order by symbol, created_at desc;




