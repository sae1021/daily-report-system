<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst"%>

<c:set var="actAtt" value="${ForwardConst.ACT_ATT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commMng" value="${ForwardConst.CMD_MANAGE.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2>勤怠 一覧</h2>
        <table id="attendance_list">
            <tbody>
                <tr class="row_header">
                    <th class="attendance_name">職員名</th>
                    <th class="attendance_date">日付</th>
                    <th class="attend_at">出勤時間</th>
                    <th class="leave_at">退勤時間</th>
                </tr>
                <c:forEach var="attendance" items="${attendances}"
                    varStatus="status">
                    <fmt:parseDate value="${attendance.attendanceDate}"
                        pattern="yyyy-MM-dd" var="attendanceDay" type="date" />
                    <fmt:parseDate value="${attendance.attendAt}"
                        pattern="yyyy-MM-dd'T'HH:mm" var="attendAt" type="both" />
                    <fmt:parseDate value="${attendance.leaveAt}"
                        pattern="yyyy-MM-dd'T'HH:mm" var="leaveAt" type="both" />
                    <tr class="row${status.count % 2}">
                        <td class="attendance_name"><c:out
                                value="${attendance.employee.name}" /></td>
                        <td class="attendance_date"><fmt:formatDate
                                value='${attendanceDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="attend_at"><fmt:formatDate value="${attendAt}"
                                pattern="HH:mm" /></td>
                        <td class="leave_at"><fmt:formatDate value="${leaveAt}"
                                pattern="HH:mm" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${attendances_count} 件）<br />
            <c:forEach var="i" begin="1"
                end="${((attendances_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a
                            href="<c:url value='?action=${actAtt}&command=${commIdx}&page=${i}' />"><c:out
                                value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p>
            <a href="<c:url value='?action=${actAtt}&command=${commMng}' />">出退勤画面に戻る</a>
        </p>

    </c:param>
</c:import>