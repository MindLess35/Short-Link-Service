<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        h1 {
            color: #333333;
            font-size: 24px;
        }

        p {
            color: #666666;
            font-size: 16px;
            line-height: 1.5;
        }

        .button {
            display: inline-block;
            background-color: #007bff;
            color: #ffffff;
            text-decoration: none;
            padding: 10px 20px;
            border-radius: 4px;
            font-size: 18px;
        }
    </style>
</head>

<body>
<#--<h1>Добро пожаловать, ${username}!</h1>-->
<p>Подтвердите свою почту, нажав по кнопке ниже:</p>
<p><a href="${verificationUrl}" class="button">Нажмите здесь для подтверждения</a></p>
</body>

</html>
