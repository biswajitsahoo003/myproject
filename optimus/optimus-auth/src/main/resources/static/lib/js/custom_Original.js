
$(document).ready(function(){

    /* Page select-configuration.html */ 
    /* On check select panel*/
    $(".check-box-right.header :checkbox").on('click', function(){
        $(this).parent().parent().toggleClass("selected");
    });

    $('.expand-btn.collapsed').on('click',function(){
        var $checkbox = $(this).parent().find('.header input[type=checkbox]');
        $checkbox.trigger("click");
    });
    /* To show tooltip*/
    $('[data-toggle="tooltip"]').tooltip();


    /*Page landing-page.html */
    /*navbar dropdown to avoid click event and overlay effect for login */
    $('.dropdown-menu').on('click', function(e) {
		/*if($(".timepicker").is(":visible")==true) {
            //e.stopPropagation();
			//$(".timepicker").hide();
			if(!$(e.target).closest(".timepicker").length){
				$(".timepicker").remove();
			}			
        }
        if($(this).hasClass('dropdown-menu-form')) {
           e.stopPropagation();
        }
        */
		if($(this).hasClass('checkbox-list-dp')) {
            e.stopPropagation();
        }
		if($(this).hasClass('radiobtn-list-dp')) {
            e.stopPropagation();
        }
	if($(this).hasClass('dropdown-menu-form')) {	
		var that = $(this);
		setTimeout(function(){
			that.addClass("show");
		},0);
	}
    });

	$('body').on("click", function(){
		$('.dropdown-menu').removeClass("show");
	});

    /* landing page card content transition */
    $(".services-content .expand-btn").mouseover(function() {
        if($(".hidden-content :visible").length > 0 && $(".expand-btn :hidden").length > 0 ){
        $(".hidden-content").slideUp(700);
        $(".expand-btn :hidden").parent().css("display","block");   

        $(this).parent().find(".hidden-content").slideDown(700);
        $(this).parent().find(".expand-btn").css("display","none");
        }else{
          $(this).parent().find(".hidden-content").slideDown(700);
          $(this).parent().find(".expand-btn").css("display","none");     
        }
    });


    /* add-locations.html - */
    /* Show address panel */
    $('.bottom-btn .btn, .add-address-btn').on('click', function(e) {
        $('.location-section').removeClass('d-none')
    });

    /* Upload excel file */
    $('.upload-button').on('click', function(e) {
        $('.reupload-button, .upload-excel-errors').removeClass('d-none');
        $('.upload-button, .upload-excel-steps').addClass('d-none');
    });

    /* Select the services */
    $('#itfServicesDone').on('click', function(e) {
        $('#selectedServices').removeClass('d-none');
        $('.radiobtn-list-dp, .terminating-pstn, #itfServices').addClass('d-none');
    });
    $('#selectedServices').on('click', function(e) {
        $('.radiobtn-list-dp').removeClass('d-none');
    });

    $('#chatNowBtn').on('click', function(e) {
        $('#helpCard').addClass('d-none');
        $('#helloUser').removeClass('d-none');
    });

    $('.schedule-header .icon-back').on('click', function(e) {
        $('#scheduleCall').addClass('d-none');
        $('#helloUser').removeClass('d-none');
        $('.dropdown-menu.dropdown-menu-form').removeClass('gray-indicator');
    });

    $('#letusCallBtn').on('click', function(e) {
        $('#scheduleCall').removeClass('d-none');
        $('#helloUser').addClass('d-none');
        $('.dropdown-menu.dropdown-menu-form').addClass('gray-indicator');
    });

    $('.primary-navigation li > a').on('click', function(e) {
        $('.primary-navigation li > a').removeClass('active');
        $(this).addClass('active');
        $('.nav-overlay').show();
    });

    $('.nav-overlay, .primary-navigation li:first-child > a').on('click', function(e) {
        $('.primary-navigation li > a').removeClass('active');
        $('.nav-overlay').hide();
    });

    $('#addConfigurations').on('click', function(e) {
        $('#addConfigurationsShow').removeClass('d-none');
    });

    $(".popover").each(function(index){
        $(this).not('#' + $(Element).attr('id')).popover('hide');
    });
    
    /* Show slider*/    
    $('.slider-for').slick({
      slidesToShow: 1,
      slidesToScroll: 1,
      arrows: false,
      asNavFor: '.slider-nav',
      draggable:false,
      infinite:false
    });
    $('.slider-nav').slick({
      slidesToShow: 3,
      slidesToScroll: 1,
      asNavFor: '.slider-for',
      dots: false,
      focusOnSelect: true,
        infinite:false,
      nextArrow: '<i class="slick-arrow right icon-caret-right"></i>',
      prevArrow: '<i class="slick-arrow left icon-caret-left"></i>',
    });
    
    
    /* get quote page - on checked change colore */
    $(".quote-list li input:checkbox").change(function(){
      $(this).closest("li").toggleClass('current', this.checked);
      if($('.quote-list li').hasClass('current')){
          $('.price-summary.hidden-content').slideDown(300);
          $('.price-summary .amt-unit').removeClass('d-none');
      }else{
          $('.price-summary.hidden-content').slideUp(300);
          $('.price-summary .amt-unit').addClass('d-none');
      }
    });
    
    /* get quote page - for group dropdown */
    $('.hide-group').hide();
    $('.hide-group.active').show();
    $('#groupDp').change(function(){
        $('.quote-list li input:checkbox').prop('checked', false);
        $('.quote-list li').removeClass('current')
        $('.price-summary.hidden-content').slideUp(300);
        $('.price-summary .amt-unit').addClass('d-none');
        
        $('.hide-group').hide();
        $('.hide-group').removeClass('active');
        $('#' + $(this).val()).show();
        $('#' + $(this).val()).addClass('active');
        if($('#geography').hasClass('active')){
            $('.country-ct-switch').removeClass('d-none');
            $('.slider-for').slick({
              slidesToShow: 1,
              slidesToScroll: 1,
              arrows: false,
              asNavFor: '.slider-nav',
              draggable:false,
                infinite:false
            });
            $('.slider-nav').slick({
              slidesToShow: 3,
              slidesToScroll: 1,
              asNavFor: '.slider-for',
              dots: false,
              focusOnSelect: true,
                infinite:false,
              nextArrow: '<i class="slick-arrow right icon-caret-right"></i>',
              prevArrow: '<i class="slick-arrow left icon-caret-left"></i>',
            });
            
        }else{
            $('.country-ct-switch').addClass('d-none');
        }
    });
   
    /* COF page */
     $("input:checkbox[value=signature-rights]").click(function() {
        if($(this).is(':checked')){
            $('#signatureModal').modal('show');
        }
    });
    $("input:checkbox[value=read-tc]").click(function() {
        if($(this).is(':checked')==true){
            $('.order-btn').removeClass('inactive-btn');
            $('.order-btn').addClass('active-btn');
        }else{
            $('.order-btn').removeClass('active-btn');
            $('.order-btn').addClass('inactive-btn');
        }
    });

    /* Sales order page */ 
    /* Script to check the radio condition to disable/enable generate report button */
    $("input:radio[name=entitynumber]").click(function() {
        if($('input[value=single]:checked').length == 1)
        {  
            $(".generate-docs button").removeClass('inactive-btn');  
            $(".generate-docs button").addClass('active-btn');  
        }
        if($('input[value=multiple]:checked').length == 1)
        {
            $(".generate-docs button").removeClass('active-btn');  
            $(".generate-docs button").addClass('inactive-btn'); 
            $('#multipledocs').modal('show');
        } 
    });

    /*To show Name of selected/checked Legal entity */
    $('.radiobtn-list-dp .radio-box ').click(function(){
        $('#selected').text($(this).text());
    });

    /* Script to show the tax excemption alert when foreign location is clicked in legal entity */
    $("#onboardEntity").click(function() {
        $('#taximplications').modal('show');
    });

    /* Script to show the address section after clicking the add address button in the modal */
    $(".add-address").click(function() {
        $('.siteaddresses').removeClass('d-none');
    });
    
    /* Script to hide the address section when there is no tax excemption */
    $("input:radio[value=taxexcempno]").click(function() {
        $('.siteaddresses').addClass('d-none');
    });

    /* Script to show the select site address modal when there is tax excemption */
    $("input:radio[value=taxexcempyes]").click(function() {
        if($(this).is(':checked')){
            $('#taxexcemption').modal('show');
        }
    });
    
    /* site list page input range slider */
    var timeout, tabMinus = $('#minus'), tabPlus = $('#plus');

	/*slider functionality */
	$('#slider').mouseup(function(){
	   var res =parseInt($('#slider').val());
       document.getElementById("delta").innerHTML=res + " Mbps";
	});

	$('#delta').keyup(function(){
    	$('#slider').val($('#delta').val());
	});

	/* plus-minus tab functionality */
	tabMinus.mouseup(function(){
		$('#delta').val($('#slider').val() - 1);
    	$('#slider').val($('#delta').val() - .2);
        var result=$('#delta').val();
        document.getElementById("delta").innerHTML=result + " Mbps";
	});

	tabPlus.mouseup(function(){
  	let delatvalue = parseInt($('#slider').val()) + 1;
		$('#delta').val(delatvalue);
		$('#slider').val(delatvalue + .2);
		var result=$('#delta').val();
        document.getElementById("delta").innerHTML=result + " Mbps";
    });

    /*Generic - On next button click - move selection to next tab to show data*/
    
    $('.next-tab').click(function (e) {
        e.preventDefault();
        $('a[href="' + $(this).attr('data-target') + '"]').tab('show');
    });
   
    
    /* Datepicker */
    $('.date-select input').datepicker({
        format: 'dd / mm / yyyy',
        autoHide: true
    });

    /* show file value after file select */
    $('.custom-file-input').on('change',function(){
        $(this).next('.form-control-file').addClass("selected").html($(this).val());
    })

    /* congratulations page */
    $('.navbar-toggler').click( function() {
        $('.sidenavbar').toggleClass("sidenavbar-toggle");
        $('main').toggleClass("togglemain");
    });
    
    /* Port on check show hide content */
    $("input[name=entitynumber]").on( "change", function() {
         var test = $(this).val();
         $(".port-number").hide();
         $("#"+test).show();
    } );
    
    /* ITFS Internal Screen */

    $('.create-configure').on('click', function(e){
        $('.itfs-2').show();
        $('.itfs-1').hide();
    });
    
    /* Alphabates Active class add and remove */
    $('.alphabate-list a').click(function(){
        $('.alphabate-list a').removeClass('active');
        $(this).addClass('active');
    });
    
    /* Flag Checkbox*/
    var checkboxes = $(".flag-checkbox input[type='checkbox']"),
        submitButt = $(".create-configure");
    checkboxes.click(function() {
        submitButt.attr("disabled", !checkboxes.is(":checked"));
    });
    /* access locations */
    $("#selectTrigger").change(function() {
        $('span[data-label=val1]').html(parseInt((Math.random()*22))+'%');
        $('span[data-label=val2]').html(parseInt((Math.random()*22))+'%');
        $('span[data-label=val3]').html(parseInt((Math.random()*22))+'%');
        $('span[data-label=val4]').html(parseInt((Math.random()*22))+'%');
        $('span[data-label=val5]').html(parseInt((Math.random()*22))+'%');
        $('span[data-label=val6]').html(parseInt((Math.random()*22))+'%');
    }).change();
    
    $(".checkbox-list.mutliSelect").find('input').on('click',function(){
         if($(this).is(":checked")){
            $(".dropdown-menu.checkbox-list-dp.show").find('.checked-order-list').append('<li><label class="check-box check-box-left"><input type="checkbox" checked><span class="checkmark"></span>'+$(this).val()+'</label></li>');
         }else{
             $(".dropdown-menu.checkbox-list-dp.show").find('.checked-order-list').find( "li:contains('"+$(this).val()+"')" ).remove();
         }
    });
  
    /* quote summary page */
    $('.qoute-summary-container .done-btn').on('click',function(){
       $(".dropdown-menu.checkbox-list-dp.show").parent().find('div.selectedValue').find(".checkbox-container").remove();
        var selectedlist=$(".dropdown-menu.checkbox-list-dp.show").find('.checked-order-list').find('li');
        if(selectedlist.length != 0){
            $(".dropdown-menu.checkbox-list-dp.show").parent().find('div.selectedValue').get(0).innerText = " ";
        }else{
             $(".dropdown-menu.checkbox-list-dp.show").parent().find('div.selectedValue').get(0).innerText = "Select";
            $(".dropdown-menu.checkbox-list-dp.show").removeClass('show'); 
            
        }
        for(let i= 0;i<selectedlist.length;i++){
            let selectedli = selectedlist[i]; 
            $(".dropdown-menu.checkbox-list-dp.show").parent().find('div.selectedValue').append('<div class="checkbox-container"><label class="check-box check-box-left"><input type="checkbox" checked><span class="checkmark"></span>'+selectedli.innerText+'</label></div>');
            if(i == (selectedlist.length-1)){
                    $(".dropdown-menu.checkbox-list-dp.show").removeClass('show');
            }    
        }
    });

    /* range slider*/
	if($("#budget-slider-1").length){
		var slider = new Slider("#budget-slider-1");
		slider.on("change", function(slideEvent){
			var currentValueSlider1 = slider.getValue();
			console.log(currentValueSlider1);
			$("#slider-value1").text(currentValueSlider1);
		});
	}
	if($("#budget-slider-2").length){
		var slider1 = new Slider("#budget-slider-2");
			slider1.on("change", function(slideEvent){
			var currentValueSlider2 = slider1.getValue();
			$("#slider-value2").text(currentValueSlider2);
		});
	}
    if($("#ext16").length){
		var slider2 = new Slider("#ext16");
			slider2.on("change", function(slideEvent){
			var currentValueSlider3 = slider2.getValue();
			$("#slider-value3").text(currentValueSlider3);
		});
	}
    
    $("#ex17,#ext18,#ex19,#ex20,#ex21,#ex22,#ex23,#ex24,#ex25").slider({
        ticks: [0, 2, 4, 6, 8, 10, 12, 14],
        ticks_snap_bounds:0,
        value: 0
	});

    /* dashboard company profile page */
    $("#v-profile-tab a").click(function() {
       $("#v-setting-tabContent").hide();
       $("#v-setting-tab a").removeClass("active");
       $("#v-profile-tabContent").show();
       
    });
    $("#v-setting-tab a").click(function() {
       $("#v-profile-tabContent").hide();
       $("#v-profile-tab a").removeClass("active");
       $("#v-setting-tabContent").show(); 
    }); 
	/* izo review your cart page */
	if($("#quotation-tabs a").length){
		$("#quotation-tabs a").click(function() {
			$("#saved-tabContent").hide();
			$("#saved-tabs a").removeClass("active");
			$("#quotation-tabContent").show();
		});
	}
	if($("#saved-tabs a").length){
		$("#saved-tabs a").click(function() {
		$("#quotation-tabContent").hide();
		$("#quotation-tabs a").removeClass("active");
		$("#saved-tabContent").show(); 
		});
	}
   // $(".bandwidth-group").hide();
    var selectBox = "";
    $('.izo-select-configuration .done-btn,.izo-quote-container .done-btn').on('click',function(){
        var selectedlist =  $(".dropdown-menu.checkbox-list-dp.show").find('.checkbox-list').find('li input:checked');
        selectBox=selectedlist.length-1;
        if(selectedlist.length > 0){
         // $(".izo-select-configuration .dropdown-menu.checkbox-list-dp").parents().find('div.data-centers').find(".seletedvalue").remove();
          $(".dropdown-menu.checkbox-list-dp.show").parent().parent().parent().find(".seletedvalue").remove()
         // $(".bandwidth-group").find(".form-group").remove();
         $(".dropdown-menu.checkbox-list-dp.show").parent().parent().parent().parent().parent().find('div.bandwidth-group').find(".form-group").remove();
        }
        for(let i= 0;i<selectedlist.length;i++){
            let selectedli = selectedlist[i]; 
            console.log(selectedli[0]);
             $(".dropdown-menu.checkbox-list-dp.show").parent().parent().parent().find('div.data-centers').append('<div class="form-group seletedvalue"><p class="form-control">'+selectedli.value+'</p></div>');
            //$(".bandwidth-group").show(selectBox);
            $(".dropdown-menu.checkbox-list-dp.show").parent().parent().parent().parent().parent().find('div.bandwidth-group').append('<div class="form-group"><div class="select"><select class="form-control"><option>100 Mbps</option><option>50 Mbps</option><option>10 Mbps</option></select></div></div>');
            if(i == (selectedlist.length-1)){
                $(".dropdown-menu.checkbox-list-dp.show").removeClass('show');
            }    
        }
    }); 
    
    
    $('.total-order-btn').on('click', function(e) {
        $(this).closest('.current').hide();
        $('.active-order').show();
    });
    
    $('.order-back-btn').on('click', function(e) {
        $(this).closest('.active-order').hide();
        $('.current').show();
    });
    
    $(".switch-toggle input:checkbox").change(function(){
        $(this).closest(".toggle2").toggleClass('active', this.checked);
    });

    $(".toggle-collapsed").on('click', function(e) {
        $(this).toggleClass('collapsed');
    });
	
    /* company profile page*/
	var max = 10;
	$(".toggle-btn").find("i").addClass('down-arrow');
	$('#v-profile-tab').each(function(){

		if ($(this).find('a').length > max) {
			$(this).find('a:gt('+max+')').hide();
			
		};
	});
	$('.toggle-btn').click( function(){
		$("#v-profile-tab").find('a:gt('+max+')').toggle();
		if($('#v-profile-tab a').length ) {
			if($("#v-profile-tab").parent().find("i").hasClass('down-arrow')){
				$("#v-profile-tab").parent().find("i").removeClass('down-arrow');
				$("#v-profile-tab").parent().find("i").addClass("up-arrow");
			}else{
				$("#v-profile-tab").parent().find("i").addClass("down-arrow");
			}
		} else {
			$("#v-profile-tab").parent().find("i").addClass('down-arrow');
		};
	});

	$(".create-configure").on("click",function(){
		$(".inner-card").removeClass("d-none");
	});
    
	$('.toggle-arrow a').on('click', function(e){
		
			$('.order-dtls-summary').addClass('order-track-on');    
			
			if($(this).hasClass('collapsed')){
			$(this).closest('.order-dtls-summary').removeClass('order-track-on');
			}
		
    });
    $(".close-btn").on("click",function(){
		$(".timepicker").hide();
	});

	
	if($('#customTime').length){
		$('#customTime').timepicker({
			autoclose:true
		});
		$('#customTime').timepicker();
	}        
});

