<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>경매 상세</title>
</head>
<body>
    <h2>최근 입찰 기록 (Scriptlet)</h2>
    <table>
        <thead>
            <tr>
                <th>입찰자 ID</th>
                <th>입찰 금액</th>
                <th>입찰 시간</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Bid> recentBids = (List<Bid>) request.getAttribute("recentBids");

                if (recentBids != null) {
                    for (Bid bid : recentBids) {
            %>
                <tr>
                    <td><%= bid.getBidder().getId() %></td>
                    <td><%= bid.getBidPrice() %></td>
                    <td><%= bid.getBidTime() %></td>
                </tr>
            <%
                    } // for 루프 닫기
                }
            %>
        </tbody>
    </table>
    <a href="/">메인으로 돌아가기</a>
</body>
</html>