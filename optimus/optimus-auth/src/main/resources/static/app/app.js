"use strict";

var onServiceClick = function (service,event) {
    $('.dropdown-menu').addClass('show');
    sessionStorage.setItem('loginFormOpened',true);
    event.stopPropagation();
    localStorage.setItem("serviceSelected", service);

};



