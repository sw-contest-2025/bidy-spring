<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>채팅</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
    <div>
        <div><h2>채팅방</h2></div>
        <div id="chatBox">
            <c:forEach var="msg" items="${messages}">
                <div><b>${msg.senderId}</b>: ${msg.message}</div>
            </c:forEach>
        </div>
        <input type="text" id="message" placeholder="채팅 내용을 입력하세요."/>
        <button id="sendBtn">전송</button>
    </div>

    <script src="https://code.jquery.com/jquery-1.12.4.js" ></script>
</body>
</html>
