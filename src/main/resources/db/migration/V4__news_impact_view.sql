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




