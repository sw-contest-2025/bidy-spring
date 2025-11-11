
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
     '$2a$10$ExYVPy.cJxkl2w8Hjj8wj.Gz.ORdW8ZAEQBl5y2vNwoWZrMwEqTT6',
     '테스트입찰자',
     'bidder',
     'MEMBER',
     '2005-11-20',
     '2025-11-06');

INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('s.chaerlyn@gmail.com',
     '$2a$10$ExYVPy.cJxkl2w8Hjj8wj.Gz.ORdW8ZAEQBl5y2vNwoWZrMwEqTT6',
     '송채린',
     '채린',
     'MEMBER',
     '2005-11-20',
     '2025-11-06');

INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('min@mail.com',
     '$2a$10$ExYVPy.cJxkl2w8Hjj8wj.Gz.ORdW8ZAEQBl5y2vNwoWZrMwEqTT6',
     '유민',
     '유민',
     'MEMBER',
     '2222-11-11',
     '2025-11-10');

INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('kayubin27@gmail.com',
     '$2a$10$.8Ab5N4rFLzECeZ5h2itkuNQMMhEIgBd/yJdHK1BCAvTBUCKMAc/W', -- yubin@27
     '가유빈',
     '유빈',
     'MEMBER',
     '2004-02-07',
     '2025-11-10');

     INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('kacajot221@fandoe.com',
     '$2a$10$UEO/cL6Uw1NsAlm9I1cwg.x/Sd8lTQR5127OTxo9jHWUhaxN1Z79S',
     'test',
     'ttt',
     'MEMBER',
     '2222-11-11',
     '2025-11-10');

INSERT INTO product
    (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
     duration_days, duration_hours, duration_minutes, views, created_at, is_ended,winner_id)
VALUES (
    1,
    '상품 A',
    '공예',
    1000,
    10000,
    '일반택배',
    '입찰 기능 테스트를 위한 상품입니다.',
    null,
    2, 1, 30,
    0,
    CURRENT_TIMESTAMP,
    FALSE,
    null
);

INSERT INTO bid
    (product_id, bidder_id, bid_price, bid_time)
VALUES (
    1,
    1,
    10000,
    CURRENT_TIMESTAMP
);

-- 채팅 테스트 데이터 ---------------
INSERT INTO member -- 판매자: member_id: 7
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('gildong@example.com',
     '$2a$10$ExYVPy.cJxkl2w8Hjj8wj.Gz.ORdW8ZAEQBl5y2vNwoWZrMwEqTT6',
     '홍길동',
     '길동',
     'MEMBER',
     '2000-01-01',
     CURRENT_TIMESTAMP);

INSERT INTO product -- product_id: 2
(member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
 duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES (
           3,
           '상품 B 판매합니다~',
           '공예',
           1000,
           10000,
           '일반택배',
           '채팅 기능 테스트 하기',
           null,
           2, 1, 30,
           0,
           CURRENT_TIMESTAMP,
           FALSE,
           null
       );


       INSERT INTO product
(member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
 duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES (
           3,
           '상품 C 판매합니다~',
           '그림',
           1000,
           52000,
           '편의점택배',
           '상품 설명입니다',
           null,
           3, 12, 45,
           0,
           CURRENT_TIMESTAMP,
           FALSE,
            null
       );


       INSERT INTO product
(member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
 duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES (
           5,
           '상품 D 처분합니다',
           '공예',
           1000,
           48000,
           '일반택배',
           '상품 설명입니다',
           null,
           5, 11, 59,
           0,
           CURRENT_TIMESTAMP,
           FALSE,
           3
       );

INSERT INTO product -- product_id: 2
(member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
 duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES (
           7,
           '목도리 판매함!!',
           '뜨개',
           20000,
           20000,
           '편의점택배',
           '채팅 기능 테스트 하기',
           null,
           2, 1, 30,
           0,
           CURRENT_TIMESTAMP,
           FALSE,
           null
       );

INSERT INTO product
(member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
 duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES (
           7,
           '테스트 상품',
           '문구류',
           88000,
           88000,
           '일반택배',
           '타이머 테스트 하기',
           null,
           0, 0, 0,
           0,
           CURRENT_TIMESTAMP,
           FALSE,
           null
       );

INSERT INTO product
(member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
 duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES (
           7,
           '알람테스트용 1시간 남으면 알람',
           '문구류',
           88000,
           88000,
           '일반택배',
           '알람 테스트 하기 1분뒤 종료 1시간 남으면 알람이 오는지 확인하기',
           null,
           0, 1, 1,
           0,
           CURRENT_TIMESTAMP,
           FALSE,
           null
       );