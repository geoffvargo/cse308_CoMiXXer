$(document).ready(function(){
  $(function () {
    $('[data-toggle="tooltip"]').tooltip()
  })
  $("#navbarUsername").text($("#current-user").text());

  if($("#viewPane").length){
    toggleViewfn();
  }

  $("#postCommentBtn").click(function(){
    var data = $("#postCommentBox").val();
    var comicId = $("#comicId").val();
    if(data.length != 0){
      $.post("/comment/addComment",{'commentData':data,'comicId':comicId},function(data){
        var s = "";
        s+="<li class=\"media my-4\" id=\"comment1\">\n" +
            "<a href=\"\/userProfile/"+ data["userId"]+"\" class=\"float-left\">\n" +
            "<img src=\""+data["pic"] +"\" alt=\"\" ,\n" +
            "class=\"profile-pic rounded-circle\">\n" +
            "</a>" +
            "<div class=\"media-body\">\n" +
            "<div class=\"text-muted float-right\">\n" +
            "<div class=\"text-muted text-sm\">"+data["age"] +"</div>\n" +
            "</div>\n" +
            "<div class=\"text-secondary font-weight-bold\">"+data["userName"] +"</div>\n" +
            "<div class=\"ml-3\">"+ data["text"]+
            "</div>\n" +
            "</div>"+
            "</li>";
        $("#commentsList").prepend(s);
        $("#postCommentBox").val("");
      })
    }
  })

  $("#createCuration_btn").click(function(){
    $.post("/user/createNewCuration",{'curationName' : $("#curationName").val()},function(data){
      if(data){
        alert("Curation created successfully!");
        $("#curationName").val("");
      }
    })
  })

  $("#loadCurations_btn").click(function(){
    $.get("/user/getMyCurations",function(data){
      var s = '';
      for(var i = 0; i < data.length; i++){
        var d = data[i];
        var comics = d["hexComics"];
        if(comics.indexOf($("#comicId").val()) >=0) {
          s += '<a id="btnid_'+d["hexId"] +'" class="dropdown-item text-primary bg-secondary" onclick="toggleCuration(\'' + d["hexId"] + '\')">' + d["title"] + '</a>'
        }
        else{
          s += '<a id="btnid_'+d["hexId"] +'" class="dropdown-item bg-info" onclick="toggleCuration(\'' + d["hexId"] + '\')">' + d["title"] + '</a>'

        }
      }
      $("#curations_dd").html(s);
    });
  })

  $("#upvote_btn").click(function(){
    $.get("/user/comic/upvote/"+$("#comicId").val(),function(){
      if($("#downvote_btn").hasClass("btn-secondary")){
        $("#totalVotes").text(parseInt($("#totalVotes").text())+2 + "");
      }
      else if(!$("#upvote_btn").hasClass("btn-secondary")){
        $("#totalVotes").text(parseInt($("#totalVotes").text())+1 + "");
      }
      $("#upvote_btn").removeClass("btn-info");
      $("#upvote_btn").addClass("btn-secondary");
      $("#downvote_btn").removeClass("btn-secondary");
      $("#downvote_btn").addClass("btn-info");
    })
  })

  $("#downvote_btn").click(function(){
    $.get("/user/comic/downvote/"+$("#comicId").val(),function(){
      if($("#upvote_btn").hasClass("btn-secondary")){
        $("#totalVotes").text(parseInt($("#totalVotes").text())-2 + "");
      }
      else if(!$("#downvote_btn").hasClass("btn-secondary")){
        $("#totalVotes").text(parseInt($("#totalVotes").text())-1 + "");
      }
      $("#upvote_btn").addClass("btn-info");
      $("#upvote_btn").removeClass("btn-secondary");
      $("#downvote_btn").addClass("btn-secondary");
      $("#downvote_btn").removeClass("btn-info");
    })
  })

  $("#toggleViewBtn").click(function(){
    toggleViewfn();
  });

  $("#expandCommentsBtn").click(function(){
    $.get("/comment/getComment/"+$("#comicId").val(),function(myData){
      var s = "";
      for(var i =0; i < myData.length; i++){
        var data = myData[i];
        s+="<li class=\"media my-4\" id=\"comment1\">\n" +
            "<a href=\"\/userProfile/"+ data["userId"]+"\" class=\"float-left\">\n" +
            "<img src=\""+data["pic"] +"\" alt=\"\" ,\n" +
            "class=\"profile-pic rounded-circle\">\n" +
            "</a>" +
            "<div class=\"media-body\">\n" +
            "<div class=\"text-muted float-right\">\n" +
            "<div class=\"text-muted text-sm\">"+data["age"] +"</div>\n" +
            "</div>\n" +
            "<div class=\"text-secondary font-weight-bold\">"+data["userName"] +"</div>\n" +
            "<div class=\"ml-3\">"+ data["text"]+
            "</div>\n" +
            "</div>"+
            "</li>";
      }
      $("#commentsList").html(s);
    })
  })
  function toggleViewfn(){
    if($("#toggleViewBtn").attr("data-mode") === "right"){
      $("#toggleViewBtn").attr("data-mode","down");
      $("#toggleViewIcon").removeClass("fa-angle-double-right");
      $("#toggleViewIcon").addClass("fa-angle-double-down");
      var downView = "";
      for(var i = 1; i <= parseInt($("#numPages").val());i++){
        downView+="<img class='d-md-block w-100' src='"+ $("#page" + i).val()+"' alt='First slide'/>";
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
            "<img class='d-md-block w-100' src='"+ $("#page" + i).val()+"' alt='First slide'/>" +
            "</div>";
        else
          carouselItems += "<div class='carousel-item'>" +
              "<img class='d-md-block w-100' src='"+ $("#page" + i).val()+"' alt='First slide'/>" +
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

  var toggleCuration = function(id){
    $.get("/user/toggleCuration/"+$("#comicId").val()+"/"+id,function(data){
      if(data && $("#btnid_"+id).hasClass("btn-secondary")) {
        $("#btnid_"+id).removeClass("btn-secondary");
        $("#btnid_"+id).addClass("btn-info");
        setTimeout(function(){
          $("#curationBox").attr('hidden',true);
          $("#btnid_"+id).removeClass("btn-info");
        },500);
      }
      else if(data && !$("#btnid_"+id).hasClass("btn-secondary")) {
        $("#btnid_"+id).removeClass("btn-info");
        $("#btnid_"+id).addClass("btn-secondary");
        setTimeout(function(){
          $("#curationBox").attr('hidden',true);
          $("#btnid_"+id).removeClass("btn-secondary");
        },500);
      }
      else{
        $("#btnid_"+id).addClass("bg-danger");
        setTimeout(function(){
          $("#curationBox").attr('hidden',true);
          $("#btnid_"+id).removeClass("bg-danger");
        }, 500);
      }
    })
  }

