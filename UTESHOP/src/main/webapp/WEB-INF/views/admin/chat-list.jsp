<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - UTESHOP Admin</title>
    <style>
        .chat-list-container {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        
        .chat-room-item {
            display: flex;
            align-items: center;
            padding: 20px;
            border: 2px solid #e5e7eb;
            border-radius: 12px;
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            color: inherit;
        }
        
        .chat-room-item:hover {
            border-color: #667eea;
            background: #f8fafc;
            transform: translateX(5px);
        }
        
        .room-avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            font-size: 20px;
            margin-right: 15px;
        }
        
        .room-info {
            flex: 1;
        }
        
        .room-name {
            font-weight: 600;
            font-size: 16px;
            margin-bottom: 5px;
        }
        
        .room-preview {
            color: #6b7280;
            font-size: 14px;
        }
        
        .unread-badge {
            background: #ef4444;
            color: white;
            border-radius: 20px;
            padding: 5px 12px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .search-box {
            margin-bottom: 30px;
        }
        
        .search-box input {
            width: 100%;
            padding: 15px 20px;
            border: 2px solid #e5e7eb;
            border-radius: 12px;
            font-size: 15px;
            outline: none;
            transition: all 0.3s;
        }
        
        .search-box input:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .enter-room-form {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 30px;
        }
        
        .enter-room-form h5 {
            color: white;
            margin-bottom: 15px;
        }
        
        .enter-room-form input {
            flex: 1;
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            margin-right: 10px;
            font-size: 15px;
        }
        
        .enter-room-form button {
            background: white;
            color: #667eea;
            border: none;
            padding: 12px 25px;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .enter-room-form button:hover {
            transform: scale(1.05);
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #6b7280;
        }
        
        .empty-state i {
            font-size: 64px;
            margin-bottom: 20px;
            opacity: 0.3;
        }
    </style>
</head>
<body>
    <div class="container-fluid py-4">
        <div class="row">
            <div class="col-12">
                <h2 class="mb-4"><i class="fas fa-comments"></i> ${pageTitle}</h2>
                
                <div class="chat-list-container">
                    <!-- Form nhập Room ID -->
                    <div class="enter-room-form">
                        <h5><i class="fas fa-door-open"></i> Tham gia phòng chat</h5>
                        <form action="${pageContext.request.contextPath}/api/admin/chat/room" method="get" class="d-flex">
                            <input 
                                type="text" 
                                name="roomId" 
                                placeholder="Nhập Room ID (vd: user-123, order-456)" 
                                required>
                            <button type="submit">
                                <i class="fas fa-arrow-right"></i> Vào phòng
                            </button>
                        </form>
                        <small class="text-white-50 d-block mt-2">
                            <i class="fas fa-info-circle"></i> Room ID thường có dạng: user-{userId} cho chat với user, order-{orderId} cho chat về đơn hàng
                        </small>
                    </div>
                    
                    <!-- Search box -->
                    <div class="search-box">
                        <input 
                            type="text" 
                            id="searchInput" 
                            placeholder="🔍 Tìm kiếm phòng chat..." 
                            onkeyup="filterRooms()">
                    </div>
                    
                    <!-- Room List -->
                    <div id="roomList">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>
                        
                        <c:choose>
                            <c:when test="${not empty rooms}">
                                <c:forEach var="room" items="${rooms}">
                                    <a href="${pageContext.request.contextPath}/api/admin/chat/room?roomId=${room.roomId}" 
                                       class="chat-room-item">
                                        <div class="room-avatar">
                                            <c:choose>
                                                <c:when test="${room.lastSenderRole eq 'ADMIN'}">
                                                    <i class="fas fa-user-shield"></i>
                                                </c:when>
                                                <c:when test="${room.lastSenderRole eq 'MANAGER'}">
                                                    <i class="fas fa-user-tie"></i>
                                                </c:when>
                                                <c:when test="${room.lastSenderRole eq 'SHIPPER'}">
                                                    <i class="fas fa-shipping-fast"></i>
                                                </c:when>
                                                <c:when test="${room.lastSenderRole eq 'USER'}">
                                                    <i class="fas fa-user"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fas fa-comments"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="room-info">
                                            <div class="room-name">${room.roomId}</div>
                                            <div class="room-preview">
                                                <strong>${room.lastSenderName}:</strong> ${room.lastMessage}
                                            </div>
                                        </div>
                                        <c:if test="${room.unreadCount > 0}">
                                            <span class="unread-badge">${room.unreadCount}</span>
                                        </c:if>
                                    </a>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <i class="fas fa-inbox"></i>
                                    <h5>Chưa có phòng chat nào</h5>
                                    <p>Các phòng chat sẽ được tạo khi có người dùng gửi tin nhắn</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function filterRooms() {
            const input = document.getElementById('searchInput');
            const filter = input.value.toUpperCase();
            const items = document.querySelectorAll('.chat-room-item');
            
            items.forEach(item => {
                const text = item.textContent || item.innerText;
                if (text.toUpperCase().indexOf(filter) > -1) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>

