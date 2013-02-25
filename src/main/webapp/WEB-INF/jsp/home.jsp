<%@taglib prefix="s" uri="/struts-tags" %>

<s:set id="contextPath"  value="#request.get('javax.servlet.forward.context_path')" />


<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="ca" class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="ca" class="ie ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="ca" class="ie ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="ca" class="ie ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="ca"> <!--<![endif]-->
<head>
	<meta charset="utf-8" />
	<title>
		<s:if test="%{teacher}">
			<s:text name="pelp.list.student"/>
		</s:if>
		<s:else>
			<s:text name="pelp.web.teacher"/>
		</s:else>
	</title>
	<meta name="description" content="Plataforma on-line per l'aprenentatge de llenguatges de programació" />
	<meta name="keywords" content="" />
	<meta name="robots" content="index, follow" /> 
	<link rel="stylesheet" type="text/css" href="css/main.css" media="all" />
	<link rel="stylesheet" type="text/css" href="css/messi.min.css" media="all" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>  
	<script type="text/javascript">window.jQuery || document.write("<script type='text/javascript' src='<s:property value="contextPath"/>/js/jquery-1.7.2.min.js'>\x3C/script>")</script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.easing.1.3.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.ba-bbq.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.customforminput.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.placeholder.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/pelp.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/messi.min.js"></script>
	<script type="text/javascript" src="http://malsup.github.com/jquery.form.js"></script>
	
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
				<h1><a href="#" title="<s:text name='pelp.init'/>"><img src="img/logo_pelp.png" alt="Pelp" /></a></h1>
				<h2><s:text name="pelp.title"></s:text></h2>
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
				<s:if test="%{fullName == null}">

					<s:form  action="home!authLocal.html" cssClass="form_login" id="form_login" theme="simple">
						<fieldset>
							<s:textfield name="username" id="username" label="username"></s:textfield>
							<s:password name="password" id="password" label="password"></s:password>
							<s:submit id="login" value="Accedir" cssClass="btn"></s:submit>
						</fieldset>
					</s:form>
					<a href="home!auth.html" id="loginopenapi" class="btnopenapi" ></a>
				</s:if>
				<s:else>
					<div class="profile"> 
							<img src="<s:property value='imageURL'/>" alt="Profile Photo" />
							<h2><s:property value="fullName"/></h2>
							<a href="home!logout.html" id="logout" class="btn"><s:text name="pelp.exit"></s:text></a>
						</div>
				</s:else>
				
			</div>

			<form action="" method="POST" class="form_filters" id="form_filters">
			<s:hidden key="ajaxCall"></s:hidden>
				<fieldset>
					 <select name="s_assign" id="s_assign" disabled="disabled">
					 <s:if test="%{!listSubjects}"><option value="-1"><s:text name="pelp.assigment"></s:text> </option></s:if>
						<s:iterator value="listSubjects" >
							<s:if test="%{s_assign == SubjectID}"> <option selected="selected" value="<s:property value="SubjectID" />"><s:property value="SubjectCode"/> - <s:property value="Description"/></option></s:if> 
							<s:else> <option value="<s:property value="SubjectID" />"><s:property value="SubjectCode"/> - <s:property value="Description"/></option> </s:else> 
						</s:iterator> 
					</select>
					<select name="s_aula" id="s_aula" disabled="disabled">
					<s:if test="%{!listClassroms}"><option value="-1"><s:text name="pelp.classroom"></s:text> </option></s:if>
						<s:iterator value="listClassroms">
							<s:if test="%{s_aula == index}"><option selected="selected" value="<s:property value="index" />"><s:text name="pelp.classroom"></s:text> <s:property value="Index" /></option></s:if>
							<s:else><option value="<s:property value="index" />"><s:text name="pelp.classroom"></s:text> <s:property value="Index" /></option></s:else>
						</s:iterator>
					</select>
					<select name="s_activ" id="s_activ" disabled="disabled">
					<s:if test="%{!listActivity}"><option value="-1"><s:text name="pelp.activiti"></s:text> </option></s:if>
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
				<li class="">
					<a href="#progMENU" id="progMENUn">
						<s:text name="pelp.prog"/>
					</a>
				</li>
				<li class="">
					<a  href="#delviMENU" id="delviMENUn">
						<s:text name="pelp.delivers"/>
					</a>
				</li>
			</ul>
		</div>
	</div>
	<!-- /menu -->
	
	<!-- main entorno -->
	<div id="main">
	<div id="progMENU" class="tab_content_menu">
		<!-- form_envios -->
