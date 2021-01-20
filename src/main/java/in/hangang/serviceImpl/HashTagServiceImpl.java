package in.hangang.serviceImpl;

import in.hangang.mapper.HashTagMapper;
import in.hangang.service.HashTagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HashTagServiceImpl implements HashTagService {

    @Resource
    HashTagMapper hashtagMapper;

    @Override
    public void updateTop3HashTag() {
        hashtagMapper.updateTop3HashTag();
        System.out.println("updateTop3HashTag 완료 : " + System.currentTimeMillis());
    }
}
