DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(52) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    pinned BOOLEAN DEFAULT FALSE ,
    title  VARCHAR(50)           NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT uq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY,
    annotation         VARCHAR(2000) UNIQUE NOT NULL,
    category_id        BIGINT               NOT NULL,
    confirmed_requests INTEGER,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000)        NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       BIGINT               NOT NULL,
    location_id        BIGINT               NOT NULL,
    paid               BOOLEAN DEFAULT FALSE,
    participant_limit  INTEGER DEFAULT 0,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT TRUE,
    state              VARCHAR(12)          NOT NULL,
    title              VARCHAR(120)         NOT NULL,
    views              INTEGER DEFAULT 0,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations (id),
    CONSTRAINT fk_initiator FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    event_id       BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT pk_compilation_event PRIMARY KEY (event_id, compilation_id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_compilation FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    event_id     BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status       VARCHAR(21),
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT uq_event_requester UNIQUE (event_id, requester_id)
);