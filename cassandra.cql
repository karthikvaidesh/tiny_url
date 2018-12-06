create keyspace tinyurl with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
create table tinyurl.urls ( tiny_url text, long_url text, count int, primary key (tiny_url) );
create table tinyurl.url_visits ( id text, tiny_url text, long_url text, primary, time timestamp,  key (id) );
create table tinyurl.summary ( tiny_url text, long_url text, count int, primary key (tiny_url, count) ) with clustering order by (count desc);
