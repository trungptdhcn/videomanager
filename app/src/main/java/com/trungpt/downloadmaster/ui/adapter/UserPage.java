package com.trungpt.downloadmaster.ui.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 12/4/2015.
 */
public class UserPage
{
    private List<User> users = new ArrayList<>();
    private String nextPage;

    public UserPage(List<User> users)
    {
        this.users = users;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    public String getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(String nextPage)
    {
        this.nextPage = nextPage;
    }
}
