package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.ThreadRepository;
import com.project.database_2b2t_org_ru.entity.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThreadServiceImpl implements ThreadService{

    @Autowired
    private ThreadRepository threadRepository;

    @Override
    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    @Override
    public void saveThread(Thread thread) {
        threadRepository.save(thread);
    }

    @Override
    public Thread getThread(int id) {
        Thread thread = null;
        Optional<Thread> thrd = threadRepository.findById(id);
        if(thrd.isPresent()){
            thread = thrd.get();
        }
        return thread;
    }

    @Override
    public void deleteThread(int id) {
        threadRepository.deleteById(id);
    }
}
