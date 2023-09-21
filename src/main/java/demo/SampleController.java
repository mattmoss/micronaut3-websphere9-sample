package demo;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
class SampleController {

    @Get
    String index() {
        return "Welcome to the sample application. Hello, world!";
    }
}
