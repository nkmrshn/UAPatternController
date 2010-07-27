package play.modules.UAPatternController;

import java.util.*;
import java.util.regex.*;

import play.Play;
import play.Logger;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Header;
import play.classloading.enhancers.LocalvariablesNamesEnhancer.LocalVariablesNamesTracer;

public abstract class Controller extends play.mvc.Controller {
    
    protected static void render(Object... args) {
        String patternKeys = Play.configuration.getProperty("uaPatternController.patterns");
        Boolean doLog = new Boolean(Play.configuration.getProperty("uaPatternController.log")).booleanValue();

        String[] configKeys = patternKeys.split(",");
        String[] configValues = new String[configKeys.length];

        for (int i = 0; i < configKeys.length; i++) {
            configValues[i] = Play.configuration.getProperty("uaPatternController.pattern." + configKeys[i]);
        }

        Map<String, Http.Header> headers = Http.Request.current().headers;
        Set headerKeys = headers.keySet();        

        for (Iterator headerKeysIterator = headerKeys.iterator(); headerKeysIterator.hasNext();) {      
            String headerKey = (String)headerKeysIterator.next();        

            if (headerKey.equals("user-agent")) {
                Http.Header header = headers.get(headerKey);
                List<String> headerValues = header.values;

                for (Iterator headerValuesIterator = headerValues.iterator(); headerValuesIterator.hasNext();) {
                    String headerValue = (String)headerValuesIterator.next();

                    if (doLog) Logger.debug(headerValue);

                    for(int i = 0; i < configKeys.length; i++) {
                        Pattern pattern = Pattern.compile(configValues[i]);
                        Matcher matcher = pattern.matcher(headerValue);

                        if (matcher.find()) {
                            if (args.length > 0 && args[0] instanceof String && args[0].toString().startsWith("@")) {
                                args[0] = "@" + configKeys[i] + "/" + args[0].toString().substring(1);
                            } else if (args.length > 0 && !LocalVariablesNamesTracer.getAllLocalVariableNames(args[0]).isEmpty()) {
                                Vector<Object> v = new Vector<Object>();
                                v.add("@" + configKeys[i] + "/" + Http.Request.current().actionMethod);
                                for (Object o: args) {
                                    v.add(o);
                                }
                                args = new Object[v.size()];
                                args = v.toArray();
                            } else if (args.length == 0) {
                                args = new Object[1];
                                args[0] = "@" + configKeys[i] + "/" + Http.Request.current().actionMethod;
                            }

                            break;
                        }
                    }
                }

                break;
            }
        }

        play.mvc.Controller.render(args);
    }

}
