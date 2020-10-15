/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kgill.devopsinttest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author 4GILLK91 <4GILLK91@solent.ac.uk>
 */
public class JMSProducer {

    static ActiveMQConnectionFactory connectionFactory;

    public static void main(String[] args) {
        connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        System.out.println("Press return to enter your input\nType 'q' to exit");
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in, "UTF-8"))) {
            while (true) {
                // running
                String s = input.readLine();
                
                if (s.equals("q")) {
                    break;
                } else if (s.equals(">read")) {
                    System.out.println(readMessage());
                } else {
                    sendMessage(s);
                }
                
            }
        } catch (Exception e) {

        }

        
    }

    public static String sendMessage(String s) {
        try {

            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue("Test");
            MessageProducer producer = session.createProducer(queue);
            TextMessage msg = session.createTextMessage();
            msg.setText(s);
            producer.send(msg);

            producer.close();
            session.close();
            connection.close();

        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
            return "ERROR";
        }
        return "SUCCESS";
    }
    
    public static String readMessage() {
        
        String message = null;
        try {

            
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue("Test");
            MessageConsumer consumer = session.createConsumer(queue);
            TextMessage textMessage = (TextMessage) consumer.receive();
            message = textMessage.getText();
            

            consumer.close();
            session.close();
            connection.close();
            
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
            return "ERROR";
        }
        return message;
    }
}
