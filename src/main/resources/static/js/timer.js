/**
 * 경매 상세 페이지의 실시간 로직 시작
 * @param {string} endTimeStr - 서버에서 받은 LocalDateTime.toString() 결과 문자열
 * @param {number} productId - Ajax 요청에 사용할 상품 ID
 */
function startAuctionFeatures(endTimeStr, productId) {

    const timerElement = document.getElementById('countdown-timer');
    const bidButton = document.getElementById('bid-button');
    const currentPriceElement = document.getElementById('current-price-display');
    const endTime = new Date(endTimeStr);

        // 경매 종료 확인
        if (isNaN(endTime.getTime()) || endTime.getTime() <= new Date().getTime()) {
            if (timerElement) timerElement.innerHTML = "경매 종료";
            if (bidButton) bidButton.disabled = true;
            return;
        }

    let timerInterval;

    function updateCountdown() {
        const now = new Date();
        const difference = endTime.getTime() - now.getTime();

        if (difference > 0) {
            // 남은 시간 계산
            const seconds = Math.floor((difference / 1000) % 60);
            const minutes = Math.floor((difference / 1000 / 60) % 60);
            const hours = Math.floor((difference / (1000 * 60 * 60)) % 24);
            const days = Math.floor(difference / (1000 * 60 * 60 * 24));

            // 화면 업데이트
            timerElement.innerHTML = `${days} 일 ${hours} 시간 ${minutes} 분 ${seconds} 초`;

        } else {
            // 경매 종료 처리
            clearInterval(timerInterval);
            if (pricePollingInterval) clearInterval(pricePollingInterval);
            timerElement.innerHTML = "경매 종료";
            if (bidButton) bidButton.disabled = true;
        }
    }

    // --- Ajax 가격 갱신 ---
    let pricePollingInterval; // setInterval 변수를 함수 외부에 선언

    function fetchCurrentPrice() {
        fetch(`/auction/api/current-price?productId=${productId}`)
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok.');
                return response.json();
            })
            .then(newPrice => {
                if (currentPriceElement) {
                    currentPriceElement.innerHTML = newPrice.toLocaleString() + '원';
                }
                const bidInput = document.getElementById('bidAmount');
                if (bidInput) {
                    bidInput.min = newPrice + 100;
                    bidInput.placeholder = `현재 최고가 ${newPrice}원보다 높게 입력`;
                }
            })
            .catch(error => {
                console.error('가격 갱신 오류:', error);
            });
    }

    // --- 3. 실행 영역 ---

    // 1초마다 타이머 실행
    timerInterval = setInterval(updateCountdown, 1000);
    updateCountdown();

    // 2초마다 가격 갱신 API 호출 (폴링)
    pricePollingInterval = setInterval(fetchCurrentPrice, 2000);
    fetchCurrentPrice();
}