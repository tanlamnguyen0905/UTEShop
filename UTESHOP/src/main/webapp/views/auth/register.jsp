<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Registration</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">Register</h2>
    <form action="/auth/register" method="post" enctype="multipart/form-data">
        <div class="mb-3">
            <label class="form-label">Full Name:</label>
            <input type="text" name="fullName" class="form-control" required />
        </div>
        <div class="mb-3">
            <label class="form-label">Date of Birth:</label>
            <input type="date" name="dob" class="form-control" required />
        </div>
        <div class="mb-3">
            <label class="form-label">Avatar:</label>
            <input type="file" name="avatar" class="form-control" accept="image/*" />
        </div>
        <div class="mb-3">
            <label class="form-label">Email:</label>
            <input type="email" name="email" class="form-control" required />
        </div>
        <div class="mb-3">
            <label class="form-label">Phone:</label>
            <input type="text" name="phone" class="form-control" required />
        </div>
        <div class="mb-3">
            <label class="form-label">Sex:</label>
            <select name="sex" class="form-select" required>
                <option value="">Select</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Username:</label>
            <input type="text" name="username" class="form-control" required />
        </div>
        <div class="mb-3">
            <label class="form-label">Password:</label>
            <input type="password" name="password" class="form-control" id="password" required />
        </div>
        <div class="mb-3">
            <label class="form-label">Confirm Password:</label>
            <input type="password" name="confirmPassword" class="form-control" id="confirmPassword" required />
        </div>
        <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="showPassword" onclick="togglePassword()">
            <label class="form-check-label" for="showPassword">Show Passwords</label>
        </div>
        <button type="submit" class="btn btn-primary">Register</button>
    </form>
</div>
<script>
    function togglePassword() {
        var pwd = document.getElementById('password');
        var cpwd = document.getElementById('confirmPassword');
        var type = pwd.type === 'password' ? 'text' : 'password';
        pwd.type = type;
        cpwd.type = type;
    }
</script>
</body>
</html>
