<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #ffffff;
        }

        .error-container {
            text-align: center;
            padding: 20px;
            border-radius: 10px;
            background-color: #fff;
        }

        .error-icon {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            background-color: #ffcccc;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0 auto 20px;
        }

        .error-icon::before {
            content: "\2716"; /* Unicode for cross symbol */
            font-size: 100px;
            color: #ff0000;
        }

        h1 {
            font-size: 2.5rem;
            color: #333;
            margin-bottom: 10px;
        }

        p {
            font-size: 1.2rem;
            color: #555;
            margin-bottom: 20px;
        }

        a {
            color: #ff0000;
            text-decoration: none;
            font-weight: bold;
            font-size: 1.1rem;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon"></div>
        <h1>Error</h1>
        <p>${errorMessage}</p>
        <a href="/home?category=Electronics">Go back to homepage</a>
    </div>
</body>
</html>
