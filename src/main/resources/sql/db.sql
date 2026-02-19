create database push_down_appro;

create user mon_user with password 'mon_password';

GRANT ALL PRIVILEGES ON DATABASE push_down_appro TO mon_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO mon_user;
GRANT ALL ON SCHEMA public TO mon_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO mon_user;

