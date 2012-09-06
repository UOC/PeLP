<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="ca" class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="ca" class="ie ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="ca" class="ie ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="ca" class="ie ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="ca"> <!--<![endif]-->
<head>
<s:set id="contextPath"  value="#request.get('javax.servlet.forward.context_path')" />
	<meta charset="utf-8" />
	<title>Pelp Envíos</title>
	<meta name="description" content="Plataforma on-line per l’aprenentatge de llenguatges de programació" />
	<meta name="keywords" content="" />
	<meta name="robots" content="index, follow" />
	<link rel="stylesheet" type="text/css" href="<s:property value="contextPath"/>/css/main.css" media="all" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>  
	<script type="text/javascript">window.jQuery || document.write("<script type='text/javascript' src='<s:property value="contextPath"/>/js/jquery-1.7.2.min.js'>\x3C/script>")</script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.customforminput.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/jquery.placeholder.min.js"></script>
	<script type="text/javascript" src="<s:property value="contextPath"/>/js/pelp.js"></script>
</head>
<body>

<!-- container -->
<div id="container">

	<!-- accessibility -->
	<div id="accessibility">
		<a href="#main" accesskey="s" title="Accés directe al contingut">Accés directe al contingut</a> | 
		<a href="#menu" accesskey="n" title="Accés directe al menú de navegació">Accés directe al menú de navegació</a> 
	</div>
	<!-- /accessibility -->

	<!-- head -->
	<div id="head-container">
		<div id="head">
			<div id="pelp">
				<h1><a href="#" title="Inicio Pelp"><img src="img/logo_pelp.png" alt="Pelp" /></a></h1>
				<h2>Plataforma on-line per l’aprenentatge de llenguatges de programació</h2>
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

				<form action="/" method="POST" class="form_login" id="form_login">
					<fieldset>
						<input type="text" id="username" name="username" placeholder="Nom d'usuari"  />
						<input type="password" id="password" name="password" placeholder="Contrasenya"  />
						<input type="submit" id="login" name="login" value="Accedir" class="btn" />
					</fieldset>
				</form>

			</div>

			<form action="/" method="POST" class="form_filters" id="form_filters">
				<fieldset>
					<select name="s_assign" id="s_assign" disabled="disabled" >
						<option value="0">Assignatura</option>
						<option value="1">05.592 Arquitectures de computadors avançades</option>
					</select>
					<select name="s_aula" id="s_aula" disabled="disabled" >
						<option value="0">Aula</option>
						<option value="1">05.592 Arquitectures de computadors avançades aula 1 - Francesc Guim Bernat, Ivan Rodero Castro</option>
					</select>
					<select name="s_activ" id="s_activ" disabled="disabled" >
						<option value="0">Activitats</option>
						<option value="1">Activitat 7: Lorem ipsum dolor sit amet consectuer</option>
					</select>
					<input type="submit" id="send_filters" name="send_filters" value="Enviar" class="btn"  />
				</fieldset>
			</form>

		</div>
	</div>
	<!-- /top -->

	<!-- menu -->
	<div id="menu-container">
		<div id="menu">
			<ul class="menu">
				<li class="active"><a href="#">Entorno programación</a></li>
				<li><a href="#">Entregas</a></li>
				<li class="disabled"><a href="javascript:void(0);">Entregas</a></li>
			</ul>
		</div>
	</div>
	<!-- /menu -->

	<!-- main -->
	<div id="main">

		<!-- form_envios -->
		<form action="/" method="POST" id="form_envios">

		<!-- tabs -->
		<ul class="tabs">  
			<li><a href="#tab_1">Gestor de ficheros</a></li>  
			<li><a href="#tab_2">Editor de texto</a></li>   
		</ul> 
		<!-- /tabs --> 

		<!-- tabs_container -->
		<div class="tabs_container">

			<div id="tab_1" class="tab_content">

				<fieldset class="fs">
					<label for="search_file" class="hlabel">Adjuntar archivo</label>
					<input type="file" id="search_file" name="search_file" />
					<input type="submit" id="adj_file" name="adj_file" value="Adjuntar" class="btn" />
				</fieldset>

				<table id="tEnvios">
					<thead>
						<tr>
							<th><input type="checkbox" name="chk_del_all" id="chk_all" value="0" /> <label for="chk_all">Archivos Subidos</label></th>
							<th>Código</th>
							<th>Memoria</th>
							<th>F. Principal</th>
						</tr>
					</thead>
					<tbody>
						<tr id="frow_1">
							<td><input type="checkbox" name="chk_del" id="chk_del_1" value="1" /> <label for="chk_del_1">Lorem ipsum dolor sit amet</label></td>
							<td class="opt"><input type="checkbox" name="chk_code" id="chk_code_1" value="c1" /> <label for="chk_code_1"><span class="hidden">Código</span></label></td>
							<td class="opt"><input type="checkbox" name="chk_memo" id="chk_memo_1" value="m1" /> <label for="chk_memo_1"><span class="hidden">Memoria</span></label></td>
							<td class="opt"><input type="checkbox" name="chk_file" id="chk_file_1" value="f1" /> <label for="chk_file_1"><span class="hidden">Fichero Principal</span></label></td>
						</tr>
						<tr id="frow_2">
							<td><input type="checkbox" name="chk_del" id="chk_del_2" value="2" /> <label for="chk_del_2">Cras egestas elementum augue</label></td>
							<td class="opt"><input type="checkbox" name="chk_code" id="chk_code_2" value="c2" /> <label for="chk_code_2"><span class="hidden">Código</span></label></td>
							<td class="opt"><input type="checkbox" name="chk_memo" id="chk_memo_2" value="m2" /> <label for="chk_memo_2"><span class="hidden">Memoria</span></label></td>
							<td class="opt"><input type="checkbox" name="chk_file" id="chk_file_2" value="f2" /> <label for="chk_file_2"><span class="hidden">Fichero Principal</span></label></td>
						</tr>
						<tr id="frow_3">
							<td><input type="checkbox" name="chk_del" id="chk_del_3" value="3" /> <label for="chk_del_3">Quisque sollicitudin risus vestibulum augue vehicula ornare</label></td>
							<td class="opt"><input type="checkbox" name="chk_code" id="chk_code_3" value="c3" /> <label for="chk_code_3"><span class="hidden">Código</span></label></td>
							<td class="opt"><input type="checkbox" name="chk_memo" id="chk_memo_3" value="m3" /> <label for="chk_memo_3"><span class="hidden">Memoria</span></label></td>
							<td class="opt"><input type="checkbox" name="chk_file" id="chk_file_3" value="f3" /> <label for="chk_file_3"><span class="hidden">Fichero Principal</span></label></td>
						</tr>
					</tbody>
				</table>

				<a href="javascript:void(0);" id="lnk_del" class="btn btndel"><span class="icon"></span>Eliminar seleccionados</a>

			</div>

			<div id="tab_2" class="tab_content">
				<fieldset class="fs">
					<label for="txt_text" class="hlabel">Añadir texto:</label>
					<textarea id="txt_text"></textarea>
				</fieldset>
			</div> 

		</div>  
		<!-- /tab_container -->

		<h2>Opciones de test</h2>

		<fieldset class="fs bdotted">
			<label class="hlabel">Input. <span>Canviar format: <a href="#" id="in" class="commut text">Adjuntar com arxiu</a></span></label>
			<div class="in_file">
				<input type="file" id="file_in" name="file_in" />
				<input type="button" id="adj_in" name="adj_in" value="Adjuntar" class="btn" />
			</div>
			<div class="in_text">
				<input type="text" id="txt_in" name="txt_in" value="" placeholder="Introduir text" />
			</div>
		</fieldset>

		<fieldset class="fs bsolid">
			<label class="hlabel">Output. <span>Canviar format: <a href="#" id="out" class="commut file">Introduir com a text</a></span></label>
			<div class="out_file">
				<input type="file" id="file_out" name="file_out" />
				<input type="button" id="adj_out" name="adj_out" value="Adjuntar" class="btn" />
			</div>
			<div class="out_text">
				<input type="text" id="txt_out" name="txt_out" value="" placeholder="Introduir text"/>
			</div>
		</fieldset>

		<fieldset class="fs fs_send">
			<input type="submit" id="btn_send" name="btn_send" value="Enviar" class="btn btnsend" />
			<input type="checkbox" name="chk_entrega" id="chk_entrega" value="1" /> <label for="chk_entrega">Enviar como entrega de actividad</label>
		</fieldset>

		</form>
		<!--/form_envios -->

		<h3>Mensajes</h3>

		<div class="messages">
			<p>
			Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum varius erat quis turpis dictum egestas. Quisque congue nulla eu dolor suscipit luctus feugiat mi laoreet. Nam aliquam sodales orci, in porta velit hendrerit ut. Ut urna tortor, sagittis sit amet rhoncus in, tincidunt ac est. Nullam non arcu ipsum. Ut id purus mauris, a volutpat metus. Donec ut ante nisi, at hendrerit dui. Fusce nisl mi, hendrerit nec tincidunt eget, vehicula at turpis. Pellentesque vitae massa nec ligula blandit ultricies. Fusce adipiscing molestie diam ut porttitor.
			</p>
			<p>
			Vestibulum lorem quam, mattis eget convallis non, feugiat eget nunc. Suspendisse potenti. Aenean eget lobortis enim. Nunc porttitor nibh et nibh sollicitudin sed porttitor ligula varius. Quisque tincidunt, tortor in pretium cursus, nibh ipsum tempor dui, id vulputate lacus mauris non urna. Suspendisse vulputate dignissim porta. Sed sed diam vel quam vestibulum sodales. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque sed sapien nisl, ac tincidunt neque. Mauris imperdiet tristique auctor. Donec sed vestibulum magna. Etiam lacinia sagittis est ac scelerisque.
			</p>
			<p>
			Suspendisse in orci ligula. Sed tincidunt, odio eget tristique tincidunt, enim est sollicitudin libero, in tempus metus orci eu eros. Mauris iaculis vehicula felis quis eleifend. Cras sodales accumsan lectus id dapibus. Integer fermentum velit ut sapien tempus a commodo eros bibendum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec erat libero, vehicula a suscipit nec, varius in felis. Quisque bibendum nunc commodo odio convallis pretium. Vestibulum imperdiet elit eget nibh gravida eget euismod neque ultrices. Nunc pellentesque semper erat.
			</p>
		</div>

	</div>
	<!-- /main -->

</div>
<!-- /container -->

</body>
</html>