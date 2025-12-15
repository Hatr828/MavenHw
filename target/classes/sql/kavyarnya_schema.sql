SELECT 'CREATE DATABASE kavyarnya WITH ENCODING ''UTF8'' LC_COLLATE ''en_US.UTF-8'' LC_CTYPE ''en_US.UTF-8'' TEMPLATE template0'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'kavyarnya')
\gexec

\connect kavyarnya
SET search_path TO public;
    
CREATE TYPE menu_category AS ENUM ('DRINK', 'DESSERT');
CREATE TYPE order_status AS ENUM ('NEW', 'IN_PROGRESS', 'PAID', 'CANCELLED');

CREATE TABLE menu_items (
    id           BIGSERIAL PRIMARY KEY,
    category     menu_category         NOT NULL,
    name_en      VARCHAR(255)          NOT NULL,
    name_uk      VARCHAR(255)          NOT NULL,
    price        NUMERIC(10,2)         NOT NULL CHECK (price > 0),
    is_available BOOLEAN               NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ           NOT NULL DEFAULT NOW()
);

CREATE TABLE staff_roles (
    id      SMALLSERIAL PRIMARY KEY,
    code    VARCHAR(32)  NOT NULL UNIQUE,
    name_en VARCHAR(100) NOT NULL,
    name_uk VARCHAR(100) NOT NULL
);

CREATE TABLE staff (
    id         BIGSERIAL PRIMARY KEY,
    full_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(32)  NOT NULL UNIQUE,
    address    TEXT         NOT NULL,
    role_id    SMALLINT     NOT NULL REFERENCES staff_roles(id),
    hired_at   DATE         NOT NULL DEFAULT CURRENT_DATE,
    active     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE customers (
    id               BIGSERIAL PRIMARY KEY,
    full_name        VARCHAR(255) NOT NULL,
    birth_date       DATE         NOT NULL,
    phone            VARCHAR(32)  NOT NULL UNIQUE,
    address          TEXT         NOT NULL,
    discount_percent NUMERIC(5,2) NOT NULL DEFAULT 0 CHECK (discount_percent BETWEEN 0 AND 100),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE work_schedule (
    id          BIGSERIAL PRIMARY KEY,
    staff_id    BIGINT      NOT NULL REFERENCES staff(id) ON DELETE CASCADE,
    shift_date  DATE        NOT NULL,
    shift_start TIME        NOT NULL,
    shift_end   TIME        NOT NULL,
    CONSTRAINT shift_time_check CHECK (shift_end > shift_start),
    UNIQUE (staff_id, shift_date, shift_start)
);

CREATE TABLE orders (
    id               BIGSERIAL PRIMARY KEY,
    customer_id      BIGINT REFERENCES customers(id),
    staff_id         BIGINT REFERENCES staff(id), -- waiter/barista handling the order
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    status           order_status NOT NULL DEFAULT 'NEW',
    discount_percent NUMERIC(5,2) NOT NULL DEFAULT 0 CHECK (discount_percent BETWEEN 0 AND 100),
    comments         TEXT
);

CREATE TABLE order_items (
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT      NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_item_id BIGINT      NOT NULL REFERENCES menu_items(id),
    quantity     INTEGER     NOT NULL CHECK (quantity > 0),
    unit_price   NUMERIC(10,2) NOT NULL CHECK (unit_price > 0),
    line_total   NUMERIC(12,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    UNIQUE (order_id, menu_item_id)
);

CREATE VIEW order_totals AS
SELECT
    o.id AS order_id,
    o.created_at,
    o.status,
    o.customer_id,
    o.staff_id,
    o.discount_percent,
    COALESCE(SUM(oi.line_total), 0)                                   AS subtotal,
    ROUND(COALESCE(SUM(oi.line_total), 0) * (1 - o.discount_percent / 100), 2) AS total_after_discount
FROM orders o
LEFT JOIN order_items oi ON oi.order_id = o.id
GROUP BY o.id;
