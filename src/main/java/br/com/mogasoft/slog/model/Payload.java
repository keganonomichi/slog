/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mogasoft.slog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author Kegan Onomichi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {
    
    String userName;
    String channel;
    String iconEmoji;
    List<Attachment> attachments;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIcon_emoji() {
        return iconEmoji;
    }

    public void setIcon_emoji(String icon_emoji) {
        this.iconEmoji = icon_emoji;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
    
}
