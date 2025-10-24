<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Debug Address Phone</title>
    <style>
        body { font-family: monospace; padding: 20px; background: #f0f0f0; }
        .test-section { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; }
        .success { color: green; }
        .error { color: red; }
        .info { color: blue; }
        button { padding: 10px 20px; margin: 5px; cursor: pointer; }
        pre { background: #f5f5f5; padding: 10px; overflow-x: auto; }
        input, textarea { width: 100%; padding: 8px; margin: 5px 0; }
    </style>
</head>
<body>
    <h1>🔍 Address Phone Debug Tool</h1>
    
    <!-- Test 1: Create Address -->
    <div class="test-section">
        <h2>Test 1: Tạo địa chỉ mới</h2>
        <form id="testForm">
            <label>Tên: <input type="text" id="test_name" value="Test User" required></label>
            <label>Phone: <input type="tel" id="test_phone" value="0987654321" required></label>
            <label>Tỉnh: <input type="text" id="test_province" value="Hà Nội" required></label>
            <label>Quận: <input type="text" id="test_district" value="Ba Đình" required></label>
            <label>Phường: <input type="text" id="test_ward" value="Điện Biên" required></label>
            <label>Địa chỉ chi tiết: <textarea id="test_addressDetail" required>123 Test Street</textarea></label>
            <label>
                <input type="checkbox" id="test_isDefault"> Đặt làm mặc định
            </label>
            <br>
            <button type="submit">🚀 Gửi Request</button>
        </form>
        
        <h3>Request Data:</h3>
        <pre id="requestData">Chưa có dữ liệu</pre>
        
        <h3>Response:</h3>
        <pre id="responseData">Chưa có dữ liệu</pre>
    </div>
    
    <!-- Test 2: Get All Addresses -->
    <div class="test-section">
        <h2>Test 2: Lấy tất cả địa chỉ</h2>
        <button onclick="getAllAddresses()">📋 Lấy danh sách địa chỉ</button>
        
        <h3>Response:</h3>
        <pre id="getAllResponse">Chưa có dữ liệu</pre>
    </div>
    
    <!-- Test 3: Check localStorage Token -->
    <div class="test-section">
        <h2>Test 3: Kiểm tra Authentication</h2>
        <button onclick="checkAuth()">🔐 Kiểm tra Token</button>
        <pre id="authInfo">Chưa kiểm tra</pre>
    </div>

    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        // Test 1: Create Address
        document.getElementById('testForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const formData = {
                name: document.getElementById('test_name').value,
                phone: document.getElementById('test_phone').value,
                province: document.getElementById('test_province').value,
                district: document.getElementById('test_district').value,
                ward: document.getElementById('test_ward').value,
                addressDetail: document.getElementById('test_addressDetail').value,
                isDefault: document.getElementById('test_isDefault').checked
            };
            
            // Display request data
            document.getElementById('requestData').textContent = JSON.stringify(formData, null, 2);
            
            console.log('=== SENDING REQUEST ===');
            console.log('URL:', contextPath + '/api/user/addresses');
            console.log('Data:', formData);
            
            try {
                const token = localStorage.getItem('authToken');
                console.log('Token:', token ? 'EXISTS' : 'NOT FOUND');
                
                const response = await fetch(contextPath + '/api/user/addresses', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': token ? 'Bearer ' + token : ''
                    },
                    body: JSON.stringify(formData)
                });
                
                const result = await response.json();
                console.log('=== RESPONSE ===');
                console.log('Status:', response.status);
                console.log('Result:', result);
                
                // Display response
                document.getElementById('responseData').innerHTML = 
                    '<span class="' + (result.success ? 'success' : 'error') + '">' +
                    'Status: ' + response.status + '\n' +
                    '</span>' +
                    JSON.stringify(result, null, 2);
                
                // Check phone in response
                if (result.success && result.data) {
                    if (result.data.phone) {
                        alert('✅ SUCCESS! Phone được lưu: ' + result.data.phone);
                    } else {
                        alert('⚠️ WARNING! Address được tạo nhưng phone = ' + result.data.phone);
                    }
                }
                
            } catch (error) {
                console.error('ERROR:', error);
                document.getElementById('responseData').innerHTML = 
                    '<span class="error">ERROR: ' + error.message + '</span>';
            }
        });
        
        // Test 2: Get All Addresses
        async function getAllAddresses() {
            console.log('=== GET ALL ADDRESSES ===');
            
            try {
                const token = localStorage.getItem('authToken');
                const response = await fetch(contextPath + '/api/user/addresses', {
                    method: 'GET',
                    headers: {
                        'Authorization': token ? 'Bearer ' + token : ''
                    }
                });
                
                const result = await response.json();
                console.log('Addresses:', result);
                
                // Display with phone check
                let output = 'Status: ' + response.status + '\n\n';
                
                if (result.success && result.data) {
                    output += 'Total addresses: ' + result.data.length + '\n\n';
                    
                    result.data.forEach((addr, index) => {
                        output += '--- Address #' + (index + 1) + ' ---\n';
                        output += 'ID: ' + addr.addressID + '\n';
                        output += 'Name: ' + addr.name + '\n';
                        output += 'Phone: ' + (addr.phone || '❌ NULL/EMPTY') + '\n';
                        output += 'Province: ' + addr.province + '\n';
                        output += 'District: ' + addr.district + '\n';
                        output += 'Ward: ' + addr.ward + '\n\n';
                    });
                }
                
                document.getElementById('getAllResponse').textContent = output;
                
            } catch (error) {
                console.error('ERROR:', error);
                document.getElementById('getAllResponse').innerHTML = 
                    '<span class="error">ERROR: ' + error.message + '</span>';
            }
        }
        
        // Test 3: Check Auth
        function checkAuth() {
            const token = localStorage.getItem('authToken');
            const session = '${sessionScope.currentUser != null ? sessionScope.currentUser.username : "NO SESSION"}';
            
            let info = '=== AUTHENTICATION INFO ===\n\n';
            info += 'Session User: ' + session + '\n';
            info += 'LocalStorage Token: ' + (token ? 'EXISTS (length: ' + token.length + ')' : '❌ NOT FOUND') + '\n\n';
            
            if (token) {
                info += 'Token Preview: ' + token.substring(0, 50) + '...\n';
            } else {
                info += '⚠️ WARNING: No token found! API requests may fail.\n';
                info += 'Please login first at: ' + contextPath + '/auth/login';
            }
            
            document.getElementById('authInfo').textContent = info;
            console.log(info);
        }
        
        // Auto check auth on page load
        window.addEventListener('load', () => {
            console.log('=== ADDRESS PHONE DEBUG TOOL ===');
            console.log('Context Path:', contextPath);
            checkAuth();
        });
    </script>
</body>
</html>


