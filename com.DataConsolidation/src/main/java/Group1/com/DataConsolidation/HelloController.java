package Group1.com.DataConsolidation;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


// Here we are just trying to write API responses to HTTP resquests. URL: localhost:8080 will just get the text "hellofrombackend".
// URL: localhost:8080/hello will bthe URL that is connected to React (meaning data from this will be passed to React somehow. To Check if this is working, run
// npm start in the frontend folder. Inspect the page, open the console, should see {data :'This is connected to React'})
@Controller
@CrossOrigin("*")
public class HelloController {

    @RequestMapping(value = "/")
    @CrossOrigin("*")
    @ResponseBody
    public String Hello() {
        return "hellofrombackend";
    }
    @RequestMapping(value = "/hello")
    @ResponseBody
    @CrossOrigin("*")
    public String index() {
        return "This is connected to React";
    }

}