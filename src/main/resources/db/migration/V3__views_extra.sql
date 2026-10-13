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




