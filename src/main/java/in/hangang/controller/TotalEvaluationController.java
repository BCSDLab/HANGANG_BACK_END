package in.hangang.controller;

import in.hangang.service.TotalEvaluationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TotalEvaluationController {

    @Resource
    TotalEvaluationService totalEvaluationService;

    @RequestMapping(value = "/rating", method = RequestMethod.PATCH)
    public void updateTotalRating(){
        totalEvaluationService.updateTotalRating();
    }
}
