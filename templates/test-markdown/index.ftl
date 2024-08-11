## Users
<#list $root as user>
### ${user.name} (${user.username})
|           |     |
| ---       | --- |
| id        | ${user.id}   |
| Name      | ${user.name}   |
| Username  | ${user.username}   |
| E-mail<   | ${user.email}   |
| Address   | ${user.address.zipcode}, ${user.address.city}, ${user.address.street}, ${user.address.suite}   |
| Phone     | ${user.phone}   |
| Website   | [${user.website}](https://${user.website})   |
</#list>