<!-- 		<form action="/" method="POST" id="form_envios"> -->
		<s:form theme="simple" method="POST" enctype="multipart/form-data" action="deliveries" >
		<s:hidden key="formCall" value="true"></s:hidden>
		<s:hidden name="s_assign"></s:hidden>
		<s:hidden name="s_activ"></s:hidden>
		<s:hidden name="s_aula"></s:hidden>
		<s:hidden name="timeFile"></s:hidden>
		<s:hidden name="maxDelivers"></s:hidden>
		<s:hidden name="totalDelivers" value="%{listDeliverDetails.length}"></s:hidden>
		<!-- tabs -->
		<ul class="tabs">  
			<li><a href="#tab_1"><s:text name="pelp.file.gest"></s:text> </a></li>  
<%-- 			<li><a href="#tab_2"><s:text name="pelp.text.edit"></s:text> </a></li>    --%>
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
					<tbody id="fileuploadajax">
						<s:iterator status="stat" value="(fileDim).{ #this }" >
							<tr id="frow_<s:property value='#stat.count'/>">
								<td>
									<s:set name="codeName" value="matrizFile[#stat.index][4]"/>
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
			<label class="hlabel"><s:text name="pelp.input"></s:text> <!-- <span>  <s:text name="pelp.change.format"></s:text> <a href="#" id="in" class="commut text"><s:text name="pelp.add.file"></s:text> </a></span> --></label>
<!-- 			<div class="in_file"> -->
<%-- 				<s:file name="testFile" id="file_in" label="file_in"></s:file> --%>
<!-- <!-- 				<input type="file" id="file_in" name="file_in" /> --> 
<!-- <!-- 				<input type="button" id="adj_in" name="adj_in" value="Adjuntar" class="btn" /> --> 
<!-- 			</div> -->
			<div class="in_text">
				<s:textfield name="testPlain" id="txt_in"/>
<!-- 				<input type="text" id="txt_in" name="txt_in" value="" placeholder="Introduir text" /> -->
			</div>
		</fieldset>

		<fieldset class="fs bsolid">
			<label class="hlabel"><s:text name="pelp.output"></s:text><!--  <span><s:text name="pelp.change.format"></s:text> <a href="#" id="out" class="commut file"><s:text name="pelp.add.text2"></s:text> </a></span>  --></label>
<!-- 			<div class="out_file"> -->
<%-- 				<s:file name="testFileOut" id="file_out" label="file_out"></s:file> --%>
<!-- <!-- 				<input type="file" id="file_out" name="file_out" />  -->
<!-- <!-- 				<input type="button" id="adj_out" name="adj_out" value="Adjuntar" class="btn" /> --> 
<!-- 			</div> -->
			<div class="out_text">
				<s:textfield name="testPlainOut" id="txt_out"/>
<!-- 				<input type="text" id="txt_out" name="txt_out" value="" placeholder="Introduir text"/> -->
			</div>
		</fieldset>

		<fieldset class="fs fs_send">
			<input type="submit" id="btn_send" name="btn_send" value="Enviar" class="btn btnsend" />
			
			<s:if test="%{s_activ.length()>0}"><s:checkbox name="finalDeliver" id="chk_entrega" value="0"/>
