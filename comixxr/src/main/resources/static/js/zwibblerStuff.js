function ZwibblerController(ctx){
    var saved = null;
    var id = null;
    var raw_data;
    var old_page = ctx.getCurrentPage();
    var img_array = [];

    var x =[];
    var y =[];

    ctx.onSave = function() {
        x[0] = ctx.save("zwibbler3");
        y[0] = $("#comicId").val();
        var page;
        for(page = 0; page < ctx.getPageCount(); page++) {
            ctx.setCurrentPage(page);
            img_array[page] = ctx.save("png");
        }

        ctx.setCurrentPage(old_page);

        $.post("/user/comic/save", {comicData: x, imageData: img_array, comicId: y})
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