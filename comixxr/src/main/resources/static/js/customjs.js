$(document).ready(function(){
  $("#navbarUsername").text($("#current-user").text());

  // $(function () {
  // $('[data-toggle="tooltip"]').tooltip()
  // })
  /*$("#toggleViewBtn").click(function(){
    if($("#toggleViewBtn").attr("data-mode") === "right"){
      $("#toggleViewBtn").attr("data-mode","down");
      $("#toggleViewIcon").removeClass("fa-angle-double-right");
      $("#toggleViewIcon").addClass("fa-angle-double-down");
      $("#viewPane").html(`<img class='d-md-block w-100' src='img/portfolio/comic.png' alt='First slide'></img>
                           <img class='d-md-block w-100' src='img/portfolio/cake.png' alt='First slide'></img>
                           <img class='d-md-block w-100' src='img/portfolio/circus.png' alt='First slide'></img>`);
      $("#viewPane").addClass("mh-page");

    }
    else{
      $("#toggleViewBtn").attr("data-mode","right");
      $("#toggleViewIcon").removeClass("fa-angle-double-down");
      $("#toggleViewIcon").addClass("fa-angle-double-right");
      $("#viewPane").removeClass("mh-page");

      $("#viewPane").html(`<div id='comicCarousel' class='carousel slide'>
        <ol class='carousel-indicators'>
          <li data-target='#comicCarousel' data-slide-to='0' class='active'></li>\
          <li data-target='#comicCarousel' data-slide-to='1'></li>\
          <li data-target='#comicCarousel' data-slide-to='2'></li>
        </ol>
        <div class='carousel-inner'>
          <div class='carousel-item active'>
            <img class='d-md-block w-100' src='img/portfolio/comic.png' alt='First slide'></img>
          </div>
          <div class='carousel-item'>
            <img class='d-md-block w-100' src='img/portfolio/cake.png' alt='First slide'></img>
          </div>
          <div class='carousel-item'>
            <img class='d-md-block w-100' src='img/portfolio/circus.png' alt='First slide'></img>
          </div>
        </div>
        <a class='carousel-control-prev' href='#comicCarousel' role='button' data-slide='prev'>
          <span class='carousel-control-prev-icon' aria-hidden='true'></span>
          <span class='sr-only'>Previous</span>
        </a>
        <a class='carousel-control-next' href='#comicCarousel' role='button' data-slide='next'>
          <span class='carousel-control-next-icon' aria-hidden='true'></span>
          <span class='sr-only'>Next</span>
        </a>
      </div>`)
    }
  })*/

});
