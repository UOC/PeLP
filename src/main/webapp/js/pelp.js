var j=jQuery.noConflict();

j(document).ready(function(){

	/* Expand Collapse Rows Funcionality */
	var tSpeed = 300;							// transition speed
												// (milliseconds)
	var tEffect = 'easeInOutExpo';				// transition effect
	var orow = null;							// link row opened (for
												// accordeon rows)

	jQuery.fn.expandCollapseRow = function (id) {
		var elm = j("#" + id);
		var tr = j(this).parent().parent();
		var acc = j(this).data('accordeon');

		if (j(this).hasClass('collapsed')) {
			j(this).removeClass('collapsed').addClass('expanded');
			tr.addClass('expanded');
			tr.find('.tmp').css({opacity: 1}).animate({opacity: 0}, tSpeed);
			
			if(acc){
				if(orow!=null && orow!=j(this)){
					var otr = orow.parent().parent();
					var ocont = j("#"+orow.attr('rel'));
					orow.removeClass('expanded').addClass('collapsed');
					otr.find('.tmp').css({opacity: 0}).animate({opacity: 1}, tSpeed);
					otr.removeClass('expanded');
					ocont.slideToggle(tSpeed, tEffect);
				}
				orow=j(this);
			}

		}else{
			j(this).removeClass('expanded').addClass('collapsed');
			tr.removeClass('expanded');
			tr.find('.tmp').css({opacity: 0}).animate({opacity: 1}, tSpeed);
			if(acc) orow=null;
		}
			
		elm.slideToggle(tSpeed, tEffect);
	};

	/* Common */

	// IE6 first-child & last-child pseudo-selectors hack
	if ( j.browser.msie && j.browser.version=='6.0') {
		j('tr td:first-child').addClass('first-child');
		j('tr th:first-child').addClass('first-child');
		j('tr:first-child').addClass('first-child');
		j('tr:last-child').addClass('last-child');
	}

	// Mac hacks
	if(navigator.userAgent.indexOf('Mac') > 0)
    	j('body').addClass('mac-os');

	// Eventos de filtros s_activ
	//j('#s_assign').change(function() { this.form.submit(); });
	//j('#s_aula').change(function() { this.form.submit(); });
	//j('#s_activ').change(function() { this.form.submit(); });

    // Ocultar elementos iniciales
	j('#send_filters').hide();
	
	// Miramos si estan selecionados los combos en caso de estar no los ponemos ocultos
	
	
	if(j("#s_activ option:selected").val()!="")j("#s_activ").attr('disabled',false);
	if(j("#s_aula option:selected").val()!="")j("#s_aula").attr('disabled',false);
	
	

    /* Envíos */

	// Establecer placeholders para navegadores no compatibles
	j('input[placeholder]').placeholder();

	// Customizar elementos de formulario
	j('input').customFormInput({
		browseLbl: 'Examinar...',				/* browse button label */
		changeLbl: 'Canviar...',				/* change button label */
		noneLbl: 'Cap fitxer seleccionat'		/* no file label */
	});

	// Control In/Out
	j("a.commut").click(function(ev) {
		ev.preventDefault();
		var id = j(this).attr('id');
		if( j(this).hasClass('text') ){
			j(this).addClass('file').html('Introduir com a text').removeClass('text');
			j('div.'+id+'_text').hide();
			j('div.'+id+'_file').show();
		}else{
			j(this).addClass('text').html('Adjuntar com arxiu').removeClass('file');
			j('div.'+id+'_file').hide();
			j('div.'+id+'_text').show();
		}
	});

	// Iniciar pestañas
	j(".tab_content").hide();
	j("ul.tabs li:first").addClass("active").show();
	j(".tab_content:first").show();
	
	// Control pestañas
	j("ul.tabs li a").click(function(ev) {
		ev.preventDefault();
		j("ul.tabs li").removeClass("active");
		j(this).parent().addClass("active");
		j(".tab_content").hide();
		var activeTab = j(this).attr("href");
		j(activeTab).show();
	});
	
	// pesta�as de navegaci�n general
	j(".tab_content_menu").hide();
	// Cargamos dependiendo de valor call
	if(j("#ajaxCall").val()=="true"){
		j("ul.menu li:first").addClass("active").show();
		j(".tab_content_menu:first").show();
	}else{
		j("ul.menu li:last").addClass("active").show();
		j(".tab_content_menu:last").show();	
	}
			// Control pestañas
	j("ul.menu li a").click(function(ev) {
		ev.preventDefault();
		j("ul.menu li").removeClass("active");
		j(this).parent().addClass("active");
		j(".tab_content_menu").hide();
		var activeTab = j(this).attr("href");
		j(activeTab).show();
	});

	// Selección multiple checkbox
	j('#chk_all').click(function(ev) {
        j("input[name='chk_del'][type='checkbox']").attr('checked', j(this).is(':checked')).trigger('updateState');
    });

    // Borrar archivos seleccionados
    j('#lnk_del').click(function(ev) {
    	// comprovamos si tiene eleguido los combos.
    	s_assign = j("#s_assign").val();
    	s_aula = j("#s_aula").val();
    	s_activ = j("#s_activ").val();
    	petitionClassroom = "";
    	
    	if(s_assign)petitionClassroom += "&s_assign="+s_assign;
    	if(s_aula)petitionClassroom += "&s_aula="+s_aula;
    	if(s_activ)petitionClassroom += "&s_activ="+s_activ;
    	j("input[name='chk_del'][type='checkbox']").each(function(i){
    		if(j(this).is(':checked')){
    			callback="";
    			j.ajax({
    				  url: "deliveries!delete.html",
    				  dataType: 'json',
    				  data: "auxInfo="+j('#chk_del_title_hash'+j(this).val()).val()+"&timeFile="+j("#deliveries_timeFile").val()+petitionClassroom,
    				  success: callback,
    				  type: "POST"
    				});
    			j('#frow_'+j(this).val()).remove();
    		}
    	});
    });
    
    // cargar dinamicamente los combo.
    j('#s_assign').change(function(ev){
    	j("#deliveries_s_assign").val(j(this).val());
    	j('#s_aula').val("-1");
    	j('#s_activ').val("-1");
    	callback="";
    	j('body').addClass("loading"); 
    	j.ajax({
			  url: "home!combo.html",
			  dataType: 'json',
			  data: "s_assign="+j(this).val(),
			  success:function(data){
				var options = j("#s_aula option")[0].outerHTML
				classrooms = data.listClassroms
				for (var i = 0; i < classrooms.length; i++) {
					options += '<option value="' + classrooms[i].index + '">' +j('.textAula').html() +" "+ classrooms[i].index + '</option>';
				}
				j('#s_aula').html(options);
				j('body').removeClass("loading"); 
			  },
			  type: "POST"
			});
    	j('#s_aula').attr('disabled',false);
    });
    
    j('#s_aula').change(function(ev){
    	
    	j("#deliveries_s_aula").val(j(this).val());
    	j('#s_activ').val("-1");
    	callback="";
    	j('body').addClass("loading"); 
    	j.ajax({
			  url: "home!combo.html",
			  dataType: 'json',
			  data: "s_assign="+j("#s_assign").val()+"&s_aula="+j(this).val(),
			  success:function(data){
				var options = j("#s_activ option")[0].outerHTML
				classrooms = data.listActivity
				for (var i = 0; i < classrooms.length; i++) {
					options += '<option value="' + classrooms[i].index + '">' + classrooms[i].description + '</option>';
				}
				j('#s_activ').html(options);
				j('body').removeClass("loading"); 
			  },
			  type: "POST"
			});
    	
    	j('#s_activ').attr('disabled',false);
    });
    
    j('#s_activ').change(function(ev){
    	j("#deliveries_s_activ").val(j(this).val());
    	this.form.submit();
    });
    
    j("#progMENUn").click(function(ev) {
    	j("#ajaxCall").val(true);
    });
    
    j("#delviMENUn").click(function(ev) {
    	j("#ajaxCall").val(false);
    });

    /* Entregas */

	j('.tablesorter').tablesorter({
		sortList: [[0,0]],
		selectorHeaders: 'thead.thead th',
		headers: { 
			4: { sorter: false }, 
            5: { sorter: false } 
        },
		textExtraction: function(node) { 
            return j(node).find('span.lbl').html(); 
        },
        debug: false
	});

	j('#tProfesor > tbody > tr > td > a').each(function(){
		j(this).data('accordeon', true);
	});

	j('a.toggle').prepend('<span class="icon"></span>');
	j('a.toggle').click(function(ev) {
		ev.preventDefault();
		j(this).expandCollapseRow(j(this).attr('rel'));
	});


	j('a.collapsed').each(function(){
		j("#" + j(this).attr('rel')).hide();
	});

	j('a.expanded').each(function(){
		var tr = j(this).parent().parent();
		tr.addClass('expanded');
	});

	/* Test Info */

	j('.accordion h3:first').addClass('active');
	j('.accordion .acontent:not(:first)').hide();
	j('.accordion h3').prepend('<span class="icon"></span>').click(function(){
		j(this).next('.acontent').slideToggle(tSpeed, tEffect).siblings(".acontent:visible").slideUp(tSpeed, tEffect);
		j(this).toggleClass('active');
		j(this).siblings('h3').removeClass('active');
	});
	
	/* ajax calls */
	
	j('.ajax-tab').each(function(ind, elem){
		var thisTab = j(this);
		thisTab.click(function(event){
			var href = thisTab.attr('href');
			var whichTab = jQuery.deparam( href )['?activeTab'];
			var modifiedHref = jQuery.param.querystring( href, 'ajaxCall=true' );
			jQuery.ajax({
				url: modifiedHref,
				beforeSend: doBeforeChangeTab,
				success: function(data, textStatus, jqXHR){
					doChangeTab(data, textStatus, jqXHR, whichTab);
				},
				error: doErrorChangingTab,
				complete: doCompleteChangeTab
			});
			event.preventDefault();
		});
	});
	
if(j('#logout').html()){	
	
	j('#deliveries_formCall').val(true);
	j('#deliveries').ajaxForm({
        beforeSubmit: function() {
        	// Comprovamos si no pasa el numeor maximo de entregas.
        	
        	if(!validate_filename((j(':file').val()))){
        		new Messi(j(".koFile").html());
        		return false;
        	}
        	
        	if(j('#deliveries_totalDelivers').val()>j('#deliveries_maxDelivers')){
        		new Messi(j(".koLimit").html());
        		return false;
        	}
        	
        	j('#messagesFINAL').html('<p></p>');
        	j('body').addClass("loading"); 
        },
        success: function(data) {
        	// Miramos si el resultado es de message o de fileupload.
            if(data.resulMessage){
            	j('#deliveries_timeFile').val(data.timeFile); // el fichero que se usa.
            	j('#messagesFINAL').html('<p>'+data.resulMessage+'</p>');
            	j('body').removeClass("loading");
            	
            	
            	if(data.resulMessage=="OK" && data.finalDeliver){
            		s_assign = j("#s_assign").val();
                	s_aula = j("#s_aula").val();
                	s_activ = j("#s_activ").val();
                	petitionClassroom = "";
                	
                	if(s_assign)petitionClassroom += "&s_assign="+s_assign;
                	if(s_aula)petitionClassroom += "&s_aula="+s_aula;
                	if(s_activ)petitionClassroom += "&s_activ="+s_activ;
                	
            		j(window).attr("location","home.html?ajaxCall=false"+petitionClassroom);
            	}else if(data.resulMessage=="OK"){
            		new Messi(j(".okCompile").html());
            	}
            	
            }else{
            	if(data.matrizFile){
	            	var out = j('#fileuploadajax'); // Creamos el listado de ficheros.
	            	content = "";
	            	j.each(data.matrizFile, function(index, value) {
	            		
	            		content += "<tr id='frow_"+index+"'><td>";
	            		content += "<input type='checkbox' name='chk_del' id='chk_del_"+index+"' value='"+index+"' />";
	            		content += "<label id='"+index+"'  for='chk_del_"+index+"'>"+value[0]+"</label>";
	            		content += "<input type='hidden' id='chk_del_title_hash"+index+"'  value='"+value[4]+"'/>";
	            		content += "</td><td class='opt'>";
	            		content += "<input type='checkbox' name='matrizFile' value='c"+value[4]+"' id='chk_code_"+index+"'/><label for='chk_code_"+index+"'><span class='hidden'>CODE HACK</span></label></td>";
	            		content += "<td class='opt'><input type='checkbox' name='matrizFile' value='m"+value[4]+"' id='chk_memo_"+index+"'/><label for='chk_memo_"+index+"'><span class='hidden'>memo HACK</span></label></td>";
	            		content += "<td class='opt'><input type='checkbox' name='matrizFile' value='f"+value[4]+"' id='chk_file_"+index+"'/><label for='chk_file_"+index+"'><span class='hidden'>principal HACK</span></label></td>";
	            		content += "</tr>";
	            	});
	            	out.html(content);
	            	// eliminamos los cambos anteriores
	            	j(".customfile").html("<input id='deliveries_upload' class='customfile-input' type='file' value='' name='upload'>")
	            	j('#deliveries_timeFile').val(data.timeFile); // el fichero que se usa.
	            	j('input').customFormInput({
	            		browseLbl: 'Examinar...',				/* browse button label */
	            		changeLbl: 'Canviar...',				/* change button label */
	            		noneLbl: 'Cap fitxer seleccionat'		/* no file label */
	            	});
            	}else{
            		new Messi(j(".needFile").html());
            	}
            	j('body').removeClass("loading"); 
            }
        }
    });
}
	
/* menu*/
		
});

function doBeforeChangeTab(jqXHR, settings){
	//alert('ajax call issued');
}

function doChangeTab(data, textStatus, jqXHR, whichTabIsActive){

	//alert('about to replace contents');

    var jqObj = jQuery(data);
    var theirMain = jqObj.find("#main");
	j('#main').html(theirMain);
	
	j('.ajax-tab').each(function(ind, e){
		var thisTab = j(this);
		var thisHref = thisTab.attr('href');
		var ind = thisHref.indexOf(whichTabIsActive);
		if (ind != -1){
			thisTab.parent().addClass('active');
		} else {
			thisTab.parent().removeClass('active');
		}
	});
	
}

function doCompleteChangeTab(jqXHR, textStatus) {
	//alert('ajax call completed.');	
}

function doErrorChangingTab(jqXHR, textStatus, errorThrown) {
	//alert('error! ' + textStatus);
}
function validate_filename(filename){
	//(/^([a-z]+\..+[a-z]+)$/.test("tesst.uoc"))
	if (!(/^([a-z]+\..+[a-z]+)$/.test(filename))){
		  return false;
	}
	return true;
}

