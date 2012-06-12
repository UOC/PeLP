<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<body>
<h2>List action!</h2>
<ul>
<s:iterator value="serviceList" status="stat">
	<li>Element <s:property value="#stat.index"/>: <a href="<s:property value="serviceList[#stat.index].url"/>"><s:property value="serviceList[#stat.index].name"/></a></li>
</s:iterator>

</ul>
<h2>Add a service</h2>
<s:form action="add" method="post">
<s:textfield label="Name" name="name"/>
<s:textfield label="URL" name="url"/>
<s:submit value="Add"/>
</s:form>
</body>
</html>
