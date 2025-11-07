<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>경매 상세</title>
</head>
<body>
    <div>
        <button disabled>
            <span id="timer">로딩중</span>
        </button>
    </div>
    <script src="/js/timer.js"></script>

    <script>
        const serverEndTimerStr = '${endTime}';
        startTimer(serverEndTimerStr);
    </script>

    <form action="/auction/bid" method="POST">
        <button type="submit">입찰 요청</button>
    </form>
</body>
</html>