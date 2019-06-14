package com.rxiu.zkui.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/11
 */
public class ZkNode implements Serializable {

    private String path;
    private String name;
    private String value;
    private boolean open = false;
    private boolean isParent;

    private List<ZkNode> children;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public List<ZkNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZkNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
