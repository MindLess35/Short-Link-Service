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

    </style>
</head>

<body>
<h1>Вы забыли пароль, ${username}?</h1>
<p>Чтобы сбросить пароль, перейдите по ссылке снизу:</p>
<p><a href="${resetPasswordUrl}" >Нажмите здесь для сброса пароля</a></p>
</body>

</html>
