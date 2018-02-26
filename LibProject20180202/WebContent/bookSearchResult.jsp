<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
		var url = "${pageContext.request.contextPath}/bookapi?search=${search}";
		console.log(url);
        $.getJSON(url, function(result){
        	var testHtml = "";
            $.each(result.items, function(i, field){
            	
            	testHtml += 
            		"<tr>" +
            		"<td>" + field.title + "</td>" +
            		"<td>" + field.author + "</td>" +
            		"<td>" + field.publisher + "</td>" +
            		"<td>" + field.pubdate + "</td>" +
            		"</tr>";
//              	$("#bookdata").append(testHtml);
            });
            console.log(testHtml);
     	$("#bookdata").html(testHtml);
        });

});
</script>
<style type="text/css">
#ulTable{
position: absolute;
top: 300px;
left : 210px;
color: white;
}

</style>
<link rel="stylesheet" type="text/css" href="css/result.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>대구대학교 중앙도서관 도서검색 결과</title>
</head>
<body>
<h1>도서검색 결과</h1>
<%-- 검색어 [키워드 / ${option } : ${content } ]<br>
총 ${cnt } 건 출력 --%>

<div class="container">
<table id="ulTable"  border="0" cellspacing="0" cellpadding="0" width=1500px;>

	<tr>
<!-- 		<td>등록번호</td> -->
		<td>도서명</td>
		<td>저자</td>
		<td>출판사</td>
		<td>출판연도</td>
<!-- 		<td>도서상태</td> -->
	</tr>

<tbody id="bookdata"></tbody>
<%-- 	<c:forEach var="book" items="${bookList}"> --%>
<!-- 	<tr> -->
<%-- 		<td>${book.registNum }</td> --%>
<%-- 		<td>${book.title }</td> --%>
<%-- 		<td>${book.author }</td> --%>
<%-- 		<td><img width=150px src='images/book/${book.icon }'></td> --%>
<%-- 		<td>${book.publishing }</td> --%>
<%-- 		<td>${book.state }</td> --%>
<!-- 	</tr> -->
<%-- 	</c:forEach> --%>

	
</table>
</div>
<a href='index.jsp'>이전으로</a>
</body>
</html>