/**
 * 위시리스트 토글 기능을 초기화 및 이벤트 리스너를 설정.
 * @param {number} memberId
 * @param {number} productId - 관심 버튼이 눌린 상품 ID
 */
async function initializeWishlistToggle(memberId, productId) {
    const wishButton = document.getElementById('interest-btn');
    if (!wishButton) return;

    // 중복 리스너 제거
    const originalButton = wishButton;
    const newWishButton = originalButton.cloneNode(true);
    originalButton.replaceWith(newWishButton);

    // 이미지 요소 참조
    const imgElement = newWishButton.querySelector('img');
    if (!imgElement) return;

    if (memberId > 0) { // 로그인된 경우에만 실행 (memberId가 유효한 ID일 때)
        try {
            const checkRes = await fetch(`/api/wishlist/check/${productId}`);

            if (checkRes.ok) {
                const checkData = await checkRes.json();

                if (checkData.isWishlisted) {
                    imgElement.src = '/images/click_heart.png'; // 꽉 찬 하트 (찜 완료)
                    imgElement.dataset.state = 'on';
                } else {
                    imgElement.src = '/images/heart.png'; // 빈 하트 (찜 안 됨)
                    imgElement.dataset.state = 'off';
                }
            } else {
                 console.error('초기 상태 확인 API 호출 실패:', checkRes.status);
            }
        } catch (e) {
            console.error('초기 위시리스트 상태 확인 실패:', e);
        }
    }
    // **************************************************


    // 클릭 이벤트 리스너 설정
    newWishButton.addEventListener('click', async(event) => {

        if (memberId <= 0) {
            alert('로그인이 필요합니다. 로그인 페이지로 이동합니다.');
            window.location.href = '/login';
            return;
        }

        const imgElement = newWishButton.querySelector('img');
        try {
            const response = await fetch(`/api/wishlist/toggle/${productId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });

            const result = await response.json();

            if (result.success) {
                  const action = result.action === 'added' ? '추가됨' : '삭제됨';
                  alert('위시리스트 ' + action + '!');

                  // 클릭 시 이미지 변경 로직
                  if (result.action === 'added') {
                                  imgElement.src = '/images/click_heart.png';
                                  imgElement.dataset.state = 'on';
                              } else {
                                  imgElement.src = '/images/heart.png';
                                  imgElement.dataset.state = 'off';
                              }
                } else {
                    alert('처리 실패: ' + result.message);
                }
            } catch (error) {
                console.error('위시리스트 토글 오류:', error);
                alert('위시리스트 통신 중 오류가 발생했습니다.');
            }
        });
}