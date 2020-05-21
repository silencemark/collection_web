 var Nav = function () {
	
	return { init: init };
	
	function init () {
		$('.p-business .nav-business').addClass('active');	
			$('.p-business-brand .nav-business-brand').addClass('active');
			$('.p-business-servicestation .nav-business-servicestation').addClass('active');
			$('.p-business-topic .nav-business-topic').addClass('active');
			$('.p-business-goods .nav-business-goods').addClass('active');
			$('.p-business-service .nav-business-service').addClass('active');

		$('.p-system .nav-system').addClass('active');	
			$('.p-system-user .nav-system-user').addClass('active');
			$('.p-system-role .nav-system-role').addClass('active');
			$('.p-system-func .nav-system-func').addClass('active');
			$('.p-system-dict-type .nav-system-dict-type').addClass('active');
			$('.p-system-dict .nav-system-dict').addClass('active');
			$('.p-system-suggest .nav-system-suggest').addClass('active');
			$('.p-system-account .nav-system-account').addClass('active');
			$('.p-member .nav-member').addClass('active');
			$('.p-system-membercheck .nav-system-membercheck').addClass('active');
			$('.p-system-memberinstance .nav-system-memberinstance').addClass('active');
			$('.p-mark .nav-mark').addClass('active');
			
		$('.p-setting .nav-setting').addClass('active');	
			$('.p-setting-dict-province .nav-setting-dict-province').addClass('active');
			$('.p-setting-site .nav-setting-site').addClass('active');
			$('.p-system-problem .nav-system-problem').addClass('active');
			$('.p-setting-qiniu .nav-setting-qiniu').addClass('active'); 
			$('.p-setting-index .nav-setting-index').addClass('active');
			$('.p-setting-search .nav-setting-search').addClass('active');
			$('.p-notice .nav-notice').addClass('active');
			$('.p-dictionarie .nav-dictionarie').addClass('active');
			$('.p-complaint .nav-complaint').addClass('active');
			$('.p-setting-coupon .nav-setting-coupon').addClass('active');
			
			
		$('.p-weixin .nav-weixin').addClass('active');
			$('.p-weixin-menu .nav-weixin-menu').addClass('active');
			$('.p-weixin-reply .nav-weixin-reply').addClass('active');
			$('.p-weixin-qunfa .nav-weixin-qunfa').addClass('active');
			$('.p-weixin-audit .nav-weixin-audit').addClass('active');
			$('.p-weixin-sucai .nav-weixin-sucai').addClass('active');
			
		$('.p-other .nav-other').addClass('active'); 
			$('.p-other-offer .nav-other-offer').addClass('active');
			
		$('.p-statistics .nav-statistics').addClass('active');
		$('.p-statistics-addmember .nav-statistics-addmember').addClass('active');
			 
		/*$('.p-dashboard .p_dashboard').addClass('active'); */
			
		var mainnav = $('.main-nav'),
			openActive = mainnav.is ('.open-active'),
			navActive = mainnav.find ('> .active');

		mainnav.find ('> .dropdown > a').bind ('click', navClick);

		if (openActive && navActive.is ('.dropdown')) {			
			navActive.addClass ('opened').find ('.sub-nav').show ();
		}
	}
	
	function navClick (e) {
		e.preventDefault ();
		var li = $(this).parents ('li');		
		if (li.is ('.opened')) { 
			closeAll ();
		} else { 
			closeAll ();
			li.addClass ('opened').find ('.sub-nav').slideDown ();
		}
	}

	function closeAll () {	
		$('.sub-nav').slideUp ().parents ('li').removeClass ('opened');
	}
}();

