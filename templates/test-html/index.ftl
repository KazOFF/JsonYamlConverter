<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Test JYCO</title>
</head>
<body>
<h2>Users</h2>
<#list $root as user>
    <h3>${user.name} (${user.username})</h3>
    <table>
        <tr>
            <td>id</td>
            <td>${user.id}</td>
        </tr>
        <tr>
            <td>Name</td>
            <td>${user.name}</td>
        </tr>
        <tr>
            <td>Username</td>
            <td>${user.username}</td>
        </tr>
        <tr>
            <td>E-mail</td>
            <td>${user.email}</td>
        </tr>
        <tr>
            <td>Address</td>
            <td>${user.address.zipcode}, ${user.address.city}, ${user.address.street}, ${user.address.suite}</td>
        </tr>
        <tr>
            <td>Phone</td>
            <td>${user.phone}</td>
        </tr>
        <tr>
            <td>Website</td>
            <td><a href="https://${user.website}">${user.website}</a></td>
        </tr>
    </table>
</#list>
</body>
</html>