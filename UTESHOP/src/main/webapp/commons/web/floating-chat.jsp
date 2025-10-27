<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Floating Chat Button -->
<c:if test="${not empty sessionScope.currentUser}">
    <%
        // Xác định URL dựa trên role
        String chatUrl = request.getContextPath() + "/chat";
        Object userObj = session.getAttribute("currentUser");
        if (userObj != null) {
            try {
                String role = (String) userObj.getClass().getMethod("getRole").invoke(userObj);
                if (role != null) {
                    role = role.toLowerCase();
                    if ("admin".equals(role) || "manager".equals(role)) {
                        chatUrl = request.getContextPath() + "/api/admin/chat";
                    } else if ("shipper".equals(role)) {
                        chatUrl = request.getContextPath() + "/chat";
                    }
                }
            } catch (Exception e) {
                // Keep default chatUrl
            }
        }
        pageContext.setAttribute("chatUrl", chatUrl);
    %>
    <a href="${chatUrl}" class="floating-chat-btn" title="Chat">
        <i class="fas fa-comment-dots"></i>
    </a>
</c:if>

<style>
    .floating-chat-btn {
        position: fixed;
        bottom: 30px;
        right: 30px;
        width: 60px;
        height: 60px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 24px;
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        text-decoration: none;
        transition: all 0.3s ease;
        z-index: 9999;
        animation: pulse 2s infinite;
    }
    
    .floating-chat-btn:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
        color: white;
    }
    
    .floating-chat-btn:active {
        transform: scale(0.95);
    }
    
    @keyframes pulse {
        0%, 100% {
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        50% {
            box-shadow: 0 4px 20px rgba(102, 126, 234, 0.7);
        }
    }
    
    /* Responsive */
    @media (max-width: 768px) {
        .floating-chat-btn {
            width: 50px;
            height: 50px;
            font-size: 20px;
            bottom: 20px;
            right: 20px;
        }
    }
</style>

