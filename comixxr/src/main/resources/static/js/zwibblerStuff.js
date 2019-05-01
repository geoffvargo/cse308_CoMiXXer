function ZwibblerController(ctx){
    var saved = null;
    var id = null;
    var raw_data;

    ctx.onSave = function() {
        saved = ctx.save("zwibbler3");
        id = $("#comicId").val();

        $.post("/user/comic/save", {comicData: saved, comicId: id})
            .done(function() {
                alert("Comic saved");
            })
            .fail(function () {
                alert("error");
            });
    };

    ctx.onPublish = function () {
        saved = ctx.save("zwibbler3");
        id = $("#comicId").val();

        $.post("/user/comic/publish", {comicData: saved, comicId: id})
            .done(function () {
                // alert("Comic published");
                window.location.replace('/user/myProfile');
            })
            .fail(function () {
                alert("error")
            });
    };

    $(document).ready(function(){
        if($("#isLoad").val()){
            raw_data = $("#raw_data").val();
            if(raw_data !== null)
                ctx.load("zwibbler3", raw_data);
        }
    })
}