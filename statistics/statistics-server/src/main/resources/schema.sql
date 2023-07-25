DROP TABLE IF EXISTS endpoint_hits;

CREATE TABLE IF NOT EXISTS endpoint_hits
(
    endpoint_hit_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app     VARCHAR(255)  NOT NULL,
    uri     VARCHAR(2048) NOT NULL,
    ip      VARCHAR(15)   NOT NULL,
    created timestamp NOT NULL
);