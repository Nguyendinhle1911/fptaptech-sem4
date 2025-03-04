
<table class="table">
    <thead>
    <tr>
        <th>Book Title</th>
        <th>Member</th>
        <th>Borrow Date</th>
        <th>Due Date</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${activeLoans}" var="loan">
        <tr class="${loan.dueDate.isBefore(LocalDate.now()) ? 'overdue' : ''}">
            <td>${loan.book.title}</td>
            <td>${loan.member.fullName}</td>
            <td><fmt:formatDate value="${loan.borrowDate}" pattern="dd/MM/yyyy"/></td>
            <td><fmt:formatDate value="${loan.dueDate}" pattern="dd/MM/yyyy"/></td>
            <td>
                <c:if test="${loan.returnDate == null}">
                    <form action="loans" method="post">
                        <input type="hidden" name="action" value="return">
                        <input type="hidden" name="id" value="${loan.id}">
                        <button type="submit" class="btn-return">Return</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>