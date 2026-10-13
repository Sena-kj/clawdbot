create table if not exists ohlc_candle (
  id bigserial primary key,
  symbol text not null,
  open_time timestamptz not null,
  open double precision not null,
  high double precision not null,
  low double precision not null,
  close double precision not null,
  volume double precision not null,
  created_at timestamptz not null
);

create table if not exists news_item (
  id bigserial primary key,
  symbol text not null,
  title text not null,
  description text,
  source text not null,
  published_at timestamptz not null,
  sentiment_score double precision not null,
  importance_score double precision not null,
  created_at timestamptz not null
);

create table if not exists backtest_result (
  id bigserial primary key,
  symbol text not null,
  start_date date not null,
  end_date date not null,
  weight_vector_score double precision not null,
  price_change_percent double precision not null,
  explanation text not null,
  created_at timestamptz not null
);

create table if not exists backtest_signal (
  id bigserial primary key,
  symbol text not null,
  start_date date not null,
  end_date date not null,
  decision text not null,
  weight_vector_score double precision not null,
  created_at timestamptz not null
);

create table if not exists backtest_metric (
  id bigserial primary key,
  symbol text not null,
  start_date date not null,
  end_date date not null,
  news_count integer not null,
  avg_sentiment double precision not null,
  avg_importance double precision not null,
  trade_volume double precision not null,
  start_close double precision not null,
  end_close double precision not null,
  price_change_percent double precision not null,
  weight_vector_score double precision not null,
  decision text not null,
  created_at timestamptz not null
);

create index if not exists idx_news_item_symbol_date on news_item(symbol, published_at);
create index if not exists idx_candle_symbol_time on ohlc_candle(symbol, open_time);
create index if not exists idx_backtest_symbol_date on backtest_result(symbol, start_date, end_date);
create index if not exists idx_backtest_signal_symbol_date on backtest_signal(symbol, start_date, end_date);
create index if not exists idx_backtest_metric_symbol_date on backtest_metric(symbol, start_date, end_date);

