-- StagePlan 데이터베이스 초기화 및 예시 데이터
-- 이 파일은 애플리케이션 시작 시 자동으로 실행됩니다.

-- 기존 데이터 삭제 (개발 환경에서만 사용)
DELETE FROM reviews;
DELETE FROM performances;
DELETE FROM users;

-- 사용자 예시 데이터
INSERT INTO users (id, email, password, name, nickname, instagram_id, band_name, profile_image_url, representative_video_url, favorite_genres, bio, role, created_at, updated_at) VALUES
(1, 'admin@stageplan.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '관리자', 'StagePlan 관리자', 'stageplan_admin', 'StagePlan Team', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', '록, 팝, 재즈', 'StagePlan 플랫폼을 관리하는 관리자입니다. 모든 공연과 리뷰를 관리합니다.', 'ADMIN', NOW(), NOW()),

(2, 'rockband@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '김록', '록킹김', 'rocking_kim', 'Thunder Storm', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face', 'https://www.youtube.com/watch?v=example1', '록, 메탈, 얼터너티브', '열정적인 록 음악을 사랑하는 밴드입니다. 무대에서의 에너지 넘치는 공연을 선사합니다!', 'USER', NOW(), NOW()),

(3, 'jazzsinger@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '박재즈', '재즈마스터', 'jazz_master_park', 'Blue Note Trio', 'https://images.unsplash.com/photo-1494790108755-2616b612b786?w=150&h=150&fit=crop&crop=face', 'https://www.youtube.com/watch?v=example2', '재즈, 블루스, 소울', '클래식 재즈의 아름다움을 현대적으로 해석하는 재즈 아티스트입니다.', 'USER', NOW(), NOW()),

(4, 'popstar@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '이팝', '팝스타리', 'popstar_lee', 'Electric Dreams', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop&crop=face', 'https://www.youtube.com/watch?v=example3', '팝, 일렉트로닉, 댄스', '신나는 팝 음악으로 관객들과 함께 즐거운 시간을 만들어갑니다!', 'USER', NOW(), NOW()),

(5, 'indieband@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '최인디', '인디스피릿', 'indie_spirit_choi', 'Urban Folk', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&h=150&fit=crop&crop=face', 'https://www.youtube.com/watch?v=example4', '인디, 포크, 어쿠스틱', '도시의 이야기를 담은 인디 포크 음악을 만듭니다. 따뜻한 감성의 공연을 선사합니다.', 'USER', NOW(), NOW()),

(6, 'classical@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '정클래식', '클래식마에스트로', 'classical_maestro', 'Symphony Orchestra', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face', 'https://www.youtube.com/watch?v=example5', '클래식, 오케스트라, 실내악', '클래식 음악의 깊이와 아름다움을 전하는 지휘자입니다.', 'USER', NOW(), NOW());

-- 공연 예시 데이터
INSERT INTO performances (id, title, content, location, performance_date, genre, band_name, ticket_price, max_audience, status, user_id, created_at, updated_at) VALUES
(1, 'Thunder Storm 첫 번째 정기공연', 'Thunder Storm의 첫 번째 정기공연입니다. 강렬한 록 사운드와 에너지 넘치는 무대를 선사합니다. 새로운 곡들과 클래식 넘버들을 함께 만나보세요!', '홍대 클럽 에반스', '2024-02-15 20:00:00', '록', 'Thunder Storm', 15000, 100, 'ACTIVE', 2, NOW(), NOW()),

(2, 'Blue Note Trio 재즈 나이트', '클래식 재즈의 아름다움을 현대적으로 해석한 Blue Note Trio의 재즈 나이트입니다. 따뜻한 재즈 선율과 함께하는 특별한 밤이 될 것입니다.', '강남 재즈클럽 블루노트', '2024-02-20 19:30:00', '재즈', 'Blue Note Trio', 25000, 80, 'ACTIVE', 3, NOW(), NOW()),

(3, 'Electric Dreams 팝 콘서트', '신나는 팝 음악과 일렉트로닉 사운드가 만나는 Electric Dreams의 콘서트입니다. 함께 춤추고 노래하는 즐거운 시간을 만들어보세요!', '올림픽공원 KSPO DOME', '2024-02-25 19:00:00', '팝', 'Electric Dreams', 35000, 500, 'ACTIVE', 4, NOW(), NOW()),

(4, 'Urban Folk 인디 공연', '도시의 이야기를 담은 Urban Folk의 인디 공연입니다. 따뜻한 감성의 어쿠스틱 사운드와 진솔한 가사로 마음을 울리는 공연입니다.', '홍대 클럽 FF', '2024-03-01 20:30:00', '인디', 'Urban Folk', 12000, 60, 'ACTIVE', 5, NOW(), NOW()),

(5, 'Symphony Orchestra 클래식 콘서트', '베토벤과 모차르트의 명곡들을 연주하는 클래식 콘서트입니다. 오케스트라의 웅장한 사운드와 함께하는 특별한 클래식의 밤입니다.', '예술의전당 콘서트홀', '2024-03-05 19:30:00', '클래식', 'Symphony Orchestra', 50000, 200, 'ACTIVE', 6, NOW(), NOW()),

(6, 'Thunder Storm 두 번째 정기공연', 'Thunder Storm의 두 번째 정기공연입니다. 첫 번째 공연의 성공에 힘입어 더욱 강력한 무대를 준비했습니다!', '홍대 클럽 에반스', '2024-03-10 20:00:00', '록', 'Thunder Storm', 15000, 100, 'ACTIVE', 2, NOW(), NOW()),

(7, 'Blue Note Trio 스프링 재즈', '봄을 맞아 준비한 Blue Note Trio의 스프링 재즈 공연입니다. 따뜻한 봄날의 감성을 재즈로 표현합니다.', '강남 재즈클럽 블루노트', '2024-03-15 19:30:00', '재즈', 'Blue Note Trio', 25000, 80, 'ACTIVE', 3, NOW(), NOW()),

(8, 'Electric Dreams 댄스 파티', 'Electric Dreams와 함께하는 댄스 파티입니다. 최신 일렉트로닉 음악과 함께 밤새 춤춰보세요!', '강남 클럽 옥타곤', '2024-03-20 22:00:00', '일렉트로닉', 'Electric Dreams', 20000, 300, 'ACTIVE', 4, NOW(), NOW()),

(9, 'Urban Folk 어쿠스틱 세션', 'Urban Folk의 어쿠스틱 세션입니다. 작은 공간에서 더욱 가까이 느낄 수 있는 따뜻한 공연입니다.', '홍대 클럽 FF', '2024-03-25 20:00:00', '포크', 'Urban Folk', 10000, 40, 'ACTIVE', 5, NOW(), NOW()),

(10, 'Symphony Orchestra 봄 콘서트', '봄을 주제로 한 Symphony Orchestra의 봄 콘서트입니다. 봄의 아름다움을 클래식 음악으로 표현합니다.', '예술의전당 콘서트홀', '2024-03-30 19:30:00', '클래식', 'Symphony Orchestra', 50000, 200, 'ACTIVE', 6, NOW(), NOW());

-- 리뷰 예시 데이터
INSERT INTO reviews (id, content, rating, user_id, performance_id, created_at, updated_at) VALUES
(1, '정말 에너지 넘치는 공연이었습니다! Thunder Storm의 무대 매너가 정말 대단했어요. 다음 공연도 꼭 가고 싶습니다.', 5, 3, 1, NOW(), NOW()),

(2, '재즈의 아름다움을 제대로 느낄 수 있었던 공연이었습니다. Blue Note Trio의 연주 실력이 정말 뛰어나네요.', 5, 2, 2, NOW(), NOW()),

(3, 'Electric Dreams의 팝 콘서트는 정말 신났어요! 함께 춤추고 노래하는 시간이 너무 즐거웠습니다.', 4, 5, 3, NOW(), NOW()),

(4, 'Urban Folk의 인디 공연은 마음이 따뜻해지는 공연이었습니다. 가사가 정말 감동적이었어요.', 5, 4, 4, NOW(), NOW()),

(5, '클래식 콘서트의 웅장함을 제대로 느낄 수 있었습니다. Symphony Orchestra의 연주가 정말 훌륭했어요.', 5, 2, 5, NOW(), NOW()),

(6, 'Thunder Storm의 두 번째 공연도 역시 대단했습니다! 첫 번째보다 더욱 발전된 모습을 보여주었어요.', 5, 4, 6, NOW(), NOW()),

(7, 'Blue Note Trio의 스프링 재즈는 봄의 감성을 정말 잘 표현한 공연이었습니다. 따뜻하고 아름다웠어요.', 4, 6, 7, NOW(), NOW()),

(8, 'Electric Dreams의 댄스 파티는 정말 신났어요! 밤새 춤춰도 지치지 않을 것 같았습니다.', 4, 3, 8, NOW(), NOW()),

(9, 'Urban Folk의 어쿠스틱 세션은 정말 가까이서 느낄 수 있는 공연이었습니다. 아늑하고 따뜻한 분위기가 좋았어요.', 5, 2, 9, NOW(), NOW()),

(10, 'Symphony Orchestra의 봄 콘서트는 클래식의 아름다움을 제대로 느낄 수 있는 공연이었습니다.', 5, 5, 10, NOW(), NOW()),

(11, 'Thunder Storm의 공연은 정말 강렬했습니다! 록의 진정한 매력을 느낄 수 있었어요.', 4, 6, 1, NOW(), NOW()),

(12, 'Blue Note Trio의 재즈는 정말 아름다웠습니다. 재즈의 깊이를 제대로 느낄 수 있었어요.', 5, 4, 2, NOW(), NOW()),

(13, 'Electric Dreams와 함께한 시간이 정말 즐거웠습니다! 팝 음악의 매력을 다시 한번 느꼈어요.', 4, 2, 3, NOW(), NOW()),

(14, 'Urban Folk의 공연은 마음이 따뜻해지는 공연이었습니다. 인디 음악의 진정한 매력을 느꼈어요.', 5, 3, 4, NOW(), NOW()),

(15, '클래식의 웅장함을 제대로 느낄 수 있었던 공연이었습니다. Symphony Orchestra의 연주가 정말 훌륭했어요.', 5, 4, 5, NOW(), NOW());

-- 시퀀스 재설정 (MySQL의 경우 AUTO_INCREMENT 값 조정)
-- ALTER TABLE users AUTO_INCREMENT = 7;
-- ALTER TABLE performances AUTO_INCREMENT = 11;
-- ALTER TABLE reviews AUTO_INCREMENT = 16;
