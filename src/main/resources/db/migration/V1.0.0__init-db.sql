CREATE TABLE IF NOT EXISTS artist
(
    id              uuid PRIMARY KEY,
    name            varchar(255) not null,
    name_aliases    varchar(255)[],
    created         timestamp with time zone not null,
    modified        timestamp with time zone not null,
    version         bigint not null
);
CREATE INDEX IF NOT EXISTS artist_created_idx ON artist (created);

CREATE TABLE IF NOT EXISTS artist_of_the_day
(
    id              uuid PRIMARY KEY,
    artist_id       uuid not null,
    seq             bigint not null,
    start_at        timestamp with time zone not null,
    end_at          timestamp with time zone not null,
    created         timestamp with time zone not null,
    CONSTRAINT artist_of_the_day_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES artist (id)
);
CREATE INDEX IF NOT EXISTS artist_of_the_day_to_idx ON artist_of_the_day (end_at);

CREATE TABLE IF NOT EXISTS track
(
    id              uuid PRIMARY KEY,
    title           varchar(255) not null,
    genre           varchar(255),
    length          numeric(59,9),
    artist_id       uuid not null,
    created         timestamp with time zone not null,
    modified        timestamp with time zone not null,
    version         bigint not null,
    CONSTRAINT track_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES artist (id)
);
CREATE INDEX IF NOT EXISTS track_artist_id_idx ON track (artist_id);
CREATE INDEX IF NOT EXISTS track_created_idx ON track (created);