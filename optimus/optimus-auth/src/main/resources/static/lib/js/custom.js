$(document).ready(function() {

  /*setTimeout(function(){
 $("#username").val('');
  $("#password").val(''); 
  },500)*/
 
  $(".alert").hide();
  $(".invalid-email").hide();
  $(".reset-block").hide();
  $(".services-content .expand-btn").mouseover(function() {
    if (
      $(".hidden-content :visible").length > 0 &&
      $(".expand-btn :hidden").length > 0
    ) {
      $(".hidden-content").slideUp(700);
      $(".expand-btn :hidden")
        .parent()
        .css("display", "block");

      $(this)
        .parent()
        .find(".hidden-content")
        .slideDown(700);
      $(this)
        .parent()
        .find(".expand-btn")
        .css("display", "none");
    } else {
      $(this)
        .parent()
        .find(".hidden-content")
        .slideDown(700);
      $(this)
        .parent()
        .find(".expand-btn")
        .css("display", "none");
    }
  });

  if(localStorage.getItem('loginFormOpened') == "true"){
	setTimeout(function(){
		$('.dropdown-menu').addClass('show');
	})
  }

  $("body").on("click", function() {
    $(".dropdown-menu").removeClass("show");
    localStorage.setItem('loginFormOpened',false);
  });

  $(".dropdown-toggle").on("click", function(event) {
    if ($(".dropdown-menu").hasClass("show")) {
      setTimeout(function() {
        $(".dropdown-menu").removeClass("show");
	localStorage.setItem('loginFormOpened',false);

      });
    } else{
	setTimeout(function() {
        $(".dropdown-menu").addClass("show");
	localStorage.setItem('loginFormOpened',true);

      });
    }
     event.stopPropagation();
  });


  $("#username").on("click", function(event){
	event.preventDefault();
	event.stopPropagation();
  });

	
  $(".dropdown-menu").on("click", function(event) {
    event.stopPropagation();
  }); 
 

  $("#forgot-pwd").on("click", function() {
    $("#pwd-block").hide();
    $(".login-block").hide();
    $(".alert").hide();
    $(".invalid-email").hide();
    $(".invalid-credential").hide();
  	$(".reset-block").show();
  	$("#username").val('');
  	$("#password").val('');
  });

  $("#cancel-btn").on("click", function() {
    $("#pwd-block").show();
    $(".login-block").show();
    $(".reset-block").hide();
    $("#emailErrorMsg").addClass("d-none");
    $("#email-block").removeClass("has-error");
    $(".alert").hide();
    $(".invalid-email").hide();
  });

  $("#reset-btn").on("click", function() {
    // validation
    var emailMandate = !$("#username").val();
    // var emailInvalid = !$("#username").val();
    if (emailMandate) {
      $("#emailErrorMsg").removeClass("d-none");
      $("#email-block").addClass("has-error");
      return;
    } else {
      $("#emailErrorMsg").addClass("d-none");
      $("#email-block").removeClass("has-error");
    }   	

	
    $.ajax ({
    url: "https://10.133.208.121/optimus-userservice/login/password/forgot",
    type: "POST",
    data: JSON.stringify({emailId: $("#username").val()}),
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function(data){
    	if(data.data.message === "SUCCESS"){	
        $(".reset-block").hide();
      	$("#pwd-block").show();
      	$(".login-block").show();
      	$(".alert").show();
    	} else {
        $(".invalid-email").show();
      }
    },
      error: function (data) {
        $(".invalid-email").show();
    }

});

  });

  $("#submit_button").on("click", function() {

/*	var emailMandate = !$("#username").val();
	var pwdMandate = !$("#password").val();
    // var emailInvalid = !$("#username").val();
    if (emailMandate) {
      $("#emailErrorMsg").removeClass("d-none");
      $("#email-block").addClass("has-error");
    } else {
      $("#emailErrorMsg").addClass("d-none");
      $("#email-block").removeClass("has-error");
    }

    if (pwdMandate) {
      $("#pwdErrorMsg").removeClass("d-none");
      $("#pwd-block").addClass("has-error");
    } else {
      $("#pwdErrorMsg").addClass("d-none");
      $("#pwd-block").removeClass("has-error");
    } */
  });
});
