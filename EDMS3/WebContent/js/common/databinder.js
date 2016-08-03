function DataBinder(scope) {
	
	var bindFormat = "{0} [data-bind={1}]";
	var bindFormatAll = "{0} [data-bind]";
	var bindDatas = {};
	var scope = scope == undefined ? "" : scope;
	var format = {};
	var databinder = {
			
			/**
			 * 바인딩 Item 추가
			 */
			set : function (attr_name, val) {
				
				// # format이 지정되 있을 경우 리터럴을 변형 시킨다.
//				if (format != undefined && format[attr_name] !== undefined) {
//					val = format[attr_name](val);
//				}
				
				// # set value
				var $dom = $(bindFormat.format(scope, attr_name));
				
				if ($dom.attr("type") === "checkbox"){
					$dom.prop("checked", val == "T");
					val = val != "T" ? "F" : val; 
				} else if ($dom.is("input, textarea, select")) {
					$dom.val(val);
					
					// <select>의 경우 ddslick 사용하면 div로 전환 됨
					if($dom.is('input') && $dom.attr('data-select') == 'true'){
						var valText;
						
						$dom.parent().parent().children('ul').find('li > a').removeClass('dd-option-selected');
						$dom.parent().parent().children('ul').find('li > a').each(function(){
							if($(this).find('input').val() == val){
								$(this).addClass('dd-option-selected');
								valText = $(this).find('label').text();
								$dom.parent().children('a').children('label').text(valText);
							}
						});
					}
				} else {
					$dom.html(val);
				}
				
				// set variable
				bindDatas[attr_name] = val;
				
				
				// # set event
				
//				$dom.unbind("keyup").bind("keyup", this.changeInput);
				$dom.unbind("change").bind("change", this.changeInput);
			},
			/**
			 * 바인딩 Item 해제
			 */
			unset : function (attr_name) {
				$(bindFormat.format(scope, attr_name)).unbind("change");
				delete bindDatas[attr_name];
			},

			/**
			 * Two way binding
			 * - jsonObject based
			 * - isFullStack 
			 *   - true : DOM과 매치되지 않는 jsonObject까지 바인딩한다
			 *   - false : DOM과 매치되는 jsonObject만 바인딩한다 
			 */
			binding : function (jsonObject, isFullStack) {
				
				var doms = $(bindFormatAll.format(scope));
				
				for (var i = 0; i < doms.length; i++) {
					var dom = $(doms[i]);
					var attrName = dom.data("bind");
					var attrValue = jsonObject[attrName];
					
					// Element에는 바인딩이 걸려 있으나, jsonObject에는 값이 없을경우 DOM의 값을 유지한다.
					if (attrValue === undefined || attrValue === "") {
						attrValue = dom.is("input, textarea, select") ? dom.val() : dom.html();
					}
					
					this.set(attrName, attrValue, scope);
					delete jsonObject[attrName];
				}
				
				if (isFullStack) {
					var keys = Object.keys(jsonObject);
					for (var i = 0; i < keys.length; i++) {
						var attrName = keys[i];
						var attrValue = jsonObject[keys[i]];
						this.set(attrName, attrValue, scope);
					}
				}
			},
			/**
			 * Two way binding
			 * - element based
			 */
			bindingElement : function () {
				var bd = this;
				var doms = $(bindFormatAll.format(scope));
				
				$(doms).each(function() {
					var attrName = $(this).data("bind");
					var attrValue = $(this).is("input, textarea, select") ? $(this).val() : $(this).html();
					bd.set(attrName, attrValue, scope);
					
				})
				
			},
			
			get : function (attr_name) {
				
				// # format이 지정되 있을 경우 리터럴을 변형 시킨다.
//				if (format != undefined && format[attr_name] !== undefined) {
//					return format[attr_name](bindDatas[attr_name]);
//				}
				
				return bindDatas[attr_name];
			},
			
			
			getDataToJson : function () {
				return bindDatas;
			},
			
			/**
			 * server 호출 시 jsonArray가 필요시 추가함
			 * server에서는 반드시 JSONArray jsonArray = JSONArray.fromObject(arrayName);
			 * 으로 변환 후 사용
			 */
			setJsonArray : function(arrayName, arrayObject) {
				bindDatas[arrayName] = JSON.stringify(arrayObject);
			},			
			
			// Event listener
			changeInput : function (evt) {
				
				var attr_name = $(this).data("bind");
				var val;
				
				if ($(this).attr("type") === "checkbox") {
					val = $(this).is(":checked") ? "T" : "F"; 
				} else if ( $(this).is("input, textarea, select") ) {
					val = $(this).val();
				} else {
					val = $(this).html();
				}
				
				bindDatas[attr_name] = val;
			}
			
	}
	
	return databinder;

}
