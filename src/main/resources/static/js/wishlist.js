/**
 * 위시리스트 토글 기능을 초기화 및 이벤트 리스너를 설정.
 * @param {number} memberId - 사용자
 * @param {number} productId - 관심 버튼이 눌린 상품 ID
 */
function initializeWishlistToggle(memberId, productId) {
  const wishButton = document.getElementById('interest-btn');
  if (!wishButton) return;

  // 중복 리스너 제거
  wishButton.replaceWith(wishButton.cloneNode(true));
  const newWishButton = document.getElementById('interest-btn');

  newWishButton.addEventListener('click', async () => {
    const data = { memberId, productId };

    try {
      const response = await fetch('/api/wishlist/toggle', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });

      const result = await response.json();

       if (result.success) {
              const action = result.action === 'added' ? '추가됨' : '삭제됨';
              alert('위시리스트 ' + action + '!');
              newWishButton.textContent = result.action === 'added' ? '찜 해제' : '🤍 관심';
            } else {
              alert('처리 실패: ' + result.message);
            }
          } catch (error) {
            console.error('위시리스트 토글 오류:', error);
            alert('위시리스트 통신 중 오류가 발생했습니다.');
          }
        });
}
