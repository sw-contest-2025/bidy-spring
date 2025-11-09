
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

INSERT INTO product (member_id, post_name, category, min_price, delivery_method, description, image_url, duration_days, duration_hours, duration_minutes, views, current_price)
VALUES (2, '상품 A', '공예', 1000, '일반택배', '테스트 상품', null, 2, 1, 30, 0, 1000);
