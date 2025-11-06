function startTimer(endTimeStr){
    const endTime = new Date(endTimeStr);
    const timerElement = document.getElementById("timer");

    if(endTime.getTime() <= new Date().getTime()){
        timerElement.innerHTML = "경매 종료";
        bidButton.disabled = true;
        return;
    }

    // 경매 시작 시
    bidButton.disabled = false;

    const timerInterval = setInterval(function() {
        const now = new Date().getTime();
        const distance = endTime.getTime() - now;

        // 남은 시간 계산
        const days = Math.floor(distance / (1000 * 60 * 60 * 24));
        const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);

        // 화면 업데이트
        if(distance > 0){
            timerElement.innerHTML = days + " : " + hours + " : " + minutes + " : " + seconds;
        }
        else{
            clearInterval(timerInterval);
            timerElement.innerHTML = "경매 종료";
            bidButton.disabled = true;
        }
    }, 1000);
}