var App = function () {
	"use strict";	
	return { init: init,  debounce: debounce };
	function init () {
		initLayout ();	
		initAutosize ();
		initFormValidation ();
		initSelect2 ();		
		initDatepicker ();
		initTimepicker ();
		initTooltips ();
		initICheck ();
		initTableCheckable ();
		//initDataTableHelper ();
	}

	function initLayout () {
		Nav.init ();	
		$('body').on('touchstart.dropdown', '.dropdown-menu', function (e) { 
		    e.stopPropagation(); 
		});
		function bodySM (){
			if ($(window).width() < 1024 ) {
				$('body').addClass('p-sm');
				$('.sidebar').addClass('sidebar-sm');
				$('.content').addClass('content-sm');
			} else {
				$('body').removeClass('p-sm');
				$('.sidebar').removeClass('sidebar-sm');
				$('.content').removeClass('content-sm');
			}		
		};
		bodySM();
		$(window).on('resize', bodySM);
	}

	function initAutosize () {
		if ($.fn.autosize) {
		$('.ui-textarea-autosize').each(function() {
			if($(this).data ('animate')) {
					$(this).addClass ('autosize-animate').autosize();
				} else {
					$(this).autosize();
				}
			});
		}
	}

	function initFormValidation () {
		if ($.fn.parsley) {
			$('.parsley-form').each (function () {
				$(this).parsley ({
					trigger: 'blur',
					validationMinlength: 0 ,
					errors: {
						container: function (element, isRadioOrCheckbox) {
							if (element.parents ('form').is ('.form-horizontal')) {
								return element.parents ("div[class^='col-']");
							}
							return element.parents ('.form-group');
						}
					},
					messages: {
					defaultMessage: "您的输入有误"
						, type: {
							email:      "请输入有效的电子邮件地址"
							, url:        "请输入有效的网络地址"
							, urlstrict:  "请输入有效的网络地址"
							, number:     "只允许输入数字"
							, digits:     "请输入数字"
							, dateIso:    "正确的日期格式为YYYY-MM-DD"
							, alphanum:   "只允许输入字母和数字"
							, phone:      "请输入有效的电话号码"
						}
						, notnull:        "必填项"
						, notblank:       "输入不能为空"
						, required:       "必填项"
						, regexp:         "输入的值不合法"
						, min:            "最小值为 %s"
						, max:            "允许的最大值为 %s"
						, range:          "%s - %s 位字符"
						, minlength:      "最少 %s 位字符"
						, maxlength:      "最多 %s 位字符"
						, rangelength:    "%s - %s 位字符"
						, mincheck:       "至少选择 %s 项"
						, maxcheck:       "最多选择 %s 项"
						, rangecheck:     "请选择 %s - %s 项"
						, equalto:        "两次输入不一致"
					}
				});
			});
			$('.modal').on('hidden.bs.modal', function () {
				$('.modal .parsley-form').parsley().reset();
			})				
		}
	}

	function initSelect2 () {
		if ($.fn.select2) {
			$('.select2-input').select2({ allowClear: true, placeholder: "Select..." });
		}
	}

	function initDatepicker () {
		if ($.fn.datepicker) { $('.ui-datepicker').datepicker ({ autoclose: true, todayHighlight: true }); }
	}

	function initTimepicker () {
		if ($.fn.timepicker) { 
			var pickers = $('.timepicker, .ui-timepicker, .ui-timepicker-modal');

			pickers.each (function () {
				$(this).parent ('.input-group').addClass ('bootstrap-timepicker');

				if ($(this).is ('.timepicker')) {
					$(this).timepicker ({ use24hours: true });
				} else {
					$(this).timepicker({ template: 'modal' });
				}	
			});		
		}
	}

	function initTooltips () {
		if ($.fn.tooltip) { $('.ui-tooltip').tooltip (); }
		if ($.fn.popover) { $('.ui-popover').popover ({ container: 'body' }); }
	}

	function initICheck () {
		if ($.fn.iCheck) {
			$('.icheck-input').iCheck({
				checkboxClass: 'ui-checkbox',
				radioClass: 'ui-radio',
				inheritClass: true
			}).on ('ifChanged', function (e) {
				$(e.currentTarget).trigger ('change');
			});
			$('.table-checkable tbody tr').on('click',function(){
				if ( $(this).children().hasClass('checkbox-column') ) {
					if ($(this).hasClass('checked')) {
						$(this).removeClass('checked').find('.icheck-input').removeAttr('checked').iCheck('update');
						$(this).parents('.table-checkable').find('thead .icheck-input').removeAttr('checked').iCheck('update');
					} else {
						$(this).addClass('checked').find('.icheck-input').prop('checked', true).iCheck('update');
						var hasChecbox = $(this).siblings().not('.checked').find('[type="checkbox"]');
						var hasRadio = $(this).siblings().find('[type="radio"]');
						if ( hasChecbox.length <= 0 ) {
							$(this).parents('.table-checkable').find('thead .icheck-input').prop('checked',true).iCheck('update');
						}						
						if ( hasRadio.length > 0 ) {
							$(hasRadio).parents('.table-checkable tbody tr').removeClass('checked');
							$(hasRadio).removeAttr('checked').iCheck('update');
						}						
					};
				}	else {
					$(this).addClass('checked');
					$(this).siblings().removeClass('checked');					
				}					
			})
		}
	}

	function initTableCheckable () {
		if ($.fn.tableCheckable) {
			$('.table-checkable')
		        .tableCheckable ()
			        .on ('masterChecked', function (event, master, slaves) { 
			            if ($.fn.iCheck) { $(slaves).iCheck ('update'); }
			        })
			        .on ('slaveChecked', function (event, master, slave) {
			            if ($.fn.iCheck) { $(master).iCheck ('update'); }
			        });
		}
	}

	function initDataTableHelper () {
		if ($.fn.dataTable) {
			$('[data-provide="datatable"]').each (function () {	
				$(this).addClass ('dataTable-helper');		
				var defaultOptions = {
						paginate: true,
						search: false,
						info: true,
						lengthChange: true,
						displayRows: 10
					},
					dataOptions = $(this).data (),
					helperOptions = $.extend (defaultOptions, dataOptions),
					$thisTable,
					tableConfig = {};

				tableConfig.iDisplayLength = helperOptions.displayRows;
				tableConfig.bFilter = true;
				tableConfig.bSort = true;
				tableConfig.bPaginate = false;
				tableConfig.bLengthChange = false;	
				tableConfig.bInfo = false;

				if (helperOptions.paginate) { tableConfig.bPaginate = true; }
				if (helperOptions.lengthChange) { tableConfig.bLengthChange = true; }
				if (helperOptions.info) { tableConfig.bInfo = true; }       
				if (helperOptions.search) { $(this).parent ().removeClass ('datatable-hidesearch'); }				

				tableConfig.aaSorting = [];
				tableConfig.aoColumns = [];

				$(this).find ('thead tr th').each (function (index, value) {
					var sortable = ($(this).data ('sortable') === true) ? true : false;
					tableConfig.aoColumns.push ({ 'bSortable': sortable });

					if ($(this).data ('direction')) {
						tableConfig.aaSorting.push ([index, $(this).data ('direction')]);
					}
				});		
				
				// Create the datatable
				$thisTable = $(this).dataTable (tableConfig);

				if (!helperOptions.search) {
					$thisTable.parent ().find ('.dataTables_filter').remove ();
				}

				var filterableCols = $thisTable.find ('thead th').filter ('[data-filterable="true"]');

				if (filterableCols.length > 0) {
					var columns = $thisTable.fnSettings().aoColumns,
						$row, th, $col, showFilter;

					$row = $('<tr>', { cls: 'dataTable-filter-row' }).appendTo ($thisTable.find ('thead'));

					for (var i=0; i<columns.length; i++) {
						$col = $(columns[i].nTh.outerHTML);
						showFilter = ($col.data ('filterable') === true) ? 'show' : 'hide';

						th = '<th class="' + $col.prop ('class') + '">';
						th += '<input type="text" class="form-control input-sm ' + showFilter + '" placeholder="' + $col.text () + '">';
						th += '</th>';
						$row.append (th);
					}

					$row.find ('th').removeClass ('sorting sorting_disabled sorting_asc sorting_desc sorting_asc_disabled sorting_desc_disabled');

					$thisTable.find ('thead input').keyup( function () {
						$thisTable.fnFilter( this.value, $thisTable.oApi._fnVisibleToColumnIndex( 
							$thisTable.fnSettings(), $thisTable.find ('thead input[type=text]').index(this) ) );
					});

					$thisTable.addClass ('datatable-columnfilter');
				}
			});

			$('.dataTables_filter input').prop ('placeholder', 'Search...');
		}
	}

	function debounce (func, wait, immediate) {
		var timeout, args, context, timestamp, result;
		return function() {
			context = this;
			args = arguments;
			timestamp = new Date();

			var later = function() {
				var last = (new Date()) - timestamp;

				if (last < wait) {
					timeout = setTimeout(later, wait - last);
				} else {
					timeout = null;
					if (!immediate) result = func.apply(context, args);
				}
			};

			var callNow = immediate && !timeout;

			if (!timeout) {
				timeout = setTimeout(later, wait);
			}

			if (callNow) result = func.apply(context, args);
			return result;
		};
	}
}();

//Bootstrap 3 Modal Vertically Centered http://cdpn.io/mKfCc
function initModalCentered (){
	$('.modal').each(function(){
	    if($(this).hasClass('in') == false){
	      $(this).show();
	    };
	    var contentHeight = $(window).height() - 60;
	    var headerHeight = $(this).find('.modal-header').outerHeight() || 2;
	    var footerHeight = $(this).find('.modal-footer').outerHeight() || 2;

	    $(this).find('.modal-content').css({
	      'max-height': function () {
	        return contentHeight;
	      }
	    });

	    $(this).find('.modal-body').css({
	      'max-height': function () {
	        return contentHeight - (headerHeight + footerHeight);
	      }
	    });

	    $(this).find('.modal-dialog').addClass('modal-dialog-center').css({
	      'margin-top': function () {
	        return -($(this).outerHeight() / 2);
	      },
	      'margin-left': function () {
	        return -($(this).outerWidth() / 2);
	      }
	    });
	    if($(this).hasClass('in') == false){
	      $(this).hide();
	    };
	});
};
if ($(window).height() >= 320){
  $(window).resize(initModalCentered).trigger("resize");
}

$(function () {
	App.init ();
	initModalCentered();
});
