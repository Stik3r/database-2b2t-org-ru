package com.project.database_2b2t_org_ru.service.interfaces;

import com.project.database_2b2t_org_ru.entity.Message;

import java.util.List;

public interface MessageService extends MainService<Message> {

    public List<Message> getAllMessafeForThread(long threadId);
}
