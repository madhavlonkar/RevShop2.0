<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <title>Detail Registration</title>
</head>
<body>
    <h1>Welcome, please complete your registration</h1>
    <p>Email: ${email}</p>
    <form action="/completeDetailRegistration" method="post">
        <!-- Add your detailed registration form here -->
        <input type="text" name="firstName" placeholder="First Name" required>
        <input type="text" name="lastName" placeholder="Last Name" required>
        <!-- Other fields -->
        <button type="submit">Submit</button>
    </form>
</body>
</html>
