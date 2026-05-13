CREATE DATABASE IF NOT EXISTS thu_canteen DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE thu_canteen;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    nickname VARCHAR(32) NOT NULL,
    avatar_url VARCHAR(255),
    role VARCHAR(16) NOT NULL DEFAULT 'USER',
    taste_preference VARCHAR(128),
    campus_card_authorized TINYINT(1) NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS canteens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    cover_url VARCHAR(255),
    address VARCHAR(255),
    open_hours VARCHAR(128),
    pay_methods VARCHAR(128),
    on_campus TINYINT(1) NOT NULL DEFAULT 1,
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_canteen_name (name)
);

CREATE TABLE IF NOT EXISTS canteen_windows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    canteen_id BIGINT NOT NULL,
    floor_no INT NOT NULL,
    name VARCHAR(64) NOT NULL,
    open_hours VARCHAR(128),
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_window_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    INDEX idx_window_canteen_floor (canteen_id, floor_no)
);

CREATE TABLE IF NOT EXISTS dishes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    canteen_id BIGINT NOT NULL,
    window_id BIGINT,
    name VARCHAR(64) NOT NULL,
    image_url VARCHAR(255),
    price DECIMAL(8, 2) DEFAULT 0,
    description VARCHAR(500),
    spice_level INT NOT NULL DEFAULT 0,
    status VARCHAR(16) NOT NULL DEFAULT 'APPROVED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_dish_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    CONSTRAINT fk_dish_window FOREIGN KEY (window_id) REFERENCES canteen_windows(id),
    INDEX idx_dish_search (name, canteen_id, status)
);

CREATE TABLE IF NOT EXISTS dish_tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dish_id BIGINT NOT NULL,
    tag VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tag_dish FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
    UNIQUE KEY uk_dish_tag (dish_id, tag),
    INDEX idx_tag (tag)
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dish_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL,
    content VARCHAR(500) NOT NULL,
    image_url VARCHAR(255),
    status VARCHAR(16) NOT NULL DEFAULT 'APPROVED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_dish FOREIGN KEY (dish_id) REFERENCES dishes(id),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uk_review_once (dish_id, user_id),
    INDEX idx_review_dish_status (dish_id, status)
);

CREATE TABLE IF NOT EXISTS review_votes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    review_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    vote INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_vote_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
    CONSTRAINT fk_vote_user FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uk_review_vote_user (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_favorite_dish FOREIGN KEY (dish_id) REFERENCES dishes(id),
    UNIQUE KEY uk_user_dish_favorite (user_id, dish_id)
);

CREATE TABLE IF NOT EXISTS crowd_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    canteen_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    level INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_crowd_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    CONSTRAINT fk_crowd_user FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_crowd_recent (canteen_id, created_at)
);

CREATE TABLE IF NOT EXISTS announcements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(80) NOT NULL,
    content VARCHAR(500) NOT NULL,
    starts_at DATETIME NOT NULL,
    ends_at DATETIME NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dish_submissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    submitter_id BIGINT NOT NULL,
    canteen_id BIGINT NOT NULL,
    window_id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    image_url VARCHAR(255),
    price DECIMAL(8, 2),
    description VARCHAR(500),
    spice_level INT NOT NULL DEFAULT 0,
    tags VARCHAR(255),
    audit_status VARCHAR(16) NOT NULL DEFAULT 'PENDING',
    audit_reason VARCHAR(200),
    approved_dish_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_submission_user FOREIGN KEY (submitter_id) REFERENCES users(id),
    CONSTRAINT fk_submission_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    CONSTRAINT fk_submission_window FOREIGN KEY (window_id) REFERENCES canteen_windows(id)
);

CREATE TABLE IF NOT EXISTS consumption_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    canteen_id BIGINT NOT NULL,
    amount DECIMAL(8, 2) NOT NULL,
    consumed_at DATETIME NOT NULL,
    CONSTRAINT fk_consumption_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_consumption_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    INDEX idx_consumption_heatmap (canteen_id, user_id, consumed_at)
);
