INSERT INTO member
    (member_email, member_pw, member_name, member_nickname, member_role, member_birthday) 
VALUES 
    ('1234@example.com', '1234', 'a', 'chanbii_dev', 'ADMIN', '2000-05-15');

INSERT INTO member 
    (member_email, member_pw, member_name, member_nickname, member_role, member_birthday) 
VALUES 
    ('5678@mail.com', '5678', '테스트입찰자', 'test_bidder', 'MEMBER', '2005-11-20');

INSERT INTO product
    (name, category, min_price, current_price, delivery_method, description, image_url, created_at, duration_days, duration_hours, duration_minutes, views)
VALUES (
    '초기 테스트 경매 상품 A',
    '디지털/가전',
    50000,
    55000,
    '택배',
    '입찰 기능 테스트를 위한 상품입니다. 기한: 2일 1시간 30분',
    'http://image.url/test_a.png',
    CURRENT_TIMESTAMP(),
    2, 1, 30,
    0
);