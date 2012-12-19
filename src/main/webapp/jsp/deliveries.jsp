<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="ca" class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="ca" class="ie ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="ca" class="ie ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="ca" class="ie ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="ca"> <!--<![endif]-->
<head>
	<s:set id="contextPath"  value="#request.get('javax.servlet.forward.context_path')" />
	<title><s:text name="pelp.title.body"></s:text> </title>
	<meta name="description" content="Plataforma on-line per l’aprenentatge de llenguatges de programació" />
	<meta name="keywords" content="" />
	<meta name="robots" content="index, follow" />
	<link rel="stylesheet" type="text/css" href="<s:property value="contextPath"/>/css/main.css" media="all" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>  
	<script type="text/javascript">window.jQuery || document.write("<script type='text/javascript' src='<s:property value="contextPath"/>/js/jquery-1.7.2.min.js'>\x3C/script>")</script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.customforminput.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.placeholder.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/pelp.js"></script>
</head>
<body>

<!-- container -->
<div id="container">

	<!-- accessibility -->
	<div id="accessibility">
		<a href="#main" accesskey="s" title="<s:text name='pelp.acces'></s:text>"> <s:text name="pelp.acces"></s:text> </a> | 
		<a href="#menu" accesskey="n" title="<s:text name='pelp.acces.nav'></s:text>"> <s:text name="pelp.acces.nav"></s:text> </a> 
	</div>
	<!-- /accessibility -->

	<!-- head -->
	<div id="head-container">
		<div id="head">
			<div id="pelp">
				<h1><a href="#" title="<s:text name='pelp.init'></s:text>"><img src="img/logo_pelp.png" alt="Pelp" /></a></h1>
				<h2> <s:text name="pelp.title"></s:text> </h2>
			</div>
			<div id="uoc">
				<a href="http://www.uoc.edu" title="UOC"><img src="img/logo_uoc.png" alt="UOC" /></a>
			</div>
		</div>
	</div>
	<!-- /head -->

	<!-- top -->
	<div id="top-container">
		<div id="top">

			<div id="user">
				<s:if test="%{imageURL == null}">
					<s:form  action="deliveries!auth.html" cssClass="form_login" id="form_login" theme="simple">
		<!-- 				<form action="/" method="POST" class="form_login" id="form_login"> -->
							<fieldset>
							<s:textfield name="username" id="username" label="username"></s:textfield>
							<s:password name="password" id="password" label="password"></s:password>
		<!-- 						<input type="text" id="username" name="username" placeholder="Nom d'usuari"  /> -->
		<!-- 						<input type="password" id="password" name="password" placeholder="Contrasenya"  /> -->
		<!-- 						<input type="submit" id="login" name="login" value="Accedir" class="btn" /> -->
								<s:submit id="login" value="Accedir" cssClass="btn"></s:submit>
							</fieldset>
		<!-- 				</form> -->
					</s:form>
				</s:if>
				<s:else>
					<div class="profile"> 
							<img src="<s:property value='imageURL'/>" alt="Profile Photo" />
							<h2><s:property value="fullName"/></h2>
							<a href="javascript:void(0);" id="logout" class="btn"> <s:text name="pelp.exit"></s:text> </a>
						</div>
				</s:else>
			</div>
			<form action="" method="POST" class="form_filters" id="form_filters">
				<fieldset>
					 <select name="s_assign" id="s_assign">
					 	<option value=""><s:text name="pelp.assigment"></s:text> </option>
						<s:iterator value="listSubjects" >
							<s:if test="%{s_assign == SubjectID}"> <option selected="selected" value="<s:property value="SubjectID" />"><s:property value="Description"/></option></s:if> 
							<s:else> <option value="<s:property value="SubjectID" />"><s:property value="Description"/></option> </s:else> 
						</s:iterator> 
					</select>
					<select name="s_aula" id="s_aula">
						<option value=""><s:text name="pelp.classroom"></s:text> </option>
						<s:iterator value="listClassroms">
							<s:if test="%{s_aula == index}"><option selected="selected" value="<s:property value="index" />">AULA HACK <s:property value="ClassroomID.ClassIdx" /></option></s:if>
							<s:else><option value="<s:property value="index" />">AULA HACK <s:property value="ClassroomID.ClassIdx" /></option></s:else>
						</s:iterator>
					</select>
					<select name="s_activ" id="s_activ">
						<option value=""><s:text name="pelp.activiti"></s:text> </option>
						<s:iterator value="listActivity" status="statsa">
							<s:if test="%{s_activ == index}"><option selected="selected" value="<s:property value="index" />"><s:property value="description" /></option></s:if>
							<s:else><option value="<s:property value="index" />"><s:property value="description" /></option></s:else>
						</s:iterator>
					</select>
					<input type="submit" id="send_filters" name="send_filters" value="Enviar" class="btn"/>
				</fieldset>
			</form>

		</div>
	</div>
	<!-- /top -->

	<!-- menu -->
	<div id="menu-container">
		<div id="menu">
			<ul class="menu">
				<li class="active"><a href="deliveries.html"><s:text name="pelp.prog"></s:text> </a></li>
				<li><a href="student.html"><s:text name="pelp.delivers"></s:text> </a></li>
