package servlet;

import authentication.Authentication;
import authentication.UserService;
import org.apache.catalina.*;

import org.apache.catalina.startup.Tomcat;


import javax.servlet.http.HttpServlet;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ServletRouter {

    private List<Class<? extends AbstractRoutableServlet>> servletClasses = new ArrayList<>();
    private  Map<String,HttpServlet> myMap = new HashMap<>();

    public ServletRouter() {
        ListServlet listServlet = new ListServlet();
        servletClasses = listServlet.getServletClasses();
    }

    public void initialisation(Context context) {

        UserService userService = new UserService();
        Authentication authentication = new Authentication(userService);

        authentication.setUserService(userService);
        for (Class<? extends AbstractRoutableServlet> servletClass : servletClasses) {
            try {
                AbstractRoutableServlet httpServlet = servletClass.getDeclaredConstructor().newInstance();
                httpServlet.setAuthentication(authentication);
                Tomcat.addServlet(context, httpServlet.getClass().getName(), httpServlet);
                context.addServletMapping(httpServlet.getPattern(), httpServlet.getClass().getName());
                myMap.put(httpServlet.getPattern(),httpServlet);


            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        }

    }



    public Map<String, HttpServlet> getMyMap() {
        return myMap;
    }




}
