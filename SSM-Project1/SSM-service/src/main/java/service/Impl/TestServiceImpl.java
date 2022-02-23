package service.Impl;

import dao.TestMapper;
import domain.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.TestService;

import java.util.List;
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;
    @Override
    public List<Test> findAllTest() {
        List<Test> allTest = testMapper.findAllTest();
        return allTest;
    }
}
