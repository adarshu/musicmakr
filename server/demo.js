$(function () {

	if (!("R" in window)) {
		alert('Something has gone wrong!');
		return;
	}

	  function showAuthenticated() {
        $('.authenticated-view').show();
        $('.username').text(R.currentUser.get('firstName') + ' ' + R.currentUser.get('lastName'));
      }

      // ----------
      function showUnauthenticated() {
        $('.unauthenticated-view').show();
      }

      // ----------
      function showError(message) {
        $('#error-message').text(message);
      }


	R.ready(function () {

		var player = new metronomik.player("player", R.player);

		$(".m-playToggle").one("click", function () {
			R.player.queue.add("a171827");
		});

	    $('.sign-in').click(function() {
          R.authenticate(function(nowAuthenticated) {
            if (nowAuthenticated) {
              $('.unauthenticated-view').hide();
              showAuthenticated();
            }
          });
        });        

        if (R.authenticated()) {
          showAuthenticated();
        } else {
          showUnauthenticated();
        }

	});

});