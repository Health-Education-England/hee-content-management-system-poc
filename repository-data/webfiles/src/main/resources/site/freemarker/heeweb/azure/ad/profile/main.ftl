<#include "../../../../include/imports.ftl">

<@hst.defineObjects/>
<#if hstRequest.requestContext.channelManagerPreviewRequest>
    <p style="color: blue;"><strong>Azure AD User Profile:</strong> This component cannot be previewed in Channel Manager as it needs to get user profile from Azure AD which in turn requires an Azure AD/Office 365 user to be logged in.</p>
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
    <#else>
        <p>You do not have an account in Azure AD. Contact Administrator</p>
    </#if>
</#if>