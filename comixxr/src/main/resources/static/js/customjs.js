$(document).ready(function(){
  $("#navbarUsername").text($("#current-user").text());

  if($("#viewPane").length){
    toggleViewfn();
  }
  $("#upvote_btn").click(function(){
    $.get("/user/comic/upvote/"+$("#comicId").val(),function(){
      if($("#downvote_btn").hasClass("btn-secondary")){
        $("#totalVotes").text(parseInt($("#totalVotes").text())+1 + "");
      }
      $("#upvote_btn").removeClass("btn-info");
      $("#upvote_btn").addClass("btn-secondary");
      $("#downvote_btn").removeClass("btn-secondary");
      $("#downvote_btn").addClass("btn-info");
      $("#totalVotes").text(parseInt($("#totalVotes").text())+1 + "");
    })
  })

  $("#downvote_btn").click(function(){
    $.get("/user/comic/downvote/"+$("#comicId").val(),function(){
      if($("#upvote_btn").hasClass("btn-secondary")){
        $("#totalVotes").text(parseInt($("#totalVotes").text())-1 + "");
      }
      $("#upvote_btn").addClass("btn-info");
      $("#upvote_btn").removeClass("btn-secondary");
      $("#downvote_btn").addClass("btn-secondary");
      $("#downvote_btn").removeClass("btn-info");
      $("#totalVotes").text(parseInt($("#totalVotes").text())-1 + "");

    })
  })

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
    $("#subscribe_btn").click(function(){toggleSubscribed()});
    toggleSubscribed = function(){
      var profileUserId = $("#userid").val();
      if ($("#subscribe_btn").text() === "Subscribe") {
        $.get("/user/subscribeToUser/" + profileUserId,
            function (data) {
              if (data) {
                $("#subscribe_btn").text("Subscribed");
                $("#subscribe_btn").removeClass("btn-secondary");
                $("#subscribe_btn").addClass("btn-dark");
              }

            });
      } else {
        $.get("/user/unsubscribeFromUser/" + profileUserId,
            function (data) {
              if (data) {
                $("#subscribe_btn").text("Subscribe");
                $("#subscribe_btn").removeClass("btn-dark");
                $("#subscribe_btn").addClass("btn-secondary");
              }
            });
      }
    }
  });
  var updateBio = function(){
    var data = $("#bioText").val();
    $.post("/user/updateBio",{"bioText" : data},function(){
      alert("Bio updated successfully!");
    })
    return false;
  }

