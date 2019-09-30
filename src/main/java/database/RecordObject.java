package database;

import java.io.Serializable;

public abstract class RecordObject implements Serializable {
    protected long id;
    private String tableName;
    private String primaryKeyName;

    public RecordObject(long id, String tableName, String primaryKeyName) {
        this.id = id;
        this.tableName = tableName;
        this.primaryKeyName = primaryKeyName;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }
}

