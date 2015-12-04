package com.trungpt.downloadmaster.ui.adapter.model;

/**
 * Created by Trung on 11/14/2015.
 */
public class VimeoCategory
{
    private String id;
    private String name;

    public VimeoCategory(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
