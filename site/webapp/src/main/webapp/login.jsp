<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>

<html>
  <head>
    <title>Login</title>
    <link rel="shortcut icon" href="login/hst/security/skin/images/icon.png" type="image/png"/>
    <link rel="stylesheet" type="text/css" href="login/hst/security/skin/screen.css" />
  </head>
  <body class="hippo-root" onload="return document.signInForm.username.focus();" style="text-align:center">
    <div class="hippo-body-back-hack">
      <div class="hippo-login-panel">
        <form class="hippo-login-panel-form" name="signInForm" action="<hst:link path='/'/>login" method="post">
          <h2><div class="hippo-global-hideme"><span>Bloomreach Experience</span></div></h2>
          <div class="hippo-login-form-container">
            <table>
                <c:if test="${param.error != null}">
                    <tr>
                        <td colspan="2">
                        <p style="color: red;">Authentication failed with user. Try again</p>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${param.logout != null}">
                    <tr>
                        <td colspan="2">
                        <p style="color: blue;">You have been logged out.</p>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td>&nbsp;</td>
                </tr>
              <tr>
                <td width="30%"><label>Username&nbsp;</label></td>
                <td><input class="hippo-form-text" type="text" value="" name="username" id="username"/></td>
              </tr>
              <tr>
                <td><label>Password&nbsp;</label></td>
                <td><input class="hippo-form-password" type="password" value="" name="password" id="password"/></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td class="hippo-global-alignright">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                  <input class="hippo-form-submit" type="submit" value="Login"/>
                </td>
              </tr>
            </table>
          </div>
        </form>
        <div class="hippo-login-panel-copyright">
          &copy; 1999-2019 BloomReach
        </div>
      </div>
    </div>
  </body>
</html>
