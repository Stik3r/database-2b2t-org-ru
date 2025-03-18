package com.project.database_2b2t_org_ru.service.interfaces;

import com.project.database_2b2t_org_ru.entity.Thread;


import java.util.List;


public interface ThreadService extends MainService<Thread> {

    List<Thread> findAllOnPage(int page, int count);
}
