/*
 * 6b. Build a servlet program to create a cookie to get your name through text box and press submit button(
through HTML) to display the message by greeting Welcome back your name ! , you have visited this page
n times ( n = number of your visit ) and demonstrate the expiry of cookie also.
 */
package com.cookieservlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String userName = request.getParameter("userName");

        int count = 0;
        String existingUser = null;

        Cookie[] cookies = request.getCookies();

        // 🔹 Read existing cookies
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    existingUser = c.getValue();
                }
                if (c.getName().equals("count")) {
                    count = Integer.parseInt(c.getValue());
                }
            }
        }

        // 🔹 First time login
        if (existingUser == null && userName != null && !userName.isEmpty()) {

            existingUser = userName;
            count = 1;

            Cookie userCookie = new Cookie("user", existingUser);
            Cookie countCookie = new Cookie("count", String.valueOf(count));

            userCookie.setMaxAge(30);   // expiry demo
            countCookie.setMaxAge(30);

            response.addCookie(userCookie);
            response.addCookie(countCookie);

            // 🔥 Important: reload so cookies are available
            response.sendRedirect("CookieServlet");
            return;
        }

        // 🔹 Returning user
        else if (existingUser != null) {

            count++;

            Cookie countCookie = new Cookie("count", String.valueOf(count));
            countCookie.setMaxAge(30);

            response.addCookie(countCookie);
        }

        out.println("<html><body>");

        // 🔹 If user exists
        if (existingUser != null) {

            out.println("<h2 style='color:blue;'>Welcome back, " + existingUser + "!</h2>");
            out.println("<h2 style='color:magenta;'>You have visited this page " + count + " times!</h2>");

            // 🔹 Display cookies
            out.println("<h3>List of Cookies:</h3>");
            if (cookies != null) {
                for (Cookie c : cookies) {
                    out.println("Name: " + c.getName() + " | Value: " + c.getValue() + "<br>");
                }
            }

            // 🔹 Logout button
            out.println("<form method='post' action='CookieServlet'>");
            out.println("<input type='submit' value='Logout'>");
            out.println("</form>");

        } else {
            // 🔹 Guest view
            out.println("<h2 style='color:red;'>Welcome Guest! you have been logged out or kindly login first time</h2>");

            out.println("<form action='CookieServlet' method='get'>");
            out.println("Enter your name: <input type='text' name='userName'>");
            out.println("<input type='submit' value='Submit'>");
            out.println("</form>");
        }

        out.println("</body></html>");
    }

    // 🔹 Logout (cookie expiry demo)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cookie c1 = new Cookie("user", "");
        c1.setMaxAge(0);

        Cookie c2 = new Cookie("count", "");
        c2.setMaxAge(0);

        response.addCookie(c1);
        response.addCookie(c2);

        response.sendRedirect("CookieServlet");
    }
}