<#include "../../../include/imports.ftl">

<@hst.defineObjects/>
<#assign isChannelManagerPreviewRequest=hstRequest.requestContext.channelManagerPreviewRequest/>

<#if hstRequest.requestContext.channelManagerPreviewRequest>
    <p>This component cannot be previewed in Channel Manager as it needs to list files from Sharepoint which in turn requires an Office 365 user to be logged in.</p>
<#else>
    <#--  Some of the details being rendered in this component could be separated out in it's own component  -->
    <h2>Azure AD User Profile</h2>
    <#if user??>
        <p>Logged in as <b>${user.displayName} [${user.username}]</b></p>
        <p>Title: <b>${user.jobTitle}</b></p>

        <h4>Member of</h4>
        <ul>
            <#list user.groups as group>
                <li>${group}</li>
            </#list>
        </ul>

        <h2>Sharepoint Documents by Group</h2>
        <#if user.siteFiles??>
            <#list user.siteFiles?keys as siteName>
                <h4>${siteName}</h4>
                <ul>
                    <#list user.siteFiles[siteName] as item>
                        <li>
                            <a href="${item.url}" target="_blank">${item.title?html}</a> [${item.fileType}] [<b>Last Modified By:</b> ${item.modifiedBy}, Date: ${item.modifiedDate?date}]
                        </li>
                    </#list>
                </ul>
            </#list>
        <#else>
            <p>You do not belong to any Sharepoint sites</p>
        </#if>
    <#else>
        <p>You do not have an account in Azure AD. Contact Administrator</p>
    </#if>
</#if>