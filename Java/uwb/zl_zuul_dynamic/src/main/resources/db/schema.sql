create table `uwb_api_gateway` (
  `id` int(50) not null,
  `path` varchar(255) not null,
  `service_id` varchar(50) default null,
  `url` varchar(255) default null,
  `retryable` tinyint(1) default null,
  `enabled` tinyint(1) not null,
  `strip_prefix` int(11) default null,
  `api_name` varchar(255) default null,
  primary key (`id`)
) engine=innodb default charset=utf8


INSERT INTO uwb_api_gateway (id, path, service_id, retryable, strip_prefix, url, enabled) VALUES ('pppp', '/pppp/**', null,0,1, 'http://localhost:8090', 1);
INSERT INTO uwb_api_gateway (id, path, service_id, retryable, strip_prefix, url, enabled) VALUES ('xxxx', '/xxxx/**', null,0,1, 'http://localhost:8090', 1);