<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Iniciar Sesión</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 2em; }
        .login-box { max-width: 350px; margin: 5em auto; border: 1px solid #ccc; padding: 2em; border-radius: 8px; background: #fafbfc; }
        h2 { color: #007bff; text-align: center; }
        input { width: 100%; padding: 8px; margin: 8px 0; box-sizing: border-box; }
        .btn { width: 100%; background: #007bff; color: #fff; border: none; padding: 10px; cursor: pointer; border-radius: 4px; }
        .btn:hover { background: #0056b3; }
        .error { color: #c00; text-align: center; }
    </style>
</head>
<body>
<div class="login-box">
    <h2>Iniciar Sesión</h2>
    <form method="post" action="login">
        <input type="text" name="email" placeholder="Correo electrónico" required />
        <input type="password" name="password" placeholder="Contraseña" required />
        <button class="btn" type="submit">Entrar</button>
    </form>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
</div>
</body>
</html>
