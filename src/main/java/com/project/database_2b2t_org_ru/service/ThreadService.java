package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.entity.Thread;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ThreadService {

    public List<Thread> getAllThreads();

    public void saveThread(Thread thread);

    public Thread getThread(int id);

    public void deleteThread(int id);
}
