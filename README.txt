==========================
UAPatternController Module
Version 0.0.1
Copyright (c) 2010 nkmrshn
==========================


Abstract
========
This module is intent to switch views by user-agent for Play!
framework. (http://www.playframework.org/)


Install
=======
Please install this module as usual in the framework module
directory.


Configuration
=============
# This is an example, how to configure this module.

# If you want to output the user-agent in debug log, set to "true".
uaPatternController.log=false

# Add a pattern(regular expression).
#
# Example for "app/views/Application/Safari" directory for Apple
# Safari web browser.
uaPatternController.pattern.Safari=Safari

# Another example for "app/views/Application/Google" directory for
# Google Chrome web browser.
uaPatternController.pattern.Google=Chrome

# Specifiy patterns which you want to add in which turn. In this
# case pattern for "Google" is first and "Safari" for second.
uaPatternController.patterns=Google,Safari


Example Usage
=============
Import the module. This Controller class is the inheritance
of play.mvc.Controller.

    import play.modules.UAPatternController.Controller;

This is an example of "index" action in the Application controller.
When render method is called and the user-agent has "Safari",
the view template should be exist in the "app/views/Application/Safari"
directory.

    public class Application extends Controller {
        public static void index() {
            List<MsgData> data = null;

            data = MsgData.findAll();
     
            render(data);
        }
    }

If it wasn't "Safari", the normal view template such as,
"app/views/Application/index.html", will be loaded.

But if you specifiy the template name like,

    render("foo/bar.html", data);

the user-agent will be ignored even the web browser was
Safari at this case.


2010/04/14