<!-- 				<li class="disabled"><a href="javascript:void(0);">Entregas</a></li> -->
			</ul>
		</div>
	</div>
	<!-- /menu -->

	<!-- main -->
	<div id="main">

		<!-- form_envios -->
<!-- 		<form action="/" method="POST" id="form_envios"> -->
		<s:form theme="simple" method="POST" enctype="multipart/form-data" action="deliveries">
		<s:hidden name="s_assign"></s:hidden>
		<s:hidden name="s_activ"></s:hidden>
		<s:hidden name="s_aula"></s:hidden>
		<!-- tabs -->
		<ul class="tabs">  
			<li><a href="#tab_1"><s:text name="pelp.file.gest"></s:text> </a></li>  
			<li><a href="#tab_2"><s:text name="pelp.text.edit"></s:text> </a></li>   
		</ul> 
		<!-- /tabs --> 

		<!-- tabs_container -->
		<div class="tabs_container">

			<div id="tab_1" class="tab_content">

				<fieldset class="fs">
					<label for="search_file" class="hlabel"><s:text name="pelp.file.attach"></s:text> </label>
					<s:file name="upload" label="Adjuntar"></s:file>
<!-- 					<input type="submit" id="adj_file" name="adj_file" value="Adjuntar" class="btn" /> -->
					<s:submit id="adj_file" value="Adjuntar" class="btn"></s:submit>
				</fieldset>

				<table id="tEnvios">
					<thead>
						<tr>
							<th><s:checkbox name="matrizFile" key="matrizFile" id="chk_all" fieldValue="deleteAll"/><label for="chk_all"><s:text name="pelp.upload.file"></s:text> </label></th>
							<th><s:text name="pelp.code"></s:text> </th>
							<th><s:text name="pelp.memori"></s:text> </th>
							<th><s:text name="pelp.f.principal"></s:text> </th>
						</tr>
					</thead>
					<tbody>

					<s:iterator status="stat" value="(fileDim).{ #this }" >
						<tr id="frow_<s:property value='#stat.count'/>">
							<td>
								<s:set name="codeName" value="matrizFile[#stat.index][4]"/>
