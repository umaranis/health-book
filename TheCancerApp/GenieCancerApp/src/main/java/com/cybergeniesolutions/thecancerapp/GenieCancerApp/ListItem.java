package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 7/02/2017.
 */
public class ListItem {

    public String listItem;
    public int listId;
    public long id;
    public ListItem(){
        super();
    }

    public ListItem(String listItem, int listId, long id) {
        super();
        this.listId = listId;
        this.listItem = listItem;
        this.id = id;
    }

    public ListItem(String listItem, int listId) {
        super();
        this.listId = listId;
        this.listItem = listItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getListItem() {
        return listItem;
    }

    public void setListItem(String listItem) {
        this.listItem = listItem;
    }
}


