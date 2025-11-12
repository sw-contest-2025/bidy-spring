
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
    ('2039215k@naver.com',
     '$2a$10$C0JicPjKICHa8lt6trbe/ukWhOynYA.yQp8IEOoiIlprWcgqk/jw2', -- qwer1234
     '찬비',
     '찬비',
     'MEMBER',
     '2025-11-22',
     '2025-11-11');

-- 사용자 데이터: 4개 (판매자 2 + 구매자 2)
-- 상품 데이터: 18개 (카테고리 9개 * 2)

--- 판매자 데이터 ---
INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('member1@example.com',
     'user@123',
     '사용자1',
     '솜솜이',
     'MEMBER',
     '2004-04-08',
     CURRENT_TIMESTAMP);

INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('member2@example.com',
     'user@123',
     '사용자2',
     '솜이불',
     'MEMBER',
     '2003-05-11',
     CURRENT_TIMESTAMP);

--- 구매자 데이터 ---
INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('member3@example.com',
     'user@123',
     '사용자3',
     '솜뭉치',
     'MEMBER',
     '1999-12-09',
     CURRENT_TIMESTAMP);

INSERT INTO member
(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
VALUES
    ('member4@example.com',
     'user@123',
     '사용자4',
     '솜사탕',
     'MEMBER',
     '2005-06-07',
     CURRENT_TIMESTAMP);

--INSERT INTO member
--(member_email, member_pw, member_name, member_nickname, member_role, member_birthday, member_create)
--VALUES
--    ('member51@example.com',
--     'user@123',
--     '사용자5',
--     '솜소미',
--     'MEMBER',
--    '2002-08-15',
--     CURRENT_TIMESTAMP);

--- 상품 데이터  ---

-- 그림
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
--- 채팅용: 타이머: 3분
(1, '해질녘 풍경, 감성 담아 팝니다', '그림', 50000, 50000, '일반택배', '편안한 해질녘 풍경을 담은 수채화입니다. 거실이나 방에 두면 분위기 좋아요.', 'https://i.pinimg.com/1200x/34/7f/04/347f048c337b036726f4679af418da26.jpg', 0, 0, 1, 0, CURRENT_TIMESTAMP, FALSE, null),
--- 입찰 종료: 입찰자: 사용자4
(1, '컬러풀 추상화, 집안 포인트용 필요하신 분 들어오세요', '그림', 75000, 75000, '반값 택배', '다채로운 색감의 추상화 작품으로, 현대적인 공간에 잘 어울립니다.', 'https://i.pinimg.com/1200x/4e/58/c0/4e58c03b8e1152bb0605bd90e75a8ab8.jpg', 0, 0, 0, 0, CURRENT_TIMESTAMP, TRUE, 4);

-- 공예
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
(1, '핸드메이드 머그컵, 커피 타임 필수템 팝니다', '공예', 12000, 12000, '일반택배', '직접 빚고 구운 세라믹 머그컵입니다. 따뜻한 커피와 함께 하세요.', 'https://i.pinimg.com/736x/33/01/7b/33017b30e9f56fdebdf324aedac93ed6.jpg', 1, 12, 30, 0, CURRENT_TIMESTAMP, FALSE, null),
(1, '원목 소품, 책상 위 포인트용 필요하신 분 들어오세요', '공예', 20000, 20000, '반값 택배', '천연 원목으로 만든 작은 장식 소품입니다. 책상이나 선반 위에 놓으면 멋집니다.', 'https://i.pinimg.com/1200x/d2/9e/10/d29e10704687c6f60e0375bad29fb684.jpg', 2, 0, 0, 0, CURRENT_TIMESTAMP, FALSE, null);

-- 도서
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
--- 입찰 시연용: 타이머 3분
(2, '세계문학 컬렉션, 독서 좋아하시는 분 필수템', '도서', 10000, 10000, '일반택배', '고전 세계문학 작품들을 모아둔 세트입니다. 책 읽는 즐거움을 느껴보세요.', 'https://image.aladin.co.kr/product/11542/23/cover500/k112531502_1.jpg', 0, 0, 3, 0, CURRENT_TIMESTAMP, FALSE, null),
(2, '최근 베스트셀러 5권, 이번 주 마감! 관심 있으신 분 들어오세요', '도서', 25000, 25000, '반값 택배', '최근 인기 있는 도서 5권을 모았습니다. 선물용으로도 좋습니다.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbJS77xXwgz9N5Boe8r3I1SIhjQr6jBVb1sw&s', 2, 8, 0, 0, CURRENT_TIMESTAMP, FALSE, null);

-- 의류/잡화
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
(2, '빈티지 청자켓, 사이즈 M 팝니다', '의류/잡화', 40000, 40000, '일반택배', '오래 입어도 멋스러운 빈티지 청자켓입니다.', 'https://i.pinimg.com/1200x/22/6b/f1/226bf1fc753a23e99773ca5339d0da5b.jpg', 2, 10, 0, 0, CURRENT_TIMESTAMP, FALSE, null),
(2, '핸드메이드 가죽 지갑, 필요하신 분 들어오세요', '의류/잡화', 35000, 35000, '반값 택배', '직접 만든 소가죽 지갑으로 내구성이 좋습니다.', 'https://i.pinimg.com/736x/1f/14/26/1f142670f2a27b220168491a5cf175ac.jpg', 3, 5, 0, 0, CURRENT_TIMESTAMP, FALSE, null);

-- 홈인테리어
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
(1, '미니멀 인테리어 소품, 집안 분위기 업! 팝니다', '홈인테리어', 15000, 15000, '일반택배', '작지만 감각적인 인테리어 소품입니다. 거실이나 서재에 좋습니다.', 'https://i.pinimg.com/736x/31/3e/c0/313ec0697908059b50cbf1178fa749a1.jpg', 2, 0, 0, 0, CURRENT_TIMESTAMP, FALSE, null),
--- 입찰 종료: 입찰자: 사용자4
(1, '무드등, 밤 분위기 필요하신 분 들어오세요', '홈인테리어', 20000, 20000, '반값 택배', '부드러운 빛의 무드등으로 침실이나 거실에 최적입니다.', 'https://i.pinimg.com/1200x/1d/41/aa/1d41aa1905c3fd998f58e1d99e6ebcbb.jpg', 0, 0, 0, 0, CURRENT_TIMESTAMP, TRUE, 4);

-- 문구류
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
(3, '수채화 붓 세트, 미술 입문자 추천 팝니다', '문구류', 10000, 10000, '일반택배', '다양한 크기의 수채화 붓 세트입니다. 초보자도 사용하기 좋아요.', 'https://i.pinimg.com/1200x/79/db/84/79db84806602acee14d4d1f17a30152b.jpg', 1, 5, 0, 0, CURRENT_TIMESTAMP, FALSE, null),
(3, '핸드메이드 노트, 소장 가치 있어요! 필요하신 분 들어오세요', '문구류', 12000, 12000, '반값 택배', '정성스럽게 만든 핸드메이드 노트입니다. 일기나 기록용으로 좋습니다.', 'https://i.pinimg.com/1200x/7f/9a/b8/7f9ab8c0cb12c23828071b80b038f5be.jpg', 2, 2, 0, 0, CURRENT_TIMESTAMP, FALSE, null);

-- 가전디지털
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
(3, '블루투스 스피커, 캠핑용 추천 팝니다', '가전디지털', 35000, 35000, '일반택배', '작지만 강력한 사운드의 블루투스 스피커입니다.', 'https://i.pinimg.com/736x/65/69/44/656944be359794acb8225a41d4bc88fc.jpg', 3, 0, 0, 0, CURRENT_TIMESTAMP, FALSE, null),
(3, '휴대용 보조배터리, 필요하신 분 들어오세요', '가전디지털', 20000, 20000, '반값 택배', '외출 시 필수템, 대용량 보조배터리입니다.', 'https://i.pinimg.com/1200x/2d/73/41/2d7341d0a0b0b445bcf6e5310921c407.jpg', 2, 10, 0, 0, CURRENT_TIMESTAMP, FALSE, null);

-- 악기
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
--- 입찰 종료: 입찰자: 사용자3
(1, '우쿨렐레 초급용, 음악 입문자 팝니다', '악기', 25000, 25000, '일반택배', '초보자도 쉽게 연주할 수 있는 우쿨렐레입니다.', 'https://i.pinimg.com/1200x/9e/92/37/9e923783eb7f2180fa0ab552ef5c35d8.jpg', 0, 0, 1, 0, CURRENT_TIMESTAMP, FALSE, null),
(1, '전자 드럼 패드, 필요하신 분 들어오세요', '악기', 80000, 80000, '반값 택배', '집에서 연습하기 좋은 전자 드럼 패드입니다.', 'https://i.pinimg.com/736x/c6/d8/51/c6d851d0ed35c818a7b78b182606d5d0.jpg', 3, 0, 0, 0, CURRENT_TIMESTAMP, FALSE, null);

-- 기타
INSERT INTO product (member_id, post_name, category, min_price, current_price, delivery_method, description, image_url,
                     duration_days, duration_hours, duration_minutes, views, created_at, is_ended, winner_id)
VALUES
--- 입찰 종료: 입찰자: 사용자2
(1, '한정판 컬렉터 아이템, 관심 있으신 분 팝니다', '기타', 50000, 50000, '일반택배', '한정판으로 제작된 컬렉터 아이템입니다. 소장가치 높습니다.', 'https://flexible.img.hani.co.kr/flexible/normal/970/582/imgdb/child/2025/0806/53_17544573803024_20250806502005.jpg', 0, 0, 0, 0, CURRENT_TIMESTAMP, TRUE, 2),
(1, 'DIY 키트, 필요하신 분 들어오세요', '기타', 30000, 30000, '반값 택배', '직접 조립해보는 재미가 있는 DIY 키트입니다.', 'https://hollyland.store/web/product/big/202510/fc7fbc1e1be34097a4ac98b410821618.png', 1, 12, 0, 0, CURRENT_TIMESTAMP, FALSE, null);



INSERT INTO TAG (TAG_NAME) VALUES ('도서');
INSERT INTO TAG (TAG_NAME) VALUES ('가전디지털');
INSERT INTO TAG (TAG_NAME) VALUES ('문구류');
INSERT INTO TAG (TAG_NAME) VALUES ('그림');
INSERT INTO TAG (TAG_NAME) VALUES ('기타');
INSERT INTO TAG (TAG_NAME) VALUES ('악기');
INSERT INTO TAG (TAG_NAME) VALUES ('공예');
INSERT INTO TAG (TAG_NAME) VALUES ('홈인테리어');
INSERT INTO TAG (TAG_NAME) VALUES ('의류/잡화');