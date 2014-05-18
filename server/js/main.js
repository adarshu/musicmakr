/*globals R, Main, Spinner, rdioUtils */

(function() {

  // ==========
  window.Main = {
    Models: {},
    Views: {},
    currentView: null,
    
    // If you use this code, please change this to your own last.fm API key,
    // which you can get at http://www.last.fm/api/account/create  
    lastfmKey: 'fd12a96515b7b16b7510975bf5ea18bb',
    
    // ----------
    init: function() {
      var self = this;
      
      if (!rdioUtils.startupChecks()) {
        return;
      }

      R.ready(function() {
        self.spin(false);
        if (R.authenticated()) {
          self.go("pictures");
        } else {
          self.go("unauthenticated");
        }          
      });
      
      this.spin(true);
    }, 
    
    // ----------
    go: function(mode) {
      var $div = $("<div>")
        .attr("id", mode)
        .append(this.template(mode));
        
      $("#content")
        .empty()
        .append($div);
        
      this.mode = mode;
      var viewClass = this.upperCaseInitial(this.mode);
      if (viewClass in this.Views) {
        this.currentView = new this.Views[viewClass]();
      }
    },
    
    // ----------
    spin: function(value) {
      if (value) {
        this.spinner = new Spinner().spin($("body")[0]);     
      } else {
        this.spinner.stop();
      }
    },
    
    // ----------
    template: function(name, config) {
      var rawTemplate = $.trim($("#" + name + "-template").text());
      var template = _.template(rawTemplate);
      var html = template(config);
      return $(html);
    },

    // ----------
    upperCaseInitial: function(val) {
      return val.replace(/^([a-z])/, function($0, $1) {
        return $1.toUpperCase();
      });
    }
  };
  
  // ----------
  $(document).ready(function() {
    Main.init();
  });
  
})();  
