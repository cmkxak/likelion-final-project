-- 프로그램 실행 시, 초기에 ADMIN 유저가 하나 존재하도록 구현
insert into mutsasns.user values(1, NOW(), null,
NOW(), '$2a$10$wsYiB52cPISSxMj8DnAqku4a1CcGNDLdX4G6CQcbOkI0AsgTTgje2', 'ADMIN', 'root');