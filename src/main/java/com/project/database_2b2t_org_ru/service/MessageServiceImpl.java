package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.MessageRepository;
import com.project.database_2b2t_org_ru.entity.Message;
import com.project.database_2b2t_org_ru.service.interfaces.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MainService<Message> {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public List<Message> getAllObjects() {
        return messageRepository.findAll();
    }

    @Override
    public void saveObject(Message object) {
        messageRepository.save(object);
    }

    @Override
    public Message getObjectById(long id) {
        Message message = null;
        Optional<Message> msg = messageRepository.findById(id);
        if (msg.isPresent()) {
            message = msg.get();
        }
        return message;
    }

    @Override
    public void deleteObjectById(long id) {
        messageRepository.deleteById(id);
    }


    public List<Message> getAllMessageForThread(long threadId) {
        return messageRepository.findAllByThreadId(threadId);
    }
}
