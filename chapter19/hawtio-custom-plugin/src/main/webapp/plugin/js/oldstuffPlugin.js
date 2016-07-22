/**
 * @module Oldstuff
 * @mail Oldstuff
 *
 * The main entry point for the Oldstuff module
 */
var Oldstuff = (function(Oldstuff) {

  /**
   * @property pluginName
   * @type {string}
   *
   * The name of this plugin
   */
  Oldstuff.pluginName = 'oldstuff_plugin';

  /**
   * @property log
   * @type {Logging.Logger}
   *
   * This plugin's logger instance
   */
  Oldstuff.log = Logger.get('Oldstuff');

  /**
   * @property contextPath
   * @type {string}
   *
   * The top level path of this plugin on the server
   */
  Oldstuff.contextPath = "/oldstuff-plugin/";

  /**
   * @property templatePath
   * @type {string}
   *
   * The path to this plugin's partials
   */
  Oldstuff.templatePath = Oldstuff.contextPath + "plugin/html/";

  /**
   * The mbean for the Camel deprecated checker
   */
  Oldstuff.mbean = "hawtio:type=CamelComponentDeprecated";

  /**
   * @property module
   * @type {object}
   *
   * This plugin's angularjs module instance.  This plugin only
   * needs hawtioCore to run, which provides services like
   * workspace, viewRegistry and layoutFull used by the
   * run function
   */
  Oldstuff.module = angular.module('oldstuff_plugin', ['hawtioCore'])
    .config(function($routeProvider) {

      /**
       * Here we define the route for our plugin.  One note is
       * to avoid using 'otherwise', as hawtio has a handler
       * in place when a route doesn't match any routes that
       * routeProvider has been configured with.
       */
      $routeProvider.
      when('/oldstuff_plugin', {
        templateUrl: Oldstuff.templatePath + 'oldstuff.html'
      });
    });

  /**
   * Here we define any initialization to be done when this angular
   * module is bootstrapped.  In here we do a number of things:
   *
   * 1.  We log that we've been loaded (kinda optional)
   * 2.  We load our .css file for our views
   * 3.  We configure the viewRegistry service from hawtio for our
   *     route; in this case we use a pre-defined layout that uses
   *     the full viewing area
   * 4.  We add our help to the help registry
   * 5.  We configure our top-level tab and provide a link to our
   *     plugin.  This is just a matter of adding to the workspace's
   *     topLevelTabs array.
   */
  Oldstuff.module.run(function(workspace, viewRegistry, helpRegistry, layoutFull) {

    Oldstuff.log.info(Oldstuff.pluginName, " loaded");

    Core.addCSS(Oldstuff.contextPath + "plugin/css/oldstuff.css");

    // tell the app to use the full layout, also could use layoutTree
    // to get the JMX tree or provide a URL to a custom layout
    viewRegistry["oldstuff_plugin"] = layoutFull;

    // add the plugin help to the help registry
    helpRegistry.addUserDoc('Oldstuff', Oldstuff.contextPath + '/plugin/doc/help.md');

    /* Set up top-level link to our plugin.  Requires an object
     with the following attributes:

     id - the ID of this plugin, used by the perspective plugin
     and by the preferences page
     content - The text or HTML that should be shown in the tab
     title - This will be the tab's tooltip
     isValid - A function that returns whether or not this
     plugin has functionality that can be used for
     the current JVM.  The workspace object is passed
     in by hawtio's navbar controller which lets
     you inspect the JMX tree, however you can do
     any checking necessary and return a boolean
     href - a function that returns a link, normally you'd
     return a hash link like #/foo/bar but you can
     also return a full URL to some other site
     isActive - Called by hawtio's navbar to see if the current
     $location.url() matches up with this plugin.
     Here we use a helper from workspace that
     checks if $location.url() starts with our
     route.
     */
    workspace.topLevelTabs.push({
      id: "oldstuff",
      content: "Oldstuff",
      title: "Plugin to check for old stuff ",
      isValid: function(workspace) { return true; },
      href: function() { return "#/oldstuff_plugin"; },
      isActive: function(workspace) { return workspace.isLinkActive("oldstuff_plugin"); }
    });

  });

  /**
   * @function OldstuffController
   * @param $scope
   * @param jolokia
   *
   * The controller for shell.html, only requires the jolokia
   * service from hawtioCore
   */
  Oldstuff.OldstuffController = function($scope, jolokia) {

    $scope.findDeprecatedComponents = function() {
      Oldstuff.log.info(Oldstuff.pluginName, " findDeprecatedComponents");
      // call mbean
      jolokia.request({
        type: 'exec',
        mbean: Oldstuff.mbean,
        operation: 'findDeprecatedComponents'
      }, onSuccess(render, {error: renderError}));
    };

    // update display with Camel result
    function render(response) {
      Oldstuff.log.info(Oldstuff.pluginName, " --> " + response.value);
      $scope.output = response.value;
      if ($scope.output.length === 0) {
        $scope.output = ["No deprecated components found"];
      }
      $scope.error = null;
      Core.$apply($scope);
    }

    function renderError(response) {
      Oldstuff.log.info(Oldstuff.pluginName, " error " + response);
      $scope.output = null;
      $scope.error = response;
      Core.$apply($scope);
    }
  };

  return Oldstuff;

})(Oldstuff || {});

// tell the hawtio plugin loader about our plugin so it can be
// bootstrapped with the rest of angular
hawtioPluginLoader.addModule(Oldstuff.pluginName);
