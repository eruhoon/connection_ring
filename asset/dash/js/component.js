/*******************************************************************************
	COMPONENT FACTORY
*******************************************************************************/

var Component = {

	getNewComponent: function(){
		var newComponent = {
			/*************************************************************************
				COMPONENT
			*************************************************************************/
			target: $(document.createElement("div")),
			naviTarget: $(document.createElement("div")),
			textarea: null,
			agenda: null,
			yearSection: null,
			dateSection: null,
			monthSection: null,

			/*************************************************************************
				VARIABLE
			*************************************************************************/
			cid: null,
			cuid: null,
			type: null,
			title: "notitle",
			memo: "",
			x: 0,
			y: 0,
			w: 100,
			h: 100,
			date: null,
			visible: true,

			/*************************************************************************
				STATE
			*************************************************************************/
			currentYear: new Date().getFullYear(),
			currentMonth: new Date().getMonth()+1,


			// MOVE EVENT //////////////////////////////////////////////////////
			isHold: false,
			clickStart: null,
			isMove: false,
			offsetX: 0,
			offsetY: 0,
		};

		newComponent.target.addClass("component");
		newComponent.naviTarget.addClass("navicomponent");
		Component.registerMouseEvent(newComponent);

		return newComponent;
	},

	setX: function(componentObj, x){
		var maxX = Config.dashWidth - componentObj.w;
		if(x < 0) x = 0;
		if(x > maxX) x = maxX;
		componentObj.x = x;
	},

	setY: function(componentObj, y){
		var maxY = Config.dashHeight - componentObj.h;
		if(y < 0) y = 0;
		if(y > maxY) y = maxY;
		componentObj.y = y;
	},


	/*****************************************************************************
		UPDATE COMPONENT @ socket.onUpdateComponent
	*****************************************************************************/
	update: function(componentObj){
		for(var idx = 0; idx < Dash.componentList.length; idx++){
			if(Dash.componentList[idx].cid == componentObj.cid){
				var c = Dash.componentList[idx];

				$(c.target).animate({
					left: componentObj.x,
					top: componentObj.y
				}, 500, function() {
					c.x = componentObj.x;
					c.y = componentObj.y;
					c.title = componentObj.sub;
					c.memo = componentObj.content;
					c.date = componentObj.date;
					Component.relocate(c);
					Component.refreshContents(c);
				});
				break;
			}
		}
	},


	/*****************************************************************************
		DELETE COMPONENT @ socket.onDeleteComponent
	*****************************************************************************/
	delComponent: function(componentObj){
		for(var idx = 0; idx < Dash.componentList.length; idx++){
			if(Dash.componentList[idx].cid == componentObj.cid){
				Dash.componentList[idx].target.fadeOut('2000', function(e){
					Dash.componentList[idx].target.remove();
					Dash.componentList[idx].naviTarget.remove();
					var c = Dash.componentList.splice(idx, 1);
					c = null;
				});
				break;
			}
		}
	},

	relocate : function(componentObj){
		componentObj.target.css("left", componentObj.x);
		componentObj.target.css("top", componentObj.y);
		componentObj.naviTarget.css("left", componentObj.x * Navi.widthRatio());
		componentObj.naviTarget.css("top", componentObj.y * Navi.heightRatio());
	},

	refreshContents: function(componentObj){
		if(componentObj.textarea) componentObj.textarea.val(componentObj.memo);
		if(componentObj.agenda){
			if(componentObj.date!="0000-00-00 00:00:00" && componentObj.date!="NaN-NaN-NaN NaN:NaN:NaN"){
				componentObj.agenda.text(componentObj.date.substring(0, 10));
			}
		}
	},


	updateCoordinate: function(componentObj){
		var did = Dash.id;
		var cid = componentObj.cid;
		var title = componentObj.title;
		var content = componentObj.memo;
		var x = componentObj.x;
		var y = componentObj.y;
		var date = componentObj.date;

		Socket.callComponentUpdate(did, cid, title, content, x, y, date, 'move');
	},


	updateContents: function(componentObj, _isDate){
		var did = Dash.id;
		var cid = componentObj.cid;
		var title = componentObj.title;
		var content = componentObj.memo;
		var x = componentObj.x;
		var y = componentObj.y;
		var date = componentObj.date;
		console.log('확인:', _isDate);
		if(_isDate) Socket.callComponentUpdate(did, cid, title, content, x, y, date, 'date');
		Socket.callComponentUpdate(did, cid, title, content, x, y, date, 'content');
	},

	registerMouseEvent: function(componentObj){

		/***********************************************************************
			CONTEXT BUTTON
		***********************************************************************/
		$('#context_deleteComponent').on("click", function(e){
			Dock.componentContextmenu.fadeOut(300);
			var cid = Dash.selectedComponent.cid;
			console.log(Dash.id, cid);
			Socket.callComponentDelete(Dash.id, cid);
		});


		/***************************************************************************
			RIGHT BUTTON CLICK
		***************************************************************************/
		componentObj.target.off("contextmenu").on("contextmenu", function(e){
			e.preventDefault();
			Dock.componentContextmenu.fadeIn(300);
			Dock.componentContextmenu.css('left', e.clientX);
			Dock.componentContextmenu.css('top', e.clientY);

			$('#contextComponentname').text(componentObj.dname);

			Dash.selectedComponent = componentObj;
			return false;
		});

		/***************************************************************************
			MOUSEDOWN
		***************************************************************************/
		componentObj.target.off("mousedown").on("mousedown", function(e){
			// 1. Event Target Check
			Config.clearSelection();
			if(Dash.selectedComponent!=null) Dash.selectedComponent.target.removeClass('selected');
			Dash.selectedComponent = componentObj;
			Dash.selectedComponent.target.addClass('selected');
			if ($(e.target).prop('tagName')!='DIV') return;
			if (componentObj.isHold) return;

			// 2. Change State : Move
			componentObj.isMove = true;
			componentObj.clickStart = new Date();
			componentObj.target.addClass("componentMoving");
			var guideBox = $(document.createElement('div'))
				.on('mousemove', function(e){
					Config.clearSelection();
				})
				.addClass('guidebox')
				.css({
					'width': Config.dashWidth,
					'height': Config.dashHeight,
					'top': -Config.dashWidth/2,
					'left': -Config.dashHeight/2
				}).appendTo(componentObj.target);

			
			// 3. Save Mouse State
			componentObj.offsetX = e.clientX;
			componentObj.offsetY = e.clientY;
		});


		/***************************************************************************
			MOUSEMOVE
		***************************************************************************/
		componentObj.target.off("mousemove").on("mousemove", function(e){
			// 1. Event Target Check
			if ($(e.target).prop('tagName')!='DIV') return;
			if(!componentObj.isMove) return;
			var interval = new Date() - componentObj.clickStart;
			if(interval<150) return;
			e.preventDefault();
			Config.clearSelection();

			// 2. Apply Present-Position
			Component.setX(componentObj, componentObj.x + (e.clientX - componentObj.offsetX));
			Component.setY(componentObj, componentObj.y + (e.clientY - componentObj.offsetY));
			
			// 3. Save Mouse State
			componentObj.offsetX = e.clientX;
			componentObj.offsetY = e.clientY;

			// 4. Refresh
			Component.relocate(componentObj);
		});


		/**************************************************************************
			MOUSEUP
		**************************************************************************/
		componentObj.target.off("mouseup").on("mouseup", function(e){
			// 1. Event Target Check
			if(!componentObj.isMove) return;

			// 2. Change State : Not Move
			componentObj.isMove = false;
			componentObj.target.removeClass("componentMoving");
			$('div.guidebox').remove();

			// 3. Refresh
			Component.updateCoordinate(componentObj);
			Component.relocate(componentObj);

		});
	},


	applyCustom: function(componentObj, cuid){
		componentObj.cuid = cuid;
		componentObj.target.css('left', parseInt(componentObj.x));
		componentObj.target.css('top', parseInt(componentObj.y));

		$.ajax({
			type: 'POST',
			url: 'http://210.118.74.153/ring/index.php/custom/xml/'+cuid,
			dataType: 'xml',
			success: function(data, status, req){
				

				/***********************************************************************
					COMPONENT
				***********************************************************************/
				var rootXML = $(data).find('component');
				if(!rootXML) return;
				componentObj.w = rootXML.attr('width');
				componentObj.h = rootXML.attr('height');
				componentObj.target.css('width', parseInt(componentObj.w));
				componentObj.target.css('height', parseInt(componentObj.h));
				

				/***********************************************************************
					LAYER
				***********************************************************************/
				$(rootXML).find('layer').each(function(idx, layerEntity){
					if(!$(layerEntity).parent().is(rootXML)) return;
					var newLayer = $(document.createElement('div')).addClass('custom_layer');

					/*********************************************************************
						LAYER > BACKGROUND
					*********************************************************************/
					var bgFlag = true;
					$(layerEntity).find('background').each(function(idx, bgEntity){
						if(!$(bgEntity).parent().is(layerEntity) || bgFlag != true) return;
						if($(bgEntity).attr('image')){
							console.log($(bgEntity).attr('image').replace(' ', '%20'));

							newLayer.css("background-image", "url("+$(bgEntity).attr('image').replace(' ', '%20')+")");
						}
						else if($(bgEntity).attr('color')){
							newLayer.css("background-color", $(bgEntity).attr('color'));
						}
						bgFlag = false;
					});

					/*********************************************************************
						LAYER > POSITION
					*********************************************************************/
					var posFlag = true;
					$(layerEntity).find('position').each(function(idx, posEntity){
						if(!$(posEntity).parent().is(layerEntity) || posFlag != true) return;
						var left = $(posEntity).attr('x') | 0;
						var top = $(posEntity).attr('y') | 0;
						var width = $(posEntity).attr('width') | 0;
						var height = $(posEntity).attr('height') | 0;
						if(!(width && height)) { newLayer.remove(); return; }
						newLayer.css('left', left);
						newLayer.css('top', top);
						newLayer.css('width', width);
						newLayer.css('height', height);
						newLayer.css("background-size", width+"px "+height+"px");
						posFlag = false;
					});

					// APPENDING //
					componentObj.target.append(newLayer);
				});

				/***********************************************************************
					LABEL
				***********************************************************************/
				$(rootXML).find('label').each(function(idx, labelEntity){
					if(!$(labelEntity).parent().is(rootXML)) return;
					var newLabel = $(document.createElement('div')).addClass('custom_layer');
					newLabel.html(decodeURI($(labelEntity).attr('text')));
					newLabel.css({
						'color': $(labelEntity).attr('color')
					})


					/*********************************************************************
						LABEL > POSITION
					*********************************************************************/
					var posFlag = true;
					$(labelEntity).find('position').each(function(idx, posEntity){
						if(!$(posEntity).parent().is(labelEntity) || posFlag != true) return;
						var left = $(posEntity).attr('x') | 0;
						var top = $(posEntity).attr('y') | 0;
						var width = $(posEntity).attr('width') | 0;
						var height = $(posEntity).attr('height') | 0;
						//if(!(width && height)) { newLabel.remove(); return; }
						newLabel.css('left', left);
						newLabel.css('top', top);
						//newLabel.css('width', width);
						//newLabel.css('height', height);
						newLabel.css("background-size", width+"px "+height+"px");
						posFlag = false;
					});

					// APPENDING //
					componentObj.target.append(newLabel);

				});

				/***********************************************************************
					MEMO
				***********************************************************************/
				var memoFlag = true;
				$(rootXML).find('memo').each(function(idx, memoEntity){
					if(!$(memoEntity).parent().is(rootXML)) return;
					var newMemo = $(document.createElement('div')).addClass('custom_layer');


					/*********************************************************************
						MEMO > POSITION
					*********************************************************************/
					var posFlag = true;
					$(memoEntity).find('position').each(function(idx, posEntity){
						if(!$(posEntity).parent().is(memoEntity) || posFlag != true) return;
						var left = $(posEntity).attr('x') | 0;
						var top = $(posEntity).attr('y') | 0;
						var width = $(posEntity).attr('width') | 0;
						var height = $(posEntity).attr('height') | 0;
						if(!(width && height)) { newMemo.remove(); return; }
						newMemo.css('left', left);
						newMemo.css('top', top);
						newMemo.css('width', width);
						newMemo.css('height', height);
						posFlag = false;
					});
					

					/*********************************************************************
						MEMO > TEXTAREA
					*********************************************************************/
					var textFlag = true;
					$(memoEntity).find('textarea').each(function(idx, textEntity){
						if(!$(textEntity).parent().is(memoEntity) || textFlag != true) return;
						var newTextarea = $(document.createElement('textarea')).addClass('custom_layer');
						componentObj.textarea = newTextarea;
						newTextarea.css({
							'border': 0,
							'background-color': 'transparent',
							'resize': 'none',
						});
						newTextarea.val(componentObj.memo);

						newTextarea.off('blur').on('blur', function(e){
							componentObj.memo = newTextarea.val();
							Socket.callComponentUpdate(Dash.id, componentObj.cid, componentObj.title, componentObj.memo, componentObj.x, componentObj.y, componentObj.date, 'content');
						});

						/*******************************************************************
							MEMO > TEXTAREA @ fontsize
						*******************************************************************/
						if($(textEntity).attr('fontsize')){
							newTextarea.css('font-size', $(textEntity).attr('fontsize')+"px");
						}

						/*******************************************************************
							MEMO > TEXTAREA > POSITION
						*******************************************************************/
						var posFlag = true;
						$(textEntity).find('position').each(function(idx, posEntity){
							if(!$(posEntity).parent().is(textEntity) || posFlag != true) return;
							var left = $(posEntity).attr('x') | 0;
							var top = $(posEntity).attr('y') | 0;
							var width = $(posEntity).attr('width') | 0;
							var height = $(posEntity).attr('height') | 0;
							if(!(width && height)) { newTextarea.remove(); return; }
							newTextarea.css('left', left);
							newTextarea.css('top', top);
							newTextarea.css('width', width);
							newTextarea.css('height', height);
							newTextarea.attr('cols', width);
							posFlag = false;
						});
						
						// APPENDING //
						newMemo.append(newTextarea);
						textFlag = false;
					});
					
					/*********************************************************************
						MEMO > AGENDA
					*********************************************************************/
					var agendaFlag = true;
					$(memoEntity).find('agenda').each(function(idx, agendaEntity){
						if(!$(agendaEntity).parent().is(memoEntity) || agendaFlag != true) return;
						var newAgenda = $(document.createElement('div')).addClass('custom_layer');
						componentObj.agenda = newAgenda;
						if(componentObj.date!="0000-00-00 00:00:00" && componentObj.date!="NaN-NaN-NaN NaN:NaN:NaN"){
							agenda = Config.getDatetime(new Date(componentObj.date)).substring(0,10);
							newAgenda.text(agenda);
						}
						newAgenda.data('datepicker', componentObj.cid);
						
						if($(agendaEntity).attr('fontsize')){
							newAgenda.css('font-size', $(agendaEntity).attr('fontsize')+"px");
						}
						
						/*******************************************************************
							MEMO > AGENDA > POSITION
						*******************************************************************/
						var posFlag = true;
						$(agendaEntity).find('position').each(function(idx, posEntity){
							if(!$(posEntity).parent().is(agendaEntity) || posFlag != true) return;
							var left = $(posEntity).attr('x') | 0;
							var top = $(posEntity).attr('y') | 0;
							var width = $(posEntity).attr('width') | 0;
							var height = $(posEntity).attr('height') | 0;
							if(!(width && height)) { newAgenda.remove(); return; }
							newAgenda.css('left', left);
							newAgenda.css('top', top);
							newAgenda.css('width', width);
							newAgenda.css('height', height);
							posFlag = false;
						});

						// APPENDING //
						newMemo.append(newAgenda);
						agendaFlag = false;
					});

					/*********************************************************************
						MEMO > BUTTON_MEMO
					*********************************************************************/
					$(memoEntity).find('button_memo').each(function(idx, btnEntity){
						if(!$(btnEntity).parent().is(memoEntity)) return;
						var newButton = $(document.createElement('button'))
							.addClass('custom_layer')
							.css({
								padding: 0,
								border: 'none',
								background: 'none'
							});
						if($(btnEntity).attr('value')){
							newButton.text($(btnEntity).attr('value'));
						}
						var command = $(btnEntity).attr('command');
						var link = null;
						if(command){
							switch(command){
								case 'delete':
									newButton.on('click', function(e){
										Socket.callComponentDelete(Dash.id, componentObj.cid);
										return false;
									});
									break;
								case 'hold':
									newButton.on('click', function(e){
										componentObj.isHold = (componentObj.isHold)? false:true;
										if(componentObj.isHold){
											newButton.css('background-image', "url("+$(btnEntity).attr('src_onmousedown')+")");
										}else{
											newButton.css('background-image', "url("+$(btnEntity).attr('src_normal')+")");
										}
									});
									break;
								case 'addcalendar':
									newButton.on('click', function(e){
										var datepicker = $(document.createElement('input'))
											.appendTo(newButton)
											.addClass('custom_layer')
											.css({
												'width': 0,
												'height': 0,
												'left': 0,
												'top': 0
											})
											.datepicker('show')
											.on('changeDate', function(e){
												componentObj.date = new Date($(this).val());
												Component.updateContents(componentObj, true);
												$(this).datepicker('hide');
											});
									});
									break;
								case 'link':
									link = $(btnEntity).attr('link');
									if(!link) link = "http://210.118.74.153/ring";
									newButton.on('click', function(e){
										window.open(link,'','resizable=1,scrollbars=0,location=no,status=no,toolbar=no');
										//width=1280,height=720
									});
									break;
								default:
									newButton.on('click', function(e){
										alert('잘못된 명령입니다.');
									});
									break;
							}
						}

						/*******************************************************************
							MEMO > BUTTON_MEMO > POSITION
						*******************************************************************/
						var posFlag = true;
						$(btnEntity).find('position').each(function(idx, posEntity){
							if(!$(posEntity).parent().is(btnEntity) || posFlag != true) return;
							var left = $(posEntity).attr('x') | 0;
							var top = $(posEntity).attr('y') | 0;
							var width = $(posEntity).attr('width') | 0;
							var height = $(posEntity).attr('height') | 0;
							if(!(width && height)) { newButton.remove(); return; }
							newButton.css('left', left);
							newButton.css('top', top);
							newButton.css('width', width);
							newButton.css('height', height);
							newButton.css("background-size", width+"px "+height+"px");
							posFlag = false;
						});

						/*******************************************************************
							MEMO > BUTTON_MEMO @ src_normal
						*******************************************************************/
						if($(btnEntity).attr('src_normal')){
							newButton.css('background-image', "url("+$(btnEntity).attr('src_normal')+")");
							newButton.on('mouseout', function(e){
								if(command=='hold') return;
								newButton.css('background-image', "url("+$(btnEntity).attr('src_normal')+")");
							});
						}

						/*******************************************************************
							MEMO > BUTTON_MEMO @ src_onmouseover
						*******************************************************************/
						if($(btnEntity).attr('src_onmouseover')){
							newButton.on('mouseover', function(e){
								if(command=='hold') return;
								newButton.css('background-image', "url("+$(btnEntity).attr('src_onmouseover')+")")
							});
						}

						/*******************************************************************
							MEMO > BUTTON_MEMO @ src_onmousedown
						*******************************************************************/
						if($(btnEntity).attr('src_onmousedown')){
							newButton.on('mousedown', function(e){
								newButton.css('background-image', "url("+$(btnEntity).attr('src_onmousedown')+")")
							});	
						}

						newMemo.append(newButton);
					});

					componentObj.target.append(newMemo);
					memoFlag = false;
				});
				
				/***********************************************************************
					CALENDAR
				***********************************************************************/
				var calFlag = true;
				$(rootXML).find('calendar').each(function(idx, calEntity){
					if(!$(calEntity).parent().is(rootXML)) return;
					var newCalendar = $(document.createElement('div')).addClass('custom_layer');

					/*********************************************************************
						CALENDAR > POSITION
					*********************************************************************/
					var posFlag = true;
					$(calEntity).find('position').each(function(idx, posEntity){
						if(!$(posEntity).parent().is(calEntity) || posFlag != true) return;
						var left = $(posEntity).attr('x') | 0;
						var top = $(posEntity).attr('y') | 0;
						var width = $(posEntity).attr('width') | 0;
						var height = $(posEntity).attr('height') | 0;
						if(!(width && height)) { newCalendar.remove(); return; }
						newCalendar.css('left', left);
						newCalendar.css('top', top);
						newCalendar.css('width', width);
						newCalendar.css('height', height);
						posFlag = false;
					});
					
					/*********************************************************************
						CALENDAR > YEAR
					*********************************************************************/
					var yearFlag = true;
					$(calEntity).find('year').each(function(idx, yearEntity){
						if(!$(yearEntity).parent().is(calEntity) || yearFlag!=true) return;
						var src = $(yearEntity).attr('src');
						var offset = parseInt($(yearEntity).attr('offset')) | 0;
						if(!src || !offset) return;

						var newYear = $(document.createElement('div')).addClass('custom_layer');
						componentObj.yearSection = newYear;

						/*******************************************************************
							CALENDAR > YEAR > POSITION
						*******************************************************************/
						var posFlag = true;
						$(yearEntity).find('position').each(function(idx, posEntity){
							if(!$(posEntity).parent().is(yearEntity) || posFlag != true) return;
							var left = $(posEntity).attr('x') | 0;
							var top = $(posEntity).attr('y') | 0;
							var width = parseInt($(posEntity).attr('width')) | 0;
							var height = $(posEntity).attr('height') | 0;
							if(!(width && height)) { newYear.remove(); return; }
							newYear.css('left', left);
							newYear.css('top', top);
							newYear.css('width', width);
							newYear.css('height', height);
							
							var yValue = ""+(componentObj.currentYear);
							var offset = width/yValue.length;
							for(var idx=0; idx<yValue.length; idx++){
								var newY = $(document.createElement('div')).addClass('custom_layer');
								newY.css('top', 0);
								newY.css('left', offset*idx);
								newY.css('width', offset);
								newY.css('height', '100%');
								newY.css('background-image', "url("+src+")");
								newY.css('background-size', offset*10);
								newY.css('background-repeat', 'no-repeat');
								newY.css('background-position', -(offset*parseInt(yValue[idx]))+"px 0px");
								newYear.append(newY);
							}

							posFlag = false;
						});

						newCalendar.append(newYear);
						yearFlag = false;
					});

					/*********************************************************************
						CALENDAR > MONTH
					*********************************************************************/
					var monthFlag = true;
					$(calEntity).find('month').each(function(idx, monthEntity){
						if(!$(monthEntity).parent().is(calEntity) || monthFlag!=true) return;
						var src = $(monthEntity).attr('src');
						var offset = parseInt($(monthEntity).attr('offset')) | 0;
						if(!src || !offset) return;

						var newMonth = $(document.createElement('div')).addClass('custom_layer');
						componentObj.monthSection = newMonth;

						/*******************************************************************
							CALENDAR > MONTH > POSITION
						*******************************************************************/
						var posFlag = true;
						$(monthEntity).find('position').each(function(idx, posEntity){
							if(!$(posEntity).parent().is(monthEntity) || posFlag != true) return;
							var left = $(posEntity).attr('x') | 0;
							var top = $(posEntity).attr('y') | 0;
							var width = parseInt($(posEntity).attr('width')) | 0;
							var height = $(posEntity).attr('height') | 0;
							if(!(width && height)) { newMonth.remove(); return; }
							newMonth.css('left', left);
							newMonth.css('top', top);
							newMonth.css('width', width);
							newMonth.css('height', height);
							
							var mValue = ""+componentObj.currentMonth;
							var offset = width/2;
							for(var idx=0; idx<mValue.length; idx++){
								var newM = $(document.createElement('div')).addClass('custom_layer');
								newM.css('top', 0);
								newM.css('left', (mValue.length==1)?offset*0.5:offset*idx);
								newM.css('width', offset);
								newM.css('height', '100%');
								newM.css('background-image', "url("+src+")");
								newM.css('background-size', offset*10);
								newM.css('background-repeat', 'no-repeat');
								newM.css('background-position', -(offset*parseInt(mValue[idx]))+"px 0px");
								newMonth.append(newM);
							}

							posFlag = false;
						});

						newCalendar.append(newMonth);
						monthFlag = false;
					});

					// CALENDAR > DATE //
					var dateFlag = true;
					$(calEntity).find('date').each(function(idx, dateEntity){
						if(!$(dateEntity).parent().is(calEntity) || dateFlag!=true) return;
						var column = parseInt($(dateEntity).attr('column'));
						var offsetX = parseInt($(dateEntity).attr('offset_x'));
						var offsetY = parseInt($(dateEntity).attr('offset_y'));
						var src = $(dateEntity).attr('src');
						var schedulesrc = $(dateEntity).attr('schedulesrc');
						var saturdaysrc = $(dateEntity).attr('saturdaysrc');
						var sundaysrc = $(dateEntity).attr('sundaysrc');
						if(!src) return;

						var newDate = $(document.createElement('div')).addClass('custom_layer');
						componentObj.dateSection = newDate;

						// CALENDAR > DATE > POSITION //
						var posFlag = true;
						$(dateEntity).find('position').each(function(idx, posEntity){
							if(!$(posEntity).parent().is(dateEntity) || posFlag != true) return;
							var left = parseInt($(posEntity).attr('x')) | 0;
							var top = parseInt($(posEntity).attr('y')) | 0;
							var width = parseInt($(posEntity).attr('width')) | 0;
							var height = parseInt($(posEntity).attr('height')) | 0;
							if(!(width && height)) { newDate.remove(); return; }
							newDate.css('left', left);
							newDate.css('top', top);
							newDate.css('width', width);
							newDate.css('height', height);

							var dObj = new Date(
								componentObj.currentYear, 
								componentObj.currentMonth-1,
								1, 0, 0, 0);
							var mValue = parseInt(dObj.getMonth()+1);
							var dValue = parseInt(dObj.getDate());
							
							var isStandard = (!column)?true:false;
							var col = (isStandard)?parseInt(dObj.getDay()) : 0;
							var row = 0;
							if(isStandard) { column=7; }
							var cellColumn = width/column;
							var cellRow = 0.8*cellColumn/offsetX*offsetY;
							var offset = cellColumn*(2/5);
							var margin = cellColumn*(1/5);
							console.log(col, row, width, column, cellColumn);
							for(var idx=1; mValue==dObj.getMonth()+1; idx++){
								
								if(parseInt(idx/10)!=0){
									var dText1 = ""+parseInt(idx/10);
									var newD1 = $(document.createElement('div')).addClass('custom_layer');
									newD1.css('left', parseInt(col*cellColumn));
									newD1.css('top', parseInt(row*cellRow));
									newD1.css('width', parseInt(offset));
									newD1.css('height', parseInt(cellRow));
									if(dObj.getDay()==6){
										newD1.css('background-image', 'url('+saturdaysrc+')');
									}else if(dObj.getDay()==0){
										newD1.css('background-image', 'url('+sundaysrc+')');
									}else{
										newD1.css('background-image', 'url('+src+')');
									}
									newD1.css('background-size', offset*10);
									newD1.css('background-repeat', 'no-repeat');
									newD1.css('background-position', -(offset*parseInt(idx/10))+"px 0px");
									newDate.append(newD1);
								}
								var dText2 = ""+parseInt(idx%10);
								var newD2 = $(document.createElement('div')).addClass('custom_layer');
								newD2.css('left', parseInt(col*cellColumn)+offset);
								newD2.css('top', parseInt(row*cellRow));
								newD2.css('width', parseInt(offset));
								newD2.css('height', parseInt(cellRow));
								if(dObj.getDay()==6){
									newD2.css('background-image', 'url('+saturdaysrc+')');
								}else if(dObj.getDay()==0){
									newD2.css('background-image', 'url('+sundaysrc+')');
								}else{
									newD2.css('background-image', 'url('+src+')');
								}
								newD2.css('background-size', offset*10);
								newD2.css('background-repeat', 'no-repeat');
								newD2.css('background-position', -(offset*parseInt(idx%10))+"px 0px");
								newDate.append(newD2);
								

								col = (col+1)%column;
								if(col==0) row++;
								dObj.setDate(idx+1);
							}

							posFlag = false;
						});

						newCalendar.append(newDate);
						dateFlag = false;
					});

					// MEMO > BUTTON_CAL //
					$(calEntity).find('button_cal').each(function(idx, btnEntity){
						if(!$(btnEntity).parent().is(calEntity)) return;
						var newButton = $(document.createElement('button'))
							.addClass('custom_layer')
							.css({
								padding: 0,
								border: 'none',
								background: 'none'
							});
						if($(btnEntity).attr('value')){
							newButton.text($(btnEntity).attr('value'));
						}
						var command = $(btnEntity).attr('command');
						var link = null;
						if(command){
							switch(command){
								case 'nextmonth':
									newButton.on('click', function(e){
										componentObj.target.empty();
										if(componentObj.currentMonth==12) componentObj.currentYear++;
										componentObj.currentMonth=(componentObj.currentMonth%12)+1;
										console.log(componentObj);
										Component.applyCustom(componentObj, componentObj.cuid);
										return false;
									});
									break;
								case 'prevmonth':
									newButton.on('click', function(e){
										componentObj.target.empty();
										if(componentObj.currentMonth==1) componentObj.currentYear--;
										componentObj.currentMonth=(componentObj.currentMonth+10)%12+1;
										console.log(componentObj);
										Component.applyCustom(componentObj, componentObj.cuid);
										return false;
									});
									break;
								default:
									break;
							}
						}

						// MEMO > BUTTON_MEMO > POSITION //
						var posFlag = true;
						$(btnEntity).find('position').each(function(idx, posEntity){
							if(!$(posEntity).parent().is(btnEntity) || posFlag != true) return;
							var left = $(posEntity).attr('x') | 0;
							var top = $(posEntity).attr('y') | 0;
							var width = $(posEntity).attr('width') | 0;
							var height = $(posEntity).attr('height') | 0;
							if(!(width && height)) { newButton.remove(); return; }
							newButton.css('left', left);
							newButton.css('top', top);
							newButton.css('width', width);
							newButton.css('height', height);
							newButton.css("background-size", width+"px "+height+"px");
							posFlag = false;
						});

						if($(btnEntity).attr('src_normal')){
							newButton.css('background-image', "url("+$(btnEntity).attr('src_normal')+")");
							newButton.on('mouseout', function(e){
								newButton.css('background-image', "url("+$(btnEntity).attr('src_normal')+")");
							});
						}
						if($(btnEntity).attr('src_onmouseover')){
							newButton.on('mouseover', function(e){
								newButton.css('background-image', "url("+$(btnEntity).attr('src_onmouseover')+")")
							});
						}
						if($(btnEntity).attr('src_onmousedown')){
							newButton.on('mousedown', function(e){
								newButton.css('background-image', "url("+$(btnEntity).attr('src_onmousedown')+")")
							});	
						}

						newCalendar.append(newButton);
					});
					
					componentObj.target.append(newCalendar);
					calFlag = false;
				});

				// MINI MAP COMPONENT //
				Component.refreshComponent(componentObj);
				Component.refreshNaviComponent(componentObj);
			}
		});
	},


	/***************************************************************************
		REFRESH COMPONENT FUNCTION
	***************************************************************************/
	refreshComponent: function(componentObj){
		// Main Component
		componentObj.target.width(componentObj.w);
		componentObj.target.height(componentObj.h);
		componentObj.target.css("visibility", componentObj.visible);
		componentObj.target.css("left", componentObj.x);
		componentObj.target.css("top", componentObj.y);
	},


	refreshNaviComponent: function(componentObj){
		componentObj.naviTarget.width(componentObj.w * Navi.widthRatio());
		componentObj.naviTarget.height(componentObj.h * Navi.heightRatio());
		componentObj.naviTarget.css("visibility", componentObj.visible);
		componentObj.naviTarget.css("left", componentObj.x * Navi.widthRatio());
		componentObj.naviTarget.css("top", componentObj.y * Navi.heightRatio());
		var r = Math.floor(Math.random()*255);
		var g = Math.floor(Math.random()*255);
		var b = Math.floor(Math.random()*255);
		componentObj.naviTarget.css("background-color", "rgb("+r+", "+g+", "+b+")");
	},
}