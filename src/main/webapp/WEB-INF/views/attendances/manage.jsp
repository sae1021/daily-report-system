<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst"%>

<c:set var="actAtt" value="${ForwardConst.ACT_ATT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commIn" value="${ForwardConst.CMD_IN.getValue()}" />
<c:set var="commOut" value="${ForwardConst.CMD_OUT.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${errors != null}">
            <div id="flush_error">
                エラーがあります。<br />
                <c:forEach var="error" items="${errors}">
                    ・<c:out value="${error}" />
                    <br />
                </c:forEach>

            </div>
        </c:if>
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>勤怠管理ページ</h2>
        <p>
            <a href="<c:url value='?action=${actAtt}&command=${commIdx}' />">勤怠一覧画面</a>
        </p>
        <table class="buttonBox">
            <tr>
                <td>
                    <div class="box whitebase">
                        <p>出勤</p>
                        <a class="link"
                            href="<c:url value='?action=${actAtt}&command=${commIn}' />"></a>
                    </div>
                </td>
                <td>
                    <div class="box blackbase">
                        <p>退勤</p>
                        <a class="link"
                            href="<c:url value='?action=${actAtt}&command=${commOut}' />"></a>
                    </div>
                </td>
            </tr>
        </table>

    </c:param>
</c:import>