Highcharts.setOptions({
    colors: ['#a8cf4e', '#f7cf10', '#000000']
});

    $(function () {
		if($("#chart1").length){
			Highcharts.chart('chart1', {
				credits: {
					enabled: false
				},

				chart: {
					type: 'solidgauge',
					margin: 1
				},
				title: {
					text: ''
					},
				tooltip: {
					borderWidth: 0,
					backgroundColor: 'none',
					shadow: false,
					pointFormat: '<span style="font-size:2.6em; color:#555;">{point.y}%</span> <br><div style="text-align:center; color: #aaa; font-weight: normal;">{series.name}</div>',
					positioner: function (labelWidth, labelHeight) {
						return {
							x: 60 - labelWidth / 2,
							y: 45
						};
					}
				},

				pane: {
					startAngle: 0,
					endAngle: 360,
					background: [{ // Track for Exercise
						outerRadius: '115%',
						innerRadius: '100%',
						
						backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[2]).setOpacity(0.1).get(),
						borderWidth: 0
					}]
				},

				yAxis: {
					min: 0,
					max: 100,
					lineWidth: 0,
					tickPositions: []
				},

				plotOptions: {
					solidgauge: {
						borderWidth: 'none',
						dataLabels: {
							enabled: false
						},
						linecap: 'round',
						stickyTracking: false
					}
				},

				series: [{
					name: 'Completed',
					borderColor: Highcharts.getOptions().colors[0],
					data: [{
						color: Highcharts.getOptions().colors[0],
						radius: '115%',
						innerRadius: '100%',
						y: 75
					}]
				}]
			});
			var chart = $('#chart1').highcharts(),
					point = chart.series[0].points[0];
			point.onMouseOver(); // Show the hover marker
			chart.tooltip.refresh(point); // Show the tooltip
			chart.tooltip.hide = function () {console.log()};
		}
	});

	$(function () {
		if($("#chart2").length){
		
	   
		Highcharts.chart('chart2', {
					
			credits: {
				enabled: false
			},

			chart: {
				type: 'solidgauge',
				margin: 1
			},
			title: {
				text: ''
				},
			tooltip: {
				borderWidth: 0,
				backgroundColor: 'none',
				shadow: false,
				pointFormat: '<span style="font-size:2.6em; color:#555;">{point.y}%</span> <br><div style="text-align:center; color: #aaa; font-weight: normal;">{series.name}</div>',
				positioner: function (labelWidth, labelHeight) {
					return {
						x: 60 - labelWidth / 2,
						y: 45
					};
				}
			},

			pane: {
				startAngle: 0,
				endAngle: 360,
				background: [{ // Track for Exercise
					outerRadius: '115%',
					innerRadius: '100%',
					
					backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[2]).setOpacity(0.1).get(),
					borderWidth: 0
				}]
			},

			yAxis: {
				min: 0,
				max: 100,
				lineWidth: 0,
				tickPositions: []
			},

			plotOptions: {
				solidgauge: {
					borderWidth: 'none',
					dataLabels: {
						enabled: false
					},
					linecap: 'round',
					stickyTracking: false
				}
			},

			series: [{
				name: 'Completed',
				borderColor: Highcharts.getOptions().colors[1],
				data: [{
					color: Highcharts.getOptions().colors[1],
					radius: '115%',
					innerRadius: '100%',
					y: 50
				}]
			}]
		});

		var chart = $('#chart2').highcharts(),
				point = chart.series[0].points[0];
		point.onMouseOver(); // Show the hover marker
		chart.tooltip.refresh(point); // Show the tooltip
		chart.tooltip.hide = function () {console.log()};
	}
	});
	$(function () {
		if($("#chart3").length){
		Highcharts.chart('chart3', {
					
			credits: {
				enabled: false
			},

			chart: {
				type: 'solidgauge',
				margin: 1
			},
			title: {
				text: ''
				},
			tooltip: {
				borderWidth: 0,
				backgroundColor: 'none',
				shadow: false,
				pointFormat: '<span style="font-size:2.6em; color:#555;">{point.y}%</span> <br><div style="text-align:center; color: #aaa; font-weight: normal;">{series.name}</div>',
                positioner: function (labelWidth, labelHeight) {
					return {
						x: 60 - labelWidth / 2,
						y: 45
					};
				}
			},

			pane: {
				startAngle: 0,
				endAngle: 360,
				background: [{ // Track for Exercise
					outerRadius: '115%',
					innerRadius: '100%',
					
					backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[2]).setOpacity(0.1).get(),
					borderWidth: 0
				}]
			},

			yAxis: {
				min: 0,
				max: 100,
				lineWidth: 0,
				tickPositions: []
			},

			plotOptions: {
				solidgauge: {
					borderWidth: 'none',
					dataLabels: {
						enabled: false
					},
					linecap: 'round',
					stickyTracking: false
				}
			},

			series: [{
				name: 'Completed',
				borderColor: Highcharts.getOptions().colors[0],
				data: [{
					color: Highcharts.getOptions().colors[0],
					radius: '115%',
					innerRadius: '100%',
					y: 75
				}]
			}]
		}
		);

		var chart = $('#chart3').highcharts(),
				point = chart.series[0].points[0];
		point.onMouseOver(); // Show the hover marker
		chart.tooltip.refresh(point); // Show the tooltip
		chart.tooltip.hide = function () {console.log()};
		}
	});
	$(function () {
		if($("#chart4").length){
		Highcharts.chart('chart4', {
					
			credits: {
				enabled: false
			},

			chart: {
				type: 'solidgauge',
				margin: 1
			},
			title: {
				text: ''
				},
			tooltip: {
				borderWidth: 0,
				backgroundColor: 'none',
				shadow: false,
				pointFormat: '<span style="font-size:2.6em; color:#555;">{point.y}%</span> <br><div style="text-align:center; color: #aaa; font-weight: normal;">{series.name}</div>',
                positioner: function (labelWidth, labelHeight) {
					return {
						x: 60 - labelWidth / 2,
						y: 45
					};
				}
			},

			pane: {
				startAngle: 0,
				endAngle: 360,
				background: [{ // Track for Exercise
					outerRadius: '115%',
					innerRadius: '100%',
					
					backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[2]).setOpacity(0.1).get(),
					borderWidth: 0
				}]
			},

			yAxis: {
				min: 0,
				max: 100,
				lineWidth: 0,
				tickPositions: []
			},

			plotOptions: {
				solidgauge: {
					borderWidth: 'none',
					dataLabels: {
						enabled: false
					},
					linecap: 'round',
					stickyTracking: false
				}
			},

			series: [{
				name: 'Completed',
				borderColor: Highcharts.getOptions().colors[0],
				data: [{
					color: Highcharts.getOptions().colors[0],
					radius: '115%',
					innerRadius: '100%',
					y: 75
				}]
			}]
		}
		);

		var chart = $('#chart4').highcharts(),
				point = chart.series[0].points[0];
		point.onMouseOver(); // Show the hover marker
		chart.tooltip.refresh(point); // Show the tooltip
		chart.tooltip.hide = function () {};
		}
	});
	$(function () {
		if($("#chart5").length){
		Highcharts.chart('chart5', {
					
			credits: {
				enabled: false
			},

			chart: {
				type: 'solidgauge',
				margin: 1
			},
			title: {
				text: ''
				},
			tooltip: {
				borderWidth: 0,
				backgroundColor: 'none',
				shadow: false,
				pointFormat: '<span style="font-size:2.6em; color:#555;">{point.y}%</span> <br><div style="text-align:center; color: #aaa; font-weight: normal;">{series.name}</div>',
                positioner: function (labelWidth, labelHeight) {
					return {
						x: 60 - labelWidth / 2,
						y: 45
					};
				}
			},

			pane: {
				startAngle: 0,
				endAngle: 360,
				background: [{ // Track for Exercise
					outerRadius: '115%',
					innerRadius: '100%',
					
					backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[2]).setOpacity(0.1).get(),
					borderWidth: 0
				}]
			},

			yAxis: {
				min: 0,
				max: 100,
				lineWidth: 0,
				tickPositions: []
			},

			plotOptions: {
				solidgauge: {
					borderWidth: 'none',
					dataLabels: {
						enabled: false
					},
					linecap: 'round',
					stickyTracking: false
				}
			},

			series: [{
				name: 'Completed',
				borderColor: Highcharts.getOptions().colors[1],
				data: [{
					color: Highcharts.getOptions().colors[1],
					radius: '115%',
					innerRadius: '100%',
					y: 75
				}]
			}]
		}
		);

		var chart = $('#chart5').highcharts(),
				point = chart.series[0].points[0];
		point.onMouseOver(); // Show the hover marker
		chart.tooltip.refresh(point); // Show the tooltip
		chart.tooltip.hide = function () {};
		}
	});
	$(function () {
		if($("#chart6").length){
		Highcharts.chart('chart6', {
					
			credits: {
				enabled: false
			},

			chart: {
				type: 'solidgauge',
				margin: 1
			},
			title: {
				text: ''
				},
			tooltip: {
				borderWidth: 0,
				backgroundColor: 'none',
				shadow: false,
				pointFormat: '<span style="font-size:2.6em; color:#555;">{point.y}%</span> <br><div style="text-align:center; color: #aaa; font-weight: normal;">{series.name}</div>',
                positioner: function (labelWidth, labelHeight) {
					return {
						x: 60 - labelWidth / 2,
						y: 45
					};
				}
			},

			pane: {
				startAngle: 0,
				endAngle: 360,
				background: [{ // Track for Exercise
					outerRadius: '115%',
					innerRadius: '100%',
					
					backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[2]).setOpacity(0.1).get(),
					borderWidth: 0
				}]
			},

			yAxis: {
				min: 0,
				max: 100,
				lineWidth: 0,
				tickPositions: []
			},

			plotOptions: {
				solidgauge: {
					borderWidth: 'none',
					dataLabels: {
						enabled: false
					},
					linecap: 'round',
					stickyTracking: false
				}
			},

			series: [{
				name: 'Completed',
				borderColor: Highcharts.getOptions().colors[0],
				data: [{
					color: Highcharts.getOptions().colors[0],
					radius: '115%',
					innerRadius: '100%',
					y: 75
				}]
			}]
		}
		);

		var chart = $('#chart6').highcharts(),
		point = chart.series[0].points[0];
		point.onMouseOver(); // Show the hover marker
		chart.tooltip.refresh(point); // Show the tooltip
		chart.tooltip.hide = function () {};
		}
	});
	