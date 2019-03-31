$(document).ready(function() {
  $('#port').damuro({
      background: "rgba(255, 255, 255, 1.0)",
      sandbox: 'deviantart_muro_sandbox.html',
      loadingText: 'This is my loading message',
      splashCss: {
        color: '#33a'
      },
      autoload: false
    }).one('click', function() {
      $(this).damuro().open();
    })
    .damuro()
    .done(function(data) {
      // Here's where you'd save the image, we'll just set the page background as an example
      if (data.image && !/'/.test(data.image)) {
          $('body').css('backgroundImage', "url('" + data.image + "')");
      }
      $(this).hide().damuro().remove();
    })
    .fail(function (data) {
            $(this).hide().damuro().remove();
            if (data.error) {
                // Something failed in saving the image.
                $('body').append('<p>All aboard the fail whale: ' + data.error + '.</p>');
            } else {
                // The user pressed "done" without making any changes.
                $('body').append("<p>Be that way then, don't edit anything.</p>");
            }
        });
})
