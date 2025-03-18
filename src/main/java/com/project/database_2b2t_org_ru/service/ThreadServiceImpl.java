package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.ThreadRepository;
import com.project.database_2b2t_org_ru.entity.Thread;
import com.project.database_2b2t_org_ru.service.interfaces.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThreadServiceImpl implements ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    @Override
    public List<Thread> getAllObjects() {
        return threadRepository.findAll();
    }

    @Override
    public void saveObject(Thread object) {
        threadRepository.save(object);
    }

    @Override
    public Thread getObjectById(int id) {
        Thread thread = null;
        Optional<Thread> thrd = threadRepository.findById(id);
        if (thrd.isPresent()) {
            thread = thrd.get();
        }
        return thread;
    }

    @Override
    public void deleteObjectById(int id) {
        threadRepository.deleteById(id);
    }

    @Override
    public List<Thread> findAllOnPage(int page, int size) {
        Page<Thread> threadPage = threadRepository.findAll(PageRequest.of(page, size));
        return threadPage.getContent();
    }
}
