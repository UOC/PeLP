/**
 * ----------------------------------------------------------------------------------
 * jQuery custom form input plugin
 * Version: 1.0
 * Author: Raúl Fernández, raul@tambaqui.es
 * Copyright (c) 2012 Raúl Fernández
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 * -----------------------------------------------------------------------------------
 * Derived from customfileinput plugin by Scott Jehl, scott@filamentgroup.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 * And customImput plugin by Maggie Wachs and Scott Jehl, http://www.filamentgroup.com
 * Dual licensed under the MIT (filamentgroup.com/examples/mit-license.txt) 
 * and GPL (filamentgroup.com/examples/gpl-license.txt) licenses.
 * -----------------------------------------------------------------------------------
 **/

(function( $ ){
	$.fn.customFormInput = function(options) {

	  	var defaults = {
			browseLbl: 'Browse...',								/* browse button label */
			changeLbl: 'Change...',								/* change button label */
			noneLbl: 'No file selected.'						/* no file selected label */
		};

		var o = $.extend(defaults, options);
		$(this).each(function(i){	
			if($(this).is('[type=file]')){
				var fileInput = $(this)
					.addClass('customfile-input')
					.mouseover(function(){ upload.addClass('customfile-hover'); })
					.mouseout(function(){ upload.removeClass('customfile-hover'); })
					.focus(function(){
						upload.addClass('customfile-focus'); 
						fileInput.data('val', fileInput.val());
					})
					.blur(function(){ 
						upload.removeClass('customfile-focus');
						fileInput.trigger('checkChange');
					})
					.bind('disable',function(){
					 	fileInput.attr('disabled',true);
						upload.addClass('customfile-disabled');
					})
					.bind('enable',function(){
						fileInput.removeAttr('disabled');
						upload.removeClass('customfile-disabled');
					})
					.bind('checkChange', function(){
						if(fileInput.val() && fileInput.val() != fileInput.data('val')){
							fileInput.trigger('change');
						}
					})
					.bind('change',function(){
						var fileName = $(this).val().split(/\\/).pop();
						uploadFeedback.text(fileName).addClass('customfile-feedback-populated');
						uploadButton.text(o.changeLbl);	
					})
					.click(function(){ // for IE and Opera, make sure change fires after choosing a file, using an async callback
						fileInput.data('val', fileInput.val());
						setTimeout(function(){fileInput.trigger('checkChange');},100);
					});
					
				var upload = $('<div class="customfile"></div>');
				var uploadButton = $('<span class="customfile-button" aria-hidden="true">'+o.browseLbl+'</span>').appendTo(upload);
				var uploadFeedback = $('<span class="customfile-feedback" aria-hidden="true">'+o.noneLbl+'</span>').appendTo(upload);

				if(fileInput.is('[disabled]')) fileInput.trigger('disable');
				
				upload
					.mousemove(function(e){fileInput.css({'left': e.pageX - upload.offset().left - fileInput.outerWidth() + 20});})
					.insertAfter(fileInput);
				
				fileInput.appendTo(upload);

			};

			if($(this).is('[type=checkbox],[type=radio]')){
				var input = $(this);
				
				// get the associated label using the input's id
				var label = $('label[for='+input.attr('id')+']');

				// prepend span for input image
				label.prepend('<span class="custom_input"></span>');
				
				//get type, for classname suffix 
				var inputType = (input.is('[type=checkbox]')) ? 'checkbox' : 'radio';
				
				// wrap the input + label in a div 
				$('<div class="custom-'+ inputType +'"></div>').insertBefore(input).append(input, label);
				
				// find all inputs in this set using the shared name attribute
				var allInputs = $('input[name='+input.attr('name')+']');
				
				// necessary for browsers that don't support the :hover pseudo class on labels
				label.hover(
					function(){ 
						$(this).addClass('hover'); 
						if(inputType == 'checkbox' && input.is(':checked')){ 
							$(this).addClass('checkedHover'); 
						} 
					},
					function(){ $(this).removeClass('hover checkedHover'); }
				);
				
				//bind custom event, trigger it, bind click,focus,blur events					
				input.bind('updateState', function(){	
					if (input.is(':checked')) {
						if (input.is(':radio')) {				
							allInputs.each(function(){
								$('label[for='+$(this).attr('id')+']').removeClass('checked');
							});		
						};
						label.addClass('checked');
					}
					else { label.removeClass('checked checkedHover checkedFocus'); }
											
				})
				.trigger('updateState')
				.click(function(){ 
					$(this).trigger('updateState'); 
				})
				.focus(function(){ 
					label.addClass('focus'); 
					if(inputType == 'checkbox' && input.is(':checked')){ 
						//$(this).addClass('checkedFocus'); 
						label.addClass('checkedFocus'); 
					} 
				})
				.blur(function(){ label.removeClass('focus checkedFocus'); });
			};

			return $(this);

		}
	)};

})( jQuery );