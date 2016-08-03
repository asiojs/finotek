(function($) {
	$.fn.validation = function(opt) {

		/**
		 * Members
		 */
		var ret = true;
		var validList 	= $.extend({}, $.fn.validation.validList);
		var messages 	= $.extend({}, $.fn.validation.messages);
		var alert		= $.extend({}, $.fn.validation.alert);
		var effect 		= $.extend({}, $.fn.validation.effect);
		var guide		= $.extend({}, $.fn.validation.guide);
		var error 		= $.extend({}, $.fn.validation.error);
		var exception 	= $.extend({}, $.fn.validation.exception);
		var options = {
				debug : true,
				alert : true,
				effect : true,
				guideMessage : false,
				notyId : '',						// 알림메세지 ID
				displayTime : 2000			// 알림메세지 보여주는 시간(초)
		}

		if (opt !== undefined) $.extend(options, opt.options);

		/**
		 * private methods
		 */
		run = function(obj) {
			try {
				var tg = $(obj);

				$("[ex-valid], [ex-length]").filter(function() {
					var ch = $(this);
					
					if(ch.attr('ex-display') === undefined)
						ch.attr('ex-display','검사 항목');

					$(this).parents().each(function() {
						if ($(this).is(tg)) {

							// 1. check option framework syntax
							var hasAttr = {
									valid : ch.is("[ex-valid]"),
									range : ch.is("[ex-length]"),
									equalTo : ch.is("[ex-equalTo]"),
									optional : ch.is("[ex-option]")
							}

							if (hasAttr.valid) {

								var valid = ch.attr("ex-valid");
								var optional = ch.attr("ex-option");
								var validValue = getElementValue(ch);

								console.log("optional="+optional);

								// 속성 ex-name 은 입력되는 필드값의 출력명임(사용자편의성 제공)
								if(optional != undefined) {

									// optional logic

									if (validValue.length > 0) {

										if (validList[valid] === undefined)
											throw new exception.valid_confValueNotExist(ch, error.isNotExistMember.format(valid));

										if (!validList[valid](getElementValue(ch))) {
											throw new exception.defaultException(ch, "{0}는(은) {1}".format(ch.attr('ex-display'), messages[valid]));
										}
									}

								}else {

									// valid logic

									if (validValue.length == 0) {
										throw new exception.valueIsRequired(ch, "{0}는(은) {1}".format(ch.attr('ex-display'),error.valueIsEmpty));
									}

									if (validList[valid] === undefined)
										throw new exception.valid_confValueNotExist(ch, error.isNotExistMember.format(valid));

									if (!validList[valid](getElementValue(ch))) {
										throw new exception.defaultException(ch, "{0}는(은) {1}".format(ch.attr('ex-display'), messages[valid]));
									}
								}
							}

							if (hasAttr.range) {

								var range = getRangeValue(ch.attr("ex-length"));
								var min = validList.min(ch, range[0]);
								var max = validList.max(ch, range[1]);
								var msg = '한글 {0}자, 영문/숫자 {1}';	
								
								if (!min) {
									throw new exception.defaultException(ch, "{0}는(은) {1}{2}".format(ch.attr('ex-display'),msg.format(parseInt(range[0]/2), range[0]),messages.rangeless));
								} else if (!max) {
									throw new exception.defaultException(ch, "{0}는(은) {1}{2}".format(ch.attr('ex-display'),msg.format(parseInt(range[1]/2), range[1]),messages.rangeover));
								}
							}

							if (hasAttr.equalTo) {		// 컬럼비교(사용자 환경설정 패스워드관리)

								var filedVal = getEqualToValue(ch.attr('ex-equalTo'));

								if($("input[name="+filedVal[0]+"]").val() != $("input[name="+filedVal[1]+"]").val()) {

									if(filedVal[0] == "current_pass" || filedVal[1] == "current_pass" )	{
										throw new exception.defaultException(ch,error.equalToCurrentPassword);
									}else {
										throw new exception.defaultException(ch,error.equalToRePassword);
									}
								}
							}

						}
					})
				})
			} catch (exception) {
				if (getOptions().debug) console.warn("validator : " + exception.msg);

				alert.info(exception.msg);
				effect.warn(exception.targetObject);
				guide.guideMessage(exception.msg);

				ret = false;
			}
		}

		getElementId = function(obj) {
			return $(obj).attr("id");
		}

		getElementValue = function(obj) {
			if (isEditable(obj)) {
				return $(obj).val();
			} else {
				return $(obj).html();
			}
		}

		getRangeValue = function(rangeVal) {
			if (rangeVal === undefined || rangeVal.trim().length === 0)
				throw new exception.number_confValueNotValid(error.configValueIsEmpty);

			var ret = [];
			var arr = rangeVal.split(",");

			if (arr.length !== 2)
				throw new exception.number_confValueNotValid(error.isNotRangeArray);

			$(arr).each(function() {
				var pVal = parseInt(this.trim());

				if (isNaN(pVal)) {
					throw new exception.number_confValueNotValid(error.isNotNumber);
				}

				ret.push(parseInt(this.trim()));
			})
			return ret;
		}

		isEditable = function(obj) {
			return $(obj).is("input, textarea, select");
		}

		getOptions = function() {
			return options;
		}

		// 알림메세지 초기화
		notyEmpty = function() {
			$("#"+getOptions().notyId).html('');
		}

		//	컬럼비교(사용자 환경설정 패스워드관리)
		getEqualToValue = function(equalTo)	{
			if (equalTo === undefined || equalTo.trim().length === 0)
				throw new exception.equalToValid(error.equalToError);

			var ret = [];
			var arr = equalTo.split(",");

			if (arr.length !== 2)
				throw new exception.equalToValid(error.isNotRangeArray);

			$(arr).each(function() {
				ret.push(this.trim());
			})

			return ret;
		}
		
		getCurrentLength = function(content) {
			var byteCount = 0;
		
			// 한글은 2byte 계산한다.
			for (var k=0;k < content.length; k++)
			{
				onechar = content.charAt(k);
				if (escape(onechar) =='%0D') {} else if (escape(onechar).length > 4) { byteCount += 2; } else { byteCount++; }
			}
			console.log('currentlength : ' + byteCount);
			return byteCount;
		}

		/**
		 * body retural
		 */
		this.each(function() {
			$.fn.validation.clearShadowBox();
			run(this);
		})

		return ret;
	};

	$.fn.validation.validList = {
		email : function(val) {
			return /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(val);
		},
		date : function(val) {
			return !/Invalid|NaN/.test(new Date(val).toString());
		},
		digit : function(val) {
			return /^\d+$/.test(val);
		},
		number : function(val) {
			return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(val);
		},
		min : function(element, val) {
			return getCurrentLength(getElementValue(element)) >= parseInt(val);
		},
		max : function(element, val) {
			return getCurrentLength(getElementValue(element)) <= parseInt(val);
		},
		phone : function(val) {
			return /^\d{2,3}-\d{3,4}-\d{4}$/.test(val);
		},
		require : function(val) {
			return val.length == 0 ? false : true;
		},
		passwd : function(val)	{		// 사용자 패스워드 정책(영문/숫자/특수문자포함)
			return /([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/.test(val);
		},
	};

	$.fn.validation.messages = {
		email 		: "email 형식에 맞지 않습니다.",
		date 		: "날짜 형식에 맞지 않습니다.",
		digit 		: "숫자 형식이 아닙니다",
		number 		: "통화 형이 아닙니다",
		rangeless 	: "자 이상 입력하세요.",
		rangeover 	: "자를 넘을 수 없습니다.",
		phone 		: "전화번호 형식이 아닙니다.",
		require		: "필수값은 비어있을수 없습니다.",
		passwd		: "비밀번호 형식에 맞지 않습니다."
	};
	$.fn.validation.alert = {
		info : function(msg) {
			if (!getOptions().alert) return;

			jAlert(msg,"확인",0);
		},
		confirm : function(msg) {
			if (!getOptions().effect) return;

			jConfirm(msg, "title", function(ret){
				console.info("선택한 값 : " + ret);
			});
		}

	};

	$.fn.validation.effect = {
		info : function(obj) {

		},
		warn : function(obj) {

			if (!getOptions().effect) return;

			$(obj).css("box-shadow", "0px 0px 5px 0px rgba(252,138,138,1)");
			$(obj).css("-moz-box-shadow", "0px 0px 5px 0px rgba(252,138,138,1)");
			$(obj).css("-webkit-box-shadow", "0px 0px 5px 0px rgba(252,138,138,1)");

		},
		error : function(obj) {

		}
	};

	$.fn.validation.guide = {
		guideMessage : function(msg) {

			/**
			 *	알림메세지 출력 후 displayTime 후에 사라지게 처리한다.
			 */
			if (!getOptions().guideMessage && getOptions().notyId.lenth == undefined ) {	return;	}
			else	{
				$("#"+getOptions().notyId).html(msg);
				setTimeout("notyEmpty()",getOptions().displayTime);
			}

		}
	};

	$.fn.validation.error = {
		configValueIsEmpty : "설정 값이 비어있습니다.",
		valueIsEmpty : "필수 항목입니다.",
		isNotNumber : "최소, 최대값 중 숫자가 아닌 값이 있습니다.\n구문이 정상 동작하지 않을 수 있습니다.",
		isNotRangeArray : "최소, 최대값 설정에 오류가 있습니다.\n값은 ','를 기준으로 min max 쌍이 있어야 합니다.",
		isNotExistMember : "'{0}'은 유효성 검사 대상이 아닙니다",
		equalToError : "비교하려는 대상이 없습니다.",
		equalToRePassword : "입력하신 새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다",
		equalToCurrentPassword : "현재 비밀번호와 일치하지 않습니다"
	};

	$.fn.validation.exception = {
		defaultException : function(obj, msg) {
			this.msg = msg;
			this.name = "default exception";
			this.targetObject = obj;
		},
		number_confValueNotValid : function(obj, msg) {
			this.msg = msg;
			this.name = "ex-length configuration value is not valid.";
			this.targetObject = obj;
		},
		valid_confValueNotExist : function(obj, msg) {
			this.msg = msg;
			this.name = "ex-valid configuration value is not exist in valid list";
			this.targetObject = obj;
		},
		valueIsRequired : function(obj, msg) {
			this.msg = msg;
			this.name = "Value is required.";
			this.targetObject = obj;
		},
		equalToValid : function(obj, msg) {
			this.msg = msg;
			this.name = "equalTo configuration value is not valid.";
			this.targetObject = obj;
		}

	};

	$.fn.validation.clearShadowBox = function() {
		// 초기황
		$("[ex-valid], [ex-length]").filter(function() {
			$(this).css("box-shadow", "");
			$(this).css("-moz-box-shadow", "");
			$(this).css("-webkit-box-shadow", "");

		});
	};

})(jQuery);