DROP TABLE IF EXISTS statistic CASCADE;

CREATE TABLE IF NOT EXISTS statistics
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app  VARCHAR(256)                NOT NULL,
    uri  VARCHAR(256)                NOT NULL,
    ip   VARCHAR(256)                NOT NULL,
    time TIMESTAMP WITHOUT TIME ZONE NOT NULL
);