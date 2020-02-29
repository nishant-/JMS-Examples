package artemis.demo;

import artemis.demo.senders.MessageSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArtemisDemoApplicationTests {

    @Autowired
    private MessageSender messageSender;


    @Test
    void testSendAndReceive() {
        messageSender.send("This message will be received by JMS listener \n " +
                "and printed on the console");
    }

}
