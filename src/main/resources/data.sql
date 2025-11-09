
INSERT INTO member
    (member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('1234@example.com',
     '1234',
     '관리자A',
     'dev_admin',
     'ADMIN',
     '2000-05-15',
     '2025-11-06');

INSERT INTO member
    (member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('5678@mail.com',
     '5678',
     '테스트입찰자',
     'bidder',
     'MEMBER',
     '2005-11-20',
     '2025-11-06');

INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('chaerinss01@gmail.com',
     '$2a$10$ExYVPy.cJxkl2w8Hjj8wj.Gz.ORdW8ZAEQBl5y2vNwoWZrMwEqTT6',
     '송채린',
     '채린',
     'MEMBER',
     '2005-11-20',
     '2025-11-06');

INSERT INTO product (member_id, post_name, category, min_price, delivery_method, description, image_url, duration_days, duration_hours, duration_minutes, views, current_price, created_at, is_ended)
VALUES (2, '상품 A', '공예', 1000, '일반택배', '테스트 상품', '/images/product1.png', 2, 1, 30, 0, 1000,  CURRENT_TIMESTAMP(),
                                                                               FALSE);


INSERT INTO product (member_id, post_name, category, min_price, delivery_method, description, image_url, duration_days, duration_hours, duration_minutes, views, current_price, created_at, is_ended)
VALUES (2, '상품 1', '공예', 40000, '편의점택배', '테스트 상품', null, 2, 1, 30, 0, 40000, CURRENT_TIMESTAMP(),
                                                                                 FALSE);

INSERT INTO product (member_id, post_name, category, min_price, delivery_method, description, image_url, duration_days, duration_hours, duration_minutes, views, current_price, created_at, is_ended)
VALUES (2, '상품 2', '공예', 60000, '일반택배', '테스트 상품', null, 2, 1, 30, 0, 60000, CURRENT_TIMESTAMP(),
                                                                                FALSE);

INSERT INTO product (member_id, post_name, category, min_price, delivery_method, description, image_url, duration_days, duration_hours, duration_minutes, views, current_price, created_at, is_ended)
VALUES (2, '상품 3', '공예', 220000, '일반택배', '테스트 상품', null, 2, 1, 30, 0, 220000, CURRENT_TIMESTAMP(),
                                                                                  FALSE);

INSERT INTO product (member_id, post_name, category, min_price, delivery_method, description, image_url, duration_days, duration_hours, duration_minutes, views, current_price, created_at, is_ended)
VALUES (2, '상품 4', '그림', 50000, '일반택배', '테스트 상품', null, 2, 1, 30, 0, 50000, CURRENT_TIMESTAMP(),
                                                                                FALSE);

INSERT INTO product
    (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
     duration_days, duration_hours, duration_minutes, views, created_at, is_ended)
VALUES (
    1,
    '상품 A',
    '공예',
    1000,
    10000,
    '택배',
    '입찰 기능 테스트를 위한 상품입니다.',
    null,
    2, 1, 30,
    0,
    CURRENT_TIMESTAMP(),
    FALSE
);

INSERT INTO bid
    (product_id, bidder_id, bid_price, bid_time)
VALUES (
    1,
    1,
    10000,
    CURRENT_TIMESTAMP()
);
