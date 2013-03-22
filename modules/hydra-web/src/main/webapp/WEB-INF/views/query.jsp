<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<div>
    <table >
        <tr>
            <td>服务名称：</td>
            <td>

            </td>
        </tr>

    </table>
    <ul >
        <li ng-repeat="service in serviceArray">
            {{service.name}}
        </li>
    </ul>
</div>
</body>
</html>