<!-- 			<input type="checkbox" name="chk_entrega" id="chk_entrega" value="1" /> --> <label for="chk_entrega"><s:text name="pelp.send.activity"></s:text> </label>
			</s:if> 
		</fieldset>

<!-- 		</form> -->
	</s:form>
		<!--/form_envios -->

		<h3><s:text name="pelp.message"></s:text> </h3>

		<div class="messages" id="messagesFINAL">
			<p> <s:property value="resulMessage"/> </p>
		</div>
	</div>
	
	<div id="delviMENU" class="tab_content_menu">
	
		<s:if test="%{teacher}">
		
<%-- 			<h4><span>Inicio: 20/06/2012</span> <span>Final: 10/07/2012</span></h4> --%>
	
			<!-- tProfesor -->
			<table id="tProfesor" class="tablesorter tlevel_1">
				<thead class="thead">
					<tr>
						<th><s:text name="pelp.deliver"></s:text> </th>
						<th><s:text name="pelp.date"></s:text> </th>
						<th><s:text name="pelp.replis"></s:text> </th>
						<th><s:text name="pelp.compile"></s:text> </th>
						<th><s:text name="pelp.test.public"></s:text> </th>
						<th><s:text name="pelp.test.private"></s:text> </th>
					</tr>
				</thead>
				<tbody>
				
				<s:iterator value="listDeliverDetails" status="teacherpos">
				
				<s:if test="%{User.UserID.length()>0}">
				<s:set name="posUser" value="User.UserID"></s:set>
					<tr> 
						<td><a id="ajaxDelivers" href="#<s:property value="User.UserID"/>" class="toggle collapsed" rel="a<s:property value="#posUser"/>"><span class="lbl"><s:property value="User.Username"/></span></a></td>
						<td><span class="tmp lbl"><s:date name="SubmissionDate" format="dd/MM/yyyy" /></span></td>
						<td><span class="tmp lbl"><s:set name="totalTest" value="TotalPublicTests+TotalPrivateTests"/><s:property value="#totalTest"/></span></td>
						<td><s:if test="CompileOK"><span class="tmp ok"><span class="lbl invisible"><s:text name="ok"></s:text> </span></span></s:if><s:else><span class="tmp ko"><span class="lbl invisible"><s:text name="ko"></s:text> </span></span></s:else></td>
						<td><div class="tests"><span class="tmp lbl ko"><s:property value="TotalPublicTests"/></span><span class="tmp lbl ok"><s:property value="PassedPublicTests"/></span></div></td>
						<td><div class="tests"><span class="tmp lbl ko"><s:property value="TotalPrivateTests"/></span> <span class="tmp lbl ok"><s:property value="PassedPrivateTests"/></span></div></td>
					</tr>
					
										
					<tr class="expand-child">
						<td colspan="6">
							<div id="a<s:property value="#posUser"/>" class="entregas">
								<table class="tlevel_2">
									<tbody>
										<tr>
											<td><a href="#" class="toggle collapsed" rel="a<s:property value="#posUser"/>_e<s:property value="#teacherpos.count"/>"><span class="lbl"><s:text name="pelp.deliver"></s:text> <s:property value="DeliverIndex"/></span></a></td>
											<td><s:date name="SubmissionDate" format="dd/MM/yyyy" /></td>
											<td><s:set name="totalTest" value="TotalPublicTests+TotalPrivateTests"/><s:property value="#totalTest"/></td>
											<td><s:if test="CompileOK"><span class="ok"><span class="invisible"><s:text name="ok"></s:text> </span></span></s:if><s:else><span class="ko"><span class="invisible"><s:text name="ko"></s:text> </span></span></s:else></td>
											<td><div class="tests"><span class="ko"><s:property value="TotalPublicTests"/></span><span class="ok"><s:property value="PassedPublicTests"/></span></div></td>
											<td><div class="tests"><span class="ko"><s:property value="TotalPrivateTests"/></span> <span class="ok"><s:property value="PassedPrivateTests"/></span></div></td>
										</tr>
										<tr class="expand-child">
											<td colspan="6">
												<div id="a<s:property value="#posUser"/>_e<s:property value="#teacherpos.count"/>" class="files_tests">
													<table class="tlevel_3">
														<thead>
															<tr>
																<th><s:text name="pelp.file.title"></s:text> </th>
																<th><s:text name="pelp.code"></s:text> </th>
																<th><s:text name="pelp.memori"></s:text> </th>
																<th><s:text name="pelp.f.principal"></s:text> </th>
															</tr>
														</thead>
														<tbody>
															<s:iterator value="DeliverFiles" status="posfile">
																<tr>
																	<td><a href="home!down.html?idDelivers=<s:property value="#pos.index"/>&idFile=<s:property value="#posfile.index"/>&s_aula=<s:property value="s_aula"/>&s_assign=<s:property value="s_assign"/>&s_activ=<s:property value="s_activ"/>"><s:property value="RelativePath"/></a></td>
																	<td><s:if test="IsCode"><span class="check" title="<s:text name='pelp.code'></s:text>"></span></s:if></td>
																	<td><s:if test="IsReport"><span class="check" title="<s:text name='pelp.memori'></s:text>"></span></s:if></td>
																	<td><s:if test="IsMain"><span class="check" title="<s:text name='pelp.f.principal'></s:text>"></span></s:if></td>
																</tr>
															</s:iterator>
														</tbody>
													</table>
													<div class="heading"><span><s:text name="pelp.test.public"></s:text></span></div>
													<ul>
														<s:iterator value="TestResults">
															<s:if test="IsPublic">
																<s:if test="IsPassed"><li><a href="#" target="_blank"><span class="ok"></span><s:property value="Output"/></a></li></s:if>
																<s:else><li><a href="#" target="_blank"><span class="ko"></span><s:property value="Output"/></a></li></s:else>
															</s:if>
														</s:iterator>
													</ul>
													<div class="heading"><span><s:text name="pelp.test.private"></s:text></span></div>
													<ul>
														<s:iterator value="TestResults">
															<s:if test="!IsPublic">
																<s:if test="IsPassed"><li><a href="#" target="_blank"><span class="ok"></span><s:property value="Output"/></a></li></s:if>
																<s:else><li><a href="#" target="_blank"><span class="ko"></span><s:property value="Output"/></a></li></s:else>
															</s:if>
														</s:iterator>
													</ul>
												</div>
	
											</td>
										</tr>
					
					
									</tbody>
								</table>
							</div>
	
						</td>
					</tr>
							
					
					</s:if>
					</s:iterator>
				</tbody>
			</table>
			<!-- /tProfesor -->
		</s:if>

		<s:else>
		
			<!-- tAlumno -->
			<table id="tAlumno" class="tlevel_1">
				<thead>
					<tr>
						<th><s:text name="pelp.deliver"></s:text> </th>
						<th><s:text name="pelp.date"></s:text> </th>
						<th><s:text name="pelp.replis"></s:text> </th>
						<th><s:text name="pelp.compile"></s:text> </th>
						<th><s:text name="pelp.test.public"></s:text> </th>
						<th><s:text name="pelp.test.private"></s:text> </th>
					</tr>
					</thead>
					<tbody>
					<s:iterator value="listDeliverDetails" status="pos">
					<s:if test="SubmissionDate">
						<tr> 
							<td><a href="#" class="toggle collapsed" rel="a1_e<s:property value="#pos.index"/>"><span class="lbl"><s:text name="pelp.deliver"></s:text> <s:property value="DeliverIndex"/></span></a></td>
							<td><s:date name="SubmissionDate" format="dd/MM/yyyy" /></td>
							<td><s:set name="totalTest" value="TotalPublicTests+TotalPrivateTests"/><s:property value="#totalTest"/></td>
							<td><s:if test="CompileOK"><span class="ok"><span class="invisible"><s:text name="ok"></s:text> </span></span></s:if><s:else><span class="ko"><span class="invisible"><s:text name="ko"></s:text> </span></span></s:else></td>
							<td><div class="tests"><span class="ko"><s:property value="TotalPublicTests"/></span><span class="ok"><s:property value="PassedPublicTests"/></span></div></td>
							<td><div class="tests"><span class="ko"><s:property value="TotalPrivateTests"/></span> <span class="ok"><s:property value="PassedPrivateTests"/></span></div></td>
						</tr>
						<tr class="expand-child">
							<td colspan="6">
		
								<div id="a1_e<s:property value="#pos.index"/>" class="files_tests">
									<table class="tlevel_2">
										<thead>
											<tr>
												<th><s:text name="pelp.file.title"></s:text> </th>
												<th><s:text name="pelp.code"></s:text> </th>
												<th><s:text name="pelp.memori"></s:text> </th>
												<th><s:text name="pelp.f.principal"></s:text> </th>
											</tr>
										</thead>
										<tbody>
										<s:iterator value="DeliverFiles" status="posfile">
											<tr>
												<td><a href="home!down.html?idDelivers=<s:property value="#pos.index"/>&idFile=<s:property value="#posfile.index"/>&s_aula=<s:property value="s_aula"/>&s_assign=<s:property value="s_assign"/>&s_activ=<s:property value="s_activ"/>"><s:property value="RelativePath"/></a></td>
												<td><s:if test="IsCode"><span class="check" title="<s:text name='pelp.code'></s:text>"></span></s:if></td>
												<td><s:if test="IsReport"><span class="check" title="<s:text name='pelp.memori'></s:text>"></span></s:if></td>
												<td><s:if test="IsMain"><span class="check" title="<s:text name='pelp.f.principal'></s:text>"></span></s:if></td>
											</tr>
										</s:iterator>
										</tbody>
									</table>
									<div class="heading"><span><s:text name="pelp.test.public"></s:text> </span></div>
									<ul>
									<s:iterator value="TestResults">
										<s:if test="IsPublic">
											<s:if test="IsPassed"><li><a href="#" target="_blank"><span class="ok"></span><s:property value="Output"/></a></li></s:if>
											<s:else><li><a href="#" target="_blank"><span class="ko"></span><s:property value="Output"/></a></li></s:else>
										</s:if>
									</s:iterator>	
									</ul>
								</div>
		
							</td>
						</tr>
					</s:if>
					</s:iterator>
			</table>
			<!-- /tAlumno -->
		</s:else>
	</div>
	
	</div>

	<!-- main entregues -->
	
	
	
	<!-- /main -->

</div>
<div class="modal"></div>
<div class="textAula" style="display: none;"><s:text name="pelp.classroom"></s:text></div>
<div class="needFile" style="display: none;"><s:text name="pelp.need.file"></s:text></div>
<div class="okCompile" style="display: none;"><s:text name="pelp.compile.text"></s:text></div>
<div class="koLimit" style="display: none;"><s:text name="pelp.limit"></s:text></div>
<div class="koFile" style="display: none;"><s:text name="pelp.alert.file"></s:text></div>
<div class="deliverFile" style="display: none;"><s:text name="pelp.deliver"></s:text></div>
<div class="code" style="display: none;"><s:text name="pelp.code"></s:text></div>
<div class="memori" style="display: none;"><s:text name="pelp.memori"></s:text></div>
<div class="principal" style="display: none;"><s:text name="pelp.f.principal"></s:text></div>
<div class="testPublic" style="display: none;"><s:text name="pelp.test.public"></s:text></div>
<div class="testPrivate" style="display: none;"><s:text name="pelp.test.private"></s:text></div>
<div class="fileTitle" style="display: none;"><s:text name="pelp.file.title"></s:text></div>




<!-- /container -->
</body>
</html>