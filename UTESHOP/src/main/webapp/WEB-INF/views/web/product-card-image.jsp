<%-- Product Card Image --%>
<c:set var="productImage" value="${empty p.images ? 'logo.png' : p.images[0]}" />
<c:url value="/assets/uploads/product/${productImage}" var="imgUrl" />