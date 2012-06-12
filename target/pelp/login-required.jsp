<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<body>
<h2>Not Authenticated!!!</h2>
<p>Must be authenticated to access the content in <s:url/>.</p>
<hr/>
<h3>Technical Details</h3>
<s:actionerror/>
    <p>
      <s:property value="%{exception.message}"/>
    </p>
    <hr/>
    <h3>Technical Details</h3>
    <pre>
      <s:property value="%{exceptionStack}"/>
    </pre>
    </body>
</html>
