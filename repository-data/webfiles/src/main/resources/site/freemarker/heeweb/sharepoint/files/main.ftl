<#include "../../../include/imports.ftl">

<@hst.defineObjects/>
<#if hstRequest.requestContext.channelManagerPreviewRequest>
    <p style="color: blue;"><strong>Sharepoint Documents by Group:</strong>This component cannot be previewed in Channel Manager as it needs to list files from Sharepoint which in turn requires an Azure AD/Office 365 user to be logged in.</p>
<#else>
    <#--  Some of the details being rendered in this component could be separated out in it's own component  -->
    <h2>Sharepoint Documents by Group</h2>
    <#if sharepointSiteFiles??>
        <#list sharepointSiteFiles?keys as siteName>
            <h4>${siteName}</h4>
            <ul>
                <#list sharepointSiteFiles[siteName] as item>
                    <li>
                        <a href="${item.url}" target="_blank">${item.title?html}</a> [${item.fileType}] [<b>Last Modified By:</b> ${item.modifiedBy}, <b>Date</b>: ${item.modifiedDate?date}]
                    </li>
                </#list>
            </ul>
        </#list>
    <#else>
        <p>You do not belong to any Sharepoint sites</p>
    </#if>
</#if>