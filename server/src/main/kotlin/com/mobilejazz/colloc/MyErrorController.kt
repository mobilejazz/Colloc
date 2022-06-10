import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class MyErrorController : ErrorController {
    @RequestMapping("/error")
    fun handleError(): String {
        //do something like logging
        return "error"
    }
}