<%-- 							<s:checkbox name="chk_del" value="%{#stat.count}" key="matrizFile"  id="chk_del_%{#stat.count}" /> <label for="chk_del_<s:property value='#stat.count'/>"><s:property value="matrizFile[#stat.index][0]"/></label></td> --%>
								<input type="checkbox" name="chk_del" id="chk_del_<s:property value='#stat.count'/>" value="<s:property value='#stat.count'/>" /> 
								<label id="chk_del_title<s:property value='#stat.count'/>"  for="chk_del_<s:property value='#stat.count'/>"><s:property value="matrizFile[#stat.index][0]"/></label>
								<input type="hidden" id="chk_del_title_hash<s:property value='#stat.count'/>"  value="<s:property value='%{#codeName}'/>"/>
							</td>
							<td class="opt">
											<s:checkbox name="matrizFile" key="matrizFile" id="chk_code_%{#stat.count}"  fieldValue="c%{#codeName}"/>
											<label for="chk_code_<s:property value='#stat.count'/>"><span class="hidden"><s:text name="pelp.code"></s:text></span></label></td>
							<td class="opt">
											<s:checkbox name="matrizFile" key="matrizFile" id="chk_memo_%{#stat.count}"  fieldValue="m%{#codeName}"/>
											<label for="chk_memo_<s:property value='#stat.count'/>"><span class="hidden"><s:text name="pelp.memori"></s:text></span></label></td>
							<td class="opt">
											<s:checkbox name="matrizFile" key="matrizFile" id="chk_file_%{#stat.count}"  fieldValue="f%{#codeName}"/>
											<label for="chk_file_<s:property value='#stat.count'/>"><span class="hidden"><s:text name="pelp.f.principal"></s:text></span></label></td>
						</tr>
					</s:iterator>
					</tbody>
				</table>

				<a href="javascript:void(0);" id="lnk_del" class="btn btndel"><span class="icon"></span><s:text name="pelp.file.delete"></s:text> </a>

			</div>

			<div id="tab_2" class="tab_content">
				<fieldset class="fs">
					<label for="txt_text" class="hlabel"><s:text name="pelp.add.text"></s:text> </label>
					<s:textarea name="codePlain" id="txt_text" />
				</fieldset>
			</div> 

		</div>  
		<!-- /tab_container -->

		<h2><s:text name="pelp.option.text"></s:text> </h2>

		<fieldset class="fs bdotted">
			<label class="hlabel"><s:text name="pelp.input"></s:text> <span><s:text name="pelp.change.format"></s:text> <a href="#" id="in" class="commut text"><s:text name="pelp.add.file"></s:text> </a></span></label>
			<div class="in_file">
				<s:file name="testFile" id="file_in" label="file_in"></s:file>
<!-- 				<input type="file" id="file_in" name="file_in" /> -->
<!-- 				<input type="button" id="adj_in" name="adj_in" value="Adjuntar" class="btn" /> -->
			</div>
			<div class="in_text">
				<s:textfield name="testPlain" id="txt_in"/>
<!-- 				<input type="text" id="txt_in" name="txt_in" value="" placeholder="Introduir text" /> -->
			</div>
		</fieldset>

		<fieldset class="fs bsolid">
			<label class="hlabel"><s:text name="pelp.output"></s:text> <span><s:text name="pelp.change.format"></s:text> <a href="#" id="out" class="commut file"><s:text name="pelp.add.text2"></s:text> </a></span></label>
			<div class="out_file">
				<s:file name="testFileOut" id="file_out" label="file_out"></s:file>
<!-- 				<input type="file" id="file_out" name="file_out" /> -->
<!-- 				<input type="button" id="adj_out" name="adj_out" value="Adjuntar" class="btn" /> -->
			</div>
			<div class="out_text">
				<s:textfield name="testPlainOut" id="txt_out"/>
<!-- 				<input type="text" id="txt_out" name="txt_out" value="" placeholder="Introduir text"/> -->
			</div>
		</fieldset>

		<fieldset class="fs fs_send">
			<input type="submit" id="btn_send" name="btn_send" value="Enviar" class="btn btnsend" />
			<s:checkbox name="finalDeliver" id="chk_entrega" value="0"/>
<!-- 			<input type="checkbox" name="chk_entrega" id="chk_entrega" value="1" /> --> <label for="chk_entrega"><s:text name="pelp.send.activity"></s:text> </label> 
		</fieldset>

<!-- 		</form> -->
	</s:form>
		<!--/form_envios -->

		<h3><s:text name="pelp.message"></s:text> </h3>

		<div class="messages">
			<p> <s:property value="resulMessage"/> </p>
		</div>

	</div>
	<!-- /main -->

</div>
<!-- /container -->

</body>
</html>