package com.bilibili.chat.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
/**
 *继承ApplicationEvent用于封装消息
 */

@Getter
public class MessageEvent extends ApplicationEvent {
    private final String message;

    public MessageEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

}
