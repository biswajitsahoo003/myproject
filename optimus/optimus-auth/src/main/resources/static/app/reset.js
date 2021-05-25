$(document).ready(function () {
  $(".alert").hide();
  $('#errMsg').hide();
  $('#unknownErr').hide();
  $('#successMsg').hide();
  $('#errMsg')[0].style.opacity = 1;
  $('#unknownErr')[0].style.opacity = 1;
  $('#successMsg')[0].style.opacity = 1;
  $('#resetForm').hide();
  
  
  function resetPassword(expireCheck) {
    /**************Reset/ Validate API Call****************/
    var token = window.location.href.split('?token=')[1];
    var httpHost=window.location.origin+"/authentication";
    var url = httpHost+"/password/reset", type, data, dataType;
    if (expireCheck) {
      url += "/validate?token=";
      type = "GET";
      data = null;
      dataType = null;
    } else {
      url += "?token=";
      type = "POST"
      data = JSON.stringify({ password: $("#password").val() });
      dataType = "json";
    }
    
    $.ajax({
      url: url + token,
      type: type,
      data: data,
      dataType: dataType,
      contentType: "application/json; charset=utf-8",
      success: function (data) {
        if (expireCheck) {
          if (data.status=== "SUCCESS") {
  	        $('#resetForm').show();
          }
          else {
        	  $('#resetForm').hide();
        	  $('#errMsg').show();
  	      }
        } else {
          if (data.status=== "SUCCESS") {
         	  $('#resetForm').hide();
            $("#successMsg").show();
          }
          else {
        	  $('#resetForm').hide();
        	  $('#unknownErr').show();
  	      }
        }
      },
      error: function (data) {
          if (expireCheck) {
        	  $('#resetForm').hide();
  	        $('#errMsg').show();
          } else {
       	    $('#resetForm').hide();
  	        $('#unknownErr').show();
          }
      }
    });
    /**************End*************/
  }
  
  resetPassword(true);

  $("#confirmPwdBtn").on("click", function () {
     $('#pwdMandate').addClass('d-none');
     $("#pwdErrorMsg").addClass("d-none");
     $("#pwdPatternError").addClass("d-none");
		
    var pwd = $("#password").val();
    var confirmPwd = $("#confirm_password").val();
    var regEx = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/);
    
    if(pwd === "" || confirmPwd === ""){
      $('#pwdMandate').removeClass('d-none');
       return;
    } else if (pwd !== confirmPwd) {
      $("#pwdErrorMsg").removeClass("d-none");
      return;
    } else if(!regEx.test(pwd)){    
      $("#pwdPatternError").removeClass("d-none"); 
      return;
    }	
    
    resetPassword(false);

  });


});
