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

create or replace view v_top_news as
select
  symbol,
  title,
  source,
  published_at,
  sentiment_score,
  importance_score
from news_item
order by importance_score desc, published_at desc;

create or replace view v_symbol_kpi as
select
  symbol,
  count(*) as total_backtests,
  avg(price_change_percent) as avg_price_change,
  avg(weight_vector_score) as avg_weight_score
from backtest_metric
group by symbol;

create or replace view v_news_impact as
select
  n.symbol,
  n.title,
  n.source,
  n.published_at,
  n.sentiment_score,
  n.importance_score,
  avg(m.price_change_percent) as avg_price_change,
  count(*) as backtest_count
from news_item n
join backtest_metric m
  on m.symbol = n.symbol
 and date(n.published_at) between m.start_date and m.end_date
group by
  n.symbol,
  n.title,
  n.source,
  n.published_at,
  n.sentiment_score,
  n.importance_score;

