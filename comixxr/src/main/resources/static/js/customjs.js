$(document).ready(function(){
  $("#navbarUsername").text($("#current-user").text());

  if($("#viewPane").length){
    toggleViewfn();
  }
  $("#toggleViewBtn").click(function(){
    toggleViewfn();
  });
  // $(function () {
  // $('[data-toggle="tooltip"]').tooltip()
  // })
  function toggleViewfn(){
    if($("#toggleViewBtn").attr("data-mode") === "right"){
      $("#toggleViewBtn").attr("data-mode","down");
      $("#toggleViewIcon").removeClass("fa-angle-double-right");
      $("#toggleViewIcon").addClass("fa-angle-double-down");
      var downView = "";
      for(var i = 1; i <= parseInt($("#numPages").val());i++){
        downView+="<img class='d-md-block w-100' src='/img/comicPages/page"+i+".png' alt='First slide'/>";
      }
      $("#viewPane").html(downView);
      $("#viewPane").addClass("mh-page");

    }
    else{
      $("#toggleViewBtn").attr("data-mode","right");
      $("#toggleViewIcon").removeClass("fa-angle-double-down");
      $("#toggleViewIcon").addClass("fa-angle-double-right");
      $("#viewPane").removeClass("mh-page");
      var lis = "";
      for(var i = 0; i < parseInt($("#numPages").val());i++){
        lis+="<li data-target='#comicCarousel' data-slide-to='"+i+"' class='active'></li>"
      }
      var upperCarousel = "<div id='comicCarousel' class='carousel slide'>" +
          "<ol class='carousel-indicators'>" +
          lis +
          "</ol>"
      var carouselItems = "<div class='carousel-inner'>";
      for(var i = 1; i <= parseInt($("#numPages").val());i++) {
        if(i == 1)
        carouselItems += "<div class='carousel-item active'>" +
            "<img class='d-md-block w-100' src='/img/comicPages/page"+i+".png' alt='First slide'/>" +
            "</div>";
        else
          carouselItems += "<div class='carousel-item'>" +
              "<img class='d-md-block w-100' src='/img/comicPages/page"+i+".png' alt='First slide'/>" +
              "</div>";
      }
      carouselItems += "</div>";

      var lowerCarousel = carouselItems +
          `<a class='carousel-control-prev' href='#comicCarousel' role='button' data-slide='prev'>
          <span class='carousel-control-prev-icon' aria-hidden='true'></span>
          <span class='sr-only'>Previous</span>
        </a>
        <a class='carousel-control-next' href='#comicCarousel' role='button' data-slide='next'>
          <span class='carousel-control-next-icon' aria-hidden='true'></span>
          <span class='sr-only'>Next</span>
        </a>
      </div>`

      $("#viewPane").html(upperCarousel+lowerCarousel);
    }
  }
  $("#toggleViewBtn").click(toggleViewfn())

});
