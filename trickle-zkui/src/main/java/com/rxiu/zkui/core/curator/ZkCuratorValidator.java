package com.rxiu.zkui.core.curator;

import com.rxiu.zkui.core.curator.validator.Validator;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public class ZkCuratorValidator implements Validator<ZkCurator> {
    @Override
    public boolean validate(ZkCurator curator) {
        return true;//curator.isStarted();
    }

    @Override
    public void invalidate(ZkCurator curator) {
        curator.close();
    }
}
