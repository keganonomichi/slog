/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mogasoft.slog;

// note: class name need not match the @Plugin name.

import br.com.mogasoft.slog.model.Attachment;
import br.com.mogasoft.slog.model.Payload;
import br.com.mogasoft.slog.utils.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="Slack", category="Core", elementType="appender", printObject=true)
public final class SlackAppender extends AbstractAppender {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();

    protected SlackAppender(String name, Filter filter, Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    // The append method is where the appender does the work.
    // Given a log event, you are free to do with it what you want.
    // This example demonstrates:
    // 1. Concurrency: this method may be called by multiple threads concurrently
    // 2. How to use layouts
    // 3. Error handling
    @Override
    public void append(LogEvent event) {
        try {
            URL obj = new URL("");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/json");
            
            Payload payload = new Payload();
            payload.setChannel("log-teste");
            payload.setIcon_emoji("TESTE");
            payload.setUserName("TESTE");
            payload.setAttachments(this.buildAttachments(event));
            
            ObjectMapper mapper = new ObjectMapper();
            String sPayload = mapper.writeValueAsString(payload);
            System.out.println(sPayload);
//            String teste = "{"
//                + "\"text\": \"" + event.getMessage().getFormattedMessage() + "\", "
//            + "}";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(sPayload);
            wr.flush();
            wr.close();
                        
            String response = "Response Code: " + con.getResponseCode();
            System.out.println(response);
        } catch (Exception ex) {
            System.out.println("Erro ao enviar log para o Slack");
            ex.printStackTrace();
        }
//        readLock.lock();
//        try {
//            final byte[] bytes = getLayout().toByteArray(event);
//            System.out.write(bytes);
//        } catch (Exception ex) {
//            if (!ignoreExceptions()) {
//                throw new AppenderLoggingException(ex);
//            }
//        } finally {
//            readLock.unlock();
//        }
    }
    
    private List<Attachment> buildAttachments(LogEvent event) {
        Attachment attachment = new Attachment();
        attachment.setColor(this.getAttachmentColor(event));
        attachment.setFallback("TESTE FALLBACK");
//        attachment.setPretext(this.getPretext(event));
        attachment.setText(this.getText(event));
        attachment.setTitle("TESTE TITLE");
        
        List<Attachment> attachments = new ArrayList<>();
        attachments.add(attachment);
        return attachments;
    }
    
    private String getAttachmentColor(LogEvent event) {
        if (event.getLevel().equals(Level.FATAL)
        ||  event.getLevel().equals(Level.ERROR)) {
            return Color.RED;
        } else if (event.getLevel().equals(Level.WARN)) {
            return Color.ORANGE;
        } else if (event.getLevel().equals(Level.INFO)) {
            return Color.BLACK;
        } else if (event.getLevel().equals(Level.DEBUG)) {
            return Color.BLUE;
        } else if (event.getLevel().equals(Level.TRACE)) {
            return Color.GREEN;
        }
        return Color.GRAY;
    }
    
    private String getPretext(LogEvent event) {
        return "Log4j entry level: " + event.getLevel().toString();
    }
    
    private String getText(LogEvent event) {
        return "Message: " + event.getMessage().getFormattedMessage();
    }

    // Your custom appender needs to declare a factory method
    // annotated with `@PluginFactory`. Log4j will parse the configuration
    // and call this factory method to construct an appender instance with
    // the configured attributes.
    @PluginFactory
    public static SlackAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Level") Level level,
            @PluginAttribute("URL") URL url,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            LOGGER.error("No name provided for SlackAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        if (url == null) {
            LOGGER.error("No URL provided for SlackAppender");
            return null;
        }
        return new SlackAppender(name, filter, layout, true);
    }
}