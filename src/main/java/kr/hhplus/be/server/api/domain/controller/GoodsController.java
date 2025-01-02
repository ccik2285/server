package kr.hhplus.be.server.api.domain.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    /*----------viewStart------------*/

    @GetMapping("/goodsInfo/{goodsNo}")
    public void getGoodsInfo(@PathVariable long goodsNo) {

    }


    /*----------viewEnd------------*/


    /*----------businessStart------------*/




    /*----------businessEnd------------*/
}
