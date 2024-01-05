<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>fastcampus</title>
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>

<body>
    <div id="menu">
        <ul>
            <li id="logo">fastcampus</li>
            <li><a href="<c:url value='/'/>">Home</a></li>
            <li><a href="<c:url value='/board/list'/>">Board</a></li>
            <li><a href="<c:url value='/login/login'/>">login</a></li>
            <li><a href="<c:url value='/register/add'/>">Sign in</a></li>
            <li><a href=""><i class="fas fa-search small"></i></a></li>
        </ul>
    </div>
    <div style="text-align:center">
        <h2>게시물 읽기</h2>
        <from id="form">
            <input type ="text" name = "bno" value="${boardDto.bno}" readonly="readonly">
            <input type="text" name="title" value="${boardDto.title}" readonly="readonly">
            <textarea name="content" id="" cols="30" rows="10" readonly="readonly">${boardDto.content}</textarea>
            <button type="button" id="Btn" class="btn">등록</button>
            <button type="button" id="Btn" class="btn">수정</button>
            <button type="button" id="Btn" class="btn">삭제</button>
            <button type="button" id="Btn" class="btn">목록</button>
        </from>

    </div>
</body>
</html>