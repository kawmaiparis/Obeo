package conversations;

import database.ObeoConnection;
import database.RecordObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ObeoConversation extends RecordObject {

    private long user1_id;
    private long user2_id;

    /*Creating ObeoConversation from a database */
    public ObeoConversation(long conversation_id, long user1_id, long user2_id) {
        super(conversation_id, "conversation", "conversation_id");
        this.user1_id = user1_id;
        this.user2_id = user2_id;
    }


    /*Creating an ObeoConversation to add to a databse */
    public ObeoConversation(long user1_id, long user2_id) {
        this(-1, user1_id, user2_id);
    }



    public long getUser1_id() {
        return user1_id;
    }

    public long getUser2_id() {
        return user2_id;
    }
}
