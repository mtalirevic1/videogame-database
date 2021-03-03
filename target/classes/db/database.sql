BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `developer`
(
    `id`          integer,
    `name`        text,
    `description` text,
    `icon`        text,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `video_game`
(
    `id`           INTEGER,
    `name`         text,
    `genre`        text,
    `dev_id`       INTEGER,
    `description`  text,
    `release_date` date,
    `image_link`   text,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`dev_id`) REFERENCES `developer` (`id`)
);

CREATE TABLE IF NOT EXISTS `user_account`
(
    `id`       integer,
    `username` text,
    `password` text,
    `avatar`   text,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `admin_account`
(
    `id`       integer,
    `username` text,
    `password` text,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `game_review`
(
    `game_id` integer,
    `user_id` integer,
    `score` integer,
    `comment` text,
    FOREIGN KEY (`game_id`) REFERENCES `video_game`,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`
);

COMMIT;