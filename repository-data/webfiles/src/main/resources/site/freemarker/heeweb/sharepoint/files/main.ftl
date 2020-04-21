<#include "../../../include/imports.ftl">

<h2>Files from '${siteDisplayName}' Sharepoint Site</h2>
<ul>
    <#list fileItems as item>
        <li>
            <a href="${item.url}" target="_blank">${item.title?html}</a> [${item.fileType}] [<b>Last Modified By:</b> ${item.modifiedBy}, Date: ${item.modifiedDate?date}]
        </li>
    </#list>